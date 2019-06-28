package com.show.dlnadmr.jni;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;

public class PlatinumJniProxy {

    static {
        System.loadLibrary("platinum-jni");
    }

    public static native int nativeStartMediaRender(String friendname, String uuid);

    public static native int nativeStopMediaRender();

    public static native boolean nativeResponseGenaEvent(int cmd, String value, String data);

    public static native boolean nativeEnableLogPrint(boolean flag);

    public static native String nativeGetConnectedDmcUserAgent();

    public static native String nativeGetConnectedDmcIP();

    public static int startMediaRender(String friendlyName, String uuid) {
        if (friendlyName == null) {
            friendlyName = "";
        }
        if (uuid == null) {
            uuid = "";
        }
        int ret = -1;
        try {
            ret = nativeStartMediaRender(friendlyName, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        enableLogPrint(false);
        return ret;
    }

    public static boolean responseGenaEvent(int cmd, String value, String data) {
        if (value == null) {
            value = "";
        }
        if (data == null) {
            data = "";
        }
        boolean ret = false;
        try {
            ret = nativeResponseGenaEvent(cmd, value, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static boolean enableLogPrint(boolean flag) {
        return nativeEnableLogPrint(flag);
    }

    public static int stopMediaRender() {
        return nativeStopMediaRender();
    }

    public static String getConnectedDmcUserAgent() {
        return nativeGetConnectedDmcUserAgent();
    }

    public static String getConnectedDmcMacAddr() {
        String dmcIP = nativeGetConnectedDmcIP();
        String line = "";
        String ip = "";
        String mac = "";
        boolean found = false;
        if (!TextUtils.isEmpty(dmcIP) && !dmcIP.equals("0.0.0.0")) {
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader("/proc/net/arp"));
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.length() < 63) {
                        continue;
                    }
                    if (line.toUpperCase(Locale.US).contains("IP")) {
                        continue;
                    }
                    ip = line.substring(0, 17).trim();
                    mac = line.substring(41, 63).trim();
                    if (mac.contains("00:00:00:00:00:00")) {
                        continue;
                    }
                    if (dmcIP.equals(ip)) {
                        found = true;
                        break;
                    }
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (found) {
            return mac;
        }

        Log.e("Platinum_JniProxy", "cannot get mac for dmcIP");
        return "00:00:00:00:00:00";
    }
}
