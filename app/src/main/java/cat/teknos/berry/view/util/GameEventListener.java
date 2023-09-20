package cat.teknos.berry.view.util;

public interface GameEventListener {
    void onRockCollision();
    void onNewHeartGenerated();
    void onHeartCollected();
}