package me.linyujie.mydribbbo.view.shot_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.dribbble.Dribbble;
import me.linyujie.mydribbbo.model.Bucket;
import me.linyujie.mydribbbo.model.Shot;
import me.linyujie.mydribbbo.utils.ModelUtils;
import me.linyujie.mydribbbo.view.bucket_list.BucketListFragment;
import me.linyujie.mydribbbo.view.shot_list.ShotListAdapter;

/**
 * Created by linyujie on 7/24/17.
 */

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";
    public static final int REQ_CODE_BUCKET = 100;

    private ShotAdapter adapter;
    private Shot shot;
    private boolean isliking;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static ShotFragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT), new TypeToken<Shot>(){});

        adapter = new ShotAdapter(this, shot);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        new LoadCollectedBucketIdsTask().execute();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_BUCKET && resultCode == Activity.RESULT_OK) {
            List<String> chosenBucketIds = data.getStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS);
            List<String> addedBucketIds = new ArrayList<>();
            List<String> removedBucketIds = new ArrayList<>();
            List<String> collectedBucketIds = adapter.getReadOnlyCollectedBucketIds();

            for (String chosenBucketId : chosenBucketIds) {
                if (!collectedBucketIds.contains(chosenBucketId)) {
                    addedBucketIds.add(chosenBucketId);
                }
            }

            for (String collectedBucketId : collectedBucketIds) {
                if (!chosenBucketIds.contains(collectedBucketId)) {
                    removedBucketIds.add(collectedBucketId);
                }
            }

            new UpdateBucketTask(addedBucketIds, removedBucketIds).execute();
        }
    }

    public void like (@NonNull String shotId, boolean like) {
        if(!isliking) {
            isliking = true;
            new LikeTask(shotId, like).execute();
        }

    }

    private class LikeTask extends AsyncTask<Void, Void, Void> {
        private String id;
        private boolean like;

        public LikeTask(String id, boolean like) {
            this.id = id;
            this.like = like;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (like) {
                    Dribbble.likeShot(id);
                } else {
                    Dribbble.unlikeShot(id);
                }
                return null;
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            isliking = false;

            shot.liked = like;
            shot.likes_count += like ? 1 : -1;
            adapter.notifyDataSetChanged();

        }
    }
    private class LoadCollectedBucketIdsTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            try {
                List<Bucket> shotBuckets = Dribbble.getShotBuckets(shot.id);
                List<Bucket> userBuckets = Dribbble.getUserBuckets();

                Set<String> userBucketIds = new HashSet<>();
                for (Bucket userBucket : userBuckets) {
                    userBucketIds.add(userBucket.id);
                }

                List<String> collectedBucketIds = new ArrayList<>();
                for (Bucket shotBucket : shotBuckets) {
                    if (userBucketIds.contains(shotBucket.id)) {
                        collectedBucketIds.add(shotBucket.id);
                    }
                }

                return collectedBucketIds;


            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> collectedBucketIds) {
            adapter.updateCollectedBucketIds(collectedBucketIds);
        }
    }

    private class UpdateBucketTask extends AsyncTask<Void, Void, Void> {

        private List<String> added;
        private List<String> removed;
        private Exception e;

        private UpdateBucketTask(@NonNull List<String> added,
                                 @NonNull List<String> removed) {
            this.added = added;
            this.removed = removed;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (String addedId : added) {
                    Dribbble.addBucketShot(addedId, shot.id);
                }
                for (String removedId : removed) {
                    Dribbble.removeBucketShot(removedId, shot.id);
                }
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
                this.e = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(e == null) {
                adapter.updateCollectedBucketIds(added, removed);
            } else {
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
