# MoviesDB
Android application using the themoviedb API

It start with a list of series where it can be paginated and can also be filtered through a search. Each item, from its detail or from the search item, can be added to a list of local subscriptions.
If a series or movie is subscribed, we will see it in a horizontal list on the main page and both its search item and its detail will reflect its status (subscribed or not).
In the detail we see the image of the series or movie (poster), which has a main color and we must cover the screen with a view that has transparency and the main color of the poster. Also notice that below the transparent view is another image. Finally, a summary of the series or movie is included, which must have contrast with the main color and must be able to be scrolled to read the detail in its entirety. When scrolling, the main image shrinks, as well as the title, the year and the subscription button becoming more transparent.

## Libraries & Architecture
This repository contains an Android project using Kotlin, MVVM + Clean Architecture, Dependency injection with Hilt, LiveData, Coroutines, Flow, Retrofit2, ROOM Database, Glide images, Data Binding, Navigation components, Material design, Color Palette, Recyclerview, Collapsing toolbar and Lottie Animations.

## App Screenshots 
<img width="800" height="409.6" src="screenshots/screenshots.jpg">

## How to integrate the Api Key
You can authenticate into the TMDB site https://developers.themoviedb.org/ to obtain your api key. Once you get it add on your local.properties below your sdk.dir as:
tmdb_api_key=103sy80d1e219553786b7f9395g14625

## Library Used
* [Dependency injection with Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Kotlin flows](https://developer.android.com/kotlin/flow)
* [MVVM Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Retrofit2](https://square.github.io/retrofit/)
* [ROOM](https://developer.android.com/topic/libraries/architecture/room?gclid=Cj0KCQjwl4v4BRDaARIsAFjATPnKKVVy9yBUiZhCcmGbmLl-6TKUusgYdur0OZq2MOQdsOLN6jXLpvQaAk5mEALw_wcB&gclsrc=aw.ds)
* [Glide](https://bumptech.github.io/glide/)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding)
* [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data)
* [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
* [Material design](https://material.io/design/)
* [Recyclerview](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* [Coroutines](https://developer.android.com/kotlin/coroutines)
* [Collapsing toolbar](https://material.io/develop/android/components/collapsing-toolbar-layout/)
* [Animations](https://developer.android.com/training/animation/overview)
* [Lottie Animations](https://lottiefiles.com/blog/working-with-lottie/getting-started-with-lottie-animations-in-android-app)
* [Palette](https://developer.android.com/reference/androidx/palette/graphics/Palette)

## Author

**Pablo Vydra** 
* [Linkedin](https://www.linkedin.com/in/pablovydra)
* [Twitter](https://twitter.com/pablovydra?lang=es)
* [Behance](https://www.behance.net/pablovydra)
