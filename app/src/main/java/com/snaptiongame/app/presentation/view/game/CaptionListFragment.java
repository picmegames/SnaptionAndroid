package com.snaptiongame.app.presentation.view.game;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Caption;

import java.util.List;

public class CaptionListFragment extends Fragment implements GameContract.View {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.caption_list_activity, container, false);

        return rootView;

    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {

    }

    @Override
    public void showCaptions(List<Caption> captions) {

    }

    @Override
    public void addCaption(Caption caption) {

    }

    @Override
    public void setPickerInfo(String profileUrl, String name) {

    }

    @Override
    public void generateInviteUrl(String inviteToken) {

    }
}
