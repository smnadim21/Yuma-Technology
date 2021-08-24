package com.yumatech.smnadim21.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.yumatech.smnadim21.db.entity.Products;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDao {

    @Insert(onConflict = REPLACE)
    long insertProduct(Products products);

    @Update
    void updateProduct(Products products);

    @Delete
    void delete(Products products);
}
