package q.rorbin.verticaltablayout.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.lang.ref.WeakReference;
import java.util.Random;

import q.rorbin.verticaltablayout.widget.QBadgeView;

/**
 * Created by chqiu on 2017/1/24.
 */

public class BadgeAnimator extends ValueAnimator {
    private BombFragment[][] mFragments;
    private WeakReference<QBadgeView> mWeakBadge;

    private BadgeAnimator(QBadgeView badge) {
        mWeakBadge = new WeakReference<>(badge);
    }

    public static BadgeAnimator startBomb(Bitmap badgeBitmap, PointF center, QBadgeView badge) {
        final BadgeAnimator anime = new BadgeAnimator(badge);
        anime.setFloatValues(0f, 1f);
        anime.setDuration(500);
        anime.mFragments = anime.getBombFragments(badgeBitmap, center);
        anime.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                QBadgeView badgeView = anime.mWeakBadge.get();
                if (badgeView == null || !badgeView.isShown()) {
                    anime.end();
                } else {
                    badgeView.invalidate();
                }
            }
        });
        anime.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                QBadgeView badgeView = anime.mWeakBadge.get();
                if (badgeView != null) {
                    badgeView.reset();
                }
            }
        });
        anime.start();
        return anime;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < mFragments.length; i++) {
            for (int j = 0; j < mFragments[i].length; j++) {
                BombFragment bf = mFragments[i][j];
                float value = Float.parseFloat(getAnimatedValue().toString());
                bf.updata(value, canvas);
            }
        }
    }


    private BombFragment[][] getBombFragments(Bitmap badgeBitmap, PointF center) {
        int width = badgeBitmap.getWidth();
        int height = badgeBitmap.getHeight();
        float fragmentSize = Math.max(width, height) / 8f;
        float startX = center.x - badgeBitmap.getWidth() / 2f;
        float startY = center.y - badgeBitmap.getHeight() / 2f;
        BombFragment[][] fragments = new BombFragment[(int) (height / fragmentSize)][(int) (width / fragmentSize)];
        for (int i = 0; i < fragments.length; i++) {
            for (int j = 0; j < fragments[i].length; j++) {
                BombFragment bf = new BombFragment();
                bf.color = badgeBitmap.getPixel((int) (j * fragmentSize), (int) (i * fragmentSize));
                bf.x = startX + j * fragmentSize;
                bf.y = startY + i * fragmentSize;
                bf.size = fragmentSize;
                bf.maxSize = Math.max(width, height);
                fragments[i][j] = bf;
            }
        }
        badgeBitmap.recycle();
        return fragments;
    }

    private class BombFragment {
        Random random;
        float x;
        float y;
        float size;
        int color;
        int maxSize;
        Paint paint;

        public BombFragment() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            random = new Random();
        }

        public void updata(float value, Canvas canvas) {
            paint.setColor(color);
            x = x + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            y = y + 0.1f * random.nextInt(maxSize) * (random.nextFloat() - 0.5f);
            canvas.drawCircle(x, y, size - value * size, paint);
        }
    }
}
