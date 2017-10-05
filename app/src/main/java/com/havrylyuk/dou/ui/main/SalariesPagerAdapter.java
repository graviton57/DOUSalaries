
package com.havrylyuk.dou.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.havrylyuk.dou.ui.main.by_cities.ByCitiesFragment;
import com.havrylyuk.dou.ui.main.by_years.ByYearsFragment;
import com.havrylyuk.dou.ui.main.demographics.DemographicFragment;
import com.havrylyuk.dou.ui.main.widget.SalaryWidgetFragment;

import javax.inject.Inject;

/**
 * Created by Igor Havrylyuk on 21.09.2017.
 */

public class SalariesPagerAdapter extends FragmentPagerAdapter {

    private String[] pages;

    @Inject
    public SalariesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPages(String[] pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages != null ? pages.length : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return SalaryWidgetFragment.newInstance();
            case 1:
                return ByYearsFragment.newInstance();
            case 2:
                return ByCitiesFragment.newInstance();
            case 3:
                return DemographicFragment.newInstance();
            default:
                return null;
        }
    }

}
