- we are going to build a Radio Player Application  with the help of Room Database , ViewModal,LiveData and Retrofit.

-It's a music streaming app which fetches songs information from a Web API and streams using the Exoplayer.

-First we will send a retrofit request to the web server for json data, then after fetching json data from the server ,
we will store that data into Room Database and then shows into the recyclerview.

-Android-java-MVVM-with-RoomDatabase-Retrofit-and-RecyclerView
  Project Architecture 🗼
-This app uses [MVVM (Model View View-Model)] architecture.
### Build With 🏗️
- [Retrofit] -  A type-safe HTTP client for Android .
- [Room] - SQLite object mapping library.
- [LiveData] - Data objects that notify views when the underlying database changes.
- [ViewModel] - Stores UI-related data that isn't destroyed on UI changes.
- [Glide] - An image loading and caching library for Android focused on smooth scrolling
-[ExoPlayer] - ExoPlayer is an application level media player for Android. It provides an alternative to Android’s MediaPlayer API for
 playing audio and video both locally and over the Internet. ExoPlayer supports features not currently supported by Android’s MediaPlayer API, including DASH and SmoothStreaming adaptive playbacks. Unlike the MediaPlayer API, ExoPlayer is easy to customize and extend, and can be updated through Play Store application updates.








