package com.BTCK.qltv.lop;

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

public class LopActivity extends AppCompatActivity {
    private ListView lvLop;
    private FloatingActionButton fabThem;
    private ImageView imgBack;
    private LopQuery lopQuery;
    private List<Lop> listLop;
    private LopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        lvLop = findViewById(R.id.lvLop);
        fabThem = findViewById(R.id.fabThemLop);
        imgBack = findViewById(R.id.imgBackLop);

        lopQuery = new LopQuery(this);

        capNhatDanhSach();

        if (imgBack != null) {
            imgBack.setOnClickListener(v -> finish());
        }

        fabThem.setOnClickListener(v -> {
            startActivity(new Intent(LopActivity.this, AddLopActivity.class));
        });

        registerForContextMenu(lvLop);
    }

    private void capNhatDanhSach() {
        listLop = lopQuery.layTatCaLop();
        adapter = new LopAdapter(this, listLop);
        lvLop.setAdapter(adapter);
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
        
        Lop lop = listLop.get(info.position);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(LopActivity.this, UpdateLopActivity.class);
            intent.putExtra("MaLop", lop.getMaLop());
            intent.putExtra("TenLop", lop.getTenLop());
            intent.putExtra("MaKhoa", lop.getMaKhoa());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa lớp " + lop.getTenLop() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (lopQuery.xoaLop(lop.getMaLop())) {
                            Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                            capNhatDanhSach();
                        } else {
                            Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}