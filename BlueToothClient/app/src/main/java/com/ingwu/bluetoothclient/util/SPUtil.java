package com.ingwu.bluetoothclient.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.ingwu.bluetoothclient.bean.Setting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by acer on 2015/9/19.
 * SharedPreferences封装类
 * 文件名存在config类中
 */
public class SPUtil {
    private static final String TAG = SPUtil.class.getSimpleName();

    // 默认值
    public static final String SHARE_STRING = "";
    public static final Integer SHARE_INTEGER = 0;
    public static final Boolean SHARE_BOOLEAN = false;
    public static final Float SHARE_FLOAT = 0.0f;
    public static final Long SHARE_LONG = 0l;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static SPUtil instance;

    public static SPUtil getInstance(Context context){
        if(instance == null){
            instance = new SPUtil(context);
        }
        return instance;
    }
    private  SPUtil(Context context){
        sp = context.getSharedPreferences("bluetooth",
                Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param key
     * @param object
     */
    private void put(String key, Object object) {
        editor.putString(key, (String) object);
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    private  String get(String key, String defaultObject) {
        return sp.getString(key,defaultObject);

    }
    // 保存手势设置信息
    public  final String KEY_GESTURE_SETTINGS = "geustre";


    /** ---------------------------------------方法--------------------------------------------- */
    // 保存手势设置信息
    public  void setGestureSettings(Setting settings) {
        String settingsStr = new Gson().toJson(settings);
        put(KEY_GESTURE_SETTINGS, settingsStr);
    }
    // 读取手势设置信息
    public Setting getGestureSettings() {
        String settingsStr = (String)get(KEY_GESTURE_SETTINGS, SPUtil.SHARE_STRING);
        Setting settings = new Gson().fromJson(settingsStr, Setting.class);
        return settings;
    }

    /** ---------------------------------------方法--------------------------------------------- */
    // 保存手势设置信息
    public  void setGestureSettings(String str) {
        put( KEY_GESTURE_SETTINGS, str);
    }
    // 读取手势设置信息
    public  String getGestureSettings(String defaultStr) {
        String settingsStr = get(KEY_GESTURE_SETTINGS, defaultStr);
        return settingsStr;
    }

    public void setUpbtnPos(String pos){
        editor.putString("UP",pos);
        editor.commit();
    }

    public String getUpbtnPos(){
        return sp.getString("UP","");
    }


    public void setDownbtnPos(String pos){
        editor.putString("Down",pos);
        editor.commit();
    }

    public String getDownbtnPos(){
        return sp.getString("Down","");
    }

//    /**
//     * 移除某个key值已经对应的值
//     * @param context
//     * @param key
//     */
//    public static void remove(Context context, String key) {
//        SharedPreferences sp = context.getSharedPreferences(Config.FILE_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(key);
//        SharedPreferencesCompat.apply(editor);
//    }
//
//    /**
//     * 清除所有数据
//     * @param context
//     */
//    public static void clear(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(Config.FILE_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        SharedPreferencesCompat.apply(editor);
//    }
//
//    /**
//     * 查询某个key是否已经存在
//     * @param context
//     * @param key
//     * @return
//     */
//    public static boolean contains(Context context, String key) {
//        SharedPreferences sp = context.getSharedPreferences(Config.FILE_NAME,
//                Context.MODE_PRIVATE);
//        return sp.contains(key);
//    }
//
//    /**
//     * 返回所有的键值对
//     * @param context
//     * @return
//     */
//    public static Map<String, ?> getAll(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(Config.FILE_NAME,
//                Context.MODE_PRIVATE);
//        return sp.getAll();
//    }
//
//    /**
//     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
//     * @author zhy
//     *
//     */
//    private static class SharedPreferencesCompat {
//        private static final Method sApplyMethod = findApplyMethod();
//
//        /**
//         * 反射查找apply的方法
//         * @return
//         */
//        @SuppressWarnings({ "unchecked", "rawtypes" })
//        private static Method findApplyMethod() {
//            try {
//                Class clz = SharedPreferences.Editor.class;
//                return clz.getMethod("apply");
//            } catch (NoSuchMethodException e) {
//                // do nothing
//            }
//
//            return null;
//        }
//
//        /**
//         * 如果找到则使用apply执行，否则使用commit
//         *
//         * @param editor
//         */
//        public static void apply(SharedPreferences.Editor editor) {
//            try {
//                if (sApplyMethod != null) {
//                    sApplyMethod.invoke(editor);
//                    return;
//                }
//            } catch (IllegalArgumentException
//                    | InvocationTargetException
//                    | IllegalAccessException e) {
//                // do nothing
//            }
//            editor.commit();
//        }
//    }
}
