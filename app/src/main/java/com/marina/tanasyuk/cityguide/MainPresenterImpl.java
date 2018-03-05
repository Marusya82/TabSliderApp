package com.marina.tanasyuk.cityguide;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.marina.tanasyuk.cityguide.MainActivity.READ_LOCATION_PERMISSIONS_REQUEST;

/**
 * Presenter created to offload fetching and view updating logic off Main Activity, imitating MVP.
 */

public class MainPresenterImpl implements MainPresenter {

    static final int NUMBER_OF_TABS = 3;
    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private PlaceDetectionClient mPlaceDetectionClient;
    private TabViewPagerAdapter tabViewPagerAdapter;
    private DistanceApiEndpointInteractor distanceApiEndpointInteractor;

    private final Context context;

    MainPresenterImpl(Context context, TabViewPagerAdapter tabViewPagerAdapter) {
        if (context instanceof MainActivity) {
            this.context = context;
            this.tabViewPagerAdapter = tabViewPagerAdapter;
            mPlaceDetectionClient = Places.getPlaceDetectionClient(context, null);
            distanceApiEndpointInteractor = new DistanceApiEndpointInteractor(this, this.context);
        } else {
            throw new IllegalArgumentException("Has to be originated from Main Activity");
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void fetchPlaces(int position) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context,
                        "Read Location permission is required for this app to work",
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                            case 9:
                                bars.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.BAR,
                                        (int) place.getRating(),
                                        place.getId()));
                                break;
                            // TODO: update second case to 15 (cafes)
                            case 15:
                                cafes.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.CAFE,
                                        (int) place.getRating(),
                                        place.getId()));
                                break;
                            // TODO: update second case to 79 (bistros)
                            case 30:
                                bistros.add(new MyPlace(place.getName().toString(),
                                        MyPlace.Type.BISTRO,
                                        (int) place.getRating(),
                                        place.getId()));
                                break;
                        }
                    }
                    updateUI(position, bars, bistros, cafes);
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

    private void updateUI(int position, List<MyPlace> bars, List<MyPlace> bistros, List<MyPlace> cafes) {
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
    }
}