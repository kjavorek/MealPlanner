package hr.ferit.kristinajavorek.mealplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import javax.xml.validation.Schema;
import static java.lang.String.valueOf;

public class DBHelper extends SQLiteOpenHelper {
    // Singleton
    private static DBHelper mMealDBHelper = null;
    private DBHelper (Context context){
        super(context.getApplicationContext(),Schema.DATABASE_NAME,null,Schema.SCHEMA_VERSION);
    }
    public static synchronized DBHelper getInstance(Context context){
        if(mMealDBHelper == null){
            mMealDBHelper = new DBHelper(context);
        }
        return mMealDBHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE_MY_MEALS); }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_MY_MEALS);
        this.onCreate(db);
    }
    //SQL statements
    static final String CREATE_TABLE_MY_MEALS = "CREATE TABLE " + Schema.TABLE_MY_MEALS +
            " (" + Schema.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Schema.DAY + " TEXT," +
            Schema.NAME + " TEXT," + Schema.DIFFICULTY + " TEXT," +  Schema.TIME + " TEXT,"
            + Schema.INGREDIENTS + " TEXT," + Schema.DIRECTIONS + " TEXT);";
    static final String DROP_TABLE_MY_MEALS = "DROP TABLE IF EXISTS " + Schema.TABLE_MY_MEALS;
    static final String SELECT_ALL_MEALS="SELECT " + Schema.DAY + "," + Schema.NAME + "," +
            Schema.DIFFICULTY + "," + Schema.TIME + "," + Schema.INGREDIENTS + "," + Schema.DIRECTIONS
            + "," + Schema.KEY_ID + " FROM " + Schema.TABLE_MY_MEALS;

    public Long insertMeal(Meal meal){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.DAY, meal.getmDay());
        contentValues.put(Schema.NAME, meal.getmName());
        contentValues.put(Schema.DIFFICULTY, meal.getmDifficulty());
        contentValues.put(Schema.TIME, meal.getmTime());
        contentValues.put(Schema.INGREDIENTS, meal.getmIngredients());
        contentValues.put(Schema.DIRECTIONS, meal.getmDirections());
        SQLiteDatabase writeableDatabase = this.getWritableDatabase();
        long id = writeableDatabase.insert(Schema.TABLE_MY_MEALS, Schema.DIFFICULTY, contentValues);
        writeableDatabase.close();
        return id;
    }

    public  void updateMeal(int mId, String day, String name, String difficulty, String time, String ingredients, String directions){
        String[] id = new String[]{Integer.toString(mId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.DAY, day);
        contentValues.put(Schema.NAME, name);
        contentValues.put(Schema.DIFFICULTY, difficulty);
        contentValues.put(Schema.TIME, time);
        contentValues.put(Schema.INGREDIENTS, ingredients);
        contentValues.put(Schema.DIRECTIONS, directions);
        SQLiteDatabase writeableDatabase = this.getWritableDatabase();
        writeableDatabase.update(Schema.TABLE_MY_MEALS,contentValues,Schema.KEY_ID+"=?",id);
        writeableDatabase.close();
    }

    public void deleteMeal(int mId){
        String[] id = new String[]{Integer.toString(mId)};
        SQLiteDatabase writeableDatabase = this.getWritableDatabase();
        writeableDatabase.delete(Schema.TABLE_MY_MEALS, Schema.KEY_ID + "=?", id);
        //writeableDatabase.execSQL("delete from sqlite_sequence where name='my_tasks'");
        //String query = "DELETE FROM my_tasks WHERE title=5";
        //writeableDatabase.execSQL(query);
        //writeableDatabase.delete(Schema.TABLE_MY_TASKS, null, null); //delete all from table
        writeableDatabase.close();
    }
    public ArrayList<Meal> getAllMeals(){
        SQLiteDatabase writeableDatabase = this.getWritableDatabase();
        Cursor mealCursor = writeableDatabase.rawQuery(SELECT_ALL_MEALS,null);
        ArrayList<Meal> meals = new ArrayList<>();
        if(mealCursor.moveToFirst()){
            do{
                String day = mealCursor.getString(0);
                String name = mealCursor.getString(1);
                String difficulty = mealCursor.getString(2);
                String time = mealCursor.getString(3);
                String ingredients = mealCursor.getString(4);
                String directions = mealCursor.getString(5);
                int id = mealCursor.getInt(6);
                meals.add(new Meal(id, day, name, difficulty, time, ingredients, directions));
            }while(mealCursor.moveToNext());
        }
        mealCursor.close();
        writeableDatabase.close();
        return meals;
    }
    public static class Schema{
        private static final int SCHEMA_VERSION = 1;
        private static final String DATABASE_NAME = "mymeal3.db";
        private static final String KEY_ID = "id";
        static final String TABLE_MY_MEALS = "my_meals3";
        static final String DAY = "day";
        static final String NAME = "name";
        static final String DIFFICULTY = "difficulty";
        static final String TIME = "time";
        static final String INGREDIENTS = "ingredients";
        static final String DIRECTIONS = "directions";
    }
}