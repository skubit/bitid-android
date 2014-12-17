package com.skubit.bitid.provider;

import com.skubit.bitid.BuildConfig;
import com.skubit.bitid.provider.key.KeyColumns;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class KeySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FILE_NAME = "keys.db";

    private static final String TAG = KeySQLiteOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_KEY = "CREATE TABLE IF NOT EXISTS "
            + KeyColumns.TABLE_NAME + " ( "
            + KeyColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KeyColumns.NICKNAME + " TEXT, "
            + KeyColumns.PUB + " BLOB, "
            + KeyColumns.PRIV + " BLOB, "
            + KeyColumns.ADDRESS + " TEXT "
            + " );";

    private static KeySQLiteOpenHelper sInstance;

    private final Context mContext;

    private final KeySQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:on

    private KeySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        mOpenHelperCallbacks = new KeySQLiteOpenHelperCallbacks();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private KeySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version,
            DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new KeySQLiteOpenHelperCallbacks();
    }


    /*
     * Pre Honeycomb.
     */

    public static KeySQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static KeySQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Post Honeycomb.
     */

    private static KeySQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new KeySQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static KeySQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new KeySQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION,
                new DefaultDatabaseErrorHandler());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_KEY);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
