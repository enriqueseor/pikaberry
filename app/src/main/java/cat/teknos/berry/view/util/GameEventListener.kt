package cat.teknos.berry.view.util

interface GameEventListener {
    fun onRockCollision()
    fun onHeartCollected()
    fun onBerryCollected(berryType: Int)
}