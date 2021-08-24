package com.yumatech.smnadim21.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.smnadim21.nadx.tools.NandX;
import com.yumatech.smnadim21.app.YumaTech;
import com.yumatech.smnadim21.db.listener.SaveStateListener;
import com.yumatech.smnadim21.tools.DatabaseSchema;
import com.yumatech.smnadim21.tools.Tools;

@Entity(tableName = DatabaseSchema.ProductTable.table_name,
        indices = {@Index(value = {
                DatabaseSchema.ProductTable.product_uuid
        }, unique = true
        )}

)
public class Products extends BaseDB implements DatabaseSchema {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = ProductTable.short_name)
    public String short_name;

    @ColumnInfo(name = ProductTable.price)
    public String price;

    @ColumnInfo(name = ProductTable.print_order)
    public String print_order;

    @ColumnInfo(name = ProductTable.product_uuid)
    public String product_uuid;

    @ColumnInfo(name = ProductTable.images)
    public String images;

    public long getId() {
        return id;
    }

    public Products setId(long id) {
        this.id = id;
        return this;
    }

    public String getShort_name() {
        return short_name;
    }

    public Products setShort_name(String short_name) {
        this.short_name = short_name;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Products setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getPrint_order() {
        return print_order;
    }

    public Products setPrint_order(String print_order) {
        this.print_order = print_order;
        return this;
    }

    public String getImages() {
        return images;
    }

    public Products setImages(String images) {
        this.images = images;
        return this;
    }

    public String getProduct_uuid() {
        return product_uuid;
    }

    public Products setProduct_uuid(String product_uuid) {
        this.product_uuid = product_uuid;
        return this;
    }

    public Products update() {
        this.setModifiedAt(Tools.getCurrentTimeString());
        YumaTech.getProductDao().updateProduct(this);
        return this;
    }


    public Products save(SaveStateListener<Products> saveStateListener) {
        try {
            this.setId(YumaTech.getProductDao().insertProduct(this));
            saveStateListener.onSaved(this);
        } catch (Exception e) {
            e.printStackTrace();
            saveStateListener.onSaveFailed(e.getMessage());
            NandX.print(e.getMessage());
            saveStateListener.onSaveFailed(e);
        }
        return this;
    }

    public Products save() {
        try {
            this.setId(YumaTech.getProductDao().insertProduct(this));
        } catch (Exception e) {
            e.printStackTrace();
            NandX.print(e.getMessage());
        }
        return this;
    }

    public Products saveOrUpdate() {
        try {
            this.setId(YumaTech.getProductDao().insertProduct(this));
        } catch (Exception e) {
            e.printStackTrace();
            NandX.print(e.getMessage());
            try {
                this.setModifiedAt(Tools.getCurrentTimeString());
                YumaTech.getProductDao().updateProduct(this);
                NandX.print(e.getMessage());
            } catch (Exception ee) {
                ee.printStackTrace();
                NandX.print(ee.getMessage());
            }
        }
        return this;
    }

    public Products delete() {
        YumaTech.getProductDao().delete(this);
        return this;
    }

}
