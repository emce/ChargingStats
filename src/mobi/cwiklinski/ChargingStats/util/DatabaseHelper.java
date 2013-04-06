package mobi.cwiklinski.ChargingStats.util;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import mobi.cwiklinski.ChargingStats.R;

import java.io.File;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/mobi.cwiklinski.ChargingStats/databases/charge_db";
    private static final String DATABASE_NAME = "charge_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "ChargingStats.DatabaseHelper";
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database");

        try {
            String[] sql = mContext.getString(R.string.onCreateDatabase).split("\n");
            db.beginTransaction();
            try {
                execMultipleSQL(db, sql);
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                Log.e("Error creating tables and debug data", e.toString());
                throw e;
            } finally {
                db.endTransaction();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Database created");
    }

    /**
     * Execute all of the SQL statements in the String[] array
     *
     * @param db  The database on which to execute the statements
     * @param sql An array of SQL statements to execute
     */
    private void execMultipleSQL(SQLiteDatabase db, String[] sql) {
        for (String s : sql) {
            if (s.trim().length() > 0) {
                System.out.print(s);
                db.execSQL(s);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        boolean isDeleted = mContext.deleteDatabase(DATABASE_NAME);
        if(isDeleted) {
            try {
                getWritableDatabase();
            } catch(RuntimeException e) {
                File dbFile = new File(DB_PATH);
                boolean exists = dbFile.exists();
                if (exists) {
                    dbFile.delete();
                }
                try {
                    dbFile.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            throw new IllegalStateException("Database is not deleted");
        }

        Log.d(TAG, "Deleting database, old version:" + oldVersion + ", new version:" + newVersion + ", is deleted: " + isDeleted);
    }

    public static boolean deleteDatabase(Context context) {
        return context.getDatabasePath(DB_PATH).delete();
    }
}
