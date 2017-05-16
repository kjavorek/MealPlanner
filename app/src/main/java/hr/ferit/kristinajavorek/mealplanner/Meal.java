package hr.ferit.kristinajavorek.mealplanner;

import android.util.Log;
import static java.lang.String.valueOf;

public class Meal {
    private String mDay, mTitle, mDescription, mPriority, mIngredients, mDirections;
    private int mId;
    public Meal(int id, String day, String title, String description, String priority, String ingredients, String directions) {
        mDay = day;
        mTitle = title;
        mDescription = description;
        mPriority = priority;
        mIngredients = ingredients;
        mDirections = directions;
        mId = id;
    }
    public String getmDay() { return mDay; }
    public String getmName() { return mTitle; }
    public String getmDifficulty() { return mDescription; }
    public String getmTime() { return mPriority; }
    public String getmIngredients() { return mIngredients; }
    public String getmDirections() { return mDirections; }
    public void setmId(int id) { mId=id; }
    public int getmId () { return mId; }
}
