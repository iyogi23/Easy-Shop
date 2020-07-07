package com.example.easyshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirt,sportwear,ladies,winterwear;
    private ImageView glasses,purse,hats,shoes;
    private ImageView headphone,laptop,watch,mobile;

    private Button logoutBtn,checkOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutBtn=(Button)findViewById(R.id.admin_logout_btn);
        checkOrderBtn=(Button)findViewById(R.id.check_order_btn);

        tshirt=(ImageView)findViewById(R.id.tshirt);
        sportwear=(ImageView)findViewById(R.id.sportswear);
        ladies=(ImageView)findViewById(R.id.ladies);
        winterwear=(ImageView)findViewById(R.id.winterwear);

        glasses=(ImageView)findViewById(R.id.glasses);
        purse=(ImageView)findViewById(R.id.purses);
        hats=(ImageView)findViewById(R.id.hats);
        shoes=(ImageView)findViewById(R.id.shoes);

        headphone=(ImageView)findViewById(R.id.headphones);
        laptop=(ImageView)findViewById(R.id.laptop);
        watch=(ImageView)findViewById(R.id.watch);
        mobile=(ImageView)findViewById(R.id.mobile);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(i);
            }
        });



        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                i.putExtra("category","tshirt");
                startActivity(i);
            }
        });

        sportwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Sports Wear");
                startActivity(i);
            }
        });

        ladies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Ladies Wear");
                startActivity(i);
            }
        });

        winterwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Winter Wear");
                startActivity(i);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Glasses");
                startActivity(i);
            }
        });

        purse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Purses");
                startActivity(i);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Hats");
                startActivity(i);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Shoes");
                startActivity(i);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Headphones");
                startActivity(i);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Laptops");
                startActivity(i);
            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Watches");
                startActivity(i);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("category","Mobiles");
                startActivity(i);
            }
        });
    }
}