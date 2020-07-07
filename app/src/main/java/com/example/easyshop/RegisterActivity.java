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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private  Button CreateAccountButton;
    private EditText InputName, InputPhone,InputPassword;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton= (Button)findViewById(R.id.register);
        InputName= (EditText) findViewById(R.id.name);
        InputPhone= (EditText)findViewById(R.id.register_phoneno);
        InputPassword= (EditText)findViewById(R.id.register_password);
        loadingbar= new ProgressDialog(this);

         CreateAccountButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 CreateAccount();
             }
         });
    }

    private void CreateAccount() {
        String name= InputName.getText().toString();
        String phone= InputPhone.getText().toString();
        String password= InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter Your Phone no", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Set Your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait, While we are checking the credential...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            validatePhoneNumber(name,phone,password);
        }

    }

    private void validatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap= new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations! Your Account has been Created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                        Intent i=new Intent(RegisterActivity.this, loginActivity.class);
                                        startActivity(i);
                                    }
                                    else
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Something went wrong. Try Again...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}