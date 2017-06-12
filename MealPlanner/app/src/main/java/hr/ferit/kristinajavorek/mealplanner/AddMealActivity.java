package hr.ferit.kristinajavorek.mealplanner;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMealActivity extends AppCompatActivity {

    public static final String MEAL_DAY = "Day";
    public static final String MEAL_NAME = "Name";
    public static final String MEAL_DIFFICULTY = "Difficilty";
    public static final String MEAL_TIME = "Time";
    public static final String MEAL_INGREDIENTS = "Ingredients";
    public static final String MEAL_DIRECTIONS = "Directions";
    EditText etMealDay, etMealName, etMealDifficulty, etMealTime, etMealIngredients, etMealDirections;
    Button bAddMeal;
    String mealDay, mealName, mealDifficulty, mealTime, mealIngredients, mealDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        setUpUI();
    }

    private void setUpUI() {
        this.etMealDay = (EditText) findViewById(R.id.etAddMealDay);
        this.etMealName = (EditText) findViewById(R.id.etAddMealName);
        this.etMealDifficulty= (EditText) findViewById(R.id.etMealDifficulty);
        this.etMealTime= (EditText) findViewById(R.id.etAddMealTime);
        this.etMealIngredients= (EditText) findViewById(R.id.etMealIngredients);
        this.etMealDirections= (EditText) findViewById(R.id.etMealDirections);
        this.bAddMeal = (Button) findViewById(R.id.bAddMeal);
        this.bAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if(etMealName.getText().toString().matches("")){
                    Toast.makeText(context,"Nothing to add.", Toast.LENGTH_SHORT).show();
                }else {
                    mealDay = etMealDay.getText().toString();
                    mealName = etMealName.getText().toString();
                    mealDifficulty = etMealDifficulty.getText().toString();
                    mealTime = etMealTime.getText().toString();
                    mealIngredients = etMealIngredients.getText().toString();
                    mealDirections = etMealDirections.getText().toString();

                    Intent result = new Intent();
                    result.setClass(getApplicationContext(), MainActivity.class);
                    result.putExtra(MEAL_DAY, mealDay);
                    result.putExtra(MEAL_NAME, mealName);
                    result.putExtra(MEAL_DIFFICULTY, mealDifficulty);
                    result.putExtra(MEAL_TIME, mealTime);
                    result.putExtra(MEAL_INGREDIENTS, mealIngredients);
                    result.putExtra(MEAL_DIRECTIONS, mealDirections);
                    startActivity(result);
                }
            }
        });
    }

}
