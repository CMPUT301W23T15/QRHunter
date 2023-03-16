/**
 * The {@link com.goblin.qrhunter.data.FirebaseLiveData} class is a LiveData implementation that
 * safely listens to changes in a Firestore query and returns a list of entities of a specific type.
 */
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

/**
 * A LiveData implementation that safely listens to changes in a Firestore query and returns a list of
 * entities of a specific type.
 *
 * @param <T> The type of the entity.
 */
public class FirebaseLiveData<T extends Entity> extends LiveData<List<T>> {

    private final Class<T> type;
    final private Query query;
    private ListenerRegistration listenerReg;
    private final String TAG = "FirebaseLiveData";

    /**
     * Constructs a new FirebaseLiveData object with the given Firestore query and entity type.
     *
     * @param query The Firestore query to listen to.
     * @param type  The class of the entity.
     */
    public FirebaseLiveData(Query query, Class<T> type) {
        this.type = type;
        this.query = query;
        startListening();
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(listenerReg == null) {
            startListening();
        }
    }

    private void startListening() {
        listenerReg = query.addSnapshotListener((QuerySnapshot snapshot, FirebaseFirestoreException e)-> {
            if (e != null) {
                Log.e(TAG, "fail.", e);
                return;
            }
            List<T> items = new ArrayList<>();
            if(snapshot != null) {
                for (DocumentSnapshot document : snapshot.getDocuments()) {
                    T item = document.toObject(type);
                    if (item != null) {
                        item.setId(document.getId());
                        items.add(item);
                    }
                }
            }
            setValue(items);
        });
    }


    /*
        Called when the LiveData becomes inactive.
        Removes the snapshot listener to prevent further updates to the LiveData.
         */
    @Override
    protected void onInactive() {
        super.onInactive();
        listenerReg.remove();
        listenerReg = null;
    }
}
