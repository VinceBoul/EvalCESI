package com.vince.evalcesi.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vince.evalcesi.fragments.OneFragment;
import com.vince.evalcesi.fragments.PlusOneFragment;

/**
 * Created by Vince on 09/11/2016.
 */

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    Bundle bundle;

    public TabsPagerAdapter(FragmentManager fm, String token) {
        super(fm);
        bundle = new Bundle();
        bundle.putString("theToken", token);
        System.out.println(token);

    }

    @Override
    public Fragment getItem(int index) {
        Fragment currentFragment = null;
        switch (index) {
            case 0:
                currentFragment = new OneFragment();
                break;
            case 1:
                currentFragment = new PlusOneFragment();
                break;
        }

        currentFragment.setArguments(bundle);
        return currentFragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}