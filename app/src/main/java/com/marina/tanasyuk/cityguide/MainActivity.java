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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * TODO Add specs, REFACTOR!!! - move fetching logic to Presenter and give it a view
 */

public class MainActivity extends AppCompatActivity {

    private static final int READ_LOCATION_PERMISSIONS_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int NUMBER_OF_TABS = 3;

    @BindView(R.id.viewPagerMain)
    CustomNonSwipeableViewPager viewPager;
    @BindView(R.id.tabLayout)
    CustomTabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    protected PlaceDetectionClient mPlaceDetectionClient;

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
        setupGooglePlaces();

        // first time fetching, get places for all tabs
        // TODO uncomment before submission
//        fetchPlaces(NUMBER_OF_TABS);
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
        for (int i = 0; i < NUMBER_OF_TABS; i++) {
            tabViewPagerAdapter.addView(new TabViewImpl(context, new ArrayList<MyPlace>(), i));
        }
        viewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupGooglePlaces() {
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void fetchPlaces(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,
                        "Read Location permission is required for this app to work",
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        READ_LOCATION_PERMISSIONS_REQUEST);
            }
        } else {
            fetchUpdatePlaces(mPlaceDetectionClient.getCurrentPlace(null), position);
        }
    }

    private void fetchUpdatePlaces(Task<PlaceLikelihoodBufferResponse> placeResult, final int position) {
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    List<MyPlace> bars = new ArrayList<>();
                    List<MyPlace> bistros = new ArrayList<>();
                    List<MyPlace> cafes = new ArrayList<>();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Place place = placeLikelihood.getPlace();
                        int placeType = place.getPlaceTypes().get(0);
                        switch (placeType) {
                            // TODO: update first case to 9 (bars)
                            case 30:
                                bars.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.BAR,
                                        (int) place.getRating()));
                                break;
                            case 15:
                                cafes.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.CAFE,
                                        (int) place.getRating()));
                                break;
                            case 79:
                                bistros.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.BISTRO,
                                        (int) place.getRating()));
                                break;
                        }
                    }
                    if (position >= 0 && position <= 2) {
                        TabViewImpl view = tabViewPagerAdapter.getItem(position);
                        MyPlacesAdapter adapter = (MyPlacesAdapter) view.rvPlaces.getAdapter();
                        adapter.clear();
                        if (position == 0) {
                            adapter.addAll(bars);
                        } else if (position == 1) {
                            adapter.addAll(bistros);
                        } else if (position == 2) {
                            adapter.addAll(cafes);
                        }
                        adapter.notifyDataSetChanged();
                        view.setSwipeContainerStatusNotRefreshing();
                    } else {
                        // position equals NUMBER_OF_TABS, update all tabs
                        for (int i = 0; i < NUMBER_OF_TABS; i++) {
                            TabViewImpl view = tabViewPagerAdapter.getItem(i);
                            MyPlacesAdapter adapter = (MyPlacesAdapter) view.rvPlaces.getAdapter();
                            adapter.clear();
                            if (i == 0) {
                                adapter.addAll(bars);
                            } else if (i == 1) {
                                adapter.addAll(bistros);
                            } else if (i == 2) {
                                adapter.addAll(cafes);
                            }
                            adapter.notifyDataSetChanged();
                            view.setSwipeContainerStatusNotRefreshing();
                        }
                    }
                    tabViewPagerAdapter.notifyDataSetChanged();
                    likelyPlaces.release();
                } else {
                    Toast.makeText(context,
                            "Oh snap! Something went wrong, try again later",
                            Toast.LENGTH_LONG).show();
                    Timber.e(TAG, "Couldn't get places");
                    if (position >=0 && position <= 2) {
                        tabViewPagerAdapter.getItem(position).setSwipeContainerStatusNotRefreshing();
                    }
                }
            }
        });
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
