package me.linyujie.mydribbbo.view.shot_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Shot;
import me.linyujie.mydribbbo.view.bucket_list.BucketListFragment;
import me.linyujie.mydribbbo.view.bucket_list.ChooseBucketActivity;


/**
 * Created by linyujie on 7/24/17.
 */

public class ShotAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_INFO = 1;

    private final ShotFragment shotFragment;
    private final Shot shot;

    private ArrayList<String> collectedBucketIds;

    public ShotAdapter(@NonNull ShotFragment shotFragment, @NonNull Shot shot) {
        this.shot = shot;
        this.shotFragment = shotFragment;
        this.collectedBucketIds = null;
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
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(shot.getImageUrl()))
                    .setAutoPlayAnimations(true)
                    .build();
            ((ImageViewHolder) holder).image.setController(controller);

        } else {
            InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
            infoViewHolder.title.setText(shot.title);
            infoViewHolder.authorName.setText(shot.user.name);
            infoViewHolder.authorPicture.setImageURI(Uri.parse(shot.user.avatar_url));
            infoViewHolder.description.setText(shot.description);
            infoViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
            infoViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
            infoViewHolder.viewCount.setText(String.valueOf(shot.views_count));

            Drawable bucketDrawable = shot.bucketed
                    ? ContextCompat.getDrawable(infoViewHolder.itemView.getContext(), R.drawable.ic_inbox_pink_500_18dp)
                    : ContextCompat.getDrawable(infoViewHolder.itemView.getContext(), R.drawable.ic_inbox_black_18dp);

            infoViewHolder.bucketButton.setImageDrawable(bucketDrawable);

            infoViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    share(view.getContext());
                }
            });
            infoViewHolder.bucketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bucket(view.getContext());
                }
            });

            infoViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shotFragment.like(shot.id, !shot.liked);
                }
            });
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

    public List<String> getReadOnlyCollectedBucketIds() {
        return Collections.unmodifiableList(collectedBucketIds);
    }

    public void updateCollectedBucketIds(@NonNull List<String> bucketIds) {
        if (collectedBucketIds == null) {
            collectedBucketIds = new ArrayList<>();
        }

        collectedBucketIds.clear();
        collectedBucketIds.addAll(bucketIds);

        shot.bucketed = !bucketIds.isEmpty();
        notifyDataSetChanged();
    }

    public void updateCollectedBucketIds(@NonNull List<String> addedIds,
                                         @NonNull List<String> removedIds) {
        if (collectedBucketIds == null) {
            collectedBucketIds = new ArrayList<>();
        }

        collectedBucketIds.addAll(addedIds);
        collectedBucketIds.removeAll(removedIds);

        shot.bucketed = !collectedBucketIds.isEmpty();
        shot.buckets_count += addedIds.size() - removedIds.size();
        notifyDataSetChanged();
    }

    private void share(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shot.title + " " + shot.html_url);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_shot)));
    }

    private void bucket(Context context) {
        Intent intent = new Intent(context, ChooseBucketActivity.class);
        intent.putStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS, collectedBucketIds);
        shotFragment.startActivityForResult(intent, ShotFragment.REQ_CODE_BUCKET);
    }
}
