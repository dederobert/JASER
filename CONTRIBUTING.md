# Contributing

When contributing to this repository, please first discuss the change you wish to make via issue, email, or any other method with the owners of this repository before making a change.

Please note we have a code of conduct, please follow it in all your interactions with the project.

## Table of Contents

* [Commit guidelines](#commit-guidelines)
* [How to make a pull request](#how-to-make-a-pull-request)

## Code format

This project uses the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). You can configure intellij to use this guide:

- in the `IntelliJ IDEA` menu, select `Editor`, `Settings`, `Code Style`
- in settings, select option `Import Scheme` > `IntelliJ IDEA code style XML`
- select the file `google-java-style.xml`

## Commit guidelines

Our guidelines for committing changes to the project are as follows:

* use the imperative, present tense form of the action. e.g. `Change` not `Changes` nor `Changed`
* don't capitalize the first letter
* no period at the end

### Commit messages structure

The commit message should be in the following format:

```
<type>(<scope>): <subject> <icon>
<BLANK LINE>
<summary>
<BLANK LINE>
<footer>
```

Where:

* \<type> is one of the following:
    * `feat`: a new feature
    * `fix`: a bug fix
    * `docs`: documentation only changes
    * `style`: changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
    * `refactor`: A code change that neither fixes a bug or adds a feature.
    * `perf`: A code change that improves performance or size.
    * `test`: Adding missing tests or correcting existing tests.
    * `build`: Changes that affect the build system or external dependencies.
    * `ci`: Changes to our continuous integration system.
    * `chore`: Changes to the build process or auxiliary tools and libraries.
    * `revert`: Revert a previous commit.
* \<icon> emoticon that represents the type of change.
    * ✨ = feat
    * 🐛 = fix
    * 📚 = docs
    * 💎 = style
    * 📦 = refactor
    * 🚀 = perf
    * 🚨 = test
    * 🛠 = build
    * ⚙️ = ci
    * ♻️ = chore
    * 🗑 = revert
* \<scope> is a comma separated list of strings indicating the scope of the change.
* \<subject> is a short description of the change.
* \<summary> is a longer description of the change.
* \<footer> commit footer

#### Commit footer

```
BREAKING CHANGE: <description>
<BLANK LINE>
<breaking-change-body>
<BLANK LINE>
<BLANK LINE>
Fixes: <issue-list>
```

Where:

* \<description> is a short description of the breaking change.
* \<breaking-change-body> is a longer description of the breaking change. It also contains migration instructions.
* \<issue-list> is a comma separated list of issue numbers that are fixed by this change.

## How to make a pull request

* Fork the repository.
* Clone the fork on your local machine.
* if you forked the repository long ago, you might have to pull the latest changes.
* Create a new branch from `main` and switch to it. You can use the `git checkout -b <branch-name>` command.
* Make your changes.
* Run the tests if there are any. You can use the `mvn test` command.
* Update the documentation.
* Commit your changes. Follow the [commit guidelines](#commit-guidelines).
* Push your changes on your fork. You can use the `git push origin <branch-name>` command.
* From your fork, create a pull request with `main` as the base branch and your new branch as the head branch.
* If changes are requested by reviewers, push them on your branch.
* If the pull request is accepted, the changes will be merged into the main branch. 