package com.edipo.uni7java.local;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppDatabase {

    private static AppDatabase _instance;

    public static AppDatabase getInstance(Context context) {
        if (_instance == null) {
            _instance = new AppDatabase(context);
        }
        return _instance;
    }

    private AppDatabaseImp database;

    private AppDatabase(Context context) {
        database = Room.databaseBuilder(context, AppDatabaseImp.class, "uni7bd").build();
    }

    public AppDatabaseImp getDatabase() {
        return database;
    }

}
