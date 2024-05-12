package com.example.carracegame

interface GameTask {
    fun closeGame(score: Int)
    fun displayGameOverMessage()
}