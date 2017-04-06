package com.snaptiongame.app.presentation.view.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;

/**
 * Created by nickromero on 4/5/17.
 */

public class CaptionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.caption_chooser_view, container, false);

        return rootView;

    }

}
