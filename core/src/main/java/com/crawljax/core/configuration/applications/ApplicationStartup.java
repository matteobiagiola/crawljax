package com.crawljax.core.configuration.applications;

import com.crawljax.browser.EmbeddedBrowser;
import org.openqa.selenium.By;

public class ApplicationStartup {

    private ApplicationNames applicationName;
    private EmbeddedBrowser browser;

    public ApplicationStartup(ApplicationNames applicationName, EmbeddedBrowser embeddedBrowser){
        this.applicationName = applicationName;
        this.browser = embeddedBrowser;
    }

    // login user since Crawljax seems having problems
    public void start(){
        if(this.applicationName.value().equals(ApplicationNames.DIMESHIFT.value())){
            this.startDimeshift();
        }else if(this.applicationName.value().equals(ApplicationNames.PAGEKIT.value())){
            this.startPagekit();
        }else if(this.applicationName.value().equals(ApplicationNames.PHOENIX.value())){
            this.startPhoenix();
        }else if(this.applicationName.value().equals(ApplicationNames.SPLITTYPIE.value())){
            this.startSplittypie();
        }else if(this.applicationName.value().equals(ApplicationNames.RETROBOARD.value())){
            this.startRetroboard();
        }
    }

    private void startDimeshift(){
        this.browser.getWebElement(By.xpath("//a[@href=\"/user/signin\"]")).click();
        this.browser.getWebElement(By.id("input_username")).sendKeys("asd@asd.com");
        this.browser.getWebElement(By.id("input_password")).sendKeys("asdfghjkl123");
        this.browser.getWebElement(By.xpath("//input[@value=\"Sign In\"]")).click();
    }

    private void startPagekit(){
        this.browser.getWebElement(By.id("username")).sendKeys("admin");
        this.browser.getWebElement(By.id("password")).sendKeys("asdfghjkl123");
        this.browser.getWebElement(By.xpath("//button[text()=\"Login\"]")).click();
    }

    private void startPhoenix(){
        this.browser.getWebElement(By.xpath("//button[text()=\"Sign in\"]")).click();
    }

    private void startSplittypie(){

    }

    private void startRetroboard(){
        this.browser.getWebElement(By.xpath("//input[@type=\"input\"]")).sendKeys("Admin");
        this.browser.getWebElement(By.xpath("//button[@type=\"button\"]")).click();
    }
}
