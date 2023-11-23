package com.example.apprentcarsfbfs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprentcarsfbfs.models.Cars;
import com.example.apprentcarsfbfs.R;

import java.util.ArrayList;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.rentaviewHolder>{

    public RentAdapter(ArrayList<Cars> dataEntrance){
        carsAvailable = dataEntrance;
    }

    ArrayList<Cars> carsAvailable;
    @NonNull
    @Override
    public RentAdapter.rentaviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vrenta = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_item, null, false);
        return new rentaviewHolder(vrenta);
    }

    @Override
    public void onBindViewHolder(@NonNull RentAdapter.rentaviewHolder holder, int position) {
        holder.PlateNumber.setText(carsAvailable.get(position).getPlateNumber().toString());
        holder.Brand.setText(carsAvailable.get(position).getBrand().toString());
        //holder.State.setText(String.valueOf(carsAvailable.get(position).isState()).toString());
        //holder.DailyValue.setText(String.valueOf(carsAvailable.get(position).getDailyValue()).toString());
    }

    @Override
    public int getItemCount() {
        return carsAvailable.size();
    }

    public class rentaviewHolder extends RecyclerView.ViewHolder {

        TextView PlateNumber, State, Brand, DailyValue;

        public rentaviewHolder(@NonNull View itemView){

            super(itemView);

            PlateNumber = itemView.findViewById(R.id.tvPlateNumber);
            Brand = itemView.findViewById(R.id.tvBrand);
            //DailyValue = itemView.findViewById(R.id.tvDailyValue);
            //State = itemView.findViewById(R.id.tvState);
        }

    }
}
