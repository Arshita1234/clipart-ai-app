# Clipart AI Generator (Android)

## Overview

An Android app that allows users to upload an image and generate multiple clipart-style variations such as cartoon, anime, pixel, sketch, etc.

## Features

* Upload image (gallery)
* Multi-style generation (parallel)
* Grid UI with loading states
* Download generated images
* Share via native share sheet

## Tech Stack

* Kotlin
* Jetpack Compose
* Coroutines
* Coil
* MediaStore API

## AI Generation Approach

Due to time constraints and API reliability during demo, the app currently uses a local rendering engine to simulate clipart styles.

The architecture is designed to integrate with real AI APIs such as:

* Replicate
* OpenAI Image APIs

## Setup Instructions

1. Clone repo
2. Open in Android Studio
3. Run on device/emulator

## APK

https://drive.google.com/drive/folders/1IallqLDaSseNfihWt_p_QNQvvxwunpkQ?usp=drive_link

## Screen Recording

https://drive.google.com/file/d/1uyzYMmvadk0fDMqIv_C6ewIJe97UlJss/view?usp=sharing

## Tradeoffs

* Used local rendering instead of live AI API for stability
* Focused on UX, async handling, and performance

## Future Improvements

* Integrate real AI image generation
* Improve style realism
* Add caching & history
