package com.goblin.qrhunter.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseLiveData<T extends Entity> extends LiveData<List<T>> {

    private final Class<T> type;
    private final ListenerRegistration listenerReg;
    private final String TAG = "FirebaseLiveData";

    public FirebaseLiveData(Query query, Class<T> type) {
        this.type = type;
        listenerReg = query.addSnapshotListener(this::onEvent);
    }

    private void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.e(TAG, "fail.", e);
            return;
        }

        List<T> items = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            T item = document.toObject(type);
            if (item != null) {
                item.setId(document.getId());
                items.add(item);
            }
        }
        setValue(items);
    }


    @Override
    protected void onInactive() {
        super.onInactive();
        listenerReg.remove();
    }
}
