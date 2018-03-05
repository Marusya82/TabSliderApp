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
 *      TODO slider can be dragged;
 *      it always shows the place type it sits above;
 *      on touch up the slider snaps to the nearest place type;
 *      on tap on a place type label (tab) the slider moves accordingly;
 *      text of a slider changes accordingly to the type of a places;
 *      TODO slider is animated on move.
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int containerWidth = sliderContainerView.getMeasuredWidth();
                float containerX = sliderContainerView.getX();
                int selectedPos = tab.getPosition();
                if (selectedPos == 2) {
                    sliderView.setX(containerX + containerWidth/3*2);
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

        setupSliderView();
    }

    public void setupSliderView() {
        sliderView.setText(R.string.title_bar);
        sliderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        // TODO finish onDrag
//        sliderView.setOnTouchListener(new DragTouchListener(sliderView.getX(), sliderView.getY()));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }

    public class DragTouchListener implements View.OnTouchListener {

        DragTouchListener(float initialX, float initialY) {
            lastX = initialX;
            lastY = initialY;
        }

        boolean isDragging = false;
        float lastX;
        float lastY;
        float deltaX;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                view.performClick();
                isDragging = true;
                deltaX = motionEvent.getX();
                return true;
            } else if (isDragging) {
                if (action == MotionEvent.ACTION_MOVE) {
                    view.setX(view.getX() + motionEvent.getX() - deltaX);
                    view.setY(view.getY());
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    isDragging = false;
                    lastX = motionEvent.getX();
                    lastY = motionEvent.getY();
                    return true;
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    view.setX(lastX);
                    view.setY(lastY);
                    isDragging = false;
                    return true;
                }
            }

            return false;
        }
    }
}
