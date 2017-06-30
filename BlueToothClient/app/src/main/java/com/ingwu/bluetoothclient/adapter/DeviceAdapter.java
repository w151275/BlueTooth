package com.ingwu.bluetoothclient.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ingwu.bluetoothclient.R;
import com.ingwu.bluetoothclient.bean.DeviceRecord;

import java.util.List;

/**
 * Created by Ing. Wu on 2017/4/10.
 */

public class DeviceAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceRecord> devices;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context, List<DeviceRecord> devices) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.devices = devices;
    }
    @Override
    public int getCount() {
        return devices != null && devices.size()>0 ?devices.size():0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluelist_lay,null);
            holder = new ViewHolder();
            holder.tvAdress = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = devices.get(position).device.getName();
        holder.tvAdress.setText(!TextUtils.isEmpty(name)?name :devices.get(position).device.getAddress());
        return convertView;
    }

    class ViewHolder{
        TextView tvAdress;
    }
}
