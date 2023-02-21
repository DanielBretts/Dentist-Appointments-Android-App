package com.example.patientqueuemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientqueuemanagement.Model.Appointment;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DoctorAppointmentsAdapter extends RecyclerView.Adapter<DoctorAppointmentsAdapter.AppointmentViewHolder> {
    private ArrayList<Appointment> allAppointments;
    public DoctorAppointmentsAdapter(ArrayList<Appointment> allAppointments) {
        this.allAppointments = allAppointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_doctor,parent,false);
        DoctorAppointmentsAdapter.AppointmentViewHolder appointmentViewHolder = new DoctorAppointmentsAdapter.AppointmentViewHolder(view);
        return appointmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = allAppointments.get(position);
        holder.DoctorAppointment_TXT_name.setText(appointment.getName());
        holder.DoctorAppointment_TXT_date.setText(appointment.getDay() +" "+appointment.prettyStringDate());
        holder.DoctorAppointment_TXT_time.setText(appointment.getTime());
        holder.DoctorAppointment_TXT_notes.setText(appointment.getNotes());
        holder.DoctorAppointment_TXT_phone.setText(appointment.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return allAppointments == null ? 0 : allAppointments.size();
    }
    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView DoctorAppointment_TXT_name;
        MaterialTextView DoctorAppointment_TXT_date;
        MaterialTextView DoctorAppointment_TXT_time;
        MaterialTextView DoctorAppointment_TXT_notes;
        MaterialTextView DoctorAppointment_TXT_phone;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            DoctorAppointment_TXT_name  = itemView.findViewById(R.id.DoctorAppointment_TXT_name);
            DoctorAppointment_TXT_date = itemView.findViewById(R.id.DoctorAppointment_TXT_date);
            DoctorAppointment_TXT_time = itemView.findViewById(R.id.DoctorAppointment_TXT_time);
            DoctorAppointment_TXT_notes = itemView.findViewById(R.id.DoctorAppointment_TXT_notes);
            DoctorAppointment_TXT_phone = itemView.findViewById(R.id.DoctorAppointment_TXT_phone);
        }
    }
}
