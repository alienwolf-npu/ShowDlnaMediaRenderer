package com.show.dlnadmr.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.UUID;

public class DlnaDmrConfigSharePreference {

    public static final String preference_name = "dlna_dmr";
    public static final String dev_name = "dmr_dev_name";
    public static final String dev_uuid = "dmr_dev_uuid";

    public static boolean commitDeviceName(Context context, String devName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(dev_name, devName);
        editor.commit();
        return true;
    }

    public static boolean commitDeviceUUID(Context context, String uuid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString(dev_uuid, uuid);
        editor.commit();
        return true;
    }

    public static String getDevName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
        String value = sharedPreferences.getString(dev_name, "小度在家1S");
        return value;
    }

    public static String getDevUUId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference_name, 0);
        String value = sharedPreferences.getString(dev_uuid, "");
        if (TextUtils.isEmpty(value)) {
            String uuid = UUID.randomUUID().toString();
            commitDeviceUUID(context, uuid);
            return uuid;
        }
        return value;
    }
}
