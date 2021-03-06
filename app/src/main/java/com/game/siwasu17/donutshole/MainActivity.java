package com.game.siwasu17.donutshole;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.game.siwasu17.donutshole.fragments.HomeFragment;


public class MainActivity
        extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener{

    HomeFragment defaultHomeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if(defaultHomeFragment == null){
                            defaultHomeFragment = HomeFragment.newInstance(HomeFragment.MODE_HOME);
                        }
                        setFragment(defaultHomeFragment);
                        return true;
                    case R.id.navigation_dashboard:
                        setFragment(HomeFragment.newInstance(HomeFragment.MODE_FAV));
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            };

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //デフォルトはHome
        defaultHomeFragment = HomeFragment.newInstance(HomeFragment.MODE_HOME);
        setFragment(defaultHomeFragment);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // 検索バーを追加(必須)
        /*
        //TODO: あとで直す
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.search_menu_search_view);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchWord(query);
                callTiqavService();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        */

        return true;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {


    }


}
