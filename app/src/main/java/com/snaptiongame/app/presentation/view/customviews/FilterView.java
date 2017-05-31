package com.snaptiongame.app.presentation.view.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.snaptiongame.app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */

public class FilterView extends LinearLayout {

    @BindView(R.id.filter_view)
    NachoTextView mFilterTextView;
    @BindView(R.id.open_check)
    CheckBox mOpenCheck;
    @BindView(R.id.closed_check)
    CheckBox mClosedCheck;

    private Context mContext;

    private static final String OPEN = "open";
    private static final String CLOSED = "closed";

    public FilterView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public void init() {
        View view = inflate(mContext, R.layout.filter_view, this);
        ButterKnife.bind(this, view);

        mFilterTextView.setChipHeight(R.dimen.chip_height);
        mFilterTextView.setChipSpacing(R.dimen.chip_spacing);
        mFilterTextView.setChipTextSize(R.dimen.chip_text_size);
        mFilterTextView.setChipVerticalSpacing(R.dimen.chip_vertical_spacing);
        mFilterTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        mFilterTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        mFilterTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        mFilterTextView.enableEditChipOnTouch(false, true);
    }

    public void chipifyAllUnterminatedTokens() {
        mFilterTextView.chipifyAllUnterminatedTokens();
    }

    public List<String> getChipValues() {
        return mFilterTextView.getChipValues();
    }

    public void clearFilterView() {
        if (mFilterTextView != null) {
            mFilterTextView.setText("");
        }

        if (mOpenCheck != null && mClosedCheck != null) {
            mOpenCheck.setChecked(true);
            mClosedCheck.setChecked(true);
        }
    }

    public String getStatus() {
        boolean isOpen = mOpenCheck.isChecked();
        boolean isClosed = mClosedCheck.isChecked();

        if (isOpen && !isClosed) {
            return OPEN;
        }
        else if (!isOpen && isClosed) {
            return CLOSED;
        }
        else {
            return null;
        }
    }
}
