package ch.beerpro.presentation.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.graphics.drawable.DrawableCompat;
import ch.beerpro.R;
import ch.beerpro.presentation.MainActivity;


public class ThemeChange {

    static final int BRIGHT_THEME = 0;
    static final int DARK_THEME = 1;

    public static void initTheme(Activity activity) {
    }
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    static void changeToTheme(Activity activity, int theme) {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("mValue", Integer.toString(theme));
        editor.apply();

        activity.finish();
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        int sTheme = Integer.parseInt(prefs.getString("mValue","0"));

        switch (sTheme) {
            default:
            case BRIGHT_THEME:
                activity.setTheme(R.style.AppTheme);
                break;
            case DARK_THEME:
                activity.setTheme(R.style.AppThemeDark);
        }
    }
}
