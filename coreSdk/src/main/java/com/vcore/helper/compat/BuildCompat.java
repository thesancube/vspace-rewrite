package com.vcore.helper.compat;

import android.os.Build;

/**
 * @author Lody
 */

public class BuildCompat {

    public static int getPreviewSDKInt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return Build.VERSION.PREVIEW_SDK_INT;
            } catch (Throwable e) {
                // ignore
            }
        }
        return 0;
    }

    public static boolean isOreo() {
        return isAndroidLevel(Build.VERSION_CODES.O);
    }

    public static boolean isPie() {
        return isAndroidLevel(Build.VERSION_CODES.P);
    }

    public static boolean isQ() {
        return isAndroidLevel(29);
    }

    public static boolean isR() {
        return isAndroidLevel(30);
    }

    public static boolean isS() {
        return isAndroidLevel(31);
    }

    public static boolean isT() {
        return isAndroidLevel(33); // Android 13 (Tiramisu)
    }

    public static boolean isU() {
        return isAndroidLevel(34); // Android 14 (Upside Down Cake)
    }

    public static boolean isV() {
        return isAndroidLevel(35); // Android 15 (Vanilla Ice Cream)
    }

    public static boolean isW() {
        return isAndroidLevel(36); // Android 16+ (future proofing)
    }

    private static boolean isAndroidLevelPreview(int level) {
        return (Build.VERSION.SDK_INT == level && getPreviewSDKInt() > 0)
                || Build.VERSION.SDK_INT > level;
    }

    private static boolean isAndroidLevel(int level) {
        return Build.VERSION.SDK_INT >= level;
    }
}