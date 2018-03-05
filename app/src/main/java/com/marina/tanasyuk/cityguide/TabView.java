package com.marina.tanasyuk.cityguide;

import android.view.View;

/**
 * Interface to enforce a simple lifecycle on a view in {@link TabViewImpl}.
 */

public interface TabView {

    void onAttachView(View view);

    void onDestroyView();

}
