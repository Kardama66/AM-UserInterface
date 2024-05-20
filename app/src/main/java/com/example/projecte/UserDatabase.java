package com.example.projecte;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.room.TypeConverters;


@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static volatile UserDatabase instance;

    public abstract UserDAO useUserDao();

    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    UserDatabase.class,
                                    "Users_DB"
                            )

                            .build();
                }
            }
        }
        return instance;
    }


}