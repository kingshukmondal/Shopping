package com.smartcheck.dsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class product_page extends AppCompatActivity {

    TextView brand,productname,rating,discount,price1,price2,details,avail,category,buynow;
    RatingBar ratingBar;
    ViewPager viewPager;
     WormDotsIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        Product product = (Product) getIntent().getSerializableExtra("product");


        brand=findViewById(R.id.brand);
        productname=findViewById(R.id.productname);
        rating=findViewById(R.id.rating);
        discount=findViewById(R.id.discount);
        price1=findViewById(R.id.price1);
        price2=findViewById(R.id.price2);
        details=findViewById(R.id.details);
        avail=findViewById(R.id.avail);
        ratingBar=findViewById(R.id.MyRating);
        indicator = findViewById(R.id.indicator);
        category=findViewById(R.id.category);
        buynow=findViewById(R.id.buynow);




        brand.setText(product.getBrand());
        productname.setText(product.getTitle());
        rating.setText(product.getRating()+"");
        discount.setText(product.getDiscountPercentage()+"%");
        details.setText(product.getDescription());
        ratingBar.setRating((float) product.getRating());
        int k=product.getStock();
        if(k!=0) avail.setText("Product Available "+ k);
        else avail.setText("Out Of Stock");
        double price=product.getPrice();
        double discountPercentage=product.getDiscountPercentage();
        double a=(discountPercentage*price)/100;
        double p2=price-a;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        numberFormat.setCurrency(Currency.getInstance("USD"));
        String s1= numberFormat.format(p2);
        price2.setText(s1);
        price1.setText(numberFormat.format(price));
        price1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        category.setText(product.getCategory());



        List<String> images=product.getImages();
        viewPager = findViewById(R.id.viewPager);
        ProductImagesAdapter imagesAdapter = new ProductImagesAdapter(images,getApplicationContext() );
        viewPager.setAdapter(imagesAdapter);
        indicator.setViewPager(viewPager);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(product_page.this, "No Action Initiated for this input.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}






