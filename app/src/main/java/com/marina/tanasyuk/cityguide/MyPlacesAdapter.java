package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * A custom adapter to render places based on its type.
 */

public class MyPlacesAdapter extends RecyclerView.Adapter<MyPlacesAdapter.ViewHolder> {

    private List<MyPlace> mMyPlaces;
    private Context mContext;

    MyPlacesAdapter(Context context, List<MyPlace> myPlaces) {
        mMyPlaces = myPlaces;
        mContext = context;
    }

    @Override
    public MyPlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View placeView = inflater.inflate(R.layout.item_place, parent, false);

        return new ViewHolder(placeView);
    }

    @Override
    public void onBindViewHolder(MyPlacesAdapter.ViewHolder viewHolder, int position) {
        MyPlace myPlace = mMyPlaces.get(position);

        TextView textView = viewHolder.nameTextView;
        textView.setText(myPlace.getName());

        ImageView iconImageView = viewHolder.iconImageView;
        switch (myPlace.getType()) {
            case BAR:
                iconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bar));
                break;
            case CAFE:
                iconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_cafe));
                break;
            case BISTRO:
                iconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bistro));
        }

        RatingView ratingView = viewHolder.ratingBar;
        ratingView.setRating(myPlace.getRating());
    }

    void clear() {
        mMyPlaces.clear();
        notifyDataSetChanged();
    }

    void addAll(List<MyPlace> list) {
        mMyPlaces.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMyPlaces != null ? mMyPlaces.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView iconImageView;
        RatingView ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
            iconImageView = itemView.findViewById(R.id.ivIcon);
            ratingBar = itemView.findViewById(R.id.ratingView);
        }
    }
}
