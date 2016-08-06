package pl.jkrolewski.stacksearch.base.database;

import android.database.sqlite.SQLiteDatabase;

public interface Table {

    void create(SQLiteDatabase db);
}