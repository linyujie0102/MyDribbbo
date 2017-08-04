package me.linyujie.mydribbbo.view.bucket_list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.model.Bucket;
import me.linyujie.mydribbbo.view.base.SingleFragmentActivity;

/**
 * Created by linyujie on 8/3/17.
 */

public class ChooseBucketActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment newFragment() {


        return BucketListFragment.newInstance(true, new ArrayList<String>());
    }

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getString(R.string.choose_bucket);
    }
}
