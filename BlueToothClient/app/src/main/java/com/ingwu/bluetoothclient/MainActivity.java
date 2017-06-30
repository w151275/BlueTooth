package com.ingwu.bluetoothclient;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.ingwu.bluetoothclient.bean.DeviceRecord;
import com.ingwu.bluetoothclient.util.SPUtil;
import com.ingwu.bluetoothclient.view.ISlideLockListener;
import com.ingwu.bluetoothclient.view.NoticeDialogView;
import com.ingwu.bluetoothclient.view.SlideLockView;
import com.ingwu.bluetoothclient.view.SlideView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener,ISlideLockListener ,NoticeDialogView.ClikCallback{

    private RelativeLayout relMainBtn;
    private RelativeLayout ivHelp;
    private TextView tvSetting;
    private ImageView ivImage,ivAdd;
    private final int REQUEST_SETTING_CODE = 1001;
    private ScreenLockBoardcast mScreenReceiver;

    private LinearLayout linearbtn;
    private ImageView ivLeftBtn ,ivRightBtn;

    private BluetoothAdapter mBtAdapter = null;


    String address ="";
    String name = "";
    String[] arrData=null;
    int cur_item = 0;
    Timer my_timer ;
    TimerTask timerTask ;
    int is_need_read=1;
    private SlideLockView slideView;
    private boolean left,right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNoticeView();
        mScreenReceiver = new ScreenLockBoardcast();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            showDialogView(getString(R.string.main_dialog_tile), getString(R.string.scan_blue_no), getString(R.string.main_dialog_postivebtn), new OnBtnClickL() {
                @Override
                public void onBtnClick(DialogInterface dialog) {
                    finish();
                }
            });
            return;
        }
        initblueService();
        ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
        myutil.SetContext(this);
        EventBus.getDefault().register(this);
        startReceiver();
        initView();
        initAction();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setBtnView();
    }

    private void showNoticeView(){
        NoticeDialogView dialogView = new NoticeDialogView(this);
        dialogView.setClick(this);
        dialogView.show();
    }

    public void initView(){
        if(!TextUtils.isEmpty(SPUtil.getInstance(this).getGestureSettings(""))){
            Intent intent1 = new Intent(this, LockOffActivity.class);
            intent1.putExtra("ISAUTH",true);
            intent1.putExtra("ISFROM","MAIN");
            startActivityForResult(intent1,REQUEST_SETTING_CODE);
        }
        slideView = (SlideLockView) findViewById(R.id.slider_ok);
        ivAdd = (ImageView) findViewById(R.id.iv_main_add);
        ivHelp = (RelativeLayout) findViewById(R.id.rel_back);
        tvSetting = (TextView) findViewById(R.id.tv_main_setting);
        ivImage = (ImageView) findViewById(R.id.image);
        linearbtn = (LinearLayout) findViewById(R.id.linear_main_btn);
        ivLeftBtn = (ImageView) findViewById(R.id.iv_main_leftbtn);
        ivLeftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        left = true;
                        Log.e(Utils.TAG,"00++++++key_down");
                        myutil.GenButtonData(1,1);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);

                        break;
                    case MotionEvent.ACTION_UP:
                        left = false;
                        Log.e(Utils.TAG,"11++++++key_up");

                        myutil.GenButtonData(1,0);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        left = false;
                        Log.e(Utils.TAG,"22++++++key_up");
                        myutil.GenButtonData(1,0);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);

                        break;
                }
                return true;
            }
        });
        ivRightBtn = (ImageView) findViewById(R.id.iv_main_rightbtn);
        ivRightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        right = true;
                        Log.e(Utils.TAG,"00++++++key_down");
                        myutil.GenButtonData(2,1);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(Utils.TAG,"11++++++key_up");
                        right = false;
                        myutil.GenButtonData(2,0);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        right = false;
                        Log.e(Utils.TAG,"22++++++key_up");
                        myutil.GenButtonData(2,0);
                        Utils.mService.writeRXCharacteristic(myutil.sendData);

                        break;
                }
                return true;
            }
        });
        relMainBtn = (RelativeLayout) findViewById(R.id.rel_main_btn);

    }

    private void setBtnView(){
        String left = spBaseUtil.getUpbtnPos();
        String right = spBaseUtil.getDownbtnPos();
        if(cusBtnBgUtil.getCusBtnbyName(left)!= null){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),cusBtnBgUtil.getCusBtnbyName(left).getDefaultId());

            if(bitmap!= null){
                ivLeftBtn.setImageBitmap(bitmap);
            }else {
                ivLeftBtn.setImageResource(R.mipmap.operationicon_up);
            }
        }else {
            ivLeftBtn.setImageResource(R.mipmap.operationicon_up);
        }
        if(cusBtnBgUtil.getCusBtnbyName(right)!= null){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),cusBtnBgUtil.getCusBtnbyName(right).getDefaultId());
            if(bitmap!= null){
                ivRightBtn.setImageBitmap(bitmap);
            }else {
                ivRightBtn.setImageResource(R.mipmap.operationicon_down);
            }
        }else {
            ivRightBtn.setImageResource(R.mipmap.operationicon_down);

        }
    }
    public void initAction(){
        ivAdd.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        ivLeftBtn.setOnClickListener(this);
        ivRightBtn.setOnClickListener(this);
        slideView.setListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_back){
            startActivity(new Intent(this,OperationDesActivity.class));
//            showDialogView(getResources().getString(R.string.main_dialog_tile),getResources().getString(R.string.main_dialog_content),getResources().getString(R.string.main_dialog_postivebtn));
        }else if(v.getId() == R.id.tv_main_setting){
            startActivity(new Intent(this,SettingActivity.class));
        }else if(v.getId() == R.id.iv_main_add){
            startActivity(new Intent(this,ScanEquipmentActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SETTING_CODE){
            if(RESULT_OK == resultCode){
                finish();
            }
        }
    }


    @Override
    public void onPause() {
        Log.e(Utils.TAG,"ManuallyActivity:onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mScreenReceiver != null){
            unRegister();
        }
        Utils.mService.disconnect();
        Utils.mService.close();
        Log.e(Utils.TAG,"ManuallyActivity:onDestroy");
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
        }
        unbindService(mServiceConnection);
        if(Utils.mService != null){
            Utils.mService.stopSelf();
            Utils.mService= null;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( DeviceRecord event) {
        initblueService();
        Toast.makeText(this,getString(R.string.main_connect),Toast.LENGTH_SHORT).show();
        address =event.device.getAddress();
        name =event.device.getName();
        Utils.mService.connect(address);
        is_need_read=1;
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());


    }

    public void startReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);

    }

    public void unRegister(){
        unregisterReceiver(mScreenReceiver);
    }


    private void initblueService() {
        Intent bindIntent = new Intent(this, BeaconService.class);
        Log.e(Utils.TAG, "service_init");
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            Utils.mService = ((BeaconService.LocalBinder) rawBinder).getService();
            Log.d(Utils.TAG, "onServiceConnected mService= " + Utils.mService);
            if (!Utils.mService.initialize()) {
                Log.e(Utils.TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }
        public void onServiceDisconnected(ComponentName classname) {
            Utils.mService = null;
        }
    };




    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            Log.e(Utils.TAG,"got action:"+action);
            if (action.equals(BeaconService.ACTION_GATT_CONNECTED)) {

                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                String msg = "["+currentDateTimeString+"] Connected to: "+ name;
                Intent msgIntent = new Intent(Utils.UPDATE_BEACON_INFO);
                msgIntent.putExtra("message", msg);
                MainActivity.this.sendBroadcast(msgIntent);

            }
            //*********************//
            if (action.equals(BeaconService.ACTION_GATT_DISCONNECTED)) {
                ivAdd.setVisibility(View.VISIBLE);
                relMainBtn.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,String.format(getString(R.string.main_connect_fail),name),Toast.LENGTH_SHORT).show();
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                String msg = "["+currentDateTimeString+"] DisConnected to: "+ name;
                Intent msgIntent = new Intent(Utils.UPDATE_BEACON_INFO);
                msgIntent.putExtra("message", msg);
                MainActivity.this.sendBroadcast(msgIntent);
            }

            if (action.equals(BeaconService.ACTION_GATT_SERVICES_DISCOVERED)) {
                relMainBtn.setVisibility(View.VISIBLE);
                ivAdd.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,String.format(getString(R.string.main_connect_success),name),Toast.LENGTH_SHORT).show();
                ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
