package com.example.soatapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AlarmDao {

    @Insert
    void insert(Alarm alarm);

    @Query("SELECT * FROM alarms LIMIT 1")
    Alarm getAlarm();

    @Delete
    void delete(Alarm alarm);
}
