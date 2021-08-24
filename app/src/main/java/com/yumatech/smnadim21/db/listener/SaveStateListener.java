package com.yumatech.smnadim21.db.listener;

public interface SaveStateListener<T> {
    void onSaved(T item);

    void onSaveFailed(String reason);

    void onSaveFailed(Exception ex);
}
