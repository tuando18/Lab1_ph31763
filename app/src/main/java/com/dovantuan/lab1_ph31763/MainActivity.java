package com.dovantuan.lab1_ph31763;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView lvMain;
    List<CarModel> listCarModel;

    CarAdapter carAdapter;

    ImageView btnThem;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvMain = findViewById(R.id.listviewMain);
        btnThem = findViewById(R.id.btnThem);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        fetchDataFromServer();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_them();
            }
        });
    }

    private void fetchDataFromServer() {
        Call<List<CarModel>> call = apiService.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();
                    carAdapter = new CarAdapter(MainActivity.this, listCarModel);
                    lvMain.setAdapter(carAdapter);
                } else {
                    Log.e("Main", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Main", "Failed to fetch data from server: " + t.getMessage());
            }
        });
    }

    public void dialog_them() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_car, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edTen, edNamSX, edHang, edGia;
        edTen = view.findViewById(R.id.ed_ten);
        edHang= view.findViewById(R.id.ed_hang);
        edNamSX = view.findViewById(R.id.ed_namSx);
        edGia = view.findViewById(R.id.ed_gia);

        // Sự kiện khi nhấn nút "Thêm" trong dialog
        Button btn_add = view.findViewById(R.id.btnAdd);
        Button btn_huy = view.findViewById(R.id.btnHuy);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập liệu
                String ten = edTen.getText().toString();
                int namSX = Integer.parseInt(edNamSX.getText().toString());
                String hang = edHang.getText().toString();
                double gia = Double.parseDouble(edGia.getText().toString());

                // Tạo đối tượng CarModel mới
                CarModel newCar = new CarModel(ten, namSX, hang, gia);

                // Gửi dữ liệu lên server
                Call<Void> addCarCall = apiService.addCar(newCar);
                addCarCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật ListView
                            listCarModel.add(newCar);
                            carAdapter.notifyDataSetChanged();
                            // Đóng dialog lại
                            dialog.dismiss();
                        } else {
                            Log.e("Main", "Failed to add car: " + response.message());
                            Toast.makeText(MainActivity.this, "Add car failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Main", "Failed to add car: " + t.getMessage());
                        Toast.makeText(MainActivity.this, "Failed to add car", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Sự kiện khi nhấn nút "Hủy" trong dialog
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog lại khi nhấn nút "Hủy"
                dialog.dismiss();
            }
        });
    }
}
