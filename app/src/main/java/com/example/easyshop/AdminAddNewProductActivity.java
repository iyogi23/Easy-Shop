package com.example.easyshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String categoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    private Button AddNewProduct;
    private ImageView SelectProduct;
    private EditText ProductName,ProductDescription,ProductPrice;
    private static final int GalleryPic=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        SelectProduct= (ImageView) findViewById(R.id.selectproduct);
        AddNewProduct= (Button)findViewById(R.id.addNewProduct);
        ProductName= (EditText)findViewById(R.id.productname);
        ProductDescription= (EditText)findViewById(R.id.productdescription);
        ProductPrice= (EditText)findViewById(R.id.productprice);
        loadingbar= new ProgressDialog(this);

        categoryName= getIntent().getExtras().get("category").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        SelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });


    }

    private void ValidateProductData()
    {
        Description=ProductDescription.getText().toString();
        Pname=ProductName.getText().toString();
        Price=ProductPrice.getText().toString();

        if(ImageUri==null)
        {
            Toast.makeText(this, "Please Select a Product Picture", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please Enter Price", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation()
    {
        loadingbar.setTitle("Add New Product");
        loadingbar.setMessage("Please wait, While we are adding new Product...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar= Calendar.getInstance();
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd:MM:yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        productRandomKey= saveCurrentDate + " " +saveCurrentTime;

        final StorageReference filePath= ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask= filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener( new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl= task.getResult().toString();
                            //Toast.makeText(AdminAddNewProductActivity.this, "Image Url received", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String,Object> productMap= new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",Price);
        productMap.put("Pname",Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent i=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(i);

                            loadingbar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added Successfully...", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message= task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery()
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPic);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPic && resultCode==RESULT_OK && data!=null)
        {
            ImageUri= data.getData();
            SelectProduct.setImageURI(ImageUri);
        }
    }



}