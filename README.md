<h1 align="center">JASER - Java Servers</h1>

<p align="center">
    <img alt="forthebadge" src="https://forthebadge.com/images/badges/made-with-java.svg" />
    <img alt="forthebadge" src="https://forthebadge.com/images/badges/built-by-developers.svg" />
</p>    
<p align="center">
    <a href="https://github.com/dederobert/JASER/actions/workflows/maven.yml" title="Java CI with Maven"><img alt="Java CI with Maven" src="https://github.com/dederobert/JASER/actions/workflows/maven.yml/badge.svg?branch=main" /></a>
    <img alt="Java version" src="https://img.shields.io/badge/java%20-%3E%3D%2017-brightgreen" />
</p>

Collection of several Java servers.

## About

Jaser is a collection of several servers written in Java. It includes:

- DNS Server

Planned servers:

- DHCP Server

### Structure of the project

<img src="https://raw.githubusercontent.com/dederobert/JASER/main/docs/structure.png" alt="Structure of the project" />

Modules :

- [BOM](#bom) Bill of Materials
- [Core](#core) the core module
- [DNS Server](#dns-server) the DNS server

## BOM

The Bill of Materials (BOM) is a list of all dependencies of the project.

## Core

Core is the core module of the project. It contains generic UDP and TCP servers classes and some utility classes.

## DNS Server

DNS Server is a server that answers DNS queries.