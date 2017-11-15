package com.game.siwasu17.donutshole;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.game.siwasu17.donutshole.fragments.HomeFragment;

import java.util.HashMap;
import java.util.Map;


public class MainActivity
        extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener {

    private Map<String, Fragment> fragmentMap = new HashMap<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                hideAllFragments();
                showFragment(HomeFragment.MODE_HOME);
                return true;
            case R.id.navigation_dashboard:
                hideAllFragments();
                showFragment(HomeFragment.MODE_FAV);
                return true;
            case R.id.navigation_notifications:
                return true;
        }
        return false;
    };


    private void showFragment(String modeKey) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.show(fragmentMap.get(modeKey));
        transaction.commit();
    }

    private void hideAllFragments(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentMap.values()) {
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    private void registerAllFragments(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentMap.values()) {
            transaction.add(R.id.main_container, fragment);
        }
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMap.clear();
        fragmentMap.put(HomeFragment.MODE_HOME, HomeFragment.newInstance(HomeFragment.MODE_HOME));
        fragmentMap.put(HomeFragment.MODE_FAV, HomeFragment.newInstance(HomeFragment.MODE_FAV));

        //使用するFragmentを登録しておく
        registerAllFragments();
        hideAllFragments();
        showFragment(HomeFragment.MODE_HOME);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }


}
