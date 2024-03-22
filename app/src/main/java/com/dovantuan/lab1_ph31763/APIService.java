package com.dovantuan.lab1_ph31763;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    String DOMAIN = "http://192.168.1.12:3000/";

    @GET("/api/list")
    Call<List<CarModel>> getCars();

    @POST("/api/add_xe")
    Call<Void> addCar(@Body CarModel car);

    @DELETE("/api/xoa/{id}")
    Call<Void> deleteCar(@Path("id") String carId);

    @PUT("/api/update/{id}") // Địa chỉ API để cập nhật sinh viên
    Call<Void> updateCar(@Path("id") String id, @Body CarModel carModel);
}
