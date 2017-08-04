package me.linyujie.mydribbbo.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.linyujie.mydribbbo.R;
import me.linyujie.mydribbbo.dribbble.Dribbble;
import me.linyujie.mydribbbo.view.bucket_list.BucketListFragment;
import me.linyujie.mydribbbo.view.shot_list.ShotListFragment;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindView(R.id.drawer) NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        System.out.println(getSupportActionBar().getHeight());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.setDrawerListener(drawerToggle);

       // set up for header

        View headerView = navigationView.getHeaderView(0);

        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(Dribbble.getCurrentUser().name);

        ((SimpleDraweeView) headerView.findViewById(R.id.nav_header_user_picture))
                .setImageURI(Uri.parse(Dribbble.getCurrentUser().avatar_url));

        headerView.findViewById(R.id.nav_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dribbble.logout(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;

                }

                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_item_likes:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_likes);
                        break;
                    case R.id.drawer_item_buckets:
                        fragment = BucketListFragment.newInstance(false, null);
                        setTitle((R.string.title_buckets));
                        break;
                }

                drawerLayout.closeDrawers();

                if(fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return  true;
                }

                return false;
            }
        });
    }
}
