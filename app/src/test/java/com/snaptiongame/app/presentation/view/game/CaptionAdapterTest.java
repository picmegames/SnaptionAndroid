package com.snaptiongame.app.presentation.view.game;

import android.app.Fragment;
import android.os.Build;
import android.view.View;

import com.snaptiongame.app.BuildConfig;
import com.snaptiongame.app.data.models.Caption;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nickromero on 2/20/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
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
        captions.add(new Caption(0, "First caption"));
        captions.add(new Caption(0, "Second caption"));
        captions.add(new Caption(0, "Third caption"));

        captions2 = new ArrayList<>();
        captions2.add(new Caption(123, "First caption"));
        captions2.add(new Caption(234, "Second caption"));
        captions2.add(new Caption(345, "Third caption"));

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

        View listitemview = inflater.inflate(R.layout.caption_chooser_view, null, false);
        holder = new CaptionCardViewHolder(listitemview);
        captionAdapter.onBindViewHolder(holder, 0);
        assertEquals(holder.captionId, 0);
        assertFalse(holder.isLiked);





    }*/
}
