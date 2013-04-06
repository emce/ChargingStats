package mobi.cwiklinski.ChargingStats.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mobi.cwiklinski.ChargingStats.Base;

public abstract class ModelAbstract {

    abstract public String getTableName();

    abstract public String getPrimaryField();

    abstract public ContentValues toContentValues(Cursor cursor);

    public SQLiteDatabase getDatabase() {
        return Base.getDatabase();
    }

    public Cursor getById(String id) {
        Cursor c = getDatabase().rawQuery("SELECT * FROM " + getTableName() + " WHERE " + getPrimaryField() + " = ?", new String[]{ id });
        if (null != c && c.moveToFirst()) {
            return c;
        } else {
            return null;
        }
    }

    public Cursor getAll() {
        String selectQuery = "SELECT * FROM " + getTableName() + " ORDER BY " + getPrimaryField() + " DESC";
        return getDatabase().rawQuery(selectQuery, null);
    }

    public int update(int id, ContentValues values) {
        return getDatabase().update(getTableName(), values, "_id = ?", new String[] { Integer.toString(id) });
    }

    public long insert(ContentValues values) {
        return getDatabase().insert(getTableName(), null, values);
    }

    public long delete(int id) {
        return getDatabase().delete(getTableName(), "_id = ?", new String[] { Integer.toString(id) });
    }

}
