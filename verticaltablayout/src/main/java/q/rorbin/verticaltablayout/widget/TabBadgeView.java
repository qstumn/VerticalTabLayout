package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by chqiu on 2017/2/10.
 */

public class TabBadgeView extends QBadgeView {
    public TabBadgeView(Context context) {
        super(context);
    }

    public TabBadgeView bindTab(TabView tab) {
        tab.addView(this, new TabView.LayoutParams(TabView.LayoutParams.MATCH_PARENT, TabView.LayoutParams.MATCH_PARENT));
        mTargetView = tab;
        return this;
    }

    @Override
    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            if (mTargetView instanceof TabView) {
                bindTab((TabView) mTargetView);
            } else {
                bindTarget(mTargetView);
            }
        }
    }
}
