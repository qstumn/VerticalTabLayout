package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.util.BadgeAnimator;
import q.rorbin.verticaltablayout.util.DisplayUtil;

/**
 * Created by chqiu on 2017/1/20.
 */

public class QBadgeView extends View implements Badge {
    private int mColorBackground;
    private int mColorBadgeNumber;
    private float mBadgeNumberSize;
    private float mBadgePadding;
    private int mBadgeNumber;
    private String mBadgeText;
    private boolean mDraggable;
    private boolean mDragging;
    private boolean mExact;
    private int mBadgeGravity;
    private int mGravityOffset;

    private float mDefalutRadius;
    private float mFinalDragDistance;

    private boolean mDragOutOfRange;

    private Rect mBadgeNumberRect;
    private RectF mBadgeBackgroundRect;
    private Path mDragPath;

    private PointF mBdageCenter;
    private PointF mDragCenter;
    private PointF mRowBdageCenter;
    private PointF mControlPoint;

    private List<PointF> mInnertangentPoints;

    private View mTargetView;

    private int mWidth;
    private int mHeight;

    private TextPaint mBadgeNumberPaint;
    private Paint mBadgeBackgroundPaint;

    private BadgeAnimator mAnimator;

    private OnDragStateChangedListener mDragStateChangedListener;

    private ViewGroup mActivityRoot;

    public QBadgeView(Context context) {
        this(context, null);
    }

