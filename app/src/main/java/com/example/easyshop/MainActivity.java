package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.model.Users;
import com.example.easyshop.prevalent.Prevalent;
import com.example.easyshop.sellers.SellerRegistrationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button loginButton, joinNowButton;
    ProgressDialog loadingbar;
    TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=(Button) findViewById(R.id.login);
        joinNowButton=(Button) findViewById(R.id.joinnow);
        sellerBegin=(TextView) findViewById(R.id.seller_begin);

        loadingbar=new ProgressDialog(this);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, loginActivity.class);
                startActivity(i);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(i);
            }
        });



        String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey= Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPhoneKey!="" && UserPasswordKey!="")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingbar.setTitle("Old User, Nice!");
                loadingbar.setMessage("Please wait, While we are checking the credential...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }
        }
    }





    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users userdata= dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent i=new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlinetUser=userdata;
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                }
                else
                {

                    Toast.makeText(MainActivity.this, "Account doesnot exists! ", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}