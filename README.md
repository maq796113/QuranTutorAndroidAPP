# QuranTutorAI

This is an Android mobile application that helps reciters of the Holy Quran track their proficiency by measuring their BLEU score using the natural language toolkit. The application was developed using Android Studio Kotlin Material Design Component XML Layout, and makes use of asynchronous functions from Coroutines for parallelism and faster response times for API calls from the Restful API built with Python 3.10 using FastAPI web framework.

# Features

-   BLEU score measurement using natural language toolkit
-   Integration with Saas Data Store from Google for efficient data storage
-   Authentication from FireStore for secure login and user management
-   Dependency Injection with Dagger Hilt for reducing boilerplate, decoupling build dependencies and simplifying configuration
-   Asynchronous Flow for Stateflow to improve efficiency and response times

# Installation

To install this application on your Android device, follow these steps:

1.  Clone this repository
2.  Open the project in Android Studio
3.  Connect your Android device to your computer via USB
4.  Click on the Run button in Android Studio and select your device from the list of available devices

Alternatively, you can download the APK file from the releases section and install it directly on your Android device.

# Usage

After installing the application on your Android device, you can launch it and start tracking your Quranic recitation proficiency. The application will prompt you to recite 8 verses of the Quran, and then measure your BLEU score using the natural language toolkit. You can view your proficiency score in the application and track your progress over time.

# Contributions

Contributions to this project are welcome. If you find any bugs or issues with the application, please open an issue on this repository. If you would like to contribute code or new features, please submit a pull request.
