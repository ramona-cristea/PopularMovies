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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for favorites movies data.
 */
class FavoritesDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    private static final String DATABASE_NAME = "favorites.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 1;

    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our favorites.
         */
        final String SQL_CREATE_FAVORITES_TABLE =

            "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +

            /*
             * FavoriteEntry did not explicitly declare a column called "_ID". However,
             * FavoriteEntry implements the interface, "BaseColumns", which does have a field
             * named "_ID". We use that here to designate our table's primary key.
             */
                FavoriteContract.FavoriteEntry._ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID              + " INT NOT NULL, "                         +
                FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE        + " TEXT NOT NULL, "                        +
                FavoriteContract.FavoriteEntry.COLUMN_DURATION              + " INT, "                                  +
                FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE          + " TEXT, "                                 +
                FavoriteContract.FavoriteEntry.COLUMN_POPULARITY            + " REAL, "                                 +
                FavoriteContract.FavoriteEntry.COLUMN_RATING                + " REAL, "                                 +
                FavoriteContract.FavoriteEntry.COLUMN_GENRES                + " TEXT, "                                 +
                FavoriteContract.FavoriteEntry.COLUMN_STORYLINE             + " TEXT, "                                 +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH           + " TEXT, "                                 +
                /*
                 * To ensure this table can only contain one Movie entry per id, we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a favorite entry for a certain id and we attempt to
                 * insert another favorite entry with that id, we replace the old favorite entry.
                 */
                " UNIQUE (" + FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}