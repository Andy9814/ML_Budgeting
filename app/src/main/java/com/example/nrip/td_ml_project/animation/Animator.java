package com.example.nrip.td_ml_project.animation;

import android.view.View;
import android.view.animation.Animation;

import androidx.core.util.Pair;

import java.util.Queue;

public class Animator {
    Queue<Pair<View,Animation>> animationQueue;


    public Animator(Queue<Pair<View,Animation>> animationQueue) {
        this.animationQueue = animationQueue;
    }

    void PlayAll() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                PlayNext(true);
            }
        });
        thread.start();
    }


    void PlayNext(boolean recursive) {
        if (animationQueue.isEmpty()!= false) {
            Pair<View,Animation> x = animationQueue.remove();

            if (recursive)
                x.second.setAnimationListener(new AnimatorListener(this));

            Animate(x.first,x.second);
        }
    }

    void Animate(View view, Animation anim) {
        view.startAnimation(anim);
    }
}
