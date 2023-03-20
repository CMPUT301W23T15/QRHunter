/**

 This package contains classes related to data management in the QRHunter app.
 It provides base classes and utilities for implementing CRUD (Create, Read, Update, Delete) operations
 on Firestore database collections for different data models.
 <p>
 The classes included in this package are:
 <ul>

 less

 <li>{@link BaseRepository}: A base repository class for CRUD operations on a single collection</li>

 less

 <li>{@link FirebaseLiveData}: A LiveData implementation for getting real-time updates from a Firestore query</li>

 </ul>
 <p>
 The {@link CommentRepository} is a subclass of {@link BaseRepository} that provides methods for managing
 {@link Comment} objects in the "comments" collection of Firestore database.

 */
package com.goblin.qrhunter.data;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.goblin.qrhunter.Comment;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * A repository class for managing {@link Comment} objects in a Firestore database.
 * This class provides basic implementations of common CRUD (Create, Read, Update, Delete)
 * operations on a single collection of comments.
 */
public class CommentRepository extends BaseRepository<Comment> {

    /**
     * default constructor for CommentRepository.
     */
    public CommentRepository() {
        super("comments", Comment.class);
    }

    public LiveData<List<Comment>> getByPost(String postId) {
        return new FirebaseLiveData<>(getCollectionRef().whereEqualTo("postId", postId), Comment.class);
    }


}
