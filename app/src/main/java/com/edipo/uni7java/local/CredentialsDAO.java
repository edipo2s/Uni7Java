package com.edipo.uni7java.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CredentialsDAO {

    @Query("SELECT * FROM credentials")
    List<Credentials> getAll();

    @Query("SELECT * FROM credentials WHERE name LIKE :name")
    Credentials findByName(String name);

    @Query("SELECT * FROM credentials WHERE name LIKE :name AND password LIKE :password LIMIT 1")
    Credentials findByNamePassword(String name, String password);

    @Insert
    void insertAll(Credentials... credentials);

    @Delete
    void delete(Credentials credentials);

}
