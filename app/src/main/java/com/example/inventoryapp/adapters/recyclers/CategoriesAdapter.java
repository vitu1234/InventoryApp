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

public class CategoriesAdapter extends AbstractFlexibleItem<CategoriesAdapter.MyViewHolder> {

    private String id, category_name;
    private int category_id;

    public CategoriesAdapter(String id, String category_name, int category_id) {
        this.id = id;
        this.category_name = category_name;
        this.category_id = category_id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof CategoriesAdapter) {
            CategoriesAdapter inItem = (CategoriesAdapter) inObject;
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
        return R.layout.recycler_category_line;
    }

    @Override
    public MyViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new MyViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, MyViewHolder holder,
                               int position,
                               List<Object> payloads) {
        holder.textViewCatname.setText(category_name);


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

        public TextView textViewCatname;


        public MyViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            textViewCatname = (TextView) view.findViewById(R.id.textViewCatName);


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
