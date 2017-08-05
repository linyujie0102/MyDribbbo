package me.linyujie.mydribbbo.view.shot_detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Shot;
import me.linyujie.mydribbbo.utils.ImageUtils;

/**
 * Created by linyujie on 8/4/17.
 */

@SuppressWarnings("deprecation")
public class ShotAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_DETAIL = 1;

    private final ShotFragment shotFragment;
    private final Shot shot;

    public ShotAdapter(@NonNull ShotFragment shotFragment, @NonNull Shot shot) {
        this.shotFragment = shotFragment;
        this.shot = shot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        switch (viewType) {
            case VIEW_TYPE_SHOT_IMAGE:
                view = LayoutInflater.from(context).inflate(R.layout.shot_item_image, parent, false);
                return new ShotImageViewHolder(view);
            case VIEW_TYPE_SHOT_DETAIL:
                view = LayoutInflater.from(context).inflate(R.layout.shot_item_info, parent, false);
                return new ShotDetailViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SHOT_IMAGE:
                ImageUtils.loadShotImage(shot, ((ShotImageViewHolder) holder).image);
                break;
            case VIEW_TYPE_SHOT_DETAIL:
                final ShotDetailViewHolder shotDetailViewHolder = (ShotDetailViewHolder) holder;
                shotDetailViewHolder.title.setText(shot.title);
                shotDetailViewHolder.authorName.setText(shot.user.name);

                if(Build.VERSION.SDK_INT >= 24) {
                    shotDetailViewHolder.description.setText(Html.fromHtml((shot.description == null ? "" : shot.description), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
                } else {
                    shotDetailViewHolder.description.setText(Html.fromHtml(shot.description == null ? "" : shot.description));
                }
                shotDetailViewHolder.description.setMovementMethod(LinkMovementMethod.getInstance());

                shotDetailViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
                shotDetailViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
                shotDetailViewHolder.viewCount.setText(String.valueOf(shot.views_count));

                ImageUtils.loadUserPicture(shot.user.avatar_url, shotDetailViewHolder.authorPicture);

                shotDetailViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shotFragment.like(shot.id, !shot.liked);
                    }
                });

                shotDetailViewHolder.bucketButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shotFragment.bucket();
                    }
                });

                shotDetailViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shotFragment.share();
                    }
                });

                Drawable likeDrawable = shot.liked ? ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_pink_500_18dp)
                                                   : ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_black_18dp);
                shotDetailViewHolder.likeButton.setImageDrawable(likeDrawable);

                Drawable bucketDrawable = shot.bucketed ? ContextCompat.getDrawable(getContext(), R.drawable.ic_inbox_pink_500_18dp)
                                                        : ContextCompat.getDrawable(getContext(), R.drawable.ic_inbox_black_18dp);
                shotDetailViewHolder.bucketButton.setImageDrawable(bucketDrawable);


        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_SHOT_IMAGE;
        } else {
            return VIEW_TYPE_SHOT_DETAIL;
        }
    }

    @NonNull
    private Context getContext() {
        return shotFragment.getContext();
    }
}