//
                Log.e(Utils.TAG,"write pwd"+address);
                myutil.GenSecurityData(address);
                Utils.mService.writePWDCharacteristic(myutil.sendData);
                is_need_read=1;

                myutil.GenLocalData(address);
                Utils.mService.writePWDCharacteristic(myutil.sendData);


            }

            if (action.equals(BeaconService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(BeaconService.EXTRA_DATA);

                try {
                    String text = new String(txValue, "UTF-8");
                    String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                    String msg = "["+currentDateTimeString+"] Rx: "+ name;
                    Intent msgIntent = new Intent(Utils.UPDATE_BEACON_INFO);
                    msgIntent.putExtra("message", msg);
                } catch (Exception e) {
                    Log.e(Utils.TAG, e.toString());
                }

            }

            if (action.equals(BeaconService.ACTION_WR_FINISH)) {

                if(is_need_read==1)
                {
                    cur_item=Utils.ID;

                    Log.e(Utils.TAG,"ACTION_GATT_SERVICES_DISCOVERED: cur_item:"+cur_item);
                    //Utils.mService.readRXCharacteristic(cur_item);
                    ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();

                    Log.e(Utils.TAG,"write "+address);
                    myutil.GenLocalData(address);
//                    Log.e(Utils.TAG,"write data:"+valueToHex(myutil.sendData));

                    Utils.mService.writePWDCharacteristic(myutil.sendData);

                    is_need_read=0;
                    return;
                }
            }

            if (action.equals(BeaconService.ACTION_READ_DATA)) {

                final byte[] rx_value = intent.getByteArrayExtra(BeaconService.EXTRA_VALUE);
/*
                int type=intent.getIntExtra(BeaconService.EXTRA_TYPE, 0);

                String res="";
                if(is_need_read==0)
                    return;
                if(type==Utils.ID)
                {
                   res=parseByteLongStr(rx_value);
                   mEditId.setText(res);
                }
                else if(type==Utils.UUID)
                {
                   res=parseByteUUIDHexStr(rx_value);
                   mEditUuid.setText(res);
                }
                else if(type==Utils.Major)
                {
                   res=parseByteShortStr(rx_value);
                   mEditMajor.setText(res);
                }
                else if(type==Utils.Minor)
                {
                   res=parseByteShortStr(rx_value);
                   mEditMinor.setText(res);

                }
                else if(type==Utils.Txpower)
                {
                   res=""+rx_value[0];
                   mEditTxPowe.setText(res);

                }
                else if(type==Utils.advslot)
                {
                   res=parseByteShortStr(rx_value);
                   mEditAdvSlot.setText(res);
                   String msg="It is ready!";
                   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                }

             cur_item++;
             if(cur_item<=Utils.advslot)
                 Utils.mService.readRXCharacteristic(cur_item);
             else
                is_need_read=0;
*/
            }




            //*********************//
            if (action.equals(BeaconService.DEVICE_DOES_NOT_SUPPORT_UART)){
                Utils.mService.disconnect();
                finish();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BeaconService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BeaconService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BeaconService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BeaconService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BeaconService.DEVICE_DOES_NOT_SUPPORT_UART);
        intentFilter.addAction(BeaconService.ACTION_WR_FINISH);
        intentFilter.addAction(BeaconService.ACTION_READ_DATA);
        return intentFilter;
    }

    @Override
    public void toBottom() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivImage.setVisibility(View.VISIBLE);
                linearbtn.setVisibility(View.INVISIBLE);
                if(left){
                    left = false;
                    sendData(1,0);
                }
                if(right){
                    right = false;
                    sendData(2,0);
                }
//                ivLeftBtn.setVisibility(View.INVISIBLE);
//                ivRightBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void toTop() {
        ivImage.setVisibility(View.INVISIBLE);
        linearbtn.setVisibility(View.VISIBLE);
    }

    private void sendData(int left,int right){
        ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
        myutil.GenButtonData(left,right);
        Utils.mService.writeRXCharacteristic(myutil.sendData);
    }

    @Override
    public void click(View v) {
        if(v.getId() == R.id.tv_dialog_confirm){
            startActivity(new Intent(this,OperationDesActivity.class));
        }else if(v.getId() == R.id.tv_dialog_refuse){
            finish();
        }
    }
}
