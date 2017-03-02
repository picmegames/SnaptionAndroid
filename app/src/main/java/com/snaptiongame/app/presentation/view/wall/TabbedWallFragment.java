package com.snaptiongame.app.presentation.view.wall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class TabbedWallFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;
    private WallPageAdapter mWallPageAdapter;
    private ActionBar mActionBar;

    private static final int TAB_ICONS[] = new int[] {
            R.drawable.ic_face_white_24dp, R.drawable.ic_explore_white_24dp, R.drawable.ic_whatshot_white_24dp};

    public static final String TAG = TabbedWallFragment.class.getSimpleName();

    public static TabbedWallFragment getInstance() {
        return new TabbedWallFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tabbed_wall_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mWallPageAdapter = new WallPageAdapter((AppCompatActivity) getActivity(), getChildFragmentManager());
        mViewPager.setAdapter(mWallPageAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        for (int index = 0; index < mTabLayout.getTabCount(); index++) {
            TabLayout.Tab currentTab = mTabLayout.getTabAt(index);

            if (currentTab != null) {
                currentTab.setIcon(TAB_ICONS[index]);
            }
        }

        int white = ContextCompat.getColor(getContext(), android.R.color.white);
        mTabLayout.setTabTextColors(white, white);

        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.my_wall);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mActionBar.setTitle(R.string.my_wall);
                        break;
                    case 1:
                        mActionBar.setTitle(R.string.discover);
                        break;
                    case 2:
                        mActionBar.setTitle(R.string.popular);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
