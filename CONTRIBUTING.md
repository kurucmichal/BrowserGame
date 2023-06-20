# Contributing to the project

## Restricted branches
- `development` - all the implemented features which are done and deployed
- `master` - stable version deployed

## Workflow

1. Create a feature branch when you start to work on a story and commit your changes to this
2. Push this frequently to the remote repository from your local
2. When the feature is done, create a Pull Request from the `feature_branch` to `development`, follow the guidelines
3. When the PR is approved, merge it, and delete your feature branch

## Git Commit Guidelines

Read this article how to write meaningful commit messages:
[How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)

We follow the rules below:
- **Commit message format**: `<type>(): <subject>`

-  **Type**: Must be one of the following:
   -  **feat**: A new feature implementation
   -  **fix**: A bug fix
   -  **refactor**: A code change that neither fixes a bug nor adds a feature
   -  **test**: Adding missing or correcting existing tests
   -  **chore**: Changes to the build process or auxiliary tools and libraries such as documentation generation

- **Subject**: Changes in the commit

## Pull Request guidelines

- From `feature_branch` to `development`: add two developers and PM as reviewers, 3 approves needed for merging
- From `development` to `master`: this is managed by the PM
