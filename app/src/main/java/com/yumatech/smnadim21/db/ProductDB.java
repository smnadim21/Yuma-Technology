package com.yumatech.smnadim21.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yumatech.smnadim21.db.entity.Products;
import com.yumatech.smnadim21.tools.DatabaseSchema;


@Database(entities =
        {
                Products.class
        }, version = 2
        , exportSchema = false
)

public abstract class ProductDB extends RoomDatabase implements DatabaseSchema {
    public static final String DATABASE_NAME = ProductTable.dataBaseName;
    private static ProductDB productDB;

    public synchronized static ProductDB getInstance(final Context context) {

        if (productDB == null)
            productDB = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ProductDB.class,
                    DATABASE_NAME
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        return productDB;
    }

    public abstract ProductDao productDao();

}
