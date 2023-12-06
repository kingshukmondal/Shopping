package com.smartcheck.dsv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {

    private List<String> images;
    private LayoutInflater inflater;

    public ProductImagesAdapter(List<String> images, Context con) {
        this.images = images;
        inflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.custom_viewholder, container, false);
        ImageView imageView = view.findViewById(R.id.imagview);
        Picasso.get().load(images.get(position)).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
