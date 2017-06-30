package com.ingwu.bluetoothclient.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Ing. Wu on 2017/5/2.
 */

public class DeviceRecord {
    public BluetoothDevice device;
    public int rssi;
    public Long last_scanned;
    public int state;
    public String bt_id;

    public DeviceRecord(BluetoothDevice device, int rssi,String id) {
        this.device = device;
        this.rssi = rssi;
        bt_id=id;
        //this.state = state;
        last_scanned = System.currentTimeMillis() / 1000;
    }
}
