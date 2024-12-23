package cat.teknos.berry.view.util

interface GameEventListener {
    fun onBerryCollected()
    fun onRockCollision()
    fun onHeartCollected()
}