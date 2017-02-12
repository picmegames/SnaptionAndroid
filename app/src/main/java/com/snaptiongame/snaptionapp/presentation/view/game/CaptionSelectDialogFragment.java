package com.snaptiongame.snaptionapp.presentation.view.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

public class CaptionSelectDialogFragment extends DialogFragment implements GameContract.View {
    enum CaptionDialogToShow {
        SET_CHOOSER, CAPTION_CHOOSER
    }

    private AlertDialog.Builder mDialogBuilder;

    /**
     * Title of a dialog that changes, depending on which dialog is shown
     */
    private String mDialogTitle;

    /**
     * Header icon associated with a dialog title. Changes depending on which dialog is shown
     */
    private int mHeaderIcon;

    private CaptionDialogToShow mDialogToShow;

    /**
     * Member variables used to create the two buttons at the bottom of a dialog
     */
    private String sNegativeButtonText;
    private String sPositiveButtonText;

    // TODO Make these string resource files
    private String SUMBIT = "Submit!";
    private String CANCEL = "Cancel";
    private String CREATE_A_CAPTION = "Create a Caption!";
    private String CHOOSE_A_SET = "Choose one of your Caption Sets!";

    /**
     * Recycler view used to hold the results of a search query run by the user
     */
    private RecyclerView mResults;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private View mDialogView;

    private FITBCaptionAdapter mFitBAdapter;
    private ArrayList<FitBCaption> mFitBCaptions = new ArrayList<>();
    private GameContract.Presenter mPresenter;
    private int mGameId;

    public CaptionSelectDialogFragment() {
    }

    static CaptionSelectDialogFragment newInstance(CaptionDialogToShow dialogToShow, int gameId) {
        CaptionSelectDialogFragment newFragment = new CaptionSelectDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("whichDialog", dialogToShow);
        args.putInt("gameId", gameId);
        newFragment.setArguments(args);

        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sPositiveButtonText = SUMBIT;
        sNegativeButtonText = CANCEL;
        mDialogToShow = (CaptionDialogToShow) getArguments().getSerializable("whichDialog");
        mGameId = getArguments().getInt("gameId");

        if (mDialogToShow == CaptionDialogToShow.SET_CHOOSER) {
            mDialogTitle = CHOOSE_A_SET;

        }
        else {
            mDialogTitle = CREATE_A_CAPTION;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mFitBAdapter = new FITBCaptionAdapter(new ArrayList<>());
        mPresenter = new GamePresenter(0, this);

        //mPresenter.loadFitBCaptions();
        mDialogBuilder = new AlertDialog.Builder(getActivity());
        mFitBAdapter = new FITBCaptionAdapter(mFitBCaptions);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        mDialogBuilder.setTitle(mDialogTitle);

        mDialogBuilder.setPositiveButton(sPositiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //addCaption("lalala", 1);
                System.out.println("ASJDASJDHASHDHSA");
            }
        });
        mDialogBuilder.setNegativeButton(sNegativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        //Build view for set chooser
        if (mDialogToShow == CaptionDialogToShow.SET_CHOOSER) {
            RecyclerView captionSetView = (RecyclerView)
                    inflater.inflate(R.layout.caption_set_holder, null);

            GridLayoutManager g = new GridLayoutManager(getActivity().getApplicationContext(), 3);
            captionSetView.setAdapter(new CaptionSetAdapter(new ArrayList<>()));
            captionSetView.setLayoutManager(g);

            mDialogBuilder.setView(captionSetView);
        }
        //Build view for caption chooser
        else {

            mDialogView = inflater.inflate(R.layout.caption_chooser_dialog, null);

            mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

            ((RecyclerView) mDialogView.findViewById(R.id.caption_card_holder)).setLayoutManager(mLinearLayoutManager);

            ((RecyclerView) mDialogView.findViewById(R.id.caption_card_holder)).setAdapter(mFitBAdapter);
            mDialogBuilder.setView(mDialogView);
        }


        return mDialogBuilder.create();
    }

/*
    @Override
    public void onResume() {
        super.onPause();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showFitBCaptions(List<FitBCaption> captions) {
        mFitBAdapter.clearCaptions();

        mFitBAdapter.setCaptions(captions);
    }

    @Override
    public void addCaption(String userEntry, int fitBId) {
        System.out.println("AddCapttion");
        mPresenter.addCaption(userEntry, fitBId);
    }

    @Override
    public void setPresenter(FitBCaptionContract.Presenter presenter) {
        mPresenter = presenter;
    }
*/

    @Override
    public void showCaptions(List<Caption> captions) {

    }

    @Override
    public void addCaption(Caption caption) {

    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {

    }
}
