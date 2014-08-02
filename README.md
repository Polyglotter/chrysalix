#Polyglotter Projects

Polyglotter is a set of libraries offering pluggable support for transforming data from one form to another.

## Get Involved

Have a question? Want to find out more about Polyglotter?  Hop into our IRC chat room at **irc.freenode.net#polyglotter** and talk to our community of contributors and users.

## Contribute fixes and features

Polyglotter is open source, and we welcome anybody that wants to participate and contribute! If you want to fix a bug or make any changes, just submit them via a pull-request from your [fork](https://github.com/Polyglotter/polyglotter/fork). Also, make sure to read our [developer documentation](https://github.com/Polyglotter/polyglotter/wiki/Developer-Documentation).
  
## Reporting Bugs and Requesting Enhancements

If you want to report a bug or request an enhancement, please log a new issue in the [GitHub issue tracker](https://github.com/Polyglotter/polyglotter/issues/new) describing the bug or new feature.

## Building Polyglotter

We use Maven to build our software. The following command compiles all the code, installs the JARs into your local Maven repository, and runs all of the unit tests:

    $ mvn clean install

Running all of the tests may take a while so, alternatively, you can specify `-Dskip.integration.tests=true` to skip all integration tests, which generally take longer to run:

    $ mvn clean install -Dskip.integration.tests=true
    
You may also skip unit tests by adding `-Dskip.unit.tests=false`:

    $ mvn clean install -Dskip.unit.tests=true
