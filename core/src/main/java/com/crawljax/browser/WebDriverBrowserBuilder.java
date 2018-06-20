package com.crawljax.browser;

import javax.inject.Inject;
import javax.inject.Provider;

import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.ProxyConfiguration.ProxyType;
import com.crawljax.core.plugin.Plugins;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedSet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default implementation of the EmbeddedBrowserBuilder based on Selenium WebDriver API.
 */
public class WebDriverBrowserBuilder implements Provider<EmbeddedBrowser> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverBrowserBuilder.class);
	private final CrawljaxConfiguration configuration;
	private final Plugins plugins;

	@Inject
	public WebDriverBrowserBuilder(CrawljaxConfiguration configuration, Plugins plugins) {
		this.configuration = configuration;
		this.plugins = plugins;
	}

	/**
	 * Build a new WebDriver based EmbeddedBrowser.
	 * 
	 * @return the new build WebDriver based embeddedBrowser
	 */
	@Override
	public EmbeddedBrowser get() {
		LOGGER.debug("Setting up a Browser");
		// Retrieve the config values used
		ImmutableSortedSet<String> filterAttributes =
		        configuration.getCrawlRules().getPreCrawlConfig().getFilterAttributeNames();
		long crawlWaitReload = configuration.getCrawlRules().getWaitAfterReloadUrl();
		long crawlWaitEvent = configuration.getCrawlRules().getWaitAfterEvent();

		// Determine the requested browser type
		EmbeddedBrowser browser;
		EmbeddedBrowser.BrowserType browserType = configuration.getBrowserConfig().getBrowsertype();
		try {
			switch (browserType) {
				case CHROME:
					browser = newChromeBrowser(filterAttributes, crawlWaitReload, crawlWaitEvent);
					break;
				case CHROME_HEADLESS:
					browser = newChromeHeadlessBrowser(filterAttributes, crawlWaitReload, crawlWaitEvent);
					break;
				case REMOTE:
					browser = newRemoteDriver(filterAttributes, crawlWaitReload, crawlWaitEvent);
					break;
				case REMOTE_HEADLESS:
					browser = newRemoteHeadlessDriver(filterAttributes, crawlWaitReload, crawlWaitEvent);
					break;
				default:
					throw new IllegalStateException("Unrecognized browsertype "
					        + configuration.getBrowserConfig().getBrowsertype());
			}
		} catch (IllegalStateException e) {
			LOGGER.error("Crawling with {} failed: " + e.getMessage(), browserType.toString());
			throw e;
		}
		plugins.runOnBrowserCreatedPlugins(browser);
		return browser;
	}

	private EmbeddedBrowser newChromeBrowser(ImmutableSortedSet<String> filterAttributes,
	        long crawlWaitReload, long crawlWaitEvent) {
		ChromeDriver driverChrome;
		if (configuration.getProxyConfiguration() != null
		        && configuration.getProxyConfiguration().getType() != ProxyType.NOTHING) {
			ChromeOptions optionsChrome = new ChromeOptions();
			String lang = configuration.getBrowserConfig().getLangOrNull();
			if (!Strings.isNullOrEmpty(lang)) {
				optionsChrome.addArguments("--lang=" + lang);
			}
			optionsChrome.addArguments("--proxy-server=http://"
			        + configuration.getProxyConfiguration().getHostname() + ":"
			        + configuration.getProxyConfiguration().getPort());
			driverChrome = new ChromeDriver(optionsChrome);
		} else {
			driverChrome = new ChromeDriver();
		}

		return WebDriverBackedEmbeddedBrowser.withDriver(driverChrome, filterAttributes,
		        crawlWaitEvent, crawlWaitReload);
	}

	private EmbeddedBrowser newRemoteDriver(ImmutableSortedSet<String> filterAttributes,
											 long crawlWaitReload, long crawlWaitEvent) {
		try{
			WebDriver driver;
			if (configuration.getProxyConfiguration() != null
					&& configuration.getProxyConfiguration().getType() != ProxyType.NOTHING) {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				ChromeOptions optionsChrome = new ChromeOptions();
				String lang = configuration.getBrowserConfig().getLangOrNull();
				if (!Strings.isNullOrEmpty(lang)) {
					optionsChrome.addArguments("--lang=" + lang);
				}
				optionsChrome.addArguments("--proxy-server=http://"
						+ configuration.getProxyConfiguration().getHostname() + ":"
						+ configuration.getProxyConfiguration().getPort());
				capabilities.setCapability(ChromeOptions.CAPABILITY, optionsChrome);
				URL url = new URL(configuration.getBrowserConfig().getRemoteHubUrl());
				driver = new RemoteWebDriver(url, capabilities);
			} else {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				ChromeOptions optionsChrome = new ChromeOptions();
				capabilities.setCapability(ChromeOptions.CAPABILITY, optionsChrome);
				URL url = new URL(configuration.getBrowserConfig().getRemoteHubUrl());
				driver = new RemoteWebDriver(url, capabilities);
			}

			return WebDriverBackedEmbeddedBrowser.withDriver(driver, filterAttributes,
					crawlWaitEvent, crawlWaitReload);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		throw new IllegalStateException("MalformedURL");
	}

	private EmbeddedBrowser newRemoteHeadlessDriver(ImmutableSortedSet<String> filterAttributes,
											long crawlWaitReload, long crawlWaitEvent) {
		try{
			WebDriver driver;
			if (configuration.getProxyConfiguration() != null
					&& configuration.getProxyConfiguration().getType() != ProxyType.NOTHING) {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				ChromeOptions optionsChrome = new ChromeOptions();
				String lang = configuration.getBrowserConfig().getLangOrNull();
				if (!Strings.isNullOrEmpty(lang)) {
					optionsChrome.addArguments("--lang=" + lang);
				}
				optionsChrome.addArguments("--proxy-server=http://"
						+ configuration.getProxyConfiguration().getHostname() + ":"
						+ configuration.getProxyConfiguration().getPort());
				optionsChrome.addArguments("--headless", "--disable-gpu", "--window-size=1200x600");
				capabilities.setCapability(ChromeOptions.CAPABILITY, optionsChrome);
				URL url = new URL(configuration.getBrowserConfig().getRemoteHubUrl());
				driver = new RemoteWebDriver(url, capabilities);
			} else {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				ChromeOptions optionsChrome = new ChromeOptions();
				optionsChrome.addArguments("--headless", "--disable-gpu", "--window-size=1200x600");
				capabilities.setCapability(ChromeOptions.CAPABILITY, optionsChrome);
				URL url = new URL(configuration.getBrowserConfig().getRemoteHubUrl());
				driver = new RemoteWebDriver(url, capabilities);
			}

			return WebDriverBackedEmbeddedBrowser.withDriver(driver, filterAttributes,
					crawlWaitEvent, crawlWaitReload);
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
		throw new IllegalStateException("MalformedURL");
	}



	private EmbeddedBrowser newChromeHeadlessBrowser(ImmutableSortedSet<String> filterAttributes,
											 long crawlWaitReload, long crawlWaitEvent) {
		ChromeOptions optionsChrome = new ChromeOptions();
		optionsChrome.addArguments("--headless", "--disable-gpu", "--window-size=1200x600");
		ChromeDriver driverChrome = new ChromeDriver(optionsChrome);

		return WebDriverBackedEmbeddedBrowser.withDriver(driverChrome, filterAttributes,
				crawlWaitEvent, crawlWaitReload);
	}

	private EmbeddedBrowser newPhantomJSDriver(ImmutableSortedSet<String> filterAttributes,
	        long crawlWaitReload, long crawlWaitEvent) {

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--webdriver-loglevel=WARN"});
		final ProxyConfiguration proxyConf = configuration
				.getProxyConfiguration();
		if (proxyConf != null && proxyConf.getType() != ProxyType.NOTHING) {
			final String proxyAddrCap = "--proxy=" + proxyConf.getHostname()
					+ ":" + proxyConf.getPort();
			final String proxyTypeCap = "--proxy-type=http";
			final String[] args = new String[] { proxyAddrCap, proxyTypeCap };
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, args);
		}
		
		PhantomJSDriver phantomJsDriver = new PhantomJSDriver(caps);

		return WebDriverBackedEmbeddedBrowser.withDriver(phantomJsDriver, filterAttributes,
		        crawlWaitEvent, crawlWaitReload);
	}
}
