package cat.teknos.berry.singleton;

public class Singleton {

    private static class SingletonInstance {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private Singleton() {}

    public void setPlayer(String player) {}

    public void setDifficulty(int difficulty) {}
}