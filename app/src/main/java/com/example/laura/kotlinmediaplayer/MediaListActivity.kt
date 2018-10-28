package com.example.laura.kotlinmediaplayer

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mtechviral.mplaylib.MusicFinder
import kotlinx.android.synthetic.main.activity_music_info.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk25.coroutines.onClick

class MediaListActivity : Activity() {

    private var mediaPlayer: MediaPlayer? = null
    private var songList: List<MusicFinder.Song>? = null
    private var isPlaying: Boolean = false
    private var currSong: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_info)

        this.shuffleButton.onClick {
            onClickShuffleButton()
        }
        this.fastNoteButton.onClick {
            onClickTextButton()
        }

        launchMediaList()

        this.listAllSong.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if(isPlaying == false || (position != currSong)){
                isPlaying = true
                mediaPlayer?.reset()
                mediaPlayer = MediaPlayer.create(ctx, songList!![position].uri)
                mediaPlayer?.start()
                currSong = position
            }else{
                isPlaying = false
                mediaPlayer?.pause()

            }
        }
    }

    private fun onClickShuffleButton(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun onClickTextButton(){
        val intent = Intent(this, TextInfoActivity::class.java)
        startActivity(intent)
    }

    private fun launchMediaList() {

        val musicFinder = MusicFinder(contentResolver)
        musicFinder.prepare()
        var allSongs = musicFinder.allSongs


        var allSongsTitles = arrayListOf<String>()
        //var allArtists = arrayListOf<String>()
        this.songList = allSongs
        allSongs.forEach{ i-> (allSongsTitles.add(i.title))}
        //allSongs.forEach{ i-> (allSongsTitles.add(i.artist))}

        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allSongsTitles);

        listAllSong.adapter = arrayAdapter


    }



}