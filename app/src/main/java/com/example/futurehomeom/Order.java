package com.example.futurehomeom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Order")

public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String updated;

    public String titleAr;
    public String stateAr;
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String title;
    public String url,Note;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String objectId;
    public int code;
    public int height;
    public int width;
    public String created,state;

}
