AstroSmash
=============

Astrosmash is a video game for the Intellivision videogame console, designed by John Sohl, and released by Mattel Electronics in 1981. The game involves using a laser cannon to destroy falling meteors, bombs, and other targets. More than one million copies were sold, and the game replaced Las Vegas Poker & Blackjack as the game included with the Intellivision console. - [Wikipedia](https://en.wikipedia.org/wiki/Astrosmash).

![AstroSmash running on Motorola Photon Q](images/AstroSmash_motorola_photon_q.jpg)

This is my port of AstroSmash from Java ME (J2ME) MIDlet to Android OS application with using SurfaceView Canvas drawing. I added touch controls and some game engine improvements. I used the [Java Decompiler](https://en.wikipedia.org/wiki/Java_Decompiler) to understand the logic and restore the behavior of the original game.

![AstroSmash Android Screen](images/AstroSmash_android_screen.png)

## Download

You can download APK-package for Android OS from the [releases](https://github.com/EXL/AstroSmash/releases) section.

## Build instructions

For example, GNU/Linux:

* Install the latest [Android SDK](https://developer.android.com/sdk/);

* Clone repository into deploy directory;

```sh
cd ~/Deploy/
git clone https://github.com/EXL/AstroSmash AstroSmashAndroid
```

* Edit "project.properties" file and set the latest Android API SDK version here, for example:

```sh
# Project target.
target=android-23
```

* Build the APK-package into deploy directory;

```sh
cd ~/Deploy/AstroSmashAndroid/
/opt/android/android-sdk-linux/tools/android update project -n AstroSmash -p .
/opt/android/apache-ant-1.9.4/bin/ant debug
```

* Install AstroSmash APK-package on your Android device via adb;

```sh
cd ~/Deploy/AstroSmashAndroid/
/opt/android/android-sdk-linux/platform-tools/adb install -r bin/AstroSmash-debug.apk
```

* Run and enjoy!

You can also import this project in your favorite IDE: Eclipse or Android Studio and build the APK-package by using these programs.

## More information

Please read [Porting Guide (In Russian)](http://exlmoto.ru/astrosmash-droid) for more info about porting AstroSmash to Android OS.
