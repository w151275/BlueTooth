package com.ingwu.bluetoothclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.chinatelecom.dialoglibrary.BounceEnter.BounceBottomEnter;
import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.chinatelecom.dialoglibrary.widget.MaterialDialog;
import com.ingwu.bluetoothclient.util.PermissionUtils;


/**
 * 申请权限
 * @author  lvpengcheng 2016/01/14
 */
public class RequestPermissionActivity extends BaseActivity {

    private static final int REQUEST_PERMISSIONS = 101;
    private int mRequestAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (null != data) {
            mRequestAction = data.getInt("PERMISSION", -1);
            String[] permissions = PermissionUtils.getPermissionNeeded(this, mRequestAction);
            if (null != permissions) {
                if (permissions.length > 0) {
                    showPermissionsDialog(permissions);
                    return;
                } else {
                    //已授权 无需申请
                    setResult(RESULT_OK);
                }
            }
        }
        finish();
    }

    private void showPermissionsDialog(final String[] permissions) {
        OnBtnClickL clickListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(RequestPermissionActivity.this, permissions,
                        REQUEST_PERMISSIONS);
            }
        };
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };

        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content("权限申请")
                .btnText( "取消","确定")
                .showAnim(new BounceBottomEnter())
                .show();
        OnBtnClickL defaultListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        };
        dialog.setOnBtnClickL(defaultListener,clickListener);
        if (null != cancelListener) {
            dialog.setOnCancelListener(cancelListener);
        }
        dialog.isTitleShow(true);
        dialog.title("权限申请");
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            showPermissionRefusedDialog();
                            return;
                        }
                    }
                    //授权通过
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void showPermissionRefusedDialog() {

        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content("权限申请")
                .btnText( "取消","确定")
                .showAnim(new BounceBottomEnter())
                .show();
        OnBtnClickL defaultListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        OnBtnClickL clickListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        dialog.setOnBtnClickL(defaultListener,clickListener);
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        if (null != cancelListener) {
            dialog.setOnCancelListener(cancelListener);
        }
        dialog.isTitleShow(true);
        dialog.title("权限申请");
        dialog.setCanceledOnTouchOutside(false);
    }
}
