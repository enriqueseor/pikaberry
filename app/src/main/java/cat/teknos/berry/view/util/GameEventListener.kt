package cat.teknos.berry.view.util

interface GameEventListener {
    fun onRockCollision()
    fun onNewHeartGenerated()
    fun onHeartCollected()
}