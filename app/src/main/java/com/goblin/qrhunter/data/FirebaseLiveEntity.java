/**
 * The {@link com.goblin.qrhunter.data.FirebaseLiveData} class is a LiveData implementation that
 * safely listens to changes in a Firestore query and returns a list of entities of a specific type.
 */
package com.goblin.qrhunter.data;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A LiveData implementation that safely listens to changes in a Firestore query and returns a list of
 * entities of a specific type.
 *
 * @param <T> The type of the entity.
 */
public class FirebaseLiveEntity<T extends Entity> extends LiveData<T> {

    private final Class<T> type;

    final private DocumentReference docRef;
    private ListenerRegistration listenerReg;
    private final String TAG = "FirebaseLiveEntity";

    /**
     * Constructs a new FirebaseLiveData object with the given Firestore query and entity type.
     *
     * @param docRef The Firestore query to listen to.
     * @param type  The class of the entity.
     */
    public FirebaseLiveEntity(DocumentReference docRef, Class<T> type) {
        this.type = type;
        this.docRef = docRef;
        startListening();
    }

    /**
     * Called when the LiveData becomes active.
     * add the snapshot listener to listen for further updates to the LiveData.
     */
    @Override
    protected void onActive() {
        super.onActive();
        if (listenerReg == null) {
            startListening();
        }
    }

    private void startListening() {
        listenerReg = docRef.addSnapshotListener((doc, error) -> {
            if (error != null) {
                Log.e(TAG, "listening fail.", error);
                return;
            }
            if(doc != null && doc.exists()) {
                T item = doc.toObject(type);
                item.setId(doc.getId());
                setValue(item);
            }

        });
    }


    /**
     * Called when the LiveData becomes inactive.
     * Removes the snapshot listener to prevent further updates to the LiveData.
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        listenerReg.remove();
        listenerReg = null;
    }
}
