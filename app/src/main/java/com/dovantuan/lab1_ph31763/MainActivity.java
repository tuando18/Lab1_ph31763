package com.dovantuan.lab1_ph31763;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dovantuan.lab1_ph31763.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<City> cityList;
    private CityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ListView listView = findViewById(R.id.lvCity);
        cityList = new ArrayList<>();  // Initialize the list
        cityAdapter = new CityAdapter(this, R.layout.itemlv, cityList);
        listView.setAdapter(cityAdapter);

        ghiDuLieu();

        docDulieu();


        Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, ManHinhCho.class));
                finish();
            }
        });

        ImageView btnThem = findViewById(R.id.btnThem);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCityDialog();
            }
        });

    }

    private void ghiDuLieu () {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);
    }

    private void docDulieu() {
        CollectionReference citiesRef = db.collection("cities");

        citiesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    City city = document.toObject(City.class);
                    cityList.add(city);
                }
                cityAdapter.notifyDataSetChanged();
            } else {

            }
        });
    }

    private void showAddCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(dialogView);


        EditText edCountry = dialogView.findViewById(R.id.ed_Country);
        EditText edName = dialogView.findViewById(R.id.ed_Name);
        EditText edPopulation = dialogView.findViewById(R.id.ed_Population);
        Spinner spCapital = dialogView.findViewById(R.id.spCapital);
        EditText edRegions = dialogView.findViewById(R.id.ed_Regions);
        EditText edState = dialogView.findViewById(R.id.ed_State);

        List<String> capitalOptions = Arrays.asList("Yes", "No");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, capitalOptions);
        spCapital.setAdapter(spinnerAdapter);
//        Button btnThem = dialogView.findViewById(R.id.btnAdd);
//        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String country = edCountry.getText().toString();
                String name = edName.getText().toString();
                String populationStr = edPopulation.getText().toString();
                String capital = spCapital.getSelectedItem().toString();
                String regions = edRegions.getText().toString();
                String state = edState.getText().toString();

                // Validate input
                if (country.isEmpty() || name.isEmpty() || populationStr.isEmpty() || regions.isEmpty() || state.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                int population = Integer.parseInt(populationStr);

                themDuLieu(country, name, population, capital, regions, state);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void themDuLieu(String country, String name, int population, String capital, String regions, String state) {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> newCity = new HashMap<>();
        newCity.put("country", country);
        newCity.put("name", name);
        newCity.put("population", population);
        newCity.put("capital", "Yes".equals(capital)); // Convert string to boolean
        newCity.put("regions", Arrays.asList(regions.split("\\s*,\\s*")));
        newCity.put("state", state);

        cities.add(newCity)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MainActivity", "New city added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "New city added", Toast.LENGTH_SHORT).show();

                        docDulieu();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "Error adding new city", e);
                        Toast.makeText(MainActivity.this, "Error adding new city", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}