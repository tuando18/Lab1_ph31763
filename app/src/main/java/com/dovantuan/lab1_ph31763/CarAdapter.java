package com.dovantuan.lab1_ph31763;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarAdapter extends BaseAdapter {

    private List<CarModel> carModelList;
    private Context context;
    private APIService apiService;

    public CarAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
    }

    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return carModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_car, parent, false);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Khởi tạo ApiService từ Retrofit
        apiService = retrofit.create(APIService.class);

        TextView tvID = rowView.findViewById(R.id.tvId);
        ImageView imgAvatar = rowView.findViewById(R.id.imgAvatatr);
        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvNamSX = rowView.findViewById(R.id.tvNamSX);
        TextView tvHang = rowView.findViewById(R.id.tvHang);
        TextView tvGia = rowView.findViewById(R.id.tvGia);

        ImageView btnUpdate = rowView.findViewById(R.id.btnUpdate);
        ImageView btnXoa = rowView.findViewById(R.id.btnXoa);

        CarModel carModel = carModelList.get(position);

//        tvID.setText(String.valueOf(carModel.get_id()));
        tvName.setText("Tên xe: " + carModel.getTen());
        tvNamSX.setText("Năm SX: " + carModel.getNamSx());
        tvHang.setText("Hãng: " + carModel.getHang());
        tvGia.setText("Giá: " + carModel.getGia());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToUpdateCar(carModel);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận xóa
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context);
                confirmDialog.setMessage("Bạn có chắc chắn muốn xóa?");
                confirmDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi phương thức để xóa xe
                        deleteCarOnServer(carModel);
                    }
                });
                confirmDialog.setNegativeButton("Không", null);
                confirmDialog.show();
            }
        });

        return rowView;
    }

    private void showDialogToUpdateCar(CarModel carModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_car, null);
        builder.setView(dialogView);

        EditText edTen = dialogView.findViewById(R.id.ed_tenUp);
        EditText edNamSX = dialogView.findViewById(R.id.ed_namSxUp);
        EditText edHang = dialogView.findViewById(R.id.ed_hangUp);
        EditText edGia = dialogView.findViewById(R.id.ed_giaUp);

        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        Button btnCancel = dialogView.findViewById(R.id.btnHuyUp);

        edTen.setText(carModel.getTen());
        edNamSX.setText(String.valueOf(carModel.getNamSx()));
        edHang.setText(carModel.getHang());
        edGia.setText(String.valueOf(carModel.getGia()));

        AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edTen.getText().toString();
                int namSX = Integer.parseInt(edNamSX.getText().toString());
                String hang = edHang.getText().toString();
                double gia = Double.parseDouble(edGia.getText().toString());

                carModel.setTen(ten);
                carModel.setNamSx(namSX);
                carModel.setHang(hang);
                carModel.setGia(gia);

                // Gửi yêu cầu cập nhật lên server
                updateCarOnServer(carModel, dialog);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateCarOnServer(CarModel carModel, AlertDialog dialog) {
        // Gửi yêu cầu cập nhật thông tin xe lên server bằng Retrofit hoặc các thư viện HTTP khác
        // Dưới đây là một ví dụ giả sử sử dụng Retrofit
        Call<Void> call = apiService.updateCar(carModel.get_id(), carModel); // Thêm id của xe vào đây
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    dialog.dismiss();
                    // Hiển thị thông báo hoặc làm gì đó khi cập nhật thành công
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                    // Cập nhật lại danh sách xe và cập nhật giao diện
                    notifyDataSetChanged();
                } else {
                    // Xử lý khi cập nhật không thành công
                    Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCarOnServer(CarModel carModel) {
        Call<Void> call = apiService.deleteCar(carModel.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa xe khỏi danh sách
                    carModelList.remove(carModel);
                    notifyDataSetChanged();
                    // Hiển thị thông báo xóa thành công
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi xóa không thành công
                    Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
