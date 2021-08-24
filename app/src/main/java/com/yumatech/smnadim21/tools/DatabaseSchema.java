package com.yumatech.smnadim21.tools;

public interface DatabaseSchema {
    interface ProductTable {
        String dataBaseName = "product_db";
        String table_name = "products_table";
        String short_name = "short_name";
        String price = "price";
        String images = "images";
        String print_order = "print_order";
        String product_uuid = "product_uuid";
    }

    public interface SyncDB {
        String created_at = "created_at";
        String modified_at = "modified_at";
        String message = "message";

    }
}
