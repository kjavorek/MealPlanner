package hr.ferit.kristinajavorek.mealplanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateMealActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static final String UPDATE_FINISH="update finish";

    EditText etUpdateMealName, etUpdateMealDirections, etIngredients;
    Button bUpdate;
    String selectedId, mealDayUpdate, mealNameUpdate, mealDifficultyUpdate, mealTimeUpdate,mealIngredients, mealIngredientsIsChecked, mealDirectionsUpdate;
    Integer id;
    Spinner updateDaySpinner, updateDifficultySpinner, updateTimeSpinner;
    TextView tvUpdateIngredients;
    List <EditText> ingredientsList;
    List <String> previousIngredients, previousIsChecked;
    LinearLayout activity_update_meal;
    String mealIngredientsUpdate = "", mealIngredientsIsCheckedUpdate = "";
    Integer etId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);
        this.setUpUI();
    }

    private void setUpUI() {
        this.ingredientsList = new ArrayList<EditText>();
        this.previousIngredients = new ArrayList<String>();
        this.previousIsChecked = new ArrayList<String>();

        this.etUpdateMealName = (EditText) this.findViewById(R.id.etUpdateMealName);
        this.updateDaySpinner = (Spinner) findViewById(R.id.updateDaySpinner);
        this.updateDifficultySpinner = (Spinner) findViewById(R.id.updateDifficultySpinner);
        this.updateTimeSpinner = (Spinner) findViewById(R.id.updateTimeSpinner);
        this.tvUpdateIngredients = (TextView) findViewById(R.id.tvUpdateIngredients);
        this.etUpdateMealDirections= (EditText) this.findViewById(R.id.etUpdateMealDirections);
        this.bUpdate = (Button) this.findViewById(R.id.bUpdate);

        Intent updateMealIntent = this.getIntent();
        selectedId = updateMealIntent.getStringExtra(MainActivity.MEAL_ID);
        mealDayUpdate = updateMealIntent.getStringExtra(MainActivity.DAY_UPDATE);
        mealNameUpdate = updateMealIntent.getStringExtra(MainActivity.NAME_UPDATE);
        mealDifficultyUpdate = updateMealIntent.getStringExtra(MainActivity.DIFFICULTY_UPDATE);
        mealTimeUpdate = updateMealIntent.getStringExtra(MainActivity.TIME_UPDATE);
        mealIngredients = updateMealIntent.getStringExtra(MainActivity.INGREDIENTS_UPDATE);
        mealIngredientsIsChecked = updateMealIntent.getStringExtra(MainActivity.INGREDIENTS_ISCHECKED_UPDATE);
        mealDirectionsUpdate = updateMealIntent.getStringExtra(MainActivity.DIRECTIONS_UPDATE);

        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_dropdown_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateDaySpinner.setAdapter(daysAdapter);
        if (!mealDayUpdate.equals(null)) {
            int spinnerPosition = daysAdapter.getPosition(mealDayUpdate);
            updateDaySpinner.setSelection(spinnerPosition);
        }

        this.etUpdateMealName.setText(mealNameUpdate);
        etUpdateMealName.setSelection(etUpdateMealName.length());

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_dropdown_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateDifficultySpinner.setAdapter(difficultyAdapter);
        if (!mealDifficultyUpdate.equals(null)) {
            int spinnerPosition = difficultyAdapter.getPosition(mealDifficultyUpdate);
            updateDifficultySpinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_dropdown_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateTimeSpinner.setAdapter(timeAdapter);
        if (!mealTimeUpdate.equals(null)) {
            int spinnerPosition = timeAdapter.getPosition(mealTimeUpdate);
            updateTimeSpinner.setSelection(spinnerPosition);
        }

        previousIngredients.addAll(Arrays.asList(mealIngredients.split("\\s*,\\s*")));
        previousIsChecked.addAll(Arrays.asList(mealIngredientsIsChecked.split("\\s*,\\s*")));
        for(int i=0;i<previousIngredients.size();i++) {
            activity_update_meal = (LinearLayout) findViewById(R.id.activity_update_meal);
            etIngredients = new EditText(this);
            ingredientsList.add(etIngredients);
            etIngredients.setId(etId);
            etIngredients.setHint("Add ingredient");
            etIngredients.setHintTextColor(Color.rgb(85, 85, 85));
            etIngredients.setText(previousIngredients.get(i));
            etIngredients.setTextColor(Color.WHITE);
            etIngredients.setPadding(30, 2, 2, 15);
            etIngredients.setMaxLines(1);
            etIngredients.setOnLongClickListener(this);
            etIngredients.requestFocus();
            activity_update_meal.addView(etIngredients, 6 + etId);
            etId++;
        }

        this.etUpdateMealDirections.setText(mealDirectionsUpdate);
        id=Integer.parseInt(selectedId);

        this.bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if(etUpdateMealName.getText().toString().matches("")){
                    Toast.makeText(context,"Nothing to update.", Toast.LENGTH_SHORT).show();
                }else {
                    mealDayUpdate = updateDaySpinner.getSelectedItem().toString();
                    mealNameUpdate = etUpdateMealName.getText().toString();
                    mealDifficultyUpdate = updateDifficultySpinner.getSelectedItem().toString();
                    mealTimeUpdate = updateTimeSpinner.getSelectedItem().toString();

                    mealIngredientsUpdate="";
                    for(int i=0; i<ingredientsList.size();i++){
                        if(!ingredientsList.get(i).getText().equals(null) || !ingredientsList.get(i).getText().equals(" ")) {
                            if (mealIngredientsUpdate == "") {
                                mealIngredientsUpdate = ingredientsList.get(i).getText().toString();
                                if(previousIngredients.size()>i) {
                                    if (ingredientsList.get(i).getText().toString().equals(previousIngredients.get(i))) {
                                        mealIngredientsIsCheckedUpdate = previousIsChecked.get(i);
                                    } else mealIngredientsIsCheckedUpdate = "0";
                                }
                                else mealIngredientsIsCheckedUpdate = "0";
                            } else {
                                mealIngredientsUpdate += "," + ingredientsList.get(i).getText().toString();
                                if(previousIngredients.size()>i) {
                                    if (ingredientsList.get(i).getText().toString().equals(previousIngredients.get(i))) {
                                        mealIngredientsIsCheckedUpdate += "," + previousIsChecked.get(i);
                                    } else mealIngredientsIsCheckedUpdate += "," + "0";
                                }
                                else mealIngredientsIsCheckedUpdate += "," + "0";
                            }
                        }
                    }

                    mealDirectionsUpdate = etUpdateMealDirections.getText().toString();
                    DBHelper.getInstance(getApplicationContext()).updateMeal(id, mealDayUpdate, mealNameUpdate, mealDifficultyUpdate, mealTimeUpdate, mealIngredientsUpdate, mealIngredientsIsCheckedUpdate, mealDirectionsUpdate);
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    intent.putExtra(UPDATE_FINISH,1);
                    startActivity(intent);
                }
            }
        });
    }

    public void handleOnClick(View view)
    {
        activity_update_meal = (LinearLayout)findViewById(R.id.activity_update_meal);
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
        activity_update_meal.addView(etIngredients, 6+etId);
        etId++;
    }

    @Override
    public boolean onLongClick(View v) {
        //Removing inserted ingredient from the list after long click
        for(int i=0; i<ingredientsList.size();i++){
            if (ingredientsList.get(i).getId() == v.getId()){
                this.ingredientsList.remove(i);
                activity_update_meal.removeView(activity_update_meal.getChildAt(i+6));
                etId=ingredientsList.size();
            }
        }
        return true;
    }
}
