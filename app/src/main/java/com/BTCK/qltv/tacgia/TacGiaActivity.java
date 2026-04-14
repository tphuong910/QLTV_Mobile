package com.BTCK.qltv.tacgia;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TacGiaActivity extends AppCompatActivity {
    private ListView lvTacGia;
    private FloatingActionButton fabThem;
    private ImageView imgBack;
    private TacGiaQuery tacGiaQuery;
    private List<TacGia> listTacGia;
    private TacGiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tac_gia);

        lvTacGia = findViewById(R.id.lvTacGia);
        fabThem = findViewById(R.id.fabThemTG);
        imgBack = findViewById(R.id.imgBackTG); // Đảm bảo ID này tồn tại trong XML

        tacGiaQuery = new TacGiaQuery(this);

        capNhatDanhSach();

        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        fabThem.setOnClickListener(v -> {
            startActivity(new Intent(TacGiaActivity.this, AddTacGiaActivity.class));
        });

        registerForContextMenu(lvTacGia);
    }

    private void capNhatDanhSach() {
        listTacGia = tacGiaQuery.layTatCaTacGia();
        adapter = new TacGiaAdapter(this, listTacGia);
        lvTacGia.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capNhatDanhSach();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);
        
        TacGia tg = listTacGia.get(info.position);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(TacGiaActivity.this, UpdateTacGiaActivity.class);
            intent.putExtra("MaTG", tg.getMaTG());
            intent.putExtra("TenTG", tg.getTenTG());
            intent.putExtra("NamSinh", tg.getNamSinh());
            intent.putExtra("GioiTinh", tg.getGioiTinh());
            intent.putExtra("QuocTich", tg.getQuocTich());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tác giả " + tg.getTenTG() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (tacGiaQuery.xoaTacGia(tg.getMaTG())) {
                            Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                            capNhatDanhSach();
                        }
                    })
                    .setNegativeButton("Hủy", null).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}