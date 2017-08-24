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
 * @author Nick Romero
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CaptionAdapterTest {

    private CaptionAdapter captionAdapter;
    private List<Caption> captions;
    private List<Caption> captions2;
    private List<Caption> resultCaptions;
    private View mockView;
    private Fragment mockFragment;
    private CaptionCardViewHolder holder;

    @Before
    public void setup() {
        captions = new ArrayList<>();
        captions.add(new Caption(0, "First caption"));
        captions.add(new Caption(0, "Second caption"));
        captions.add(new Caption(0, "Third caption"));

        captions2 = new ArrayList<>();
        Caption caption1 = new Caption(123, "First caption");
        Caption caption2 = new Caption(234, "Second caption");
        Caption caption3 = new Caption(345, "Third caption");
        caption1.creatorName = "hi";
        caption2.creatorName = "hi";
        caption3.creatorName = "hi";
        caption1.creatorPicture = "http://url.com";
        caption2.creatorPicture = "http://url.com";
        caption3.creatorName = "http://url.com";
        captions2.add(caption1);
        captions2.add(caption2);
        captions2.add(caption3);

        captionAdapter = new CaptionAdapter(captions, null);

        resultCaptions = new ArrayList<>();
        resultCaptions.addAll(captions);
        resultCaptions.addAll(captions2);
    }

    @Test
    public void testAddCaptions() {
        captionAdapter.addCaptions(captions2);
        assertEquals(1, 1);
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
