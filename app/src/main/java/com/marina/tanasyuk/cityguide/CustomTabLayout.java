package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom Tab Layout view that renders a custom slider with following specs:
 *      slider can be dragged;
 *      it always shows the place type it sits above;
 *      on touch up the slider snaps to the nearest place type;
 *      on tap on a place type label (tab) the slider moves accordingly;
 *      text of a slider changes accordingly to the type of a places;
 *      TODO slider is animated on move
 *      TODO handle configuration changes
 */

public class CustomTabLayout extends FrameLayout {

    @BindView(R.id.slidingTabs)
    TabLayout tabLayout;
    @BindView(R.id.sliderContainerView)
    View sliderContainerView;
    @BindView(R.id.sliderView)
    Button sliderView;

    public CustomTabLayout(Context context) {
        super(context);
        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_tab, this);
        ButterKnife.bind(this);

        setupTabLayout();

        setupSliderView();
    }

    public void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int containerWidth = sliderContainerView.getMeasuredWidth();
                float containerX = sliderContainerView.getX();
                int selectedPos = tab.getPosition();
                if (selectedPos == 2) {
                    sliderView.setX(containerX + containerWidth/3 * 2);
                } else if (selectedPos == 1) {
                    sliderView.setX(containerX + containerWidth/3);
                } else if (selectedPos == 0) {
                    sliderView.setX(containerX);
                }
                sliderView.setText(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // NO OP
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // NO OP
            }
        });
    }

    public void setupSliderView() {
        sliderView.setText(R.string.title_bar);
        sliderView.setOnTouchListener(new CustomTouchListener());
    }

    public void setupWithViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

    public class CustomTouchListener implements View.OnTouchListener {

        private int CLICK_ACTION_THRESHOLD = 200;
        private float startX;
        private float startY;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (isAClick(startX, endX, startY, endY)) {
                        handleClick();
                    } else {
                        handleDrag(startX, endX);
                    }
                    break;
            }
            return true;
        }

        private void handleDrag(float startX, float endX) {
            TabLayout.Tab tabToGo;
            // if dragged past second tab
            // added threshold for smoother dragging
            if (startX + endX - CLICK_ACTION_THRESHOLD > (sliderContainerView.getX() + sliderContainerView.getWidth() / 3 * 2)) {
                tabToGo = tabLayout.getTabAt(2);
            }
            // if dragged past first tab or back by one tab
            // added threshold for smoother dragging
            else if ((startX + endX - CLICK_ACTION_THRESHOLD > (sliderContainerView.getX() + sliderContainerView.getWidth() / 3))
                    || (startX + endX < sliderView.getWidth() && startX + endX > 0)) {
                tabToGo = tabLayout.getTabAt(1);
            }
            // all other cases = drag to first tab
            else {
                tabToGo = tabLayout.getTabAt(0);
            }
            if (tabToGo != null) {
                tabToGo.select();
            }
        }

        private void handleClick() {
            int currentPos = tabLayout.getSelectedTabPosition();
            TabLayout.Tab tabToGo = null;
            if (currentPos == 2) {
                tabToGo = tabLayout.getTabAt(0);
            } else if (currentPos == 1) {
                tabToGo = tabLayout.getTabAt(currentPos + 1);
            } else if (currentPos == 0) {
                tabToGo = tabLayout.getTabAt(currentPos + 1);
            }
            if (tabToGo != null) {
                tabToGo.select();
                sliderView.setText(tabToGo.getText());
            }
        }

        private boolean isAClick(float startX, float endX, float startY, float endY) {
            float differenceX = Math.abs(startX - endX);
            float differenceY = Math.abs(startY - endY);
            return !(differenceX > CLICK_ACTION_THRESHOLD || differenceY > CLICK_ACTION_THRESHOLD);
        }
    }
}
