package me.linyujie.mydribbbo.view.shot_list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;


import java.util.List;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Shot;
import me.linyujie.mydribbbo.utils.ImageUtils;
import me.linyujie.mydribbbo.utils.ModelUtils;
import me.linyujie.mydribbbo.view.base.BaseViewHolder;
import me.linyujie.mydribbbo.view.base.InfiniteAdapter;
import me.linyujie.mydribbbo.view.shot_detail.ShotActivity;
import me.linyujie.mydribbbo.view.shot_detail.ShotFragment;

/**
 * Created by linyujie on 7/23/17.
 */

public class ShotListAdapter extends InfiniteAdapter<Shot> {

    private final ShotListFragment shotListFragment;

    public ShotListAdapter(@NonNull ShotListFragment shotListFragment,
                           @NonNull List<Shot> data,
                           @NonNull LoadMoreListener loadMoreListener) {
        super(shotListFragment.getContext(), data, loadMoreListener);
        this.shotListFragment = shotListFragment;
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_shot, parent, false);
        return  new ShotViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        ShotViewHolder shotViewHolder = (ShotViewHolder) holder;

        final Shot shot = getData().get(position);
        shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>() {}));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                shotListFragment.startActivityForResult(intent, ShotListFragment.REQ_CODE_SHOT);
            }
        });

        shotViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        shotViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotViewHolder.viewCount.setText(String.valueOf(shot.views_count));

        ImageUtils.loadShotImage(shot, shotViewHolder.image);

    }
}
