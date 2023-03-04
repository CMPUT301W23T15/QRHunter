package com.goblin.qrhunter.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T extends Entity> {

    private final FirebaseFirestore db;
    private final CollectionReference collectionRef;
    private final Class<T> type;

    protected BaseRepository(String collectionPath, Class<T> type) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionRef = db.collection(collectionPath);
        this.type = type;
    }

    public Task<Void> add(@NonNull T item) {
        Map<String, Object> map = item.toMap();
        if(item.getId() != null && item.getId().length() > 0) {
            return this.update(item);
        }
        return collectionRef.add(map)
                .continueWith(task -> {
                    String documentId = task.getResult().getId();
                    item.setId(documentId);
                    return null;
                });
    }

    public Task<Void> update(@NonNull T item) {
        String id = item.getId();
        Map<String, Object> map = item.toMap();
        return collectionRef.document(id).set(map);
    }

    public Task<Void> delete(String id) {
        return collectionRef.document(id).delete();
    }

    public Task<T> get(String id) {
        return collectionRef.document(id).get().continueWith(task -> {
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

    public Task<Boolean> exists(String id) {
        return collectionRef.document(id).get().continueWith(task -> task.getResult().exists());
    }

    public LiveData<List<T>> getAll() {
        Query query = collectionRef.orderBy("timestamp", Query.Direction.DESCENDING);
        return new FirebaseLiveData<>(query, type);
    }

    public CollectionReference getCollectionRef() {
        return collectionRef;
    }
}
