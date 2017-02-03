package q.rorbin.verticaltablayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import q.rorbin.verticaltablayout.util.DisplayUtil;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */
public class QTabView extends TabView {
    private Context mContext;
    private ImageView mIcon;
    private TextView mTitle;
    private QBadgeView mBadgeView;
    //    private TextView mBadge;
    private int mMinHeight;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private TabBadge mTabBadge;
    private boolean mChecked;
    private LinearLayout mContainer;
    private GradientDrawable gd;
    private Drawable mBackground;


    public QTabView(Context context) {
        super(context);
        mContext = context;
        gd = new GradientDrawable();
        gd.setColor(0xFFE84E40);
        mMinHeight = DisplayUtil.dp2px(context, 30);
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder().build();
        mTabBadge = new TabBadge.Builder().build();
        initView();
        setDefaultBackground();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }

    private void initView() {
        initContainer();
        initIconView();
        initTitleView();
        addView(mContainer);
        initBadge();
    }

    private void initContainer() {
        mContainer = new LinearLayout(mContext);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setMinimumHeight(mMinHeight);
        mContainer.setPadding(DisplayUtil.dp2px(mContext, 5), DisplayUtil.dp2px(mContext, 5),
                DisplayUtil.dp2px(mContext, 5), DisplayUtil.dp2px(mContext, 5));
        mContainer.setGravity(Gravity.CENTER);
    }


    private void initBadge() {
        if (mBadgeView != null) removeView(mBadgeView);
        mBadgeView = QBadgeView.bindTab(mContext, this);
        mBadgeView.setBadgeBackgroundColor(mTabBadge.getColorBackground());
        mBadgeView.setBadgeGravity(mTabBadge.getBadgeGravity());
        mBadgeView.setBadgeNumber(mTabBadge.getBadgeNumber());
        mBadgeView.setBadgeNumberColor(mTabBadge.getColorBadgeNumber());
        mBadgeView.setBadgeNumberSize(mTabBadge.getBadgeNumberSize(), true);
        mBadgeView.setBadgePadding(mTabBadge.getBadgePadding(), true);
        mBadgeView.setExactMode(mTabBadge.isExactMode());
        mBadgeView.setGravityOffset(mTabBadge.getGravityOffset(), true);
        mBadgeView.setOnDragStateChangedListener(mTabBadge.getOnDragStateChangedListener());
    }

    private void initTitleView() {
        if (mTitle != null) mContainer.removeView(mTitle);
        mTitle = new TextView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTitle.setLayoutParams(params);
        mTitle.setTextColor(mTabTitle.getColorNormal());
        mTitle.setTextSize(mTabTitle.getTitleTextSize());
        mTitle.setText(mTabTitle.getContent());
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setSingleLine();
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        requestContainerLayout(mTabIcon.getIconGravity());
    }

    private void initIconView() {
        if (mIcon != null) mContainer.removeView(mIcon);
        mIcon = new ImageView(mContext);
        LayoutParams params = new LayoutParams(mTabIcon.getIconWidth(), mTabIcon.getIconHeight());
        mIcon.setLayoutParams(params);
        if (mTabIcon.getNormalIcon() != 0) {
            mIcon.setImageResource(mTabIcon.getNormalIcon());
        } else {
            mIcon.setVisibility(View.GONE);
        }
        requestContainerLayout(mTabIcon.getIconGravity());
    }

//    @Override
//    public QTabView setBadge(int num) {
//        getBadgeView().setBadgeNumber(num);
//        return this;
//    }

    @Override
    public QTabView setBadge(TabBadge badge) {
        if (badge != null) {
            mTabBadge = badge;
        }
        initBadge();
        return this;
    }

    @Override
    public QTabView setIcon(TabIcon icon) {
        if (icon != null)
            mTabIcon = icon;
        initIconView();
        setChecked(mChecked);
        return this;
    }

    @Override
    public QTabView setTitle(TabTitle title) {
        if (title != null)
            mTabTitle = title;
        initTitleView();
        setChecked(mChecked);
        return this;
    }

    /**
     * @param resId The Drawable res to use as the background, if less than 0 will to remove the
     *              background
     */
    @Override
    public QTabView setBackground(int resId) {
        if (resId == 0) {
            setDefaultBackground();
        } else if (resId <= 0) {
            setBackground(null);
        } else {
            mContainer.setBackgroundResource(resId);
        }
        return this;
    }

    @Override
    public TabBadge getBadge() {
        return mTabBadge;
    }

    @Override
    public TabIcon getIcon() {
        return mTabIcon;
    }

    @Override
    public TabTitle getTitle() {
        return mTabTitle;
    }

    @Override
    public ImageView getIconView() {
        return mIcon;
    }

    @Override
    public TextView getTitleView() {
        return mTitle;
    }

    @Override
    public Badge getBadgeView() {
        return mBadgeView;
    }

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.setBackground(background);
        } else {
            mContainer.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        setBackground(resid);
    }

    private void setDefaultBackground() {
        if (mBackground == null) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs);
            mBackground = a.getDrawable(0);
            a.recycle();
        }
        if (getBackground() != mBackground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContainer.setBackground(mBackground);
            } else {
                mContainer.setBackgroundDrawable(mBackground);
            }
        }
    }

    public Drawable getBackground() {
        return mContainer.getBackground();
    }

    private void requestContainerLayout(int gravity) {
        mContainer.removeAllViews();
        switch (gravity) {
            case Gravity.LEFT:
                mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, 0, mTabIcon.getMargin(), 0);
                    mIcon.setLayoutParams(lp);
                }
                if (mTitle != null)
                    mContainer.addView(mTitle);
                break;
            case Gravity.TOP:
                mContainer.setOrientation(LinearLayout.VERTICAL);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, 0, 0, mTabIcon.getMargin());
                    mIcon.setLayoutParams(lp);
                }
                if (mTitle != null)
                    mContainer.addView(mTitle);
                break;
            case Gravity.RIGHT:
                mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (mTitle != null)
                    mContainer.addView(mTitle);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(mTabIcon.getMargin(), 0, 0, 0);
                    mIcon.setLayoutParams(lp);
                }

                break;
            case Gravity.BOTTOM:
                mContainer.setOrientation(LinearLayout.VERTICAL);
                if (mTitle != null)
                    mContainer.addView(mTitle);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, mTabIcon.getMargin(), 0, 0);
                    mIcon.setLayoutParams(lp);
                }
                break;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        refreshDrawableState();
        if (mChecked) {
            mTitle.setTextColor(mTabTitle.getColorSelected());
            if (mTabIcon.getSelectedIcon() != 0) {
                mIcon.setVisibility(View.VISIBLE);
                mIcon.setImageResource(mTabIcon.getSelectedIcon());
            } else {
                mIcon.setVisibility(View.GONE);
            }
        } else {
            mTitle.setTextColor(mTabTitle.getColorNormal());
            if (mTabIcon.getNormalIcon() != 0) {
                mIcon.setVisibility(View.VISIBLE);
                mIcon.setImageResource(mTabIcon.getNormalIcon());
            } else {
                mIcon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        mContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClick(QTabView.this);
            }
        });
    }


}