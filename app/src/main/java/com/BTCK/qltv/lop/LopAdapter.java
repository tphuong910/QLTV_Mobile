package com.BTCK.qltv.lop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.BTCK.qltv.R;
import java.util.List;

public class LopAdapter extends BaseAdapter {
    private Context context;
    private List<Lop> list;

    public LopAdapter(Context context, List<Lop> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_lop, viewGroup, false);
        }
        TextView txtTenLop = view.findViewById(R.id.txtTenLop);
        TextView txtTenKhoa = view.findViewById(R.id.txtTenKhoa);
        TextView txtMaLop = view.findViewById(R.id.txtMaLop);

        Lop lop = list.get(i);
        txtTenLop.setText(lop.getTenLop());
        txtTenKhoa.setText("Khoa: " + lop.getTenKhoa());
        txtMaLop.setText("Mã: " + lop.getMaLop());

        return view;
    }
}
