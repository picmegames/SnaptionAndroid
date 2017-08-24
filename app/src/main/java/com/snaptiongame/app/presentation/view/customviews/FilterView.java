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
    NachoTextView filterTextView;
    @BindView(R.id.open_check)
    CheckBox openCheck;
    @BindView(R.id.closed_check)
    CheckBox closedCheck;

    private Context context;

    private static final String OPEN = "open";
    private static final String CLOSED = "closed";

    public FilterView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void init() {
        View view = inflate(context, R.layout.filter_view, this);
        ButterKnife.bind(this, view);

        filterTextView.setChipHeight(R.dimen.chip_height);
        filterTextView.setChipSpacing(R.dimen.chip_spacing);
        filterTextView.setChipTextSize(R.dimen.chip_text_size);
        filterTextView.setChipVerticalSpacing(R.dimen.chip_vertical_spacing);
        filterTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        filterTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        filterTextView.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        filterTextView.enableEditChipOnTouch(false, true);
    }

    public void chipifyAllUnterminatedTokens() {
        filterTextView.chipifyAllUnterminatedTokens();
    }

    public List<String> getChipValues() {
        return filterTextView.getChipValues();
    }

    public void clearFilterView() {
        if (filterTextView != null) {
            filterTextView.setText("");
        }

        if (openCheck != null && closedCheck != null) {
            openCheck.setChecked(true);
            closedCheck.setChecked(true);
        }
    }

    public String getStatus() {
        boolean isOpen = openCheck.isChecked();
        boolean isClosed = closedCheck.isChecked();

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
