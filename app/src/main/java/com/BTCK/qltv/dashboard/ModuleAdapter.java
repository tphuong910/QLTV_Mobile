package com.BTCK.qltv.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BTCK.qltv.R;
import com.BTCK.qltv.dashboard.Module;

import java.util.List;

public class ModuleAdapter extends BaseAdapter {
    private Context context;
    private List<Module> moduleList;

    public ModuleAdapter(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @Override
    public int getCount() {
        return moduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return moduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dashboard_module, parent, false);
        }

        ImageView imgIcon = convertView.findViewById(R.id.imgModuleIcon);
        TextView tvName = convertView.findViewById(R.id.tvModuleName);

        Module currentModule = moduleList.get(position);

        imgIcon.setImageResource(currentModule.getIconId());
        tvName.setText(currentModule.getName());

        return convertView;
    }
}