package com.marina.tanasyuk.cityguide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom view to show ratings.
 */

public class RatingView extends LinearLayout {

    @BindView(R.id.starOne)
    ImageView starOne;
    @BindView(R.id.starTwo)
    ImageView starTwo;
    @BindView(R.id.starThree)
    ImageView starThree;
    @BindView(R.id.starFour)
    ImageView starFour;
    @BindView(R.id.starFive)
    ImageView starFive;

    public RatingView(Context context) {
        super(context);
        init();
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.rating_view, this);
        ButterKnife.bind(this);
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            switch (rating) {
                case 1:
                    starOne.setSelected(true);
                    starTwo.setSelected(false);
                    starThree.setSelected(false);
                    starFour.setSelected(false);
                    starFive.setSelected(false);
                    break;
                case 2:
                    starOne.setSelected(true);
                    starTwo.setSelected(true);
                    starThree.setSelected(false);
                    starFour.setSelected(false);
                    starFive.setSelected(false);
                    break;
                case 3:
                    starOne.setSelected(true);
                    starTwo.setSelected(true);
                    starThree.setSelected(true);
                    starFour.setSelected(false);
                    starFive.setSelected(false);
                    break;
                case 4:
                    starOne.setSelected(true);
                    starTwo.setSelected(true);
                    starThree.setSelected(true);
                    starFour.setSelected(true);
                    starFive.setSelected(false);
                    break;
                case 5:
                    starOne.setSelected(true);
                    starTwo.setSelected(true);
                    starThree.setSelected(true);
                    starFour.setSelected(true);
                    starFive.setSelected(true);
                    break;
            }
        } else {
            starOne.setSelected(false);
            starTwo.setSelected(false);
            starThree.setSelected(false);
            starFour.setSelected(false);
            starFive.setSelected(false);
        }
    }
}
