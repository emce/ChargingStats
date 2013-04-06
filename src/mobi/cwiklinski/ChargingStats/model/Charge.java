package mobi.cwiklinski.ChargingStats.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Charge extends ModelAbstract {

    @Override
    public String getTableName() {
        return "charge";
    }

    @Override
    public String getPrimaryField() {
        return "_id";
    }

    @Override
    public ContentValues toContentValues(Cursor cursor) {
        ContentValues values = new ContentValues();
        int idColumn = cursor.getColumnIndex("_id");
        int startColumn = cursor.getColumnIndex("start");
        int endColumn = cursor.getColumnIndex("end");
        int typeColumn = cursor.getColumnIndex("type");
        values.put("_id", cursor.getInt(idColumn));
        values.put("start", cursor.getString(startColumn));
        values.put("end", cursor.getString(endColumn));
        values.put("type", cursor.getString(typeColumn));
        return values;
    }

    public Cursor getLastCharging() {
        String sql = "SELECT * FROM " + getTableName() + " WHERE end IS NULL ORDER BY _id DESC LIMIT 1";
        Cursor result = getDatabase().rawQuery(sql, null);
        if (result.moveToFirst()) {
            return result;
        }
        return null;
    }
}
