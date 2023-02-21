package com.example.patientqueuemanagement.Utils;

import androidx.annotation.NonNull;

import com.example.patientqueuemanagement.Interfaces.DataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DataManager {

    /**
     * This function reads the data from the firebase and returns a datasnapshot through the listener
     * @param databaseReference the reference you want to retrieve the data from
     * @param dataListener the listener you implement in your function
     */
    public static void readDataFromFirebase(DatabaseReference databaseReference, DataListener dataListener){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListener.getDataSnapshot(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
