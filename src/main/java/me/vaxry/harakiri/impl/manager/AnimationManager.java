package me.vaxry.harakiri.impl.manager;

import me.vaxry.harakiri.framework.animation.Animation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public final class AnimationManager {

    private final List<Animation> animations = new CopyOnWriteArrayList<>();

    public AnimationManager() {
        (new Thread() {
            public void run() {
                AnimationManager.this.update();
            }
        }).start();
    }

    private void update() {
        while (true) {
            long beforeAnimation = System.nanoTime();

            if (this.animations.size() > 0)
                this.animations.forEach(Animation::update);

            int milliseconds = (int) ((System.nanoTime() - beforeAnimation) / 1000000L);

            try {
                TimeUnit.MILLISECONDS.sleep((16 - milliseconds));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void unload() {
        this.animations.clear();
    }

    public void addAnimation(Animation animation) {
        this.animations.add(animation);
    }

    public List<Animation> getAnimations() {
        return animations;
    }
}
