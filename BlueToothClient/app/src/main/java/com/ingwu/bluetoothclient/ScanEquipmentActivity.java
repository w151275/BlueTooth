package com.ingwu.bluetoothclient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.adapter.DeviceAdapter;
import com.ingwu.bluetoothclient.bean.DeviceRecord;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class ScanEquipmentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final int OPEN_BLUETOOTH = 1001;
    private TextView tvStartScan,tvNo;
    private RelativeLayout ivback;
    private ListView listViewBlue;
    private DeviceAdapter mBlueListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    List<DeviceRecord> deviceList;
    private DeviceAdapter deviceAdapter;
    private ServiceConnection onService = null;
    Map<String, Integer> devRssiValues;
    private Handler mHandler;
    private boolean mScanning;

    private final long SCAN_PERIOD = 10000; //10 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanequipment_lay);
        initBlue();
        initView();
        initAction();
        initData();
        scanLeDevice(true);
        tvStartScan.setText(getString(R.string.set_equipment_stop));

    }

    private  void initBlue(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
            return;
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }
        mBluetoothAdapter.enable();
    }

    private void initData(){
        deviceList = new ArrayList<DeviceRecord>();
        deviceAdapter = new DeviceAdapter(this, deviceList);
        devRssiValues = new HashMap<String, Integer>();
        mHandler = new Handler();
        listViewBlue.setAdapter(deviceAdapter);

    }

    private void initView(){
        ivback = (RelativeLayout) findViewById(R.id.rel_back);
        tvNo = (TextView) findViewById(R.id.tv_not_support);
        tvStartScan = (TextView) findViewById(R.id.tv_scan_startscan);
        listViewBlue  = (ListView) findViewById(R.id.list_bluetoothlist);
    }

    private void initAction(){
        ivback.setOnClickListener(this);
        tvStartScan.setOnClickListener(this);
        listViewBlue.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.rel_back == id){
            finish();
        }else if(R.id.tv_scan_startscan == id){
            if (mScanning==false){
                scanLeDevice(true);
            } else{
                scanLeDevice(false);
            }


        }
    }

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        DeviceRecord bluetoothRecord = deviceList.get(position);
//        if("BWRC012".equals(bluetoothRecord.device.getName())){
            EventBus.getDefault().post(bluetoothRecord);
            finish();
//        }

    }

    private void scanLeDevice( boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    tvStartScan.setText(getString(R.string.set_equipment_start));
                }
            }, 10*1000);
            if(mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.enable();
            }
            mScanning = true;
            tvStartScan.setText(getString(R.string.set_equipment_stop));
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            clear();
            tvStartScan.setText(getString(R.string.set_equipment_start));
        }

    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String devname=device.getName();
                            ILetrylinkUtil myutil=ILetrylinkUtil.getDefaultUtil();
                            String info;
//                            int distance=myutil.CalculateAccuracy(rssi);
                            info=devname;//myutil.letrylink_uuid+"-"+myutil.letrylink_major+"-"+myutil.letrylink_minor+"-dist:"+distance;
                            addDevice(device,rssi,info);

                        }
                    });
                }
            };

    private void addDevice(BluetoothDevice device, int rssi,String bt_id) {
        boolean deviceFound = false;

        for (DeviceRecord listDev : deviceList) {
            if (listDev.device.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        devRssiValues.put(device.getAddress(), rssi);
        if (!deviceFound) {
            if("BWRC012".equals(device.getName())){
                deviceList.add(new DeviceRecord(device, rssi,bt_id));
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvStartScan.setText(getString(R.string.set_equipment_start));
                    }
                });
                mScanning = false;
            }
            deviceAdapter.notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized(deviceList) {
            deviceList.clear();
            deviceAdapter.notifyDataSetChanged();

        }
    }
}
