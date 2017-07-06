package hr.ferit.kristinajavorek.mealplanner;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMealActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static final String MEAL_DAY = "Day";
    public static final String MEAL_NAME = "Name";
    public static final String MEAL_DIFFICULTY = "Difficilty";
    public static final String MEAL_TIME = "Time";
    public static final String MEAL_INGREDIENTS = "Ingredients";
    public static final String MEAL_INGREDIENTS_ISCHECKED = "IngredientsIsChecked";
    public static final String MEAL_DIRECTIONS = "Directions";
    public static final String MEAL_WEEK_NUM = "Week number";
    public static final String MEAL_FIRST_DAY = "Monday";

    LinearLayout activity_add_meal;
    EditText etMealName, etIngredients, etMealDirections;
    List<EditText> ingredientsList;
    TextView tvAddIngredients;
    Button bAddMeal;
    String mealDay, mealName, mealDifficulty, mealTime, mealIngredients = "", mealIngredientsIsChecked = "", mealDirections = "", weekDay, weekNum;
    Integer etId = 0;
    Spinner daySpinner, difficultySpinner, timeSpinner;
    String title="", link="", ingredientsString="", difficulty="", time="";
    ArrayList<String> recipesIngredients = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        setUpUI();
    }

    private void setUpUI() {
        this.ingredientsList = new ArrayList<EditText>();

        this.daySpinner = (Spinner) findViewById(R.id.daySpinner);
        
        Intent addedMealIntent = this.getIntent();
        if(addedMealIntent.hasExtra(MainActivity.TODAY)){
            mealDay = addedMealIntent.getStringExtra(MainActivity.TODAY);
            List<String> list = new ArrayList<String>();
            list.add(mealDay);
            ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
            daySpinner.setBackgroundColor(Color.TRANSPARENT);
            daySpinner.setAdapter(dayAdapter);
        }
        else {
            ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_dropdown_item);
            daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySpinner.setAdapter(daysAdapter);
        }
        if(addedMealIntent.hasExtra(MainActivity.WEEK_DAY)){
            weekDay = addedMealIntent.getStringExtra(MainActivity.WEEK_DAY);
            weekNum = addedMealIntent.getStringExtra(MainActivity.WEEK_NUM);
        }
        if(addedMealIntent.hasExtra(RecipesActivity.TITLE)) {
            title = addedMealIntent.getStringExtra(RecipesActivity.TITLE);
            link = addedMealIntent.getStringExtra(RecipesActivity.LINK);
            ingredientsString = addedMealIntent.getStringExtra(RecipesActivity.INGREDIENTS);
        }
        else if(addedMealIntent.hasExtra(MainActivity.PAST_TITLE)){
            title = addedMealIntent.getStringExtra(MainActivity.PAST_TITLE);
            difficulty = addedMealIntent.getStringExtra(MainActivity.PAST_DIFFICULTY);
            time = addedMealIntent.getStringExtra(MainActivity.PAST_TIME);
            ingredientsString = addedMealIntent.getStringExtra(MainActivity.PAST_INGREDIENTS);
            link = addedMealIntent.getStringExtra(MainActivity.PAST_DIRECTIONS);
        }

        this.etMealName = (EditText) findViewById(R.id.etAddMealName);
        if(title!="") this.etMealName.setText(title);

        this.difficultySpinner = (Spinner)findViewById(R.id.difficultySpinner);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_dropdown_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!difficulty.equals(null) || !difficulty.equals("")) {
            int spinnerDifficultyPosition = difficultyAdapter.getPosition(difficulty);
            difficultySpinner.setSelection(spinnerDifficultyPosition);
        }
        difficultySpinner.setAdapter(difficultyAdapter);

        this.timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!time.equals(null) || !time.equals("")) {
            int spinnerTimePosition = timeAdapter.getPosition(time);
            timeSpinner.setSelection(spinnerTimePosition);
        }
        timeSpinner.setAdapter(timeAdapter);

        this.tvAddIngredients= (TextView) findViewById(R.id.tvAddIngredients);
        recipesIngredients.addAll(Arrays.asList(ingredientsString.split("\\s*,\\s*")));
        for(int i=0;i<recipesIngredients.size();i++) {
            activity_add_meal = (LinearLayout) findViewById(R.id.activity_add_meal);
            etIngredients = new EditText(this);
            ingredientsList.add(etIngredients);
            etIngredients.setId(etId);
            etIngredients.setHint("Add ingredient");
            etIngredients.setHintTextColor(Color.rgb(85, 85, 85));
            etIngredients.setText(recipesIngredients.get(i));
            etIngredients.setTextColor(Color.WHITE);
            etIngredients.setPadding(30, 2, 2, 15);
            etIngredients.setMaxLines(1);
            etIngredients.setOnLongClickListener(this);
            etIngredients.requestFocus();
            activity_add_meal.addView(etIngredients, 6 + etId);
            etId++;
        }

        this.etMealDirections= (EditText) findViewById(R.id.etMealDirections);
        if(link!="") this.etMealDirections.setText(link);
        this.bAddMeal = (Button) findViewById(R.id.bAddMeal);
        this.bAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean comma = false;
                Context context = getApplicationContext();

                if(etMealName.getText().toString().matches("")){
                    Toast.makeText(context,"Nothing to add.", Toast.LENGTH_SHORT).show();
                }else {
                    mealDay = daySpinner.getSelectedItem().toString();
                    //Toast.makeText(getApplicationContext(),mealDay,Toast.LENGTH_LONG).show();

                    mealName = etMealName.getText().toString();
                    mealDifficulty = difficultySpinner.getSelectedItem().toString();
                    mealTime = timeSpinner.getSelectedItem().toString();
                    mealDirections = etMealDirections.getText().toString();
                    for(int i=0; i<ingredientsList.size();i++){
                        if (mealIngredients == "") {
                            mealIngredients = ingredientsList.get(i).getText().toString();
                            mealIngredientsIsChecked = "0";
                        }
                        else{
                            mealIngredients += ","+ingredientsList.get(i).getText().toString();
                            mealIngredientsIsChecked += ","+"0";
                        }
                        if(ingredientsList.get(i).getText().toString().contains(",")) comma = true;
                    }

                    if(comma) Toast.makeText(context,"Please do not write a comma in a list of ingredients.", Toast.LENGTH_SHORT).show();
                    else{
                    //Monday date
                    Calendar calendar = Calendar.getInstance();
                    String thisDay = weekDay;
                    if(!thisDay.equals("Monday")){
                        if(thisDay.equals("Sunday")) calendar.add(Calendar.DATE, -6);
                        else if(thisDay.equals("Saturday")) calendar.add(Calendar.DATE, -5);
                        else if(thisDay.equals("Friday")) calendar.add(Calendar.DATE, -4);
                        else if(thisDay.equals("Thursday")) calendar.add(Calendar.DATE, -3);
                        else if(thisDay.equals("Wednesday")) calendar.add(Calendar.DATE, -2);
                        else if(thisDay.equals("Tuesday")) calendar.add(Calendar.DATE, -1);
                    }
                    else calendar.add(Calendar.DATE, 0);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedMondayDate = df.format(calendar.getTime());

                    Intent result = new Intent();
                    result.setClass(getApplicationContext(), MainActivity.class);
                    result.putExtra(MEAL_DAY, mealDay);
                    result.putExtra(MEAL_NAME, mealName);
                    result.putExtra(MEAL_DIFFICULTY, mealDifficulty);
                    result.putExtra(MEAL_TIME, mealTime);
                    result.putExtra(MEAL_INGREDIENTS, mealIngredients);
                    result.putExtra(MEAL_INGREDIENTS_ISCHECKED, mealIngredientsIsChecked);
                    result.putExtra(MEAL_DIRECTIONS, mealDirections);
                    result.putExtra(MEAL_WEEK_NUM, weekNum);
                    result.putExtra(MEAL_FIRST_DAY, formattedMondayDate);
                    startActivity(result);
                    }
                }
            }
        });
    }

    public void handleOnClick(View view)
    {
        activity_add_meal = (LinearLayout)findViewById(R.id.activity_add_meal);
        etIngredients = new EditText(this);
        ingredientsList.add(etIngredients);
        etIngredients.setId(etId);
        etIngredients.setHint("Add ingredient");
        etIngredients.setHintTextColor(Color.rgb(85,85,85));
        etIngredients.setTextColor(Color.WHITE);
        etIngredients.setPadding(30, 2, 2, 15);
        etIngredients.setMaxLines(1);
        etIngredients.setOnLongClickListener(this);
        etIngredients.requestFocus();
        activity_add_meal.addView(etIngredients, 6+etId);
        etId++;
    }

    @Override
    public boolean onLongClick(View v) {
        //Removing inserted ingredient from the list after long click
        for(int i=0; i<ingredientsList.size();i++){
            if (ingredientsList.get(i).getId() == v.getId()){
                this.ingredientsList.remove(i);
                activity_add_meal.removeView(activity_add_meal.getChildAt(i+6));
                etId=ingredientsList.size();
            }
        }
        return true;
    }
}
