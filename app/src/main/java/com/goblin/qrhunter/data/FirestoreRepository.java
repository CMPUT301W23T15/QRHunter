package com.goblin.qrhunter.data;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FirestoreRepository<T extends DAO> {

    private final FirebaseFirestore db;
    private final CollectionReference collectionRef;
    private final Class<T> type;

    protected FirestoreRepository(String collectionPath, Class<T> type) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionRef = db.collection(collectionPath);
        this.type = type;
    }

    public Task<DocumentReference> add(T item) {
        return collectionRef.add(item);
    }

    public Task<Void> update(String id, Map<String, Object> updates) {
        return collectionRef.document(id).update(updates);
    }

    public Task<Void> delete(String id) {
        return collectionRef.document(id).delete();
    }

    public Task<T> get(String id) {
        return collectionRef.document(id).get().continueWith(task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                return document.toObject(type);
            } else {
                return null;
            }
        });
    }

    public Task<List<T>> getAll() {
        return collectionRef.get().continueWith(task -> {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            List<T> items = new ArrayList<>(documents.size());
            for (DocumentSnapshot document : documents) {
                items.add(document.toObject(type));
            }
            return items;
        });
    }
}
