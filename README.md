# Flow Android App

Android app for [UW Flow](https://uwflow.com): plan your University of Waterloo courses with friends.

<a href="https://play.google.com/store/apps/details?id=com.uwflow.flow_android">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

## Setup

This project uses the [Gradle build system](http://www.gradle.org).

The following instructions are tested for Intellij on Mac OS X and Windows:

1. Ensure you have
   [the latest Android SDKs and build tools installed](https://developer.android.com/sdk/index.html).
1. Create a file in the project root directory called `local.properties` and add the line `sdk.dir=/path/to/your/sdk`.
1. Download [Gradle](http://www.gradle.org/downloads) (tested with version 1.10).
1. Open Intellij (or Android Studio). Ensure you have the Gradle Intellij plugin.
1. Select "File" > "Import project".
1. Pick the `build.gradle` file in this repository.
1. Select "Use local Gradle distribution" and set Gradle home to the unzipped file location.
1. Let Intellij download Gradle tools and project dependencies.
1. Done! You can press run to deploy to a device or simulator.

### Changes to database files

If you change any of the database files or add new ones, you must re-run the main method in
`util.DatabaseConfigUtil` to update the DB schema file. Note that this should be run as a different Java application.

## Authors

This app was originally built during Waterloo's
[Winter 2014 CS 446 class](https://cs.uwaterloo.ca/~rtholmes/teaching/2014winter/cs446/index.html), by:

- [Jasper Fung](https://github.com/jasperfung)
- [David Hu](https://github.com/divad12)
- [Wentao Ji](https://github.com/w2ji)
- [Chinmay Raval](https://github.com/ChinmayR)
