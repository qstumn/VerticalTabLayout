package q.rorbin.verticaltablayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chqiu on 2017/1/16.
 */

public class TabFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView view = new TextView(inflater.getContext());
        String text = getArguments().getString("index", "fragment");
        view.setText(text);
        view.setTextSize(30);
        return view;
    }
}
