package com.example.patientqueuemanagement.Interfaces;

import com.google.firebase.database.DataSnapshot;
/**
 * This interface is used as a callback for waiting that the data will retrieve from the firebase database
 */
public interface DataListener {
    void getStringVal(String s);
    void getDataSnapshot(DataSnapshot dataSnapshot);
    void fail();
}
