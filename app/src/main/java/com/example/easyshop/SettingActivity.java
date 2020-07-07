package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextBtn, closeTextBtn,saveTextBtn;

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("profile picture");

        profileImageView=(CircleImageView)findViewById(R.id.settings_profileImage);
        fullNameEditText=(EditText) findViewById(R.id.settings_fullName);
        userPhoneEditText=(EditText)findViewById(R.id.settings_phoneNumber);
        addressEditText=(EditText)findViewById(R.id.settings_address);
        profileChangeTextBtn=(TextView)findViewById(R.id.Change_profile_image_btn);
        closeTextBtn=(TextView)findViewById(R.id.close_settings_btn);
        saveTextBtn=(TextView)findViewById(R.id.update_account_settings_btn);


        userInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }

            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });
    }

    private void updateOnlyUserInfo()
    {
        if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Full Address", Toast.LENGTH_SHORT).show();
        }
        else if(!checker.equals("clicked"))
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users");

            HashMap<String,Object> userMap= new HashMap<>();
            userMap.put("name",fullNameEditText.getText().toString());
            userMap.put("phone",userPhoneEditText.getText().toString());
            userMap.put("address",addressEditText.getText().toString());
            ref.child(Prevalent.currentOnlinetUser.getPhone()).updateChildren(userMap);


            startActivity(new Intent(SettingActivity.this, HomeActivity.class));
            Toast.makeText(SettingActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    private void userInfoSaved()
    {

            uploadImage();

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait,Your is Profile information is updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef= storageProfilePictureRef
                    .child(Prevalent.currentOnlinetUser.getPhone()+ ".jpg");

            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                                .child("Users");

                        HashMap<String,Object> userMap= new HashMap<>();
                       // userMap.put("name",fullNameEditText.getText().toString());
                       // userMap.put("phone",userPhoneEditText.getText().toString());
                      //  userMap.put("address",addressEditText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlinetUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                        Toast.makeText(SettingActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode  ==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Something went wrong,Try aAgain..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this,SettingActivity.class));
            finish();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference()
                .child("User").child(Prevalent.currentOnlinetUser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image= dataSnapshot.child("image").getValue().toString();
                        String name= dataSnapshot.child("name").getValue().toString();
                        String phone= dataSnapshot.child("phone").getValue().toString();
                        String address= dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
