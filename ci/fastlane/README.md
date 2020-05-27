fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
## Android
### android lint_debug
```
fastlane android lint_debug
```
Run lint for debug build
### android lint_release
```
fastlane android lint_release
```
Run lint for release build
### android assemble_release
```
fastlane android assemble_release
```
Assemble release
### android distribute
```
fastlane android distribute
```
Distribute app to firebase
### android assemble_debug
```
fastlane android assemble_debug
```
Assemble debug
### android tests
```
fastlane android tests
```
Runs all the tests

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
