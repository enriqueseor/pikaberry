package dev.enriqueseor.pikaberry.view.util

interface GameEventListener {
    fun onBerryCollected()
    fun onRockCollision()
    fun onHeartCollected()
    fun onScoreUpdated(newScore: Int)
}