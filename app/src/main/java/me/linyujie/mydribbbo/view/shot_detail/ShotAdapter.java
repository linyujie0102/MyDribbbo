package me.linyujie.mydribbbo.view.shot_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Shot;


/**
 * Created by linyujie on 7/24/17.
 */

public class ShotAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_INFO = 1;

    private final Shot shot;

    public ShotAdapter(@NonNull Shot shot) {
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_item_image, parent, false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_item_info, parent, false);
            return new InfoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_IMAGE) {

        } else {
            InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
            infoViewHolder.title.setText(shot.title);
            infoViewHolder.authorName.setText(shot.user.name);
            infoViewHolder.description.setText(shot.description);
            infoViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
            infoViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
            infoViewHolder.viewCount.setText(String.valueOf(shot.views_count));
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_IMAGE;
        } else {
            return VIEW_TYPE_INFO;
        }
    }
}
