package q.rorbin.verticaltablayoutdemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class TabFragmentActivity extends AppCompatActivity {
    private VerticalTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);
        tabLayout = (VerticalTabLayout) findViewById(R.id.tablayout);
        List<Fragment> fragments = getFragments();
        tabLayout.setupWithFragment(getSupportFragmentManager(), R.id.fragment_container, fragments
                , new TabAdapter() {
                    @Override
                    public int getCount() {
                        return 10;
                    }

                    @Override
                    public QTabView.TabBadge getBadge(int position) {
                        return null;
                    }

                    @Override
                    public QTabView.TabIcon getIcon(int position) {
                        return null;
                    }

                    @Override
                    public QTabView.TabTitle getTitle(int position) {
                        return new QTabView.TabTitle.Builder().setContent(String.valueOf(position)).build();
                    }

                    @Override
                    public int getBackground(int position) {
                        return 0;
                    }
                });
        tabLayout.setTabSelected(6);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("index", String.valueOf(i));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        return fragments;
    }
}
