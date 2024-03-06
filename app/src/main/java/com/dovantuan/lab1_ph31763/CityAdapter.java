package com.dovantuan.lab1_ph31763;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {
    private List<City> listCity;
    private Context context;

    public CityAdapter(@NonNull Context context, int resource, List<City> listCity) {
        super(context, resource, listCity);
        this.context = context;
        this.listCity = listCity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.itemlv, parent, false);

            holder = new ViewHolder();
            holder.capitalTextView = convertView.findViewById(R.id.capital);
            holder.countryTextView = convertView.findViewById(R.id.country);
            holder.nameTextView = convertView.findViewById(R.id.name);
            holder.populationTextView = convertView.findViewById(R.id.population);
            holder.regionsTextView = convertView.findViewById(R.id.regions);
            holder.stateTextView = convertView.findViewById(R.id.state);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        City city = getItem(position);
        if (city != null) {
            holder.capitalTextView.setText("Capital: " + (city.isCapital() ? "Yes" : "No"));
            holder.countryTextView.setText("Country: " + city.getCountry());
            holder.nameTextView.setText("Name: " + city.getName());
            holder.populationTextView.setText("Population: " + String.valueOf(city.getPopulation()));
            holder.regionsTextView.setText("Regions: " + city.getRegions());
            holder.stateTextView.setText("State: " + city.getState());

        }


        return convertView;
    }
    static class ViewHolder {
        TextView capitalTextView;
        TextView countryTextView;
        TextView nameTextView;
        TextView populationTextView;
        TextView regionsTextView;
        TextView stateTextView;
    }
}

