##[![Build Status](https://drone.io/github.com/Polyglotter/polyglotter-eclipse/status.png)](https://drone.io/github.com/Polyglotter/polyglotter-eclipse/latest) [Downloads](https://drone.io/github.com/Polyglotter/polyglotter-eclipse/files)

#Polyglotter Eclipse

Polyglotter Eclipse is a set of libraries offering pluggable support for transforming data from one form to another.

## Use Polyglotter

[Download](https://drone.io/github.com/Polyglotter/polyglotter-eclipse/files) the latest builds for the polyglotter eclipse projects and try it out.  See our [user documentation](https://github.com/Polyglotter/polyglotter-eclipse/wiki/User-Documentation) for how to get started.

## Get Involved

Have a question? Want to find out more about Polyglotter Eclipse?  Hop into our IRC chat room at **irc.freenode.net#polyglotter** and talk to our community of contributors and users.

## Contribute fixes and features

Polyglotter Eclipse is open source, and we welcome anybody that wants to participate and contribute! If you want to fix a bug or make any changes, just submit them via a pull-request from your [fork](https://github.com/Polyglotter/polyglotter-eclipse/fork). Also, make sure to read our [developer documentation](https://github.com/Polyglotter/polyglotter-eclipse/wiki/Developer-Documentation).
  
## Reporting Bugs and Requesting Enhancements

If you want to report a bug or request an enhancement, please log a new issue in the [GitHub issue tracker](https://github.com/Polyglotter/polyglotter-eclipse/issues/new) describing the bug or new feature.

## Building

We use Maven to build our software. The following command compiles all the code, installs the JARs into your local Maven repository, and runs all of the unit tests:

	$ mvn clean install

Running all of the tests may take a while so, alternatively, you can specify `-Dskip.unit.tests=true` to skip all tests:

    $ mvn clean install -Dskip.unit.tests=true
    
Integration tests are long-running and many depend on on-line resources, so they are skipped by default. However, you may still include them by adding `-Dskip.integration.tests=false`:

    $ mvn clean install -Dskip.integration.tests=false
	
This command can then be used to run *just* the integration tests:

	$ mvn verify -Pintegration