    private QBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private QBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBadgeNumberRect = new Rect();
        mBadgeBackgroundRect = new RectF();
        mDragPath = new Path();
        mBdageCenter = new PointF();
        mDragCenter = new PointF();
        mRowBdageCenter = new PointF();
        mControlPoint = new PointF();
        mInnertangentPoints = new ArrayList<>();
        mBadgeNumberPaint = new TextPaint();
        mBadgeNumberPaint.setAntiAlias(true);
        mBadgeNumberPaint.setFakeBoldText(true);
        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        mColorBackground = 0xFFE84E40;
        mColorBadgeNumber = 0xFFFFFFFF;
        mBadgeNumberSize = DisplayUtil.dp2px(getContext(), 10);
        mBadgePadding = DisplayUtil.dp2px(getContext(), 4f);
        mBadgeNumber = 0;
        mBadgeGravity = Gravity.END | Gravity.TOP;
        mGravityOffset = DisplayUtil.dp2px(getContext(), 5);
        mFinalDragDistance = DisplayUtil.dp2px(getContext(), 100);
    }

    public static QBadgeView bindTab(Context context, TabView tab) {
        QBadgeView badge = new QBadgeView(context);
        tab.addView(badge, new TabView.LayoutParams(TabView.LayoutParams.MATCH_PARENT, TabView.LayoutParams.MATCH_PARENT));
        badge.mTargetView = tab;
        return badge;
    }

    @Override
    public Badge bindTarget(View targetView) {
        if (targetView == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup) {
            mTargetView = targetView;
            if (targetParent instanceof FrameLayout) {
                ((FrameLayout) targetParent).addView(this);
            } else {
                ViewGroup targetContainer = (ViewGroup) targetParent;
                int index = targetContainer.indexOfChild(targetView);
                ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
                targetContainer.removeView(targetView);
                FrameLayout badgeContainer = new FrameLayout(getContext());
                badgeContainer.setLayoutParams(targetParams);
                targetView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                targetContainer.addView(badgeContainer, index, targetParams);
                badgeContainer.addView(targetView);
                badgeContainer.addView(this);
            }
        } else {
            throw new IllegalStateException("targetView must have a parent");
        }
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mActivityRoot == null) findActivityRoot(mTargetView);
    }

    private void findActivityRoot(View view) {
        if (view.getParent() != null && view.getParent() instanceof View) {
            findActivityRoot((View) view.getParent());
        } else if (view instanceof ViewGroup) {
            mActivityRoot = (ViewGroup) view;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mDraggable && event.getPointerId(event.getActionIndex()) == 0
                        && getPointDistance(mBdageCenter, new PointF(event.getX(), event.getY()))
                        <= DisplayUtil.dp2px(getContext(), 10) && mBadgeNumber != 0) {
                    mDragging = true;
                    updataListener(OnDragStateChangedListener.STATE_START);
                    mDefalutRadius = DisplayUtil.dp2px(getContext(), 7);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    int[] screen = new int[2];
                    getLocationOnScreen(screen);
                    mRowBdageCenter.x = mBdageCenter.x + screen[0];
                    mRowBdageCenter.y = mBdageCenter.y + screen[1];
                    screenFromWindow(true);
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerId(event.getActionIndex()) == 0 && mDragging) {
                    mDragging = false;
                    onPointerUp();
                }
                break;
        }
        return mDragging || super.onTouchEvent(event);
    }

    private void onPointerUp() {
        if (mDragOutOfRange) {
            animateHide(mDragCenter);
            updataListener(OnDragStateChangedListener.STATE_SUCCEED);
        } else {
            reset();
            updataListener(OnDragStateChangedListener.STATE_CANCELED);
        }
    }

    private Bitmap createBadgeBitmap() {
        Rect rect = new Rect();
        mBadgeNumberPaint.getTextBounds(mBadgeText.toCharArray(), 0, mBadgeText.length(), rect);
        Bitmap bitmap = Bitmap.createBitmap((int) (rect.width() + mBadgePadding * 2),
                (int) (rect.width() + mBadgePadding * 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, canvas.getWidth() / 2f, mBadgeBackgroundPaint);
        canvas.drawText(mBadgeText, canvas.getWidth() / 2f, canvas.getHeight() / 2f + rect.height() / 2f, mBadgeNumberPaint);
        return bitmap;
    }

    private void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this);
        } else {
            if (mTargetView instanceof TabView) {
                ((TabView) mTargetView).addView(this,
                        new TabView.LayoutParams(TabView.LayoutParams.MATCH_PARENT, TabView.LayoutParams.MATCH_PARENT));
            } else {
                bindTarget(mTargetView);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.draw(canvas);
            return;
        }
        if (mBadgeNumber != 0) {
            float badgeRadius = getBadgeCircleRadius();
            float startCircleRadius = mDefalutRadius * (1 - getPointDistance(mRowBdageCenter, mDragCenter) / mFinalDragDistance);
            if (mDraggable && mDragging) {
                if (mDragOutOfRange = startCircleRadius < DisplayUtil.dp2px(getContext(), 1.5f)) {
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING_OUT_OF_RANGE);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                } else {
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING);
                    drawDragging(canvas, startCircleRadius, badgeRadius);
                    drawDragStart(canvas, mRowBdageCenter, startCircleRadius);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                }
            } else {
                findBadgeCenter();
                drawBadge(canvas, mBdageCenter, getBadgeCircleRadius());
            }
        }
    }

    private void drawDragging(Canvas canvas, float startRadius, float badgeRadius) {
        float dy = mDragCenter.y - mRowBdageCenter.y;
        float dx = mDragCenter.x - mRowBdageCenter.x;
        mInnertangentPoints.clear();
        if (dx != 0) {
            double k1 = dy / dx;
            double k2 = -1 / k1;
            getInnertangentPoints(mDragCenter, badgeRadius, k2);
            getInnertangentPoints(mRowBdageCenter, startRadius, k2);
        } else {
            getInnertangentPoints(mDragCenter, badgeRadius, 0d);
            getInnertangentPoints(mRowBdageCenter, startRadius, 0d);
        }
        mDragPath.reset();
        mControlPoint.x = (mRowBdageCenter.x + mDragCenter.x) / 2.0f;
        mControlPoint.y = (mRowBdageCenter.y + mDragCenter.y) / 2.0f;
        mDragPath.moveTo(mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.lineTo(mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
        mDragPath.close();
        mBadgeBackgroundPaint.setColor(mColorBackground);
        canvas.drawPath(mDragPath, mBadgeBackgroundPaint);
    }

    private void drawDragStart(Canvas canvas, PointF startCenter, float radius) {
        canvas.drawCircle(startCenter.x, startCenter.y, radius, mBadgeBackgroundPaint);
    }

    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeNumberPaint.setColor(mColorBadgeNumber);
        mBadgeNumberPaint.setTextAlign(Paint.Align.CENTER);
        if (mBadgeNumber < 0) {
            canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
        } else if (mBadgeNumber <= 9) {//draw circle badge
            canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
            canvas.drawText(mBadgeText, center.x, center.y + mBadgeNumberRect.height() / 2f, mBadgeNumberPaint);
        } else {//>9draw rect badge
            float padding = mBadgeNumber <= 99 ? 1.2f : 1.0f;
            mBadgeBackgroundRect.left = center.x - (mBadgeNumberRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeNumberRect.height() / 2f + mBadgePadding / padding);
            mBadgeBackgroundRect.right = center.x + (mBadgeNumberRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeNumberRect.height() / 2f + mBadgePadding / padding);
            canvas.drawRoundRect(mBadgeBackgroundRect,
                    DisplayUtil.dp2px(getContext(), 10), DisplayUtil.dp2px(getContext(), 10),
                    mBadgeBackgroundPaint);
            canvas.drawText(mBadgeText, center.x, center.y + mBadgeNumberRect.height() / 2f, mBadgeNumberPaint);
        }
    }

    private float getBadgeCircleRadius() {
        float radius = mBadgeBackgroundRect.height() / 2f;
        if (mBadgeNumber < 0) {
            radius = mBadgePadding;
        } else if (mBadgeNumber <= 9) {
            radius = mBadgeNumberRect.height() > mBadgeNumberRect.width() ?
                    mBadgeNumberRect.height() / 2f + mBadgePadding : mBadgeNumberRect.width() / 2f + mBadgePadding;
        }
        return radius;
    }

    private void findBadgeCenter() {
        mBadgeNumberPaint.setTextSize(mBadgeNumberSize);
        char[] chars = mBadgeText.toCharArray();
        mBadgeNumberPaint.getTextBounds(chars, 0, chars.length, mBadgeNumberRect);
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBdageCenter.x = mGravityOffset + mBadgePadding + mBadgeNumberRect.width() / 2f;
                mBdageCenter.y = mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f;
                break;
            case Gravity.END | Gravity.TOP:
                mBdageCenter.x = mWidth - (mGravityOffset + mBadgePadding + mBadgeNumberRect.width() / 2f);
                mBdageCenter.y = mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBdageCenter.x = mGravityOffset + mBadgePadding + mBadgeNumberRect.width() / 2f;
                mBdageCenter.y = mHeight - (mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f);
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBdageCenter.x = mWidth - (mGravityOffset + mBadgePadding + mBadgeNumberRect.width() / 2f);
                mBdageCenter.y = mHeight - (mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBdageCenter.x = mWidth / 2f;
                mBdageCenter.y = mHeight / 2f;
                break;
        }
    }

    private void animateHide(PointF center) {
        if (mBadgeNumber == 0) {
            return;
        }
        if (mAnimator == null || !mAnimator.isRunning()) {
            mAnimator = BadgeAnimator.start(createBadgeBitmap(), center, this);
            setBadgeNumber(0);
        }
    }

    public void reset() {
        mDragCenter.x = -1000;
        mDragCenter.y = -1000;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }

    @Override
    public void hide(boolean animate) {
        if (animate) {
            animateHide(mBdageCenter);
        } else {
            setBadgeNumber(0);
        }
    }

    /**
     * @param badgeNumber equal to zero badge will be hidden, less than zero show dot
     */
    @Override
    public void setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        }
        invalidate();
    }

    @Override
    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    @Override
    public void setExactMode(boolean isExact) {
        mExact = isExact;
        setBadgeNumber(mBadgeNumber);
    }

    @Override
    public boolean isExactMode() {
        return mExact;
    }

    @Override
    public void setBadgeBackgroundColor(int color) {
        mColorBackground = color;
        invalidate();
    }

    @Override
    public int getBadgeBackgroundColor() {
        return mColorBackground;
    }

    @Override
    public void setBadgeNumberColor(int color) {
        mColorBadgeNumber = color;
        invalidate();
    }

    @Override
    public int getBadgeNumberColor() {
        return mColorBadgeNumber;
    }

    @Override
    public void setBadgeNumberSize(float size, boolean isSpValue) {
        mBadgeNumberSize = isSpValue ? DisplayUtil.dp2px(getContext(), size) : size;
        invalidate();
    }

    @Override
    public float getBadgeNumberSize(boolean isSpValue) {
        return isSpValue ? DisplayUtil.px2dp(getContext(), mBadgeNumberSize) : mBadgeNumberSize;
    }

    @Override
    public void setBadgePadding(float padding, boolean isDpValue) {
        mBadgePadding = isDpValue ? DisplayUtil.dp2px(getContext(), padding) : padding;
        invalidate();
    }

    @Override
    public float getBadgePadding(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mBadgePadding) : mBadgePadding;
    }

    @Override
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER
     */
    @Override
    public void setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER)) {
            mBadgeGravity = gravity;
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER");
        }
    }

    @Override
    public int getBadgeGravity() {
        return mBadgeGravity;
    }

    @Override
    public void setGravityOffset(int offset, boolean isDpValue) {
        mGravityOffset = isDpValue ? DisplayUtil.dp2px(getContext(), offset) : offset;
        invalidate();
    }

    @Override
    public int getGravityOffset(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mGravityOffset) : mGravityOffset;
    }

    private float getPointDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * this formula is designed by mabeijianxi
     * website : http://blog.csdn.net/mabeijianxi/article/details/50560361
     *
     * @param circleCenter The circle center point.
     * @param radius       The circle radius.
     * @param slopeLine    The slope of line which cross the pMiddle.
     */
    private void getInnertangentPoints(PointF circleCenter, float radius, Double slopeLine) {
        float radian, xOffset, yOffset;
        if (slopeLine != null) {
            radian = (float) Math.atan(slopeLine);
            xOffset = (float) (Math.cos(radian) * radius);
            yOffset = (float) (Math.sin(radian) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        mInnertangentPoints.add(new PointF(circleCenter.x + xOffset, circleCenter.y + yOffset));
        mInnertangentPoints.add(new PointF(circleCenter.x - xOffset, circleCenter.y - yOffset));
    }

    private void updataListener(int state) {
        if (mDragStateChangedListener != null)
            mDragStateChangedListener.onDragStateChanged(state, this, mTargetView);
    }

    @Override
    public void setOnDragStateChangedListener(OnDragStateChangedListener l) {
        mDraggable = l != null;
        mDragStateChangedListener = l;
    }

    @Override
    public PointF getDragCenter() {
        if (mDraggable && mDragging) return mDragCenter;
        return null;
    }
}
