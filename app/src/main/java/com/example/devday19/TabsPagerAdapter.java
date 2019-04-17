package com.example.devday19;

/**
 * Created by Hp on 1/27/2019.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
               // RequestsFragment requestsFragment = new RequestsFragment();
                //return requestsFragment;
            SocialFragment socialFragment = new SocialFragment();
            return socialFragment;

            case 1:
                 NewsFragment newsFragment= new NewsFragment();
                return newsFragment;

             case 2:
                UpdateFragment updateFragmemt = new UpdateFragment();
                return updateFragmemt;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position){

            case 0:
                return "Social";

            case 1:
                return "Updates";

            case 2:
                return "News";

            default:
                return null;
        }
    }
}
