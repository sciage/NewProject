package in.voiceme.app.voiceme.infrastructure;

import android.content.SharedPreferences;

/**
 * Created by harish on 1/2/2017.
 */

public class MySharedPreferences {

    private static final String AMAZON_USER_ID = "AMAZON_USER_ID";

    public static void wipe(SharedPreferences sharedPreferences) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, AMAZON_USER_ID, null);
    }

    protected static void storeValueInSharedPreferences(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected static String getValueFromSharedPreferences(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, null);
    }

    public static void registerUserId(SharedPreferences sharedPreferences, String userId) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, AMAZON_USER_ID, userId);
    }

    public static String getUserId(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, AMAZON_USER_ID);
    }

    public static void checkContactSent(SharedPreferences sharedPreferences, String checked) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.SENT_CONTACT, checked);
    }

    public static String getContactSent(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.SENT_CONTACT);
    }


}
