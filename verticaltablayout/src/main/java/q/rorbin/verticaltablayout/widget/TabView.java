package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import q.rorbin.badgeview.Badge;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */
public abstract class TabView extends FrameLayout implements Checkable {

    public TabView(Context context) {
        super(context);
    }

    public abstract TabView setBadge(TabBadge badge);

    public abstract TabView setIcon(TabIcon icon);

    public abstract TabView setTitle(TabTitle title);

    public abstract TabView setBackground(int resid);

    public abstract TabBadge getBadge();

    public abstract TabIcon getIcon();

    public abstract TabTitle getTitle();

    public abstract ImageView getIconView();

    public abstract TextView getTitleView();

    public abstract Badge getBadgeView();

    public static class TabIcon {

        private Builder mBuilder;

        public TabIcon(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getSelectedIcon() {
            return mBuilder.mSelectedIcon;
        }

        public int getNormalIcon() {
            return mBuilder.mNormalIcon;
        }

        public int getIconGravity() {
            return mBuilder.mIconGravity;
        }

        public int getIconWidth() {
            return mBuilder.mIconWidth;
        }

        public int getIconHeight() {
            return mBuilder.mIconHeight;
        }

        public int getMargin() {
            return mBuilder.mMargin;
        }

        public static class Builder {
            private int mSelectedIcon;
            private int mNormalIcon;
            private int mIconGravity;
            private int mIconWidth;
            private int mIconHeight;
            private int mMargin;

            public Builder() {
                mSelectedIcon = 0;
                mNormalIcon = 0;
                mIconWidth = LayoutParams.WRAP_CONTENT;
                mIconHeight = LayoutParams.WRAP_CONTENT;
                mIconGravity = Gravity.START;
                mMargin = 0;
            }

            public Builder setIcon(int selectIconResId, int normalIconResId) {
                mSelectedIcon = selectIconResId;
                mNormalIcon = normalIconResId;
                return this;
            }

            public Builder setIconSize(int width, int height) {
                mIconWidth = width;
                mIconHeight = height;
                return this;
            }

            public Builder setIconGravity(int gravity) {
                if (gravity != Gravity.START && gravity != Gravity.END
                        & gravity != Gravity.TOP & gravity != Gravity.BOTTOM) {
                    throw new IllegalStateException("iconGravity only support Gravity.START " +
                            "or Gravity.END or Gravity.TOP or Gravity.BOTTOM");
                }
                mIconGravity = gravity;
                return this;
            }

            public Builder setIconMargin(int margin) {
                mMargin = margin;
                return this;
            }

            public TabIcon build() {
                return new TabIcon(this);
            }
        }
    }

    public static class TabTitle {
        private Builder mBuilder;

        public TabTitle(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getColorSelected() {
            return mBuilder.mColorSelected;
        }

        public int getColorNormal() {
            return mBuilder.mColorNormal;
        }

        public int getTitleTextSize() {
            return mBuilder.mTitleTextSize;
        }

        public String getContent() {
            return mBuilder.mContent;
        }

        public static class Builder {
            private int mColorSelected;
            private int mColorNormal;
            private int mTitleTextSize;
            private String mContent;

            public Builder() {
                this.mColorSelected = 0xFFFF4081;
                this.mColorNormal = 0xFF757575;
                this.mTitleTextSize = 16;
                this.mContent = "title";
            }

            public Builder setTextColor(int colorSelected, int colorNormal) {
                mColorSelected = colorSelected;
                mColorNormal = colorNormal;
                return this;
            }

            public Builder setTextSize(int sizeSp) {
                mTitleTextSize = sizeSp;
                return this;
            }

            public Builder setContent(String content) {
                mContent = content;
                return this;
            }

            public TabTitle build() {
                return new TabTitle(this);
            }
        }
    }

    public static class TabBadge {
        private Builder mBuilder;

        private TabBadge(Builder mBuilder) {
            this.mBuilder = mBuilder;
        }

        public int getBackgroundColor() {
            return mBuilder.colorBackground;
        }

        public int getBadgeNumberColor() {
            return mBuilder.colorBadgeNumber;
        }

        public float getBadgeNumberSize() {
            return mBuilder.badgeNumberSize;
        }

        public float getBadgePadding() {
            return mBuilder.badgePadding;
        }

        public int getBadgeNumber() {
            return mBuilder.badgeNumber;
        }

        public int getBadgeGravity() {
            return mBuilder.badgeGravity;
        }

        public int getGravityOffset() {
            return mBuilder.gravityOffset;
        }

        public boolean isExactMode() {
            return mBuilder.exactMode;
        }

        public boolean isShowShadow() {
            return mBuilder.showShadow;
        }

        public Badge.OnDragStateChangedListener getOnDragStateChangedListener() {
            return mBuilder.dragStateChangedListener;
        }

        public static class Builder {
            private int colorBackground;
            private int colorBadgeNumber;
            private float badgeNumberSize;
            private float badgePadding;
            private int badgeNumber;
            private int badgeGravity;
            private int gravityOffset;
            private boolean exactMode;
            private boolean showShadow;
            private Badge.OnDragStateChangedListener dragStateChangedListener;

            public Builder() {
                colorBackground = 0xFFE84E40;
                colorBadgeNumber = 0xFFFFFFFF;
                badgeNumberSize = 10;
                badgePadding = 4f;
                badgeNumber = 0;
                badgeGravity = Gravity.END | Gravity.TOP;
                gravityOffset = 5;
                exactMode = false;
                showShadow = true;
            }

            public TabBadge build() {
                return new TabBadge(this);
            }

            public Builder setShowShadow(boolean showShadow) {
                this.showShadow = showShadow;
                return this;
            }

            public Builder setOnDragStateChangedListener(Badge.OnDragStateChangedListener dragStateChangedListener) {
                this.dragStateChangedListener = dragStateChangedListener;
                return this;
            }

            public Builder setExactMode(boolean exactMode) {
                this.exactMode = exactMode;
                return this;
            }

            public Builder setBackgroundColor(int colorBackground) {
                this.colorBackground = colorBackground;
                return this;
            }

            public Builder setBadgeNumberColor(int colorBadgeNumber) {
                this.colorBadgeNumber = colorBadgeNumber;
                return this;
            }

            public Builder setBadgeNumberSize(float spValue) {
                this.badgeNumberSize = spValue;
                return this;
            }

            public Builder setBadgePadding(float dpValue) {
                this.badgePadding = dpValue;
                return this;
            }

            public Builder setBadgeNumber(int badgeNumber) {
                this.badgeNumber = badgeNumber;
                return this;
            }

            /**
             * @param badgeGravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
             *                     Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER
             */
            public Builder setBadgeGravity(int badgeGravity) {
                this.badgeGravity = badgeGravity;
                return this;
            }

            public Builder setGravityOffset(int dpValue) {
                this.gravityOffset = dpValue;
                return this;
            }
        }
    }
}
