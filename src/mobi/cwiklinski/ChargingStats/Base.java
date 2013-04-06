package mobi.cwiklinski.ChargingStats;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import mobi.cwiklinski.ChargingStats.util.DatabaseHelper;

public class Base extends Application {

    private static final String TAG = "ChargingStats.Base";
    private static DatabaseHelper databaseHelper;
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
        initDatabaseHelper(this);
    }

    @Override
    public void onTerminate() {
        try {
            super.onTerminate();
        } finally {
            databaseHelper.close();
        }
    }

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        if (Base.databaseHelper != null) {
            Base.databaseHelper.close();
        }
        Base.databaseHelper = databaseHelper;
    }

    public void initDatabaseHelper(Context context) {
        Log.w(TAG, "database initiated");
        setDatabaseHelper(new DatabaseHelper(context));
    }

    /**
     * Get SQLiteDatabase. Default get writable database. Shorter name.
     *
     * @return Instance of database
     */
    public static SQLiteDatabase getDatabase() {
        return databaseHelper.getWritableDatabase();
    }

    /**
     * Get writable SQLiteDatabase
     *
     * @return Instance of database
     */
    public static SQLiteDatabase getWritableDatabase() {
        return databaseHelper.getWritableDatabase();
    }

    /**
     * Get readable SQLiteDatabase
     *
     * @return Instance of database
     */
    public static SQLiteDatabase getReadableDatabase() {
        return databaseHelper.getReadableDatabase();
    }

    /**
     * Close any open database object.
     */
    public static void closeDatabase() {
        databaseHelper.close();
    }

    public static boolean deleteDatabase() {
        Base.closeDatabase();
        return DatabaseHelper.deleteDatabase(applicationContext);
    }
}
