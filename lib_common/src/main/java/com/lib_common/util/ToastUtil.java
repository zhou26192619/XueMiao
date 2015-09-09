package com.lib_common.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lib_common.config.BaseConfig;

/**
 * 提示工具
 *
 * @author m
 */
public class ToastUtil {
    private Toast toast;
    private static ToastUtil instance;

    private ToastUtil() {

    }

    public static ToastUtil getInstance() {
        if (instance == null) {
            synchronized (ToastUtil.class) {
                instance = new ToastUtil();
            }
        }
        return instance;
    }

    public void showLongToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    public void showShortToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void printErr(Exception e) {
        if (BaseConfig.IS_DEBUG) {
            e.printStackTrace();
        }
    }

    public static void log(String tag, Object text) {
        if (BaseConfig.IS_DEBUG) {
            if (text == null) {
                Log.i(tag, "" + null);
            } else {
                Log.i(tag, text.toString());
            }

        }
    }

    public static void logE(String tag, Object text) {
        if (BaseConfig.IS_DEBUG) {
            Log.e(tag, text.toString());
        }
    }

    public static void toast(Context context, Object text) {
        if (BaseConfig.IS_DEBUG) {
            toastAlways(context, text);
        }
    }

    public static void toastAlways(Context context, Object text) {
        Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT).show();
    }
}
