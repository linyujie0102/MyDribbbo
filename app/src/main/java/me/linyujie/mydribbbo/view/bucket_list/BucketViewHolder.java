package me.linyujie.mydribbbo.view.bucket_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.view.base.BaseViewHolder;

/**
 * Created by linyujie on 7/24/17.
 */

public class BucketViewHolder extends BaseViewHolder{

    @BindView(R.id.bucket_layout) View bucketLayout;
    @BindView(R.id.bucket_name) TextView bucketName;
    @BindView(R.id.bucket_shot_count) TextView bucketCount;
    @BindView(R.id.bucket_shot_chosen) ImageView bucketChosen;

    public BucketViewHolder(View itemView) {
        super(itemView);
    }
}
