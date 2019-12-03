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

    void PlayAllUsingConclusion() {
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

    public void PlayAllUsingDelay(int delayStart) {

        if (!animationQueue.isEmpty()) {

            //animationQueue.peek().first.startAnimation(animationQueue.peek().second);
            PlayNext(delayStart);
        }
    }

    void PlayNext(final int delayStart) {
        Pair<View,Animation> x = animationQueue.remove();
        x.first.startAnimation(x.second);

        x.first.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!animationQueue.isEmpty())
                    PlayNext(delayStart);
            }
        }, delayStart);
    }




    //        ProgressBarAnimation anim1 = new ProgressBarAnimation(firstBar, false, 0, budgAlreadySpent.intValue(), tvBudgetToBeSpent);
//        anim1.setDuration(400);
//        firstBar.startAnimation(anim1);
//        firstBar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //tv.setX((int)(firstBar.getProgress()/firstBar.getWidth()));
//                //tv.setX(40);
//                ProgressBarAnimation anim2 = new ProgressBarAnimation(firstBar, true, budgAlreadySpent.intValue(), budgAlreadySpent.add(budgToBeSpent).intValue(), tvBudgetToBeSpent);
//                anim2.setDuration(4000);
//                firstBar.startAnimation(anim2);
//            }
//        }, 400);

    void Animate(View view, Animation anim) {
        view.startAnimation(anim);
    }
}
