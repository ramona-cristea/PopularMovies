# PopularMovies

Project which uses themoviedb.org API, implemented for educational purpose only. The app will:
* Display a GridView of movies posters
* Possibility to change movies sort order in GridView via a menu. The sort order can be by most popular or by highest-rated.
* By tapping on a movie poster, user can see a details screen with additional movie's information.

In order test the app, one needs to get an API Key from themoviedb.org API, apply a Base64 encoding to it and add it in `MoviesRepositoryImpl` file in `getApiKey` method, instead of "YOUR_API_KEY" string. 
