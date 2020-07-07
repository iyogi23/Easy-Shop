package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easyshop.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText,addressEditText,cityEditText,pinEditText;
    private Button confirmOrderBtn;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");

        nameEditText=(EditText)findViewById(R.id.shipment_name);
        phoneEditText=(EditText)findViewById(R.id.shipment_phone);
        addressEditText=(EditText)findViewById(R.id.shipment_address);
        cityEditText=(EditText)findViewById(R.id.shipment_city);
        pinEditText=(EditText)findViewById(R.id.shipment_pin);
        confirmOrderBtn=(Button) findViewById(R.id.confirm_final_order_btn);


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your City", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pinEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Your Pincode", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate,saveCurrentTime;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd:MM:yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlinetUser.getPhone());

        HashMap<String,Object> ordersMap= new HashMap<>();

        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("pin",pinEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not shipped");


        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(Prevalent.currentOnlinetUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully..", Toast.LENGTH_SHORT).show();
                                        Intent i= new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);

                                    }

                                }
                            });
                }

            }
        });
    }
}
