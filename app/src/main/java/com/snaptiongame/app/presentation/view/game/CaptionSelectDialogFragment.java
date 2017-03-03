package com.snaptiongame.app.presentation.view.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.FitBCaption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickromero on 2/7/17.
 */

public class CaptionSelectDialogFragment extends DialogFragment implements GameContract.CaptionDialogView,
        CaptionContract.CaptionSetClickListener, CaptionContract.CaptionClickListener {


    private static final int FITB_OFFSET = 1;
    public static final String FITB_PLACEHOLDER = "______";

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
    private static final String SUBMIT = "Submit!";
    private static final String CANCEL = "Cancel";
    private static final String CREATE_A_CAPTION = "Create a Caption!";
    private static final String CHOOSE_A_SET = "Choose one of your Caption Sets!";

    /**
     * Recycler view used to hold the results of a search query run by the user
     */
    private LinearLayoutManager mLinearLayoutManager;
    private View mDialogView;
    private TextInputLayout fitBEditTextLayout;
    private TextInputEditText fitBEditText;

    private FITBCaptionAdapter mFitBAdapter;
    private CaptionSetAdapter mCaptionSetAdapter;

    private GameContract.Presenter mPresenter;
    private int mGameId;
    private int mSetId;

    private int curFitbPos;
    private TextWatcher captionClickListener;

    public CaptionSelectDialogFragment() {
    }

    static CaptionSelectDialogFragment newInstance(CaptionDialogToShow dialogToShow, int gameId, int setId) {
        CaptionSelectDialogFragment newFragment = new CaptionSelectDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("whichDialog", dialogToShow);
        args.putInt("gameId", gameId);
        args.putInt("setId", setId);

        newFragment.setArguments(args);

        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sPositiveButtonText = SUBMIT;
        sNegativeButtonText = CANCEL;
        mDialogToShow = (CaptionDialogToShow) getArguments().getSerializable("whichDialog");
        mGameId = getArguments().getInt("gameId");
        mSetId = getArguments().getInt("setId");

        if (mDialogToShow == CaptionDialogToShow.SET_CHOOSER) {
            mDialogTitle = CHOOSE_A_SET;

        } else {
            mDialogTitle = CREATE_A_CAPTION;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mPresenter = new GamePresenter(mGameId, this);

        mDialogBuilder = new AlertDialog.Builder(getActivity());
        mFitBAdapter = new FITBCaptionAdapter(new ArrayList<>(), this,
                getActivity().getLayoutInflater());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        mDialogBuilder.setTitle(mDialogTitle);

        if (mDialogToShow == CaptionDialogToShow.CAPTION_CHOOSER) {
            mDialogBuilder.setPositiveButton(sPositiveButtonText, (DialogInterface dialog, int which) -> {
                String userText = ((TextInputEditText)
                        fitBEditTextLayout.findViewById(R.id.fitbEditText)).getText().toString();

                mPresenter.addCaption(mFitBAdapter.getCaption(curFitbPos).id, userText);
            });
        }
        mDialogBuilder.setNegativeButton(sNegativeButtonText, (DialogInterface dialog, int which) -> {
            ((GameActivity) getActivity()).negativeButtonClicked(mDialogToShow);
        });


        //Build view for set chooser
        if (mDialogToShow == CaptionDialogToShow.SET_CHOOSER) {
            RecyclerView captionSetView = (RecyclerView)
                    inflater.inflate(R.layout.caption_set_holder, null);

            mCaptionSetAdapter = new CaptionSetAdapter(new ArrayList<>(), this);

            mPresenter.loadCaptionSets();
            GridLayoutManager g = new GridLayoutManager(getActivity().getApplicationContext(), 2);
            captionSetView.setAdapter(mCaptionSetAdapter);


            captionSetView.setLayoutManager(g);

            mDialogBuilder.setView(captionSetView);

        }
        //Build view for caption chooser
        else {
            mDialogView = inflater.inflate(R.layout.caption_chooser_dialog, null);
            GridView captionView = ((GridView) mDialogView.findViewById(R.id.caption_card_holder));
            mPresenter.loadRandomFITBCaptions();

            mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);




            captionView.setAdapter(mFitBAdapter);

            SnapHelper helper = new LinearSnapHelper();


            mDialogBuilder.setView(mDialogView);
            fitBEditTextLayout = (TextInputLayout) mDialogView.findViewById(R.id.fitbEditTextLayout);
            fitBEditText = (TextInputEditText) fitBEditTextLayout.findViewById(R.id.fitbEditText);

        }

        return mDialogBuilder.create();
    }


    @Override
    public void showFitBCaptions(List<FitBCaption> captions) {
        mFitBAdapter.setCaptions(captions);
    }

    @Override
    public void showRandomCaptions(List<FitBCaption> captions) {
        mFitBAdapter.setCaptions(captions);
    }


    @Override
    public void showCaptionSets(List<CaptionSet> captionSets) {
        mCaptionSetAdapter.setCaptionSets(captionSets);
    }

    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void captionSetClicked(View v, int position) {
        ((GameActivity) getActivity()).displayCaptionChoosingDialog(position);
    }

    @Override
    public void captionClicked(View v, int position) {
        curFitbPos = position;
        fitBEditTextLayout.setVisibility(View.VISIBLE);

        String[] textPieces = ((TextView)
                v.findViewById(R.id.fitb_caption_card_text)).getText().toString().split(FITB_PLACEHOLDER);

        final String beforeText = textPieces[0];
        String afterText = "";
        if (textPieces.length >= 2)
            afterText = textPieces[1];

        String finalAfterText = afterText;


        if (captionClickListener != null)
            fitBEditText.removeTextChangedListener(captionClickListener);


        captionClickListener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((TextView)
                        v.findViewById(R.id.fitb_caption_card_text)).setText("");
                ((TextView)
                        v.findViewById(R.id.fitb_caption_card_text)).setText(beforeText + s + finalAfterText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        fitBEditText.addTextChangedListener(captionClickListener);
        fitBEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(fitBEditText, InputMethodManager.SHOW_IMPLICIT);
    }





}
