package com.snaptiongame.app.presentation.view.photo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.snaptiongame.app.R

import kotlinx.android.synthetic.main.activity_immersive.*

/**
 * @author Tyler Wong
 */

class ImmersiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_immersive)

        val imageUrl = intent.getStringExtra(IMAGE_URL)

        if (imageUrl != null && !imageUrl.isEmpty()) {
            val options = RequestOptions()
                    .dontAnimate()
                    .priority(Priority.IMMEDIATE)

            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .into(photoView)

            photoView.setOnClickListener { super.onBackPressed() }
        }
    }

    companion object {
        const val IMAGE_URL = "imageUrl"
    }
}
