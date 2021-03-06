package cz.destil.glasquare.api;

import android.app.Activity;
import android.preference.PreferenceManager;

import cz.destil.glasquare.App;
import cz.destil.glasquare.activity.LoginActivity;
import retrofit.RetrofitError;

/**
 * Helper for handling auth-related stuff.
 *
 * @author David 'Destil' Vavra (david@vavra.me)
 */
public class Auth {

    public static final int REQUEST_CODE = 42;

    public static boolean handle(Activity activity, RetrofitError retrofitError) {
        FoursquareResponse response = (FoursquareResponse) retrofitError.getBody();
        if (response.isMissingAuth()) {
            LoginActivity.call(activity);
            return true;
        }
        return false;
    }

    public static String getToken() {
        return PreferenceManager.getDefaultSharedPreferences(App.get()).getString("TOKEN", null);
    }

    public static void saveToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(App.get()).edit().putString("TOKEN", token).commit();
    }

    public static class FoursquareResponse {
        FoursquareError meta;

        public boolean isMissingAuth() {
            return meta.isMissingAuth();
        }
    }

    public static class FoursquareError {
        int code;
        String errorType;

        public boolean isMissingAuth() {
            return "invalid_auth".equals(errorType) || "not_authorized".equals(errorType);
        }
    }

}
