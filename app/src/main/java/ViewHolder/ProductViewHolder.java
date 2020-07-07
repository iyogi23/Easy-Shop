package ViewHolder;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshop.R;

import Interface.ItemClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView= (ImageView) itemView.findViewById(R.id.product_Image);
        txtProductName= (TextView) itemView.findViewById(R.id.product_Name);
        txtProductDescription= (TextView) itemView.findViewById(R.id.product_Description);
        txtProductPrice= (TextView) itemView.findViewById(R.id.product_price);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;
    }

    @Override
    public void onClick(View view)
    {

        listner.onClick(view,getAdapterPosition(),false);
    }
}
