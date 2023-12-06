package com.smartcheck.dsv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class All_Products extends AppCompatActivity{

    private static final String API_URL = "https://dummyjson.com/products";
    RecyclerView rv;
    ProductAdapter adapter;
    List<Product> list;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        rv=findViewById(R.id.rv);
        manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(manager);
        list = new ArrayList<>();
        updation();

    }

    private List<Product> parseProducts(JSONArray productsArray) throws JSONException {
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < productsArray.length(); i++) {
            JSONObject productObject = productsArray.getJSONObject(i);
            int id = productObject.getInt("id");
            String title = productObject.getString("title");
            String description = productObject.getString("description");
            double price = productObject.getDouble("price");
            double discountPercentage = productObject.getDouble("discountPercentage");
            double rating = productObject.getDouble("rating");
            int stock = productObject.getInt("stock");
            String brand = productObject.getString("brand");
            String category = productObject.getString("category");
            String thumbnail = productObject.getString("thumbnail");
            JSONArray imagesArray = productObject.getJSONArray("images");
            List<String> imagesList = new ArrayList<>();
            for (int j = 0; j < imagesArray.length(); j++) {
                String imageUrl = imagesArray.getString(j);
                imagesList.add(imageUrl);
            }
            Product product = new Product(id, title, description, price, discountPercentage,
                    rating, stock, brand, category, thumbnail, imagesList);
            productList.add(product);
        }

        return productList;
    }





    private void updation() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);;
        StringRequest mStringRequest = new StringRequest(com.android.volley.Request.Method.GET, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject js = new JSONObject(response);
                    JSONArray productsArray = js.getJSONArray("products");
                    list = parseProducts(productsArray);
                    adapter = new ProductAdapter(getApplicationContext(), list);
                    rv.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Product product) {
                            openProductDetailsActivity(product);
                        }
                    });

                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(All_Products.this, "There was an error while loading the data", Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void openProductDetailsActivity(Product product) {
        Intent intent = new Intent(this, product_page.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

}



































class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {


    private Context context;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;


    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.setData(product.getTitle(),product.getPrice(),product.getDiscountPercentage(),product.getBrand(),product.getRating());
        Picasso.get().load(product.getThumbnail()).placeholder(R.drawable.img_1).into(holder.imageViewThumbnail);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname,price1,price2,discount,branding;
        RatingBar rating;
        ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.thubnail);
            productname = itemView.findViewById(R.id.productname);
            price1 = itemView.findViewById(R.id.price1);
            price2 = itemView.findViewById(R.id.price2);
            discount = itemView.findViewById(R.id.discount);
            branding = itemView.findViewById(R.id.brand);
            rating = itemView.findViewById(R.id.rating);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(productList.get(position));
                        }
                    }
                }
            });
        }

        public void setData(String title, double price, double discountPercentage, String brand, double r) {
            productname.setText(title);
            discount.setText(discountPercentage+"%");
            branding.setText(brand);
            rating.setRating((float)r);
            double a=(discountPercentage*price)/100;
            double p2=price-a;
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            numberFormat.setCurrency(Currency.getInstance("USD"));
            String s1= numberFormat.format(p2);
            price2.setText(s1);
            price1.setText(numberFormat.format(price));
            price1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

