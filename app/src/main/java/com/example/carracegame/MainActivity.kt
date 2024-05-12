package com.example.carracegame

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var soundToggleButton: ToggleButton

    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val HIGH_SCORE_KEY = "highScore"
        const val SOUND_STATE_KEY = "soundState"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highScore)
        soundToggleButton = findViewById(R.id.soundToggleButton)
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(HIGH_SCORE_KEY, 0)
            putBoolean(SOUND_STATE_KEY, true)
            apply()
        }

        startBtn.setOnClickListener {
            if (startBtn.visibility == View.VISIBLE) {
                val highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
                mGameView = GameView(this, this, highScore)
                mGameView.setBackgroundResource(R.drawable.road)
                rootLayout.addView(mGameView)
                startBtn.visibility = View.GONE
                score.visibility = View.GONE
                updateHighScore(0)
                if (isSoundEnabled()) {
                    mediaPlayer.start()
                }
            }
        }

        soundToggleButton.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean(SOUND_STATE_KEY, isChecked)
                apply()
            }

            if (isChecked) {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            } else {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
        }
    }

    override fun closeGame(mScore: Int) {
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        updateHighScore(mScore)
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
        displayGameOverMessage()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun updateHighScore(newScore: Int) {
        var highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0)

        if (newScore > highScore) {
            highScore = newScore
            with(sharedPreferences.edit()) {
                putInt(HIGH_SCORE_KEY, highScore)
                apply()
            }
        }

        highScoreTextView.text = "High Score : $highScore"
    }

    private fun isSoundEnabled(): Boolean {
        return sharedPreferences.getBoolean(SOUND_STATE_KEY, true)
    }
    override fun displayGameOverMessage() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Game Over")
        alertDialogBuilder.setMessage("Play Again?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            finish()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
