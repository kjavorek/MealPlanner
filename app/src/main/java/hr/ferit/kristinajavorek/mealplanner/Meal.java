package hr.ferit.kristinajavorek.mealplanner;

import android.util.Log;
import static java.lang.String.valueOf;

public class Meal {
    private String mDay, mTitle, mDescription, mPriority, mIngredients, mIngredientsIsChecked, mDirections, mWeekNum, mWeekFirstDay;
    private int mId;
    public Meal(int id, String day, String title, String description, String priority, String ingredients, String isChecked, String directions, String weekNum, String firstDay) {
        mDay = day;
        mTitle = title;
        mDescription = description;
        mPriority = priority;
        mIngredients = ingredients;
        mIngredientsIsChecked = isChecked;
        mDirections = directions;
        mWeekNum = weekNum;
        mWeekFirstDay = firstDay;
        mId = id;
    }
    public String getmDay() { return mDay; }
    public String getmName() { return mTitle; }
    public String getmDifficulty() { return mDescription; }
    public String getmTime() { return mPriority; }
    public String getmIngredients() { return mIngredients; }
    public String getmIngredientsIsChecked() { return mIngredientsIsChecked; }
    public String getmDirections() { return mDirections; }
    public String getmWeekNum() { return mWeekNum; }
    public String getmWeekFirstDay() { return mWeekFirstDay; }
    public int getmId () { return mId; }
    public void setmIngredientsIsChecked(String ingredientsIsChecked){ mIngredientsIsChecked=ingredientsIsChecked; }
    public void setmId(int id) { mId=id; }
}
