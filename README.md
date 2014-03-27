flow-android
============

Android app for [UW Flow](https://uwflow.com).

This project uses the [Gradle build system](http://www.gradle.org).

## Setup

1. Go to gradle.org and get the 1.10 (tested) or later versions (not sure if it will work)
1. Unzip the file
1. Open Intellij (or Android Studio)
1. Select "Import new project"
1. Select "Import project from external model"
1. Pick Gradle -> next
1. Select Use local gradle distribution and set gradle home to the unzipped tar file location
1. Let Intellij download gradle tools
1. Done! You can press run to deploy to a device or simulator



NOTE: IF YOU CHANGE ANYTHING IN ANY OF THE DATABASE FILES OR ADD NEW DATABASE FILES
YOU MUST RE-RUN THE MAIN METHOD IN DatabaseConfigUtil class in the util folder
Use Run as a different java application! Not the same CONTEXT as flow.
