/**
 * A base repository class. Provides generic CRUD on a firestore collection of a {@link Entity}
 */
package com.goblin.qrhunter.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Map;

/**
 * A base repository class for CRUD operations on a single collection.
 * <p>
 * This class provides basic implementations of common CRUD (Create, Read, Update, Delete)
 * operations on a single collection
 * <p>
 * To use this class, you need to extend it and specify a type of the data model that implements Entity.
 * stored in the collection. ie. to create a repository for a collection of
 * "Player" objects, you would create a subclass of this class as follows:
 * <pre>
 * public class PlayerRepository extends BaseRepository&lt;Player&gt; {
 *     // ...
 * }
 * </pre>
 * <p>
 * You can then use the methods provided by this class to interact with the collection
 * for the specified data model type.
 *
 * @param <T> the type of the data model (that extends Entity) stored in the collection
 */
public abstract class BaseRepository<T extends Entity> {


    private String TAG = "BaseRepository";
    private final FirebaseFirestore db;
    private final CollectionReference collectionRef;
    private final Class<T> type;

    protected BaseRepository(String collectionPath, Class<T> type) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionRef = db.collection(collectionPath);
        this.type = type;
    }

    /**
     * Adds the given item to the collection.
     * @param item The model object to add to the collection.
     * @return A task indicating whether the operation was successful.
     */
    public Task<Void> add(@NonNull T item) {
        getCollectionRef().document().getId();
        if(item.getId() == null && item.getId().isEmpty()) {
            String id = getCollectionRef().document().getId();
            item.setId(id);
        }
        Map<String, Object> map = item.toMap();
        return getCollectionRef().document(item.getId()).set(map);
    }

    /**
     * Updates the item with the given ID in the collection.
     * @param item The new model object to replace the old one with.
     * @return A task indicating whether the operation was successful.
     */
    public Task<Void> update(@NonNull T item) {
        String id = item.getId();
        Map<String, Object> map = item.toMap();
        return getCollectionRef().document(id).update(map);
    }

    /**
     * Deletes the item with the given ID from the collection.
     * @param id The ID of the model object to delete.
     * @return A task indicating whether the operation was successful.
     */
    public Task<Void> delete(@NonNull String id) {
        return collectionRef.document(id).delete();
    }


    /**
     * Gets the item with the given ID from the collection.
     * @param id The ID of the model object to get.
     * @return A task that returns the model object when it is complete.
     */
    public Task<T> get(@NonNull String id) {
        return getCollectionRef().document(id).get().continueWith(task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                T item = document.toObject(type);
                if(item != null) {
                    item.setId(document.getId());
                }
                return item;
            } else {
                return null;
            }
        });
    }

    /**
     * Gets an updating item with the given ID from the collection.
     * @param id The ID of the model object to get.
     * @return A  item as livedata.
     */
    public LiveData<T> getAsLiveData(@NonNull String id) {
        DocumentReference docRef = getCollectionRef().document(id);
        return new FirebaseLiveEntity<T>(docRef, type);
    }

    /**
     * Returns a LiveData object containing a list of all the items in the Firestore
     * collection that have a given field equal to a specific value.
     *
     * @param field The name of the field to filter by.
     * @param value The value that the field must be equal to.
     * @return A LiveData object containing a list of all the items in the Firestore
     * collection that have a given field equal to a specific value.
     */
    protected LiveData<List<T>> getWhereEqualTo(@NonNull String field, Object value) {
        Query q =  getCollectionRef().whereEqualTo(field, value);
        return new FirebaseLiveData<>(q, type);
    }

    /**
     * Checks if a document with the given ID exists in the collection.
     *
     * @param id the ID of the document to check.
     * @return a {@link Task} that on completes indicates if the item exists or not.
     */
    public Task<Boolean> exists(@NonNull String id) {
        return getCollectionRef().document(id).get().continueWith(task -> task.getResult().exists());
    }

    /**
     * Gets all the items in the collection.
     * @return A task that returns a list of model objects when it is complete.
     */
    public LiveData<List<T>> getAll() {
        Query query = getCollectionRef().orderBy("timestamp", Query.Direction.DESCENDING);
        return new FirebaseLiveData<>(query, type);
    }

    /**
     * Returns the Firestore collection reference for the repository.
     *
     * @return The Firestore collection reference for the repository.
     */
    public CollectionReference getCollectionRef() {
        return collectionRef;
    }
}
