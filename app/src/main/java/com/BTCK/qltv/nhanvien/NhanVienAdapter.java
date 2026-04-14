package com.BTCK.qltv.nhanvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.BTCK.qltv.R;

import java.util.List;

public class NhanVienAdapter extends BaseAdapter {
    private Context context;
    private List<NhanVien> list;
    private OnActionClickListener listener;

    public interface OnActionClickListener {
        void onEdit(NhanVien nv);
        void onDelete(NhanVien nv);
    }

    public NhanVienAdapter(Context context, List<NhanVien> list, OnActionClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nhan_vien, parent, false);
        }

        TextView tvTenNV = convertView.findViewById(R.id.tvTenNV);
        TextView tvMaNV = convertView.findViewById(R.id.tvMaNV);
        TextView tvVaiTro = convertView.findViewById(R.id.tvVaiTro);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        NhanVien nv = list.get(position);
        tvTenNV.setText(nv.getTenNV());
        tvMaNV.setText("Mã NV: " + nv.getMaNV());
        tvVaiTro.setText("Vai trò: " + nv.getVaiTro());

        btnEdit.setOnClickListener(v -> listener.onEdit(nv));
        btnDelete.setOnClickListener(v -> listener.onDelete(nv));

        return convertView;
    }
}
