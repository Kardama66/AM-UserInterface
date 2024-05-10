package com.example.projecte;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface UserDAO {
    @Insert
    public void InsertToDatabase(User user);

    @Delete
    public void DeleteFromDatabase(User user);

}
