package mobi.cwiklinski.ChargingStats.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.BatteryManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import mobi.cwiklinski.ChargingStats.R;
import mobi.cwiklinski.ChargingStats.util.Utility;

import java.text.DateFormat;

public class ChargeAdapter extends CursorAdapter {

    private final String TAG = "ChargingStats.ChargeAdapter";
    private LayoutInflater mInflater;

    public ChargeAdapter(Context context, Cursor c) {
        super(context, c, true);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item, null);
        ChargeViewHolder rowData = new ChargeViewHolder();
        rowData.start = (TextView) view.findViewById(R.id.startTime);
        rowData.end = (TextView) view.findViewById(R.id.endTime);
        rowData.time = (TextView) view.findViewById(R.id.time);
        rowData.timeLabel = (TextView) view.findViewById(R.id.timeLabel);
        rowData.type = (TextView) view.findViewById(R.id.type);
        view.setTag(rowData);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ChargeViewHolder rowData = (ChargeViewHolder) view.getTag();
        int idColumn = cursor.getColumnIndex("_id");
        int startColumn = cursor.getColumnIndex("start");
        int endColumn = cursor.getColumnIndex("end");
        int typeColumn = cursor.getColumnIndex("type");
        String start = cursor.getString(startColumn);
        String end = cursor.getString(endColumn);
        int type = cursor.getInt(typeColumn);
        rowData.id = cursor.getInt(idColumn);
        final long startTime = Long.parseLong(start);
        rowData.start.setText(DateFormat.getDateTimeInstance().format(startTime));
        try {
            final long endTime = Long.parseLong(end);
            rowData.timeLabel.setVisibility(View.VISIBLE);
            rowData.end.setText(DateFormat.getDateTimeInstance().format(endTime));
            String timeDifference = Utility.generateTimeDifference(context, startTime, endTime);
            rowData.time.setText(timeDifference);
        } catch (NumberFormatException e) {
            rowData.timeLabel.setVisibility(View.INVISIBLE);
            rowData.end.setText(R.string.still_charging);
            rowData.time.setText("");
        }
        switch(type) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                rowData.type.setText(R.string.charging_type_ac);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                rowData.type.setText(R.string.charging_type_usb);
                break;
        }
    }

    public static class ChargeViewHolder {
        public TextView time;
        public TextView timeLabel;
        public TextView start;
        public TextView end;
        public TextView type;
        public int id;
    }
}
