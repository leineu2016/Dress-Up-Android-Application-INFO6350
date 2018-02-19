package edu.northeastern.wardrobeapp.android_wardrobeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class ClothingDetailedViewActivity extends BaseActivity {

    private ImageView imageView;
    private Button btnSave;
    private Bitmap imageBitmap;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_detailed_view);

        // Set Views
        imageView = (ImageView) findViewById(R.id.imageViewSmall);
        btnSave = (Button) findViewById(R.id.saveDetails);
        btnSave.setEnabled(true);

        Intent sourceIntent = getIntent();
        Bundle bundle = sourceIntent.getExtras();
        // Set the image preview from intent bundle
        String filename = bundle.getString("filename");
        try {
            FileInputStream is = this.openFileInput(filename);
            imageBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(imageBitmap);
        // Read from saved file
        String path = getFilesDir() + "/" + filename;
        imageFile = new File(path);
    }

    /**
     * Save and pass in onSuccess + onFailure listener
     * TODO: for ken. Save detail data before upload (in DataAccess class)
     * @param view
     */
    public void SaveClothing(View view) {
        final Context context = this;
        final DataAccess DA = new DataAccess();
        // TODO: Progressbar?
        btnSave.setEnabled(false);
        OnSuccessListener<UploadTask.TaskSnapshot> onSuccess = new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                // Save clothing data
                HashMap<String, String> clothingData = new HashMap<>();
                // TODO: Add all field values here
                clothingData.put("url", downloadUrl.toString());
                DA.saveClothingData(getCurrentUserId(), clothingData);
                // Go back to main with a toast request
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("toast", "Upload success!");
                startActivity(i);
            }
        };
        OnFailureListener onFailure = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Upload failed...", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true);
            }
        };
        DA.saveToStorage("clothing_test", getCurrentUserId() , imageFile, onSuccess, onFailure);
    }
}
