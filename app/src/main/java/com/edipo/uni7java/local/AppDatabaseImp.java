package com.edipo.uni7java.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Credentials.class}, exportSchema = false, version = 1)
public abstract class AppDatabaseImp extends RoomDatabase {

    public abstract CredentialsDAO getCredentialsDAO();

}
