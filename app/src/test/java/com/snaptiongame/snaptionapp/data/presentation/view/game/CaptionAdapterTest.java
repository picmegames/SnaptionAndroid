package com.snaptiongame.snaptionapp.data.presentation.view.game;

import android.app.Fragment;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.presentation.view.game.CaptionAdapter;
import com.snaptiongame.snaptionapp.presentation.view.game.CaptionCardViewHolder;
import com.snaptiongame.snaptionapp.presentation.view.game.CaptionSelectDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nickromero on 2/20/17.
 */

@RunWith(RobolectricTestRunner.class)
public class CaptionAdapterTest {

    private CaptionAdapter captionAdapter;
    private List<Caption> captions;
    private List<Caption> captions2;
    private View mockView;
    private Fragment mockFragment;
    private CaptionCardViewHolder holder;
    @Before
    public void setup() {
       // mockFragment = Robolectric.buildFragment(CaptionSelectDialogFragment.class)
        //        .create().resume().get();

        captions = new ArrayList<>();
        captions.add(new Caption(0, "First caption", 123));
        captions.add(new Caption(0, "Second caption", 234));
        captions.add(new Caption(0, "Third caption", 345));

        captions2 = new ArrayList<>();
        captions2.add(new Caption(123, "First caption", 12));
        captions2.add(new Caption(234, "Second caption", 4));
        captions2.add(new Caption(345, "Third caption", 35));

        captionAdapter = new CaptionAdapter(captions);

    }

    @Test
    public void testSetCaptions() {
        captionAdapter.setCaptions(captions2);
        assertEquals(captionAdapter.getCaptions(), captions2);
    }
    @Test
    public void testGetCaption() {

        assertEquals(captionAdapter.getCaptions(), captions);
    }
/*
    @Test
    public void testGetItemCount() {
        assertEquals(captionAdapter.getItemCount(), 3);
    }

    @Test
    public void testOnBindViewHolder() {
        LayoutInflater inflater =
                (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listitemview = inflater.inflate(R.layout.caption_chooser_dialog, null, false);
        holder = new CaptionCardViewHolder(listitemview);
        captionAdapter.onBindViewHolder(holder, 0);
        assertEquals(holder.captionId, 0);
        assertFalse(holder.isLiked);





    }*/
}
