package me.linyujie.mydribbbo.view.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.view.base.BaseViewHolder;

/**
 * Created by linyujie on 7/23/17.
 */

public class ShotViewHolder extends BaseViewHolder {

    @BindView(R.id.shot_clickable_cover) View cover;
    @BindView(R.id.shot_like_count) TextView likeCount;
    @BindView(R.id.shot_bucket_count) TextView bucketCount;
    @BindView(R.id.shot_view_count) TextView viewCount;
    @BindView(R.id.shot_image) ImageView image;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}
