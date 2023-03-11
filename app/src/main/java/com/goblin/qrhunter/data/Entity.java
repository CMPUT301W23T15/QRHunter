/**
 * The Entity interface represents an object that can be stored in a Firestore database.
 * Implementing classes should provide a unique identifier and a method to convert the object to a Map
 * for storage and retrieval.
 */
package com.goblin.qrhunter.data;

import java.util.Map;

/**
 * The Entity interface represents a data model that can be stored in Firestore.
 * <p>
 * Classes that implement this interface should provide methods to get and set the ID
 * of the document, as well as a method to convert the data model to a map of field
 * names and values that can be stored in Firestore.
 */
public interface Entity {

    /**
     * Gets the ID of the document.
     * @return the ID of the document.
     */
    public String getId();

    /**
     * Sets the ID of the document.
     * @param id the new ID of the document.
     */
    public void setId(String id);

    /**
     * Converts the data model to a map of field names and values that can be stored in Firestore.
     * @return a map of field names and values that can be stored in Firestore.
     */
    public Map<String, Object> toMap();

}
