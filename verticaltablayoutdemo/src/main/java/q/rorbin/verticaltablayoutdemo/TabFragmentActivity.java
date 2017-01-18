package q.rorbin.verticaltablayoutdemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.TabAdapter;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;

import static q.rorbin.verticaltablayoutdemo.R.id.fragment_container;

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
                    public int getBadge(int position) {
                        return 0;
                    }

                    @Override
                    public QTabView.TabIcon getIcon(int position) {
                        return null;
                    }

                    @Override
                    public QTabView.TabTitle getTitle(int position) {
                        return new QTabView.TabTitle.Builder(getApplicationContext()).setContent(String.valueOf(position)).build();
                    }

                    @Override
                    public int getBackground(int position) {
                        return 0;
                    }
                });
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
