package cat.teknos.berry.singleton;

public class Singleton {
    private String player;
    private int difficulty;

    private static class SingletonInstance {
        private static Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return SingletonInstance.INSTANCE;
    }
    private Singleton() {
        //Constructor Singleton
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}