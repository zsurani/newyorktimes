package com.example.zsurani.nytsearch1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zsurani on 6/24/16.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView imageView;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindDrawable(R.drawable.clipart_news)
        Drawable place;

        public ViewHolder(View itemView) {
            super(itemView);
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            ButterKnife.bind(this, itemView);

        }
    }

    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public ArticleAdapter(Context context, List<Article> contacts) {
        mArticles = contacts;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        viewHolder.imageView.setImageResource(0);

        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        viewHolder.tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Glide.with(getContext()).load(thumbnail).into(viewHolder.imageView);
        }
        else {
            Glide.with(getContext()).load(R.drawable.news4).into(viewHolder.imageView);
            
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
