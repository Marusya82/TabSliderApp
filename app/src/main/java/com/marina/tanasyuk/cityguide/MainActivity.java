package com.marina.tanasyuk.cityguide;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.marina.tanasyuk.cityguide.MainPresenterImpl.NUMBER_OF_TABS;

/**
 *
 */

public class MainActivity extends AppCompatActivity {

    public static final int READ_LOCATION_PERMISSIONS_REQUEST = 1;

    @BindView(R.id.viewPagerMain)
    CustomNonSwipeableViewPager viewPager;
    @BindView(R.id.tabLayout)
    CustomTabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private MainPresenterImpl presenter;

    private Context context;
    TabViewPagerAdapter tabViewPagerAdapter;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        setupToolbar();
        setupViewPager();

        // TODO Inject Presenter
        presenter = new MainPresenterImpl(context, tabViewPagerAdapter);

        // first time fetching, get places for all tabs
        fetchPlaces(NUMBER_OF_TABS);
    }

    public void fetchPlaces(int position) {
        presenter.fetchPlaces(position);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(NUMBER_OF_TABS);
        tabViewPagerAdapter = new TabViewPagerAdapter(this);

        TabViewImpl.TabViewOnRefreshListener tabViewOnRefreshListener = new TabViewImpl.TabViewOnRefreshListener() {
            @Override
            public void onRefresh(int position) {
                fetchPlaces(position);
            }
        };

        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            tabViewPagerAdapter.addView(
                    new TabViewImpl(context,
                            new ArrayList<MyPlace>(),
                            i,
                            tabViewOnRefreshListener));
        }
        viewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_LOCATION_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Read Location permission granted",
                        Toast.LENGTH_SHORT).show();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (showRationale) {
                    Toast.makeText(this,
                            "Read Location permission is required for this app to work",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,
                            "Read Location permission denied",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if (googleApiAvailability.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            Toast.makeText(this,
                    "Oh snap! No access to google play services",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
