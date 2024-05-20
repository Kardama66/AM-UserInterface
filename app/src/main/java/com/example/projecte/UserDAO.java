package com.example.projecte;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    public void insertToDatabase(User user);

    @Delete
    public void deleteFromDatabase(User user);

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    User getUserByLoginAndPassword(String login, String password);

    @Query("SELECT * FROM users WHERE login = :login")
    User getUserByLogin(String login);

    @Update
    void updateUser(User user);
}
