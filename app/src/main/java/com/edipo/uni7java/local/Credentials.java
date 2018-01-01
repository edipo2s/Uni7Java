package com.edipo.uni7java.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Credentials {

    @PrimaryKey
    @NonNull
    private String name;
    private String password;

    public Credentials(@NonNull String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
