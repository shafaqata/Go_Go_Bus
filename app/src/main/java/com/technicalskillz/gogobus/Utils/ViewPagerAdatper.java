package com.technicalskillz.gogobus.Utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.technicalskillz.gogobus.Fragments.FavouriteFragment;
import com.technicalskillz.gogobus.Fragments.NearbyFragment;
import com.technicalskillz.gogobus.Fragments.SearchFragment;

public class ViewPagerAdatper extends FragmentPagerAdapter {
    Context ctx;

    public ViewPagerAdatper(@NonNull FragmentManager fm,Context context, int behavior) {
        super(fm, behavior);
        ctx=context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NearbyFragment(ctx);
//            return new FavouriteFragment();
        }
        if (position == 1) {
            return new SearchFragment(ctx);
        }
//        if (position == 2) {
//
//        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
//            return "Favorite";
            return "NearBy";
        }
        if (position == 1) {
            return "Search";
        }
//        if (position == 2) {
//
//        }
        return null;
    }
}
