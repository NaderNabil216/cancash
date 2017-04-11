package com.android.example.trash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by NaderNabil on 2/13/2017.
 */

public class myAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> arrayList ;
    public myAdapter(FragmentManager fm , ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList=arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String s="";
        if (position==0){
            s="Schedule";
        }else if(position==1){
            s="Profile";
        }
        return s;
    }
}

