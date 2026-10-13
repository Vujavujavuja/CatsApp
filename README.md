# CatsApp

An Android app for browsing cat breeds, built with Kotlin and Jetpack Compose on top of [The Cat API](https://thecatapi.com/). First homework for the Mobile Application Development course (RMA).

## Features

- Searchable list of cat breeds, with the search debounced as you type
- Breed details: photo, description, temperament, weight and alternative names
- Trait ratings such as energy level, affection, and how the breed gets on with children, dogs and strangers
- Wikipedia link for each breed, opened in a custom tab

## How it's built

- Jetpack Compose with Material 3 and Compose Navigation
- MVI-style screens: each one has a `Contract` (state and events), a `ViewModel` and a composable
- Retrofit and OkHttp for networking, Coil for images
- Kotlin coroutines and `StateFlow` for state

## Running it

1. Get a free API key from [thecatapi.com](https://thecatapi.com/).
2. Add it to `local.properties` in the project root (this file is not committed):

   ```properties
   CAT_API_KEY=your_key_here
   ```

3. Open the project in Android Studio and run the `app` configuration. Minimum SDK is 24.
