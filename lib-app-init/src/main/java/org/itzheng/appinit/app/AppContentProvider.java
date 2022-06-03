package org.itzheng.appinit.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppContentProvider extends ContentProvider {
    private static Context mApplicationContext;

    public static Context getApplicationContext() {
        if (mApplicationContext == null) {
            //如果有需要可以用其他方式获取
        }
        return mApplicationContext;
    }

    @Override
    public boolean onCreate() {
        mApplicationContext = getContext().getApplicationContext();
        init();
        return false;
    }

    private void init() {
        Log.w("lib-app-init", "init: ");
        InitAppClass.getInstance().initAll();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
