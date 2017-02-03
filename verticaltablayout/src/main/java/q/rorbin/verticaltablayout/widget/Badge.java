package q.rorbin.verticaltablayout.widget;

import android.graphics.PointF;
import android.view.View;

/**
 * Created by chqiu on 2017/1/20.
 */

public interface Badge {

    void setBadgeNumber(int badgeNum);

    int getBadgeNumber();

    void setExactMode(boolean isExact);

    boolean isExactMode();

    void setBadgeBackgroundColor(int color);

    int getBadgeBackgroundColor();

    void setBadgeNumberColor(int color);

    int getBadgeNumberColor();

    void setBadgeNumberSize(float size, boolean isSpValue);

    float getBadgeNumberSize(boolean isSpValue);

    void setBadgePadding(float padding, boolean isDpValue);

    float getBadgePadding(boolean isDpValue);

    boolean isDraggable();

    void setBadgeGravity(int gravity);

    int getBadgeGravity();

    void setGravityOffset(int offset, boolean isDpValue);

    int getGravityOffset(boolean isDpValue);

    void setOnDragStateChangedListener(OnDragStateChangedListener l);

    PointF getDragCenter();

    Badge bindTarget(View view);

    void hide(boolean animate);

    interface OnDragStateChangedListener {
        int STATE_START = 1;
        int STATE_DRAGGING = 2;
        int STATE_DRAGGING_OUT_OF_RANGE = 3;
        int STATE_CANCELED = 4;
        int STATE_SUCCEED = 5;

        void onDragStateChanged(int dragState, Badge badge, View targetView);
    }
}
