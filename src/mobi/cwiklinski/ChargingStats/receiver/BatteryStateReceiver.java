package mobi.cwiklinski.ChargingStats.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.os.BatteryManager;
import android.util.Log;
import mobi.cwiklinski.ChargingStats.MainActivity;
import mobi.cwiklinski.ChargingStats.R;
import mobi.cwiklinski.ChargingStats.model.Charge;
import mobi.cwiklinski.ChargingStats.util.Utility;

public class BatteryStateReceiver extends BroadcastReceiver {

    private final String TAG = "ChargingStats.BatteryStateReceiver";
    private Charge mChargeModel = new Charge();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "broadcast received");

        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        if (isCharging) {
            ContentValues values = new ContentValues();
            values.put("start", Utility.getTimestamp());
            values.put("type", chargePlug);
            mChargeModel.insert(values);
            String body = context.getString(R.string.notification_charging);
            makeNotification(context, R.string.app_name, body);
        } else {
            Cursor last = mChargeModel.getLastCharging();
            if (last != null) {
                ContentValues values = mChargeModel.toContentValues(last);
                values.remove("end");
                values.put("end", Utility.getTimestamp());
                long start = values.getAsLong("start");
                long end = values.getAsLong("end");
                mChargeModel.update(values.getAsInteger("_id"), values);
                String body = context.getString(R.string.notification_discharging);
                body += " " + Utility.generateTimeDifference(context, start, end);
                makeNotification(context, R.string.app_name, body);
            }
        }
    }

    private void makeNotification(Context context, int title, String body) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
            context,
            1024,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
            .setContentIntent(contentIntent)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_launcher)
            .setAutoCancel(true)
            .setContentTitle(context.getString(title))
            .setContentText(body);
        nm.notify(title, builder.getNotification());
    }
}
