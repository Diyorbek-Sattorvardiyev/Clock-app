package com.example.soatapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long alarmTimeInMillis;

    public Alarm(long alarmTimeInMillis) {
        this.alarmTimeInMillis = alarmTimeInMillis;
    }
}
