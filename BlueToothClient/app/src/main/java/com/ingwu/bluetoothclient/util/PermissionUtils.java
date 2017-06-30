package com.ingwu.bluetoothclient.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理工具类
 * @author lvpengcheng 2016/01/14
 */
public class PermissionUtils {

    public static final int ACTION_REQUEST_CONTRACT = 1;
    public static final int ACTION_REQUEST_CAMERA = 2;
    public static final int ACTION_REQUEST_MICROPHONE = 3;


    public static boolean checkPermission(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 基本权限是否已授权
     *
     * @param requirePermissionList 方法执行完毕后后赋值为需要获取的基本权限列表
     */
    public static boolean checkHasBasePermission(Context context, @NonNull List<String> requirePermissionList){
        String basePermissions[] = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String permission : basePermissions) {
            if (!checkPermission(context, permission)){
                requirePermissionList.add(permission);
            }
        }
        return requirePermissionList.size() <= 0;
    }

    /**
     * 获取需要申请的权限
     * @param action 需要什么功能的权限
     */
    public static String[] getPermissionNeeded(Context context, int action){
        switch (action){
            case ACTION_REQUEST_CONTRACT:
                return getDeniedPermissions(context, new String[]{Manifest.permission.READ_CONTACTS});
            case ACTION_REQUEST_CAMERA:
                return getDeniedPermissions(context, new String[]{Manifest.permission.CAMERA});
            case ACTION_REQUEST_MICROPHONE:
                return getDeniedPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO});
        }
        return  null;
    }

    /**
     * 获取需要但未授权的权限
     */
    private static  String[] getDeniedPermissions(Context contexts, String[] permissions) {
        List<String> permissionNeed = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(contexts, permission)) {
                permissionNeed.add(permission);
            }
        }
        return permissionNeed.toArray(new String[permissionNeed.size()]);
    }
}
