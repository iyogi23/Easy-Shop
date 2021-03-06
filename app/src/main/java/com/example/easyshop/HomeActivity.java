package com.example.easyshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easyshop.model.Products;
import com.example.easyshop.prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ViewHolder.ProductViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

/*
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;



import com.example.easyshop.Model.Products;
import com.example.easyshop.ecommerce.Model.Users;
import com.example.easyshop.ecommerce.Prevalent.Prevalent;
import com.example.easyshop.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


*/






 public class HomeActivity extends AppCompatActivity {

     private AppBarConfiguration mAppBarConfiguration;

     private DatabaseReference ProductRef;
     private RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home);

         ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");

         Toolbar toolbar = findViewById(R.id.toolbar);
         toolbar.setTitle("Home");
         setSupportActionBar(toolbar);


        final FloatingActionButton fab = findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i= new Intent(HomeActivity.this, CartActivity.class);
                 startActivity(i);
             }
         });


         DrawerLayout drawer = findViewById(R.id.drawer_layout);
         NavigationView navigationView = findViewById(R.id.nav_view);

         View headerView= navigationView.getHeaderView(0);
         TextView userNameTextView= headerView.findViewById(R.id.userProfileName);
         TextView userPhoneTextView= headerView.findViewById(R.id.email);
         CircleImageView profileImageView= headerView.findViewById(R.id.userProfileImage);

         userNameTextView.setText(Prevalent.currentOnlinetUser.getName());
         userPhoneTextView.setText(Prevalent.currentOnlinetUser.getPhone());
         Picasso.get().load(Prevalent.currentOnlinetUser.getImage()).placeholder(R.drawable.profile)
                 .into(profileImageView);

         recyclerView=findViewById(R.id.recycler_menu);
         recyclerView.setHasFixedSize(true);
         layoutManager=new LinearLayoutManager(this);
         recyclerView.setLayoutManager(layoutManager);





         mAppBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.profile,R.id.categories,R.id.cart,R.id.setting,R.id.search,R.id.developer)
                 .setDrawerLayout(drawer)
                 .build();
         NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
         NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
         NavigationUI.setupWithNavController(navigationView, navController);



         navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
             @Override
             public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                 int menuId= destination.getId();
                 switch(menuId)
                 {
                     case R.id.setting:{
                         startActivity(new Intent(getBaseContext(), SettingActivity.class));
                         fab.hide();
                         break;
                     }
                     case R.id.logout:{
                         Paper.book().destroy();
                         Intent i= new Intent(HomeActivity.this, loginActivity.class);
                         i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(i);
                         finish();
                         break;
                     }
                     case R.id.cart:{
                         //Intent i= new Intent(HomeActivity.this,CartActivity.class);
                         //startActivity(i);
                         Intent i = new Intent(getApplicationContext(), CartActivity.class);
                         startActivity(i);
                         break;
                     }
                     case R.id.search:{
                         Intent i= new Intent(HomeActivity.this, SearchProductsActivity.class);
                         startActivity(i);
                         break;
                     }

                     case R.id.developer:{
                         Intent i= new Intent(HomeActivity.this, DeveloperActivity.class);
                         startActivity(i);
                         break;
                     }
                     default:
                         fab.show();
                         break;
                 }
             }
         });
     }

     @Override
     protected void onStart()
     {
         super.onStart();
         FirebaseRecyclerOptions<Products> options=
                 new FirebaseRecyclerOptions.Builder<Products>()
                 .setQuery(ProductRef,Products.class)
                 .build();

         FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                 new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                     @Override
                     protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products)
                     {
                         productViewHolder.txtProductName.setText(products.getPname());
                         productViewHolder.txtProductDescription.setText(products.getDescription());
                         productViewHolder.txtProductPrice.setText("Price = "+products.getPrice()+" Rupees");
                         Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                         productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v)
                             {
                                 Intent i= new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                 i.putExtra("pid",products.getPid());
                                 startActivity(i);

                             }
                         });


                     }

                     @NonNull
                     @Override
                     public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                         ProductViewHolder holder= new ProductViewHolder(view);
                         return holder;


                     }
                 };

         recyclerView.setAdapter(adapter);
         adapter.startListening();
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.home, menu);
         return true;
     }

     @Override
     public boolean onSupportNavigateUp() {
         NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
         return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                 || super.onSupportNavigateUp();
     }




 }
//SuppressWarnings("StatementWithEmptyBody")
    // @Override
   /* public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_cart) {

    } else if (id == R.id.nav_orders) {

    } else if (id == R.id.nav_category) {

    } else if (id == R.id.nav_setting) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    } else if (id == R.id.nav_logout) {
        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
}
}


    */


 //New code




/*
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userProfileName);
        CircleImageView profileImageView = headerView.findViewById(R.id.userProfileImage);

        userNameTextView.setText(Prevalent.currentOnlinetUser.getName());
        Picasso.get().load(Prevalent.currentOnlinetUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {

        }
        else if (id == R.id.nav_orders)
        {

        }
        else if (id == R.id.nav_categories)
        {

        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
*/
