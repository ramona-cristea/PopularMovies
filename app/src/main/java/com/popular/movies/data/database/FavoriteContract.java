/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.popular.movies.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the FavoriteEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    static final String CONTENT_AUTHORITY = "com.popular.movies";

    // The base content URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "favorites" directory
    static final String PATH_FAVORITES = "favorites";

    /* FavoriteEntry is an inner class that defines the contents of the favorites table */
    public static final class FavoriteEntry implements BaseColumns {

        // FavoriteEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();


        // Task table and column names
        static final String TABLE_NAME = "favorites";

        /* Movie ID as returned by API, used to identify movie which is added to favorites */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_STORYLINE = "storyline";
        public static final String COLUMN_POSTER_PATH = "poster_path";
    }
}
