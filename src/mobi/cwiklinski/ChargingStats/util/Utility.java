package mobi.cwiklinski.ChargingStats.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import mobi.cwiklinski.ChargingStats.Base;
import mobi.cwiklinski.ChargingStats.R;

import java.util.regex.Pattern;

public class Utility {

    public static final String TAG = "whereRU.util.Utility";
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9_]*$");
    public static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(\\+\\d{2})?[\\d\\s]{9,}$"
    );
    public Context mContext;
    private static Utility instance;
    public static final String[] MONTH_NUMBERS = new String[] {
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
    };

    public Utility(Activity activity) {
        mContext = activity;
    }

    public static Utility getInstance(Activity activity) {
        if (instance == null) {
            instance = new Utility(activity);
        }
        return instance;
    }

    public final void makeToast(int resource) {
        Toast.makeText(mContext, mContext.getString(resource), Toast.LENGTH_LONG).show();
    }

    public final void makeToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    public static boolean deleteDatabase() {
        return Base.deleteDatabase();
    }

    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static String generateTimeDifference(Context context, long startTime, long endTime) {
        long difference = endTime - startTime;
        long hours = difference / (1000 * 60 * 60);
        difference = difference - (hours * 1000 * 60 * 60);
        long minutes = difference / (1000 * 60);
        difference = difference - (minutes * 1000 * 60);
        long seconds = difference / 1000;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(context.getString(R.string.hours));
            sb.append(": ");
            sb.append(hours);
            sb.append(", ");
        }
        if (minutes > 0) {
            sb.append(context.getString(R.string.minutes));
            sb.append(" ");
            sb.append(minutes);
            sb.append(", ");
        }
        sb.append(context.getString(R.string.seconds));
        sb.append(": ");
        sb.append(seconds);
        return sb.toString();
    }
}
