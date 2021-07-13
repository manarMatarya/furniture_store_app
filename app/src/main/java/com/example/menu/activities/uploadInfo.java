package com.example.menu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class uploadInfo extends AppCompatActivity {

    EditText name,desc,price,type;
    ImageView image;
    RatingBar rating;
    Button upload;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    String Database_Path = "";

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 8;

    ProgressDialog progressDialog ;

    Boolean CheckImageViewEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_info);

        name = findViewById(R.id.upname);
        desc= findViewById(R.id.updesc);
        price= findViewById(R.id.upprice);
        type=findViewById(R.id.uptype);
        image= findViewById(R.id.upimage);
        rating= findViewById(R.id.uprating);
        upload= findViewById(R.id.upbtn);


        progressDialog = new ProgressDialog(uploadInfo.this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Image_Request_Code);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database_Path = type.getText().toString();
                // Assign FirebaseStorage instance to storageReference.
                storageReference = com.google.firebase.storage.FirebaseStorage.getInstance().getReference();

                databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
                UploadImageFileToFirebaseStorage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            Picasso.get().load(FilePathUri).into(image);
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("adding furniture...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
             final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            final String TempName = name.getText().toString().trim();
                            final String TempDesc = desc.getText().toString().trim();
                            final float TempPrice = Float.valueOf(price.getText().toString().trim());
                            final float TempRating = rating.getRating();
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            final String ImageUploadId = databaseReference.push().getKey();
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    @SuppressWarnings("VisibleForTests")
                                    Dish dish = new Dish(TempName,TempDesc, uri.toString(),TempPrice ,TempRating);
                                    databaseReference.child(ImageUploadId).setValue(dish);

                                    // Showing toast message after done uploading.
                                    Toast.makeText(getApplicationContext(), "added Successfully ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(uploadInfo.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("adding furniture...");

                        }
                    });
        }
        else {

            Toast.makeText(uploadInfo.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
}