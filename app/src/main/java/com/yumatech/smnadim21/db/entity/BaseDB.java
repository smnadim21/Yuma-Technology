package com.yumatech.smnadim21.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;


import com.yumatech.smnadim21.tools.DatabaseSchema;
import com.yumatech.smnadim21.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author : Arif
 * @date : 20-March-2021 11:11 AM
 * @package : com.akij.app.db.entity
 * -------------------------------------------
 * Copyright (C) 2021 - All Rights Reserved
 **/



public class BaseDB implements DatabaseSchema {

    @ColumnInfo(name = SyncDB.created_at)
    public String created_at;
    @ColumnInfo(name = SyncDB.modified_at)
    public String modified_at;
    @ColumnInfo(name = SyncDB.message)
    public String message;

    public String getMessage() {
        return message;
    }

    public BaseDB setMessage(String message) {
        this.message = message;
        return this;
    }

    @Ignore
    public final static String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public BaseDB() {
        this.created_at = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }

    public String getCreated_at() {
        return created_at;
    }

    public BaseDB setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    public String getModifiedAt() {
        return modified_at;
    }

    public BaseDB setModifiedAt(String modifiedAt) {
        this.modified_at = modifiedAt;
        return this;
    }

    public BaseDB update() {
        this.setModifiedAt(Tools.getCurrentTimeString());
/*        if (this instanceof Chalan) {
            {
                Akij.getFarmersDao().update((Chalan) this);
            }
        }*/
        return this;
    }

/*
    public SyncableDB update() {
        this.setModifiedAt(Tools.getCurrentTimeString());
        Akij.getFarmersDao().update(this);
        return this;
    }
*/




/*    public RoundTP save(SaveStateListener<RoundTP> saveStateListener) {
        try {
            this.setId(Akij.getFarmersDao().insertRounds(this));
            saveStateListener.onSaved(this);
        } catch (Exception e) {
            e.printStackTrace();
            saveStateListener.onSaveFailed(e.getMessage());
            Tools.print(e.getMessage());
            saveStateListener.onSaveFailed(e);
        }
        return this;
    }

    public RoundTP save() {
        try {
            this.setId(Akij.getFarmersDao().insertRounds(this));
        } catch (Exception e) {
            e.printStackTrace();
            Tools.print(e.getMessage());
        }
        return this;
    }*/
}
