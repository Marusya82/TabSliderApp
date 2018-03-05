package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Tab View implementation that shows a list of places via Recycler View
 * and is a lightweight replacement of a fragment for the purpose of paging.
 * The view has a simple lifecycle:
 *      onAttachView(View view);
 *      onDestroyView().
 */

public class TabViewImpl implements TabView {

    @BindView(R.id.rvPlaces)
    RecyclerView rvPlaces;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private List<MyPlace> myPlaces;
    private final Context context;
    private Unbinder unbinder;
    private final int position;

    TabViewImpl(Context context, List<MyPlace> myPlaces, int position) {
        this.myPlaces = myPlaces;
        this.context = context;
        this.position = position;
    }

    @Override
    public void onAttachView(View view) {
        unbinder = ButterKnife.bind(this, view);
        MyPlacesAdapter myPlacesAdapter = new MyPlacesAdapter(context, myPlaces);
        rvPlaces.setAdapter(myPlacesAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvPlaces.setLayoutManager(layoutManager);
        rvPlaces.addItemDecoration(new DividerItemDecoration(rvPlaces.getContext(),
                layoutManager.getOrientation()));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (context instanceof MainActivity) {
                    ((MainActivity) context).fetchPlaces(position);
                }
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);
    }

    void setSwipeContainerStatusNotRefreshing() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
    }
}
