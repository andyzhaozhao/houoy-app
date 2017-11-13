package gov.smart.health.fragment.Splash;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import gov.smart.health.R;

/**
 * Created by laoniu on 11/13/17.
 */
public class SplashFragment  extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";

    public static SplashFragment newInstance(int IdRes) {
        SplashFragment frag = new SplashFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, IdRes);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_splash_fragment, null);
        ImageView image = (ImageView) view.findViewById(R.id.splash_image);
        image.setImageResource(getArguments().getInt(BACKGROUND_COLOR));
        return view;
    }
}