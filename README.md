# Snaption Vertical Prototype - Fightin' Puffins

[![Build Status](https://travis-ci.com/tylerbwong/SnaptionVerticalPrototype.svg?token=AABXGtYrzS4uRtMAUqq3&branch=master)](https://travis-ci.com/tylerbwong/SnaptionVerticalPrototype)

This is the vertical prototype for the Snaption Android Application.

## Features
* Login (Facebook and Google with Auth0)
* Snaption Upload

## Building

These are some of the necessary tools and steps that you will need to 
build the Snaption Vertical Prototype on your own machine.

### Requirements
* [Android Studio](https://developer.android.com/studio/index.html)
* [JDK 7+](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

You may be asked to install extra SDKs or Build tools upon opening the 
project in Android Studio.

### Instructions

1. To grab the latest source code from the remote master branch, run:
   ```
   git clone https://github.com/tylerbwong/SnaptionVerticalPrototype.git
   ```

2. Open up Android Studio and select `Open Existing Android Project`
3. Android Studio should then walk you through the steps to set up the
project.
4. Depending on if you have used Android Studio in the past, the IDE
may prompt you to install extra plugins and dependencies in order to 
build the project (i.e. Gradle Plugins, Android SDKs, Android Build Tools).
Go ahead and install everything it asks you to.
5. Once the project has been opened and all plugins and dependencies
have been installed, try building the project by going to `Build -> 
Rebuild Project`. Hopefully it builds!
6. Once the project builds successfully, go ahead and add your name to
the bottom of this README.md and push it to the repository to initiate 
a remote build.
7. To push your new code change to the remote master branch, run:
   ```
   git add <Any files to be commited, "." or "-A" for all>
   git commit -m "<Meaningful commit message (Note the quotes)>"
   git push
   ```
   
8. This push should initiate a remote build either on Travis CI or
Jenkins (whichever we decide to use) and hopefully it builds successfully
too!

### Running the Application

In Android Studio, you will have the option of running the application
on your compatible personal Android device, or through an emulator.

#### Through an Emulator

1. In Android Studio, go to `Tools -> Android -> AVD Manager`.
2. Click on `Create New Virtual Device`
3. Specify which kind of device you would like to run. For our purposes,
a Nexus 5X will suffice. Click `Next`.
4. You will now be asked to select an Android System Image. This is the
version of Android that the emulator will run. Select anything API 23
and above. Click `Next`.
5. The next page will show different options about the hardware of the
virtual device. Here you will be able to specify the amount of RAM, the
VM Heap size, and even the size of an SD card (external storage). Leave
everything as the default, but increase the amount of RAM to 2048 MB.
Click `Finish`. 
6. You are now ready to run the application. Find the green arrow at the
top of Android Studio and click it. It will bring up a window that will
let you choose a device to run the application on. Click the virtual
device that you just created. Click `OK`. If everything builds and you
created the virtual device correctly, the virtual device should start up
and the app should run automatically!

#### Through Your Own Device

1. In order to run the application on your own device, you will need to
enable Developer options. On your Android device, navigate to 
`Settings -> About Phone`.
2. Scroll down until you see a "Build Number". Keep tapping that row
until a message says that you are a developer.
3. Go back to the Settings page and click on `Developer Options`. Make
sure Developer Options is turned on.
4. Scroll down and make sure that `USB debugging` is enabled.
5. Now your device is ready to run your application. Plug in your device
to your computer and press the green arrow to run the application. Instead
of choosing an emulator, you should see your personal device listed under
"Connected devices". Choose your device and click `OK`.
6. If this is your first time using your device to run an app through
Android Studio, a pop-up might appear on your device asking if you would
like to accept your computer's RSA fingerprint. Check `Always remember
this computer` and click `OK`. The app will be installed and you should
be ready to run your app on your own device!

### Troubleshooting

If you have any questions feel free to ask me or anyone else who has
gotten the project to build successfully!

### Successful Builders
* Tyler Wong
* Javon Negahban
* Nick Romero
* Jacob Boyles
* Brian Gouldsberry
* Quang Ngo
