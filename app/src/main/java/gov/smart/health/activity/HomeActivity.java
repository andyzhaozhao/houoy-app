package gov.smart.health.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import gov.smart.health.R;
import gov.smart.health.fragment.FindFragment;
import gov.smart.health.fragment.MessageFragment;
import gov.smart.health.fragment.SelfFragment;
import gov.smart.health.fragment.VRFragment;

public class HomeActivity extends AppCompatActivity {

    private  TabHost mTabHost;
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mInflater = getLayoutInflater();

        mTabHost =(TabHost) findViewById(R.id.home_tabHost);
        mTabHost.setup();
        TabWidget tabWidget = mTabHost.getTabWidget();

        mTabHost.addTab(mTabHost.newTabSpec("Sport").setIndicator(getItemView("运动",android.R.drawable.ic_menu_agenda)).setContent(android.R.id.tabcontent));
        mTabHost.addTab(mTabHost.newTabSpec("Find").setIndicator(getItemView("发现",android.R.drawable.ic_menu_search)).setContent(android.R.id.tabcontent));
        mTabHost.addTab(mTabHost.newTabSpec("Message").setIndicator(getItemView("消息",android.R.drawable.ic_menu_info_details)).setContent(android.R.id.tabcontent));
        mTabHost.addTab(mTabHost.newTabSpec("Self").setIndicator(getItemView("我的",android.R.drawable.ic_menu_myplaces)).setContent(android.R.id.tabcontent));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View view = mTabHost.getCurrentTabView().findViewById(R.id.home_tab_ll);
                view.setSelected(true);
                FragmentManager fm= getSupportFragmentManager();
                Fragment fragment = null;
                switch (tabId){
                    case "Sport":
                        fragment=new VRFragment();
                        break;
                    case "Find":
                        fragment=new FindFragment();
                        break;
                    case "Message":
                        fragment=new MessageFragment();
                        break;
                    case "Self":
                        fragment=new SelfFragment();
                        break;
                }
                fm.beginTransaction().replace(android.R.id.tabcontent,fragment).commit();
            }
        });

        mTabHost.setCurrentTab(1);
    }

    private View getItemView(String text,int resId){
        View tabView = mInflater.inflate(R.layout.home_tab_item, null);
        TextView tabText = (TextView)tabView.findViewById(R.id.tab_text);
        tabText.setText(text);
        ImageView tabImg = (ImageView)tabView.findViewById(R.id.tab_img);
        tabImg.setImageResource(resId);
        return tabView;
    }
}
