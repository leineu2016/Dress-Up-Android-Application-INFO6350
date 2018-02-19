package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import edu.northeastern.wardrobeapp.android_wardrobeapp.utils.Clothing;

public class DataAccess {
    // Firebase variables
    private StorageReference storageRef;
    private DatabaseReference dbRef;

    public DataAccess() {
        storageRef = FirebaseStorage.getInstance().getReference();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void getWardrobe(String userId, ValueEventListener listener) {
        dbRef.child("userdata").child(userId).child("wardrobe")
                .addListenerForSingleValueEvent(listener);
    }

    // tODO: function to store detail data then upload file
    public void saveClothingData(String userId, Map<String, String> clothingData) {
        // Set ID if new
        if (!clothingData.containsKey("id")) {
            clothingData.put("id", "clothing_" + getTimeStamp());
        }
        // Store data in Firebase
        DatabaseReference clothingRef = dbRef.child("userdata").child(userId).child("wardrobe").child(clothingData.get("id"));
        for (Map.Entry<String, String> item : clothingData.entrySet()) {
            String key = item.getKey();
            String value = item.getValue();
            if (key.equals("id")) {
                continue;
            }
            clothingRef.child(key).setValue(value);
        }
    }

    /**
     * Saves off an image bitmap to firebase. Needs an onSuccess and onFailure handler.
     *
     * @param fileName - Name of file without unique identified
     * @param userId - The firebase uuid
     * @param fileObj - Image file
     * @param onSuccess - Override onSuccess(UploadTask.TaskSnapshot taskSnapshot). Do a taskSnapshot.getDownloadUrl() to get the uploaded image URL
     * @param onFailure - Override onFailure(@NonNull Exception exception)
     */
    public void saveToStorage(String fileName, String userId, File fileObj, OnSuccessListener<UploadTask.TaskSnapshot> onSuccess, OnFailureListener onFailure) {
        // Create storage reference
        String fullName = fileName.concat(getFileNameSuffix());
        StorageReference filePath = storageRef.child(userId).child(WardrobeApp.STORAGE_DIR_CLOTHING).child(fullName);

        // Use path
        try {
            InputStream stream = new FileInputStream(fileObj);
            UploadTask uploadTask = filePath.putStream(stream);
            uploadTask.addOnSuccessListener(onSuccess).addOnFailureListener(onFailure);
        } catch(FileNotFoundException err) {
            err.printStackTrace();
        }
    }

    /**
     * Generates a unique file name prefix for captured photos
     * @return String
     */
    public String getFileNameSuffix() {
        return "_" + getTimeStamp() + ".png";
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}
