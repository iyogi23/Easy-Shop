package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.model.Users;
import com.example.easyshop.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
   private EditText InputNumber,InputPassword;
   private Button LoginButton,AdminLink,NotAdminLink;
   private ProgressDialog loadingbar;
   private String ParentDbName= "Users";
   private CheckBox ChkBoxRememberMe;
   private TextView forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton= (Button)findViewById(R.id.login);
        InputNumber= (EditText) findViewById(R.id.login_phoneno);
        InputPassword= (EditText)findViewById(R.id.login_password);
        loadingbar= new ProgressDialog(this);
        ChkBoxRememberMe=(CheckBox)findViewById(R.id.rememberme);
        AdminLink=(Button) findViewById(R.id.admin);
        NotAdminLink=(Button) findViewById(R.id.notadmin);
        forgetPassword=(TextView)findViewById(R.id.forgetPassword);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                ParentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                ParentDbName="Users";
            }
        });
    }

    private void LoginUser()
    {
        String phone= InputNumber.getText().toString();
        //System.out.println("We are at line number 85....");
        String password= InputPassword.getText().toString();
        if(TextUtils.isEmpty(phone))
         {
            Toast.makeText(this, "Please Enter Your Phone no", Toast.LENGTH_SHORT).show();
         }
        else if(TextUtils.isEmpty(password))
         {
                Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
         }
        else
        {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait, While we are checking the credential...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccessToAccount(phone,password);

        }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(ChkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(ParentDbName).child(phone).exists())
                {
                    Users userdata= dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            if(ParentDbName.equals("Admins"))
                            {
                                Toast.makeText(loginActivity.this, "Welcome Admin, You'r logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent i=new Intent(loginActivity.this, AdminCategoryActivity.class);
                                Prevalent.currentOnlinetUser=userdata;
                                startActivity(i);
                            }
                            else if(ParentDbName.equals("Users"))
                            {
                                Toast.makeText(loginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent i=new Intent(loginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlinetUser=userdata;
                                startActivity(i);
                            }
                        }
                        else
                        {
                            Toast.makeText(loginActivity.this, "Password is incorrect ", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                }
                else
                {

                    Toast.makeText(loginActivity.this, "Account does not exists! ", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}