# SnaptionVerticalPrototype

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
build the project. Go ahead and install everything it asks you to.
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

### Troubleshooting

If you have any questions feel free to ask me or anyone else who has
gotten the project to build successfully!

### Successful Builders
* Tyler Wong
* Javon Negahban
* Nick Romero
