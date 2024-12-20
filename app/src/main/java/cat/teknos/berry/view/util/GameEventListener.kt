package cat.teknos.berry.view.util

interface GameEventListener {
    fun onBerryCollected(berryType: Int)
    fun onRockCollision()
    fun onHeartCollected()
}