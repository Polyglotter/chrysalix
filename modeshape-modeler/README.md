##[![Build Status](https://drone.io/github.com/Polyglotter/modeshape-modeler/status.png)](https://drone.io/github.com/Polyglotter/modeshape-modeler/latest) [Downloads](https://drone.io/github.com/Polyglotter/modeshape-modeler/files)

#ModeShape Modeler Projects

ModeShape Modeler is an API that allows applications to manage files, and particularly models, in an embedded ModeShape workspace. The projects temporarily reside here until we find an appropriate home (like in the ModeShape project).

## Use Modeler ModeShape

[Download](https://drone.io/github.com/Polyglotter/modeshape-modeler/files) the latest build and try it out.  See our [user documentation](https://github.com/Polyglotter/modeshape-modeler/wiki/User-Documentation) for how to get started.

## Get Involved

Have a question? Want to find out more about ModeShape Modeler?  Hop into our IRC chat room at **irc.freenode.net#polyglotter** and talk to our community of contributors and users.

## Contribute fixes and features

ModeShape Modeler is open source, and we welcome anybody that wants to participate and contribute! If you want to fix a bug or make any changes, just submit them via a pull-request from your [fork](https://github.com/Polyglotter/modeshape-modeler/fork). Also, make sure to read our [developer documentation](https://github.com/Polyglotter/modeshape-modeler/wiki/Developer-Documentation).
  
## Reporting Bugs and Requesting Enhancements

If you want to report a bug or request an enhancement, please log a new issue in the [GitHub issue tracker](https://github.com/Polyglotter/modeshape-modeler/issues/new) describing the bug or new feature.

## Building

We use Maven to build our software. The following command compiles all the code, installs the JARs into your local Maven repository, and runs all of the unit tests:

	$ mvn clean install

Running all of the tests may take a while so, alternatively, you can specify `-Dskip.unit.tests=true` to skip all tests:

    $ mvn clean install -Dskip.unit.tests=true
    
Integration tests are long-running and many depend on on-line resources, so they are skipped by default. However, you may still include them by adding `-Dskip.integration.tests=false`:

    $ mvn clean install -Dskip.integration.tests=false
	
This command can then be used to run *just* the integration tests:

	$ mvn verify -Pintegration
