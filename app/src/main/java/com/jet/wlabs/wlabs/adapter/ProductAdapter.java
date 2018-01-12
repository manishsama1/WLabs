package com.jet.wlabs.wlabs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jet.wlabs.wlabs.model.Product;
import com.jet.wlabs.wlabs.R;
import com.jet.wlabs.wlabs.ui.ProductDetailActivity;

import java.util.List;

/**
 * Created by Manish on 1/9/2018.
 */
//Adapter to display list of products
public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView thumbnailView;
        private TextView productName;
        private TextView productReview;
        private TextView reviewCount;
        private TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productReview = (TextView) itemView.findViewById(R.id.review);
            reviewCount = (TextView) itemView.findViewById(R.id.review_count);
            price = (TextView) itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Product product = productList.get(position);

            //return in case the selected product is null
            if (product == null) {
                Toast.makeText(context, context.getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(Product.PRODUCT, product);
            context.startActivity(intent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Product product = productList.get(position);
        viewHolder.productName.setText(product.getProductName());
        viewHolder.price.setText(product.getPrice());
        viewHolder.productReview.setText(product.getReviewRating() != 0 ? context.getString(R.string.ratings) + String.valueOf(product.getReviewRating()) : "");
        viewHolder.reviewCount.setText(product.getReviewCount() != 0.0 ? "(" + product.getReviewCount() + ")" : context.getString(R.string.no_rating));
        setThumbnail(viewHolder, position);
    }

    //set image for the product using Glide library if the URL is not empty
    private void setThumbnail(ViewHolder viewHolder, int position) {
        Product product = productList.get(position);

        if (TextUtils.isEmpty(product.getProductImageURL())) {
            viewHolder.thumbnailView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context)
                    .load(product.getProductImageURL())
                    .placeholder(R.mipmap.ic_launcher)//placeholder image while the actual image is getting downloaded
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//cache image
                    .override(160, 160)//override the image size
                    .into(viewHolder.thumbnailView);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
