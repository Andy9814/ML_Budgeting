package com.example.nrip.td_ml_project.animation;

import android.view.animation.Animation;

public class AnimatorListener implements Animation.AnimationListener {
    Animator animator;

    AnimatorListener(Animator animator) {
        this.animator = animator;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animator.PlayNext(true);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
