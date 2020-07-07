package com.example.easyshop.sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easyshop.MainActivity;
import com.example.easyshop.R;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);


        sellerLoginButton=(Button)findViewById(R.id.seller_already_have_account_btn);

        sellerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(i);
            }
        });

    }
}
