package com.jet.wlabs.wlabs.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jet.wlabs.wlabs.model.Product;
import com.jet.wlabs.wlabs.R;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView thumbnailView;
    private TextView productName;
    private TextView productReview;
    private TextView reviewCount;
    private TextView price;
    private TextView description;
    private TextView stockStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        thumbnailView = (ImageView) findViewById(R.id.thumbnail);
        productName = (TextView) findViewById(R.id.product_name);
        productReview = (TextView) findViewById(R.id.review);
        reviewCount = (TextView) findViewById(R.id.review_count);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        stockStatus = (TextView) findViewById(R.id.stock_status);

        Product product = getIntent().getParcelableExtra(Product.PRODUCT);
        setData(product);
    }

    private void setData(Product product) {
        Spanned descriptionText = Html.fromHtml(product.getLongDescription() != null ? product.getLongDescription() : "");
        productName.setText(product.getProductName());
        price.setText(product.getPrice());
        productReview.setText(product.getReviewRating() != 0 ? getString(R.string.ratings) + String.valueOf(product.getReviewRating()) : "");
        reviewCount.setText(product.getReviewCount() != 0.0 ? "(" + product.getReviewCount() + ")" : getString(R.string.no_rating));
        description.setText(descriptionText);
        stockStatus.setText(getString(product.isInStock() ? R.string.status_in_stock : R.string.status_out_of_stock));
        setThumbnail(product.getProductImageURL());
    }

    //fetch the image thumbnail using glide library
    private void setThumbnail(String productImageURL) {
        if (TextUtils.isEmpty(productImageURL)) {
            thumbnailView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(this)
                    .load(productImageURL)//image download url
                    .placeholder(R.mipmap.loading_icon)//placeholder image
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//image caching
                    .error(R.mipmap.ic_no_preview)//error case
                    .into(thumbnailView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }

        return super.onOptionsItemSelected(item);
    }
}
