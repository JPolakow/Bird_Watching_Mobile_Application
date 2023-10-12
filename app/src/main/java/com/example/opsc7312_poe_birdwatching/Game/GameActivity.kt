package com.example.opsc7312_poe_birdwatching.Game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.opsc7312_poe_birdwatching.R
import com.example.opsc7312_poe_birdwatching.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        gameView = binding.gameView
        run()
    }

    private fun run(){
        gameView.resume()
    }
}