package com.example.laura.kotlinmediaplayer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.text_info_activity.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TextInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_info_activity)
        this.shuffleButton.onClick {
            singleSongOnClick()
        }
        this.songListButton.onClick {
            mediaListOnClick()
        }
    }

    private fun singleSongOnClick(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun mediaListOnClick(){
        var intent = Intent(this,MediaListActivity::class.java)
        startActivity(intent)
    }
}