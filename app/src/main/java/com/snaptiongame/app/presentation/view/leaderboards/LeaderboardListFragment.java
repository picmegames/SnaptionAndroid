package com.snaptiongame.app.presentation.view.leaderboards;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Tyler Wong
 */

public class LeaderboardListFragment extends Fragment {

    public static final String TYPE = "type";

    public static final String TAG = LeaderboardListFragment.class.getSimpleName();

    public static LeaderboardListFragment getInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        LeaderboardListFragment leaderboardListFragment = new LeaderboardListFragment();
        leaderboardListFragment.setArguments(args);
        return leaderboardListFragment;
    }
}
