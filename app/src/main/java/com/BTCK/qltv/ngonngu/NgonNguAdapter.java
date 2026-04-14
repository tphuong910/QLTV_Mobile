package com.BTCK.qltv.ngonngu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NgonNguAdapter extends BaseAdapter {

    private Context context;
    private List<NgonNgu> listNgonNgu;

    public NgonNguAdapter(Context context, List<NgonNgu> listNgonNgu) {
        this.context = context;
        this.listNgonNgu = listNgonNgu;
    }

    @Override
    public int getCount() {
        return listNgonNgu.size();
    }

    @Override
    public Object getItem(int position) {
        return listNgonNgu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Sử dụng layout có sẵn của Android gồm 2 dòng chữ
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView txtTenNN = convertView.findViewById(android.R.id.text1);
        TextView txtMaNN = convertView.findViewById(android.R.id.text2);

        NgonNgu nn = listNgonNgu.get(position);

        txtTenNN.setText(nn.getTenNN());
        txtMaNN.setText("Mã ngôn ngữ: " + nn.getMaNN());

        return convertView;
    }
}