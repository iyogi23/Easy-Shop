package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyshop.model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.easyshop.R.id.orders_list;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter=
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model) {

                        holder.userName.setText("Name: "+model.getName());
                        holder.userPhoneNumber.setText("Phone: "+model.getPhone());
                        holder.userShippingAddress.setText("Shipping Address: "+model.getAddress()+", "+model.getCity()+ ", "+model.getPin());
                        holder.userDateTime.setText("Order at: "+model.getDate() +"  "+model.getTime());
                        holder.userTotalPrice.setText("Total Amount: "+model.getTotalAmount()+" Rupees");
                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uID= getRef(position).getKey();

                                Intent i= new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                                i.putExtra("uid",uID);
                                startActivity(i);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrderViewHolder(view);
                    }
                };
                ordersList.setAdapter(adapter);
                adapter.startListening();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrdersBtn;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
            showOrdersBtn=itemView.findViewById(R.id.show_all_products_btn);
        }
    }
}

