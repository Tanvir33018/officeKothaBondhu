package net.islbd.kothabondhu.ui.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.ui.fragment.MyAccountFragment;
import net.islbd.kothabondhu.ui.fragment.AgentListFragment;
import net.islbd.kothabondhu.ui.fragment.PackageListFragment;

/**
 * Created by wahid.sadique on 8/30/2017.
 */

public class HomeTabAdapter extends FragmentPagerAdapter {
    private Context context;

    public HomeTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AgentListFragment();
                break;
            case 1:
                fragment = new MyAccountFragment();
                break;
            case 2:
                fragment = new PackageListFragment();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.agents_tab);
            case 1:
                return context.getString(R.string.my_account_tab);
            case 2:
                return context.getString(R.string.packages_tab);
            /*case 3:
                return context.getString(R.string.log_tab);*/
            default:
                return null;
        }
    }
}
