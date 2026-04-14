package com.BTCK.qltv.tacgia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.BTCK.qltv.R;
import java.util.List;

public class TacGiaAdapter extends BaseAdapter {
    private Context context;
    private List<TacGia> list;

    public TacGiaAdapter(Context context, List<TacGia> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int i) { return list.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tac_gia, viewGroup, false);
        }
        TextView txtTen = view.findViewById(R.id.txtTenTG);
        TextView txtInfo = view.findViewById(R.id.txtThongTinTG);
        TextView txtMa = view.findViewById(R.id.txtMaTG);

        TacGia tg = list.get(i);
        txtTen.setText(tg.getTenTG());
        txtInfo.setText(tg.getNamSinh() + " - " + tg.getGioiTinh() + " - " + tg.getQuocTich());
        txtMa.setText("Mã: " + tg.getMaTG());

        return view;
    }
}