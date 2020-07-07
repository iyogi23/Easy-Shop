package com.example.easyshop;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.model.Cart;
import com.example.easyshop.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtTotalAmount;

    private int totalPrice=100;
    int price,quantity,singleTotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Button proceedToBuy = (Button) findViewById(R.id.proceed_to_buy_btn);
        txtTotalAmount=(TextView)findViewById(R.id.cart_totalPrice);


        proceedToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalPrice==0)
                {
                    Toast.makeText(CartActivity.this, "Please add product to cart", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent i= new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    System.out.println(String.valueOf(totalPrice));
                    i.putExtra("Total Price",String.valueOf(totalPrice));
                    startActivity(i);
                    finish();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlinetUser.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder( CartViewHolder holder, int position, @NonNull final Cart model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price " + model.getPrice()+ " Rupees");
                        holder.txtProductQuantity.setText("Quantity "+model.getQuantity());


                        // int singleTotalPrice=(((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity()));
                        //   totalPrice= totalPrice + singleTotalPrice;



                        // price =Integer.parseInt(model.getPrice());
                        //  quantity =Integer.parseInt(model.getQuantity());
                        ////  singleTotalPrice = (int) (price*quantity);

                        //   totalPrice = totalPrice + singleTotalPrice;

                      //totalPrice=totalPrice + ((Integer.parseInt(model.getPrice()))*(Integer.parseInt(model.getQuantity())));

                        txtTotalAmount.setText("Total Price = " +String.valueOf(totalPrice)+ " Rupees");

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]= new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"
                                        };

                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if(which==0)
                                        {
                                            Intent i= new Intent(CartActivity.this, ProductDetailsActivity.class);
                                            i.putExtra("pid",model.getPid());
                                            startActivity(i);
                                        }
                                        if(which==1)
                                        {
                                            cartListRef.child("User View").child(Prevalent.currentOnlinetUser.getPhone())
                                                    .child("Products").child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(CartActivity.this, "Product removed from Cart", Toast.LENGTH_SHORT).show();

                                                                Intent i= new Intent(CartActivity.this, HomeActivity.class);
                                                                startActivity(i);
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return holder;

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}

