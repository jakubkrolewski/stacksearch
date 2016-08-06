package pl.jkrolewski.stacksearch.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import rx.schedulers.Schedulers;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    BriteDatabase provideBriteDatabase(@NonNull Context context) {
        SqlBrite sqlBrite = SqlBrite.create();
        SQLiteOpenHelper openHelper = new ApplicationDatabaseOpenHelper(context);

        return sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    }
}
