package com.example.nrip.td_ml_project.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;


public class FadeInAnimation extends AlphaAnimation {

    public FadeInAnimation(float fromAlpha, float toAlphas, int d) {
        super(fromAlpha,toAlphas);
        super.setInterpolator(new DecelerateInterpolator()); //add this
        super.setDuration(d);
    }


}
