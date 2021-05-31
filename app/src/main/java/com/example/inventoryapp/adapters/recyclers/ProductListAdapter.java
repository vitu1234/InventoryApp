package com.example.inventoryapp.adapters.recyclers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;


public class ProductListAdapter extends AbstractFlexibleItem<ProductListAdapter.MyViewHolder> {

    private String id, product_name;
    private int product_id, countAvailable;
    double product_price;

    public ProductListAdapter(String id, String product_name, int product_id, int countAvailable, double product_price) {
        this.id = id;
        this.product_name = product_name;
        this.product_id = product_id;
        this.countAvailable = countAvailable;
        this.product_price = product_price;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof ProductListAdapter) {
            ProductListAdapter inItem = (ProductListAdapter) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recycler_products_line;
    }

    @Override
    public MyViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new MyViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, MyViewHolder holder,
                               int position,
                               List<Object> payloads) {
        holder.textViewProdname.setText(product_name);
        holder.textViewAvailable.setText(countAvailable + " in stock");
        holder.textViewProdPrice.setText("MWK " + product_price + "");


        // Title appears disabled if item is disabled
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context1 = v.getContext();
//                context1.startActivity(new Intent(context1, TripDetailsActivity.class));
            }
        });
    }

    /**
     * The ViewHolder used by this item.
     * Extending from FlexibleViewHolder is recommended especially when you will use
     * more advanced features.
     */
    public class MyViewHolder extends FlexibleViewHolder {

        public TextView textViewProdname, textViewAvailable, textViewProdPrice;


        public MyViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            textViewProdname = (TextView) view.findViewById(R.id.txt_product_name);
            textViewAvailable = (TextView) view.findViewById(R.id.txt_product_quantity);
            textViewProdPrice = (TextView) view.findViewById(R.id.txt_product_price);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Below line is just like a safety check, because sometimes holder could be null,
                    // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                }
            });
        }
    }
}