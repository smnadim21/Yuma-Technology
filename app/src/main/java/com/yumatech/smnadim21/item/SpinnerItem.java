package com.yumatech.smnadim21.item;

import org.jetbrains.annotations.NotNull;

public class SpinnerItem {

    String name = "name expected!", id;


    public String getName() {
        return name;
    }

    public SpinnerItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public SpinnerItem setId(String id) {
        this.id = id;
        return this;
    }


    @Override
    public @NotNull String toString() {
        return name;
    }
}

