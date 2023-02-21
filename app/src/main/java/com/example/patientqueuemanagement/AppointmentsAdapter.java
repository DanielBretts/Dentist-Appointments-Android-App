package com.example.patientqueuemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientqueuemanagement.Model.Appointment;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ScoreViewHolder> {
    private ArrayList<Appointment> appointments;

    public AppointmentsAdapter(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment,parent,false);
        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(view);
        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.appointment_TXT_date.setText(appointment.prettyStringDate());
        holder.appointment_TXT_time.setText(appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments == null ? 0 : appointments.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView appointment_TXT_date;
        MaterialTextView appointment_TXT_time;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            appointment_TXT_date = itemView.findViewById(R.id.appointment_TXT_date);
            appointment_TXT_time = itemView.findViewById(R.id.appointment_TXT_time);
        }
    }
}
