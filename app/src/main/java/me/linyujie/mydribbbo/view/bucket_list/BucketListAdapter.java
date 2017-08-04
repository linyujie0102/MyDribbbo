package me.linyujie.mydribbbo.view.bucket_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Bucket;
import me.linyujie.mydribbbo.view.shot_list.ShotListFragment;



/**
 * Created by linyujie on 7/24/17.
 */

public class BucketListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BUCKET = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Bucket> data;
    private LoadMoreListener loadMoreListener;
    private boolean isChoosingMode;
    private boolean showLoading;


    public BucketListAdapter(@NonNull List<Bucket> data,
                             @NonNull LoadMoreListener loadMoreListener,
                             boolean isChoosingMode) {
        this.data = data;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
        this.isChoosingMode = isChoosingMode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_BUCKET) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bucket, parent, false);
            return new BucketViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        } else {

            final Bucket bucket = data.get(position);
            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            final Context context = holder.itemView.getContext();

            String bucketShotCountString = MessageFormat.format(
                    context.getResources().getString(R.string.shot_count),
                    bucket.shots_count);

            bucketViewHolder.bucketName.setText(bucket.name);
            bucketViewHolder.bucketShotCount.setText(bucketShotCountString);

            if(isChoosingMode) {
                bucketViewHolder.bucketChosen.setVisibility(View.VISIBLE);
                bucketViewHolder.bucketChosen.setImageDrawable(
                        bucket.isChoosing
                            ? ContextCompat.getDrawable(context, R.drawable.ic_check_box_black_24dp)
                            : ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank_black_24dp));
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bucket.isChoosing = !bucket.isChoosing;
                        notifyDataSetChanged();
                    }
                });
            } else {
                bucketViewHolder.bucketChosen.setVisibility(View.GONE);
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //later
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size() ? VIEW_TYPE_BUCKET : VIEW_TYPE_LOADING;
    }

    @NonNull
    public ArrayList<String> getSelectedBucketIds() {
        ArrayList<String> selectedBucketIds = new ArrayList<>();
        for (Bucket bucket : data) {
            if (bucket.isChoosing) {
                selectedBucketIds.add(bucket.id);
            }
        }
        return selectedBucketIds;
    }


    public void append(@NonNull List<Bucket> moreBuckets) {
        data.addAll(moreBuckets);
        notifyDataSetChanged();
    }

    public void prepend(@NonNull List<Bucket> data) {
        this.data.addAll(0, data);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }

    public  void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
