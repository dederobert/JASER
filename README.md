<h1 align="center">JASER - Java Servers</h1>
<p align="center">
    A collection of Java servers.
</p>
<p align="center">
    <img alt="forthebadge" src="https://forthebadge.com/images/badges/made-with-java.svg" />
    <img alt="forthebadge" src="https://forthebadge.com/images/badges/built-by-developers.svg" />
</p>    
<p align="center">
    <a href="https://github.com/dederobert/JASER/actions/workflows/maven.yml" title="Java CI with Maven"><img alt="Java CI with Maven" src="https://github.com/dederobert/JASER/actions/workflows/maven.yml/badge.svg?branch=main" /></a>
    <img alt="GitHub issues" src="https://img.shields.io/github/issues/dederobert/JASER">    
    <img alt="Java version" src="https://img.shields.io/badge/java%20-%3E%3D%2017-brightgreen" />
    <br />
    <a href="https://github.com/dederobert/JASER/wiki" title="Wiki">Explore the Wiki</a>
     . 
    <a href="https://github.com/dederobert/JASER/issues/new?assignees=&labels=priority%3A+medium%2C+type%3A+bug&template=bug_report.md&title=%5BBUG%5D" title="Issue">Report an issue</a>
     . 
    <a href="https://github.com/dederobert/JASER/issues/new?assignees=&labels=priority%3A+low%2C+type%3A+feature+request&template=feature_request.md&title=%5BFEAT%5D" title="Feature">Request Feature</a>
</p>

# Table of Contents

* [Table of Contents](#table-of-contents)
* [About this project](#about-this-project)
* [Version](#version)
* [Built with](#built-with)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
* [Usage](#usage)
* [Structure of the project](#structure-of-the-project)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)
* [Acknowledgements](#acknowledgements)

# About this project

JASER is a collection of several Java servers. Currently, it consists of:

- A DNS server (`/dns`) that can be used to resolve DNS queries based on master files.

# Version

> For a detailed informations about changes, see the [changelog](CHANGELOG.md).

## Arctic tern (1.0.0) - 29/07/2022

- [GitHub release notes](https://github.com/dederobert/JASER/releases/tag/v1.0.0-arctic-tern)
- [Changelog](CHANGELOG.md#v1-0-0-arctic-tern)

First release of JASER. It is a DNS server that can be used to resolve DNS queries based on master files.

# Built with

This project is built with the following technologies:

* [Maven](https://maven.apache.org/) - The Maven build system.
* [Java](https://www.java.com/) - The Java programming language.
* [Handlebars](https://handlebarsjs.com/) - The Handlebars templating engine.

And the following libraries:

* [Picocli](https://picocli.github.io/) - A command line interface library for Java.
* [Micrometer](https://micrometer.io/) - A metrics library for Java.
* [LOG4J](https://logging.apache.org/log4j/) - A logging framework for Java.
* [Cucumber](https://cucumber.io/) - A BDD framework for Java.

And the following tools:

* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - A Java IDE.
* [Wireshark](https://www.wireshark.org/) - A network protocol analyzer.
* [GitHub Actions](https://github.com/features/actions) - A continuous integration tool for GitHub.
* [Jira](https://www.atlassian.com/software/jira/) - A ticketing system.

# Getting Started

## Prerequisites

In order to setup the project, you need to have the following prerequisites:

* [maven](https://maven.apache.org/) (version 3.8.0 or higher)
* [Java](https://www.java.com/) (version 17 or higher)

## Installation

1. Clone the repository.

````shell
> git clone https://github.com/dederobert/JASER.git
````

3. Move to the root directory of the repository.

````shell
> cd JASER
````

4. Run `mvn clean install` to build the project.

```shell
    > mvn clean install
```

# Usage

## Start DNS Server

After the project is built, you can start the DNS server with:

```shell
  > cd dns
  > java -jar target/jaser-dns-X.X.X.jar -M --metrics-address=localhost --metrics-port=8080 -F=example.com.zone localhost 53
```

with the following options:

- `-M`: Enable metrics _(default: disabled)_.
- `--metrics-address=localhost`: The address to bind the metrics server to _(default: localhost)_.
- `--metrics-port=8080`: The port to bind the metrics server to _(default: 8080)_.

* `-F`: The zone files to use _(mandatory)_.
* `localhost`: The IP address of the DNS server _(default: `localhost`)_.
* `53`: The port of the DNS server _(default: `53`)_.

## DNS server usage

When the DNS server is started, you can use the following commands:

- `h` or `help`: Show the help.
- `r` or `records`: Show the records.
- `q` or `quit`: Quit the DNS server.

# Structure of the project

<img src="https://raw.githubusercontent.com/dederobert/JASER/main/docs/structure.png" alt="Structure of the project" />

The project is divided into the following modules:

- [BOM](#bom) Bill of Materials: The list of dependencies.
- [Core](#core) the core module
- [DNS Server](#dns-server) the DNS server

# Contributing

If you want to contribute to the project, your help is welcome. It is a good opportunity to learn more about the project and to get involved.

## How to make a pull request
* Fork the repository.
* Clone the fork on your local machine.
* if you forked the repository long ago, you might have to pull the latest changes.
* Create a new branch from `main` and switch to it. You can use the `git checkout -b <branch-name>` command.
* Make your changes. __(see the [contributing guide](CONTRIBUTING.md))__
* Run the tests if there are any. You can use the `mvn test` command.
* Update the documentation.
* Commit your changes. Follow the [commit guidelines](CONTRIBUTING.md#commit-guidelines).
* Push your changes on your fork. You can use the `git push origin <branch-name>` command.
* From your fork, create a pull request with `main` as the base branch and your new branch as the head branch.
* If changes are requested by reviewers, push them on your branch.
* If the pull request is accepted, the changes will be merged into the main branch. 

# License

Distributed under the [CeCILL License](LICENCE.md) version 2.1.

# Authors

* __Lehtto__ - [GitHub](https://github.com/dederobert)
