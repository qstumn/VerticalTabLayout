package q.rorbin.verticaltablayoutdemo;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import q.rorbin.verticaltablayout.TabAdapter;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab0();
        initTab1();
        initTab2();
        initTab3();
    }

    private void initTab0() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout0);
        tablayout.setTabAdapter(new MyTabAdapter());
    }

    private void initTab1() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout1);
        tablayout.setTabAdapter(new MyTabAdapter());
    }

    private void initTab2() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout2);
        tablayout.setTabAdapter(new MyTabAdapter());
    }

    private void initTab3() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyPagerAdapter());
        tablayout.setupWithViewPager(viewpager);
    }

    class MyTabAdapter implements TabAdapter {

        List<String> titles;

        {
            titles = new ArrayList<>();
            Collections.addAll(titles, "Android", "IOS", "Web", "JAVA", "C++",
                    ".NET", "JavaScript", "Swift", "PHP", "Python", "C#", "Groovy", "SQL", "Ruby");
        }

        @Override
        public int getCount() {
            return 14;
        }

        @Override
        public int getBadge(int position) {
            if (position == 5) return position;
            return 0;
        }

        @Override
        public QTabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public QTabView.TabTitle getTitle(int position) {
            return new QTabView.TabTitle.Builder(MainActivity.this)
                    .setContent(titles.get(position))
                    .setTextColor(Color.WHITE, Color.WHITE)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }
    }

    class MyPagerAdapter extends PagerAdapter implements TabAdapter{
        List<MenuBean> menus;

        public MyPagerAdapter() {
            menus = new ArrayList<>();
            Collections.addAll(menus, new MenuBean(R.drawable.man_01_pressed, R.drawable.man_01_none, "汇总")
                    , new MenuBean(R.drawable.man_02_pressed, R.drawable.man_02_none, "图表")
                    , new MenuBean(R.drawable.man_03_pressed, R.drawable.man_03_none, "收藏")
                    , new MenuBean(R.drawable.man_04_pressed, R.drawable.man_04_none, "竞拍"));
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int getBadge(int position) {
            return position * 1000;
        }

        @Override
        public QTabView.TabIcon getIcon(int position) {
            MenuBean menu = menus.get(position);
            return new QTabView.TabIcon.Builder()
                    .setIcon(menu.mSelectIcon, menu.mNormalIcon)
                    .setIconGravity(Gravity.LEFT)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(20), dp2px(20))
                    .build();
        }

        @Override
        public QTabView.TabTitle getTitle(int position) {
            MenuBean menu = menus.get(position);
            return new QTabView.TabTitle.Builder(MainActivity.this)
                    .setContent(menu.mTitle)
                    .setTextColor(0xFF36BC9B, 0xFF757575)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(MainActivity.this);
            tv.setText("" + position);
            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    protected int dp2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
