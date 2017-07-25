package me.linyujie.mydribbbo.view.shot_detail;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import me.linyujie.mydribbbo.view.base.SingleFragmentActivity;

/**
 * Created by linyujie on 7/24/17.
 */

public class ShotActivity extends SingleFragmentActivity {

    public static final String KEY_SHOT_TITLE = "shot_title";

    @NonNull
    @Override
    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());

    }

    @NonNull
    @Override
    protected String getActivityTitle() {
        return getIntent().getStringExtra(KEY_SHOT_TITLE);
    }
}
