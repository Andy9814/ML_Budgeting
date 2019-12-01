package com.example.nrip.td_ml_project.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private float from;
    private float  to;
    private boolean animateSecondaryBar;
    private TextView tv;

    public ProgressBarAnimation(ProgressBar progressBar, boolean animateSecondaryBar, float from, float to, TextView tv) {
        super();
        this.progressBar = progressBar;
        this.animateSecondaryBar = animateSecondaryBar;
        this.from = from;
        this.to = to;
        this.tv = tv;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;

        float multiplicator = ((float)progressBar.getWidth())/((float)progressBar.getMax());

        if (animateSecondaryBar){
            progressBar.setSecondaryProgress((int) value);
            tv.setX(value * multiplicator - tv.getWidth()/2 + progressBar.getX());
            tv.setText( String.valueOf(NumberFormat.getCurrencyInstance().format(value)));
        }
        else {
            progressBar.setProgress((int) value);
        }


        //tv.setText("multi: " + String.valueOf(multiplicator) + "   x: " + String.valueOf(value * multiplicator) + "  width: " + String.valueOf(progressBar.getWidth()));
        // tv.setText("value: " + String.valueOf(value) + "   x: " + String.valueOf(value * multiplicator) + "  width: " + String.valueOf(progressBar.getWidth()));
    }

}
