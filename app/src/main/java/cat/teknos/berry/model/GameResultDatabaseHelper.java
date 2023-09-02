package cat.teknos.berry.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameResultDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_results.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "game_results";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PLAYER_NAME = "player_name";
    private static final String COLUMN_SCORE = "score";

    public GameResultDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER_NAME + " TEXT, " +
                COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }
}