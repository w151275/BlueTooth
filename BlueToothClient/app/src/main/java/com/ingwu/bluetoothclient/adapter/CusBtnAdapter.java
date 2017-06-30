package com.ingwu.bluetoothclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingwu.bluetoothclient.R;
import com.ingwu.bluetoothclient.bean.CusBtnBean;
import java.util.List;

/**
 * Created by Ing. Wu on 2017/4/25.
 */

public class CusBtnAdapter extends BaseAdapter{

    private Context context;
    private List<CusBtnBean> idlist;

    public CusBtnAdapter(Context context, List<CusBtnBean> idlist) {
        this.context = context;
        this.idlist = idlist;
    }

    @Override
    public int getCount() {
        return idlist.size();
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
        ViewHolder holder = null;
        if(convertView == null){
            holder= new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cusbtn_lay,null);
            holder.ivBtn = (ImageView) convertView.findViewById(R.id.iv_cus_btn);
            holder.tvBtn = (TextView) convertView.findViewById(R.id.tv_cus_btn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvBtn.setText(idlist.get(position).getName());
         if(idlist.get(position).isSelect()){
             holder.ivBtn.setBackgroundResource(idlist.get(position).getId());
             holder.tvBtn.setTextColor(context.getResources().getColor(R.color.green));
        }else {
             holder.ivBtn.setBackgroundResource(idlist.get(position).getDefaultId());
             holder.tvBtn.setTextColor(context.getResources().getColor(R.color.white));
         }
        return convertView;
    }

    class ViewHolder {
        ImageView ivBtn;
        TextView tvBtn;
    }
}
