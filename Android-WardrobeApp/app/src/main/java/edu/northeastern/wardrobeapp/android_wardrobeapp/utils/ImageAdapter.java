package edu.northeastern.wardrobeapp.android_wardrobeapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.northeastern.wardrobeapp.android_wardrobeapp.R;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<ImageItem> galleryList;
    private Context context;
    private ImageAdapter.ViewHolder viewHolder;

    public ImageAdapter(Context context, ArrayList<ImageItem> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, int i) {
        this.viewHolder = viewHolder;
        renderImage(galleryList.get(i));
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public void renderImage(ImageItem item) {
        viewHolder.title.setText(item.getTitle());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Log.d("ImageAdapter", "Found image: " + item.getTitle());
        String url = item.getUrl();
        Picasso.with(context)
                .load(url)
                .resize(200, 200)
                .centerCrop().into(viewHolder.img);
    }

    public void addItem(ImageItem item) {
        galleryList.add(item);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
