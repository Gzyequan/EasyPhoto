package com.yequan.easyphoto.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesUtil {

    private String fileName;

    private static final String PREFERENCES_FILE = "preferences";
    private static PreferencesUtil preferences;

    /**
     * 应用配置工具
     *
     * @return 工具类实例
     */
    public static PreferencesUtil getPreferences() {
        if (preferences == null) {
            preferences = new PreferencesUtil(PREFERENCES_FILE);
        }
        return preferences;
    }

    private static final String CACHE_FILE = "cache";
    private static PreferencesUtil cache;

    /**
     * 应用缓存工具
     *
     * @return 工具类实例
     */
    public static PreferencesUtil getCache() {
        if (cache == null) {
            cache = new PreferencesUtil(CACHE_FILE);
        }
        return cache;
    }

    //构造器私有化，防止实例化
    private PreferencesUtil(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 存入int类型数据
     *
     * @param context 应用上下文
     * @param key     数据的键
     * @param value   将要存储的数据的值
     */
    public void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value).apply();
    }

    /**
     * 存入int类型数据
     *
     * @param context      应用上下文
     * @param key          数据的键
     * @param defaultValue 如果之前未存储(不存在)那么缺省值为defaultValue
     */
    public int getInt(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    /**
     * 存入String类型数据
     *
     * @param context 应用上下文
     * @param key     数据的键
     * @param value   将要存储的数据的值
     */
    public void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.putString(key, value).apply();
    }


    public String getString(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }


    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }


}
