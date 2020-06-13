package com.example.ecommerceapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ecommerceapplication.Model.Products;
import com.example.ecommerceapplication.Model.Users;
import com.google.android.gms.tasks.*;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.util.Log.d;

public class EditActivity extends AppCompatActivity {

    private DatabaseReference ProductsRef;
    private EditText txtProductName, txtProductDescription, txtProductPrice;
    private ImageView imageView;
    private Button saveButton, deleteButton;
    private ImageButton editName, editPrice, editDescription;
    private Uri ImageUri = Uri.EMPTY;
    private String Description, Price, Pname, saveCurrentDate, saveCurrentTime, productRandomKey;
    private String downloadImageUrl = "";
    private StorageReference ProductImagesRef;
    private String pId;

    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Paper.init(this);
        Users user = Paper.book().read("userDetail");

        pId = getIntent().getStringExtra("pId");

        imageView = findViewById(R.id.product_image);
        txtProductName = findViewById(R.id.product_name);
        txtProductDescription = findViewById(R.id.product_description);
        txtProductPrice = findViewById(R.id.product_price);

        editName = findViewById(R.id.edit_name_btn);
        editDescription = findViewById(R.id.edit_description_btn);
        editPrice = findViewById(R.id.edit_price_btn);

        saveButton = findViewById(R.id.save_btn);
        deleteButton = findViewById(R.id.delete_btn);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(pId);

        loadProductData();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductData();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }

    private void loadProductData() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        txtProductName.setFocusable(false); //to disable it

        editName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtProductName.setFocusableInTouchMode(true); //to enable it
                txtProductName.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        txtProductDescription.setFocusable(false); //to disable it

        editDescription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtProductDescription.setFocusableInTouchMode(true); //to enable it
                txtProductDescription.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        txtProductPrice.setFocusable(false); //to disable it

        editPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtProductPrice.setFocusableInTouchMode(true); //to enable it
                txtProductPrice.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products productsData = dataSnapshot.getValue(Products.class);
                txtProductName.setText(productsData.getPname());
                txtProductDescription.setText(productsData.getDescription());
                txtProductPrice.setText(productsData.getPrice());
                Picasso.get().load(productsData.getImage()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProductData() {
        ProductsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditActivity.this, "Product Deleted ", Toast.LENGTH_SHORT).show();
                            delProductData();
                            Intent intent = new Intent(EditActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditActivity.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ValidateProductData() {
        Description = txtProductDescription.getText().toString();
        Price = txtProductPrice.getText().toString();
        Pname = txtProductName.getText().toString();

        if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }


    private void StoreProductInformation() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());



        productRandomKey = saveCurrentDate + saveCurrentTime;

        if (!Uri.EMPTY.equals(ImageUri)) {
            doUploadTask();
        } else {
            SaveProductInfoToDatabase();
        }


    }

    private void doUploadTask() {
        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(EditActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            //Toast.makeText(EditActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }


    private void SaveProductInfoToDatabase() {
        final HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("description", Description);
        if (!downloadImageUrl.isEmpty()) {
            productMap.put("image", downloadImageUrl);
        }
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        ProductsRef.updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(EditActivity.this, HomeActivity.class);
                            startActivity(intent);

                            updateProductData(productMap);
                            Toast.makeText(EditActivity.this, "Product Updated...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(EditActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateProductData(final HashMap<String, Object> productMap) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsnapshot: snapshot.getChildren()){
                        if (childsnapshot.getKey().equals(pId)){
                        d("order", childsnapshot.getKey()+" "+pId);
                            childsnapshot.getRef().updateChildren(productMap);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void delProductData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsnapshot: snapshot.getChildren()){
                        if (childsnapshot.getKey().equals(pId)){
                            childsnapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
