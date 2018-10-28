package com.example.laura.kotlinmediaplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.mtechviral.mplaylib.MusicFinder
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {

    var albumArt: ImageView? = null

    var playButton: ImageButton? = null
    var shuffleButton: ImageButton? = null
    var songListButton: ImageButton? = null

    var titleView: TextView? = null
    var artistView: TextView? = null

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            launchMusicPlayer()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchMusicPlayer()
        } else {
            finish()
        }
    }

    private fun launchMusicPlayer() {

            val musicFinder = MusicFinder(contentResolver)
            musicFinder.prepare()
            val allSongs = musicFinder.allSongs

            val musicPlayerDesign = object : AnkoComponent<MainActivity> {
                override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

                    relativeLayout {
                        albumArt = imageView {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }.lparams(matchParent, matchParent)

                        verticalLayout {
                            artistView = textView {
                                textSize = 20f
                            }
                            titleView = textView {
                                textSize = 22f
                            }
                            linearLayout {

                                shuffleButton = imageButton {
                                    imageResource = android.R.drawable.ic_menu_revert
                                    background = null
                                    onClick {
                                        shuffleSongs()
                                    }
                                }.lparams(0, wrapContent, 0.8f)

                                playButton = imageButton {
                                    imageResource = android.R.drawable.ic_media_play
                                    background = null
                                    onClick {
                                        playSongs()
                                    }
                                }.lparams(0, wrapContent, 0.8f)

                                songListButton = imageButton {
                                    imageResource = android.R.drawable.ic_menu_info_details
                                    background = null
                                    onClick {
                                        val intent = Intent(it?.context, MediaListActivity::class.java)
                                        startActivity(intent)
                                    }
                                }.lparams(0, wrapContent, 0.8f)

                            }.lparams(matchParent, wrapContent) {
                            }

                            }.lparams(matchParent, matchParent){

                        }.lparams(matchParent, wrapContent) {
                            alignParentBottom()
                        }
                    }

                }

                fun shuffleSongs() {
                    allSongs.shuffle()
                    val song = allSongs[0]
                    mediaPlayer?.reset()


                    mediaPlayer = MediaPlayer.create(ctx, song.uri)
                    mediaPlayer?.setOnCompletionListener {
                        shuffleSongs()
                    }
                    playButton?.imageResource = android.R.drawable.ic_media_pause
                    artistView?.text = song.artist
                    titleView?.text = song.title
                    albumArt?.imageURI = song.albumArt
                    mediaPlayer?.start()

                }

                fun playSongs() {
                    var isPlaying: Boolean? = mediaPlayer?.isPlaying

                    if (isPlaying == true) {
                        mediaPlayer?.pause()
                        playButton?.imageResource = android.R.drawable.ic_media_play
                    } else {
                        mediaPlayer?.start()
                        playButton?.imageResource = android.R.drawable.ic_media_pause
                    }
                }
            }
            musicPlayerDesign.setContentView(this@MainActivity)
            musicPlayerDesign.shuffleSongs()
        //}
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}
