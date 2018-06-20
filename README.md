Crawljax 
========

Crawljax is a tool for automatically crawling and testing modern web applications. 
Crawljax can explore any JavaScript-based Ajax web application through an event-driven dynamic crawling engine.
It produces as output a state-flow graph of the dynamic DOM states and the event-based transitions between them.
Crawljax can easily be extended through its easy-to-use [plugin architecture](https://github.com/crawljax/crawljax/wiki/Writing-a-plugin).

For more project info visit the [Crawljax website](http://crawljax.com).


Documentation
-------------

You can find more technical documentation in our [project wiki](https://github.com/crawljax/crawljax/wiki/). 


Community
---------

Keep track of development and community news.

* Follow [@crawljax](https://twitter.com/crawljax) on Twitter.
* Read the Crawljax [Blog](http://crawljax.com/).
* Need a new feature or bug report? [Use the issue tracker](https://github.com/crawljax/crawljax/issues).
* Have a question that's not a feature request or bug report? Ask on the [mailing list](https://groups.google.com/group/crawljax).

License
-------

This project is licensed under the ["Apache License, Version 2.0"](https://github.com/crawljax/crawljax/blob/master/LICENSE).

Changelog
---------

Detailed change history is avaialbe in our [changelog](https://github.com/crawljax/crawljax/blob/master/CHANGELOG.md).


[![Analytics](https://ga-beacon.appspot.com/UA-12224196-2/crawljax/crawljax?pixel)](https://github.com/igrigorik/ga-beacon)


Testing
-------

You can run the tests using maven with the command

	mvn clean package
	
If you want to run all the test, including browser tests, run 

	mvn clean test -P integrationtests

The default driver used for testing uses [PhantomJS](http://phantomjs.org) so make sure you have PhantomJS installed on your machine. You can also specify which browser you'd like to use with the `test.browser` variable. For example, if you want to test with Firefox, use:

	mvn clean test -P integrationtests -Dtest.browser=FIREFOX

Again, make sure a recent version of Firefox is installed on your machine before running the tests.	

Changes w.r.t the original repository
-------
- Fix old dependencies so that it is possible to compile the project;
- Option to run Crawljax using chrome headless;
- Login to web applications before starting crawling: possible for 5 applications (see package `com.crawljax.core.configuration.applications`). I noticed that Crawljax has problems with applications where a login is needed. Even if Crawljax has the right credentials to login, it stops some seconds after the login. The reason is still a mistery for me. In order to make it work I modified the Crawljax core in such a way that it starts crawling the application after the login is done; in this way I managed to find a configuration to make Crawljax crawl an application for at least 30 minutes. The login is specific for each application so if I want to test a new application I have to modify the Crawljax core to add the login process for that application.

Installation
-------

Once the repository is cloned run `mvn clean install -DskipTests` to install the dependencies. Now the modified version of
Crawljax can be imported as a maven dependencies in a java project in this way:

```xml
<dependency>
    <groupId>com.crawljax</groupId>
    <artifactId>crawljax-core</artifactId>
    <version>3.7-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.crawljax.plugins</groupId>
    <artifactId>crawloverview-plugin</artifactId>
    <version>3.7-SNAPSHOT</version>
</dependency>
```
