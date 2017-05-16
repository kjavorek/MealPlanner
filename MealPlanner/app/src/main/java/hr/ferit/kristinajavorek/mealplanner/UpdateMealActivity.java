package hr.ferit.kristinajavorek.mealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMealActivity extends AppCompatActivity {

    EditText etUpdateMealDay, etUpdateMealName, etUpdateMealDifficulty, etUpdateMealTime, etUpdateMealIngredients, etUpdateMealDirections;
    Button bUpdate;
    String selectedId, mealDayUpdate, mealNameUpdate, mealDifficultyUpdate, mealTimeUpdate, mealIngredientsUpdate, mealDirectionsUpdate;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);
        this.setUpUI();
    }

    private void setUpUI() {
        this.etUpdateMealDay = (EditText) this.findViewById(R.id.etUpdateMealDay);
        this.etUpdateMealName = (EditText) this.findViewById(R.id.etUpdateMealName);
        this.etUpdateMealDifficulty = (EditText) this.findViewById(R.id.etUpdateMealDifficulty);
        this.etUpdateMealTime = (EditText) this.findViewById(R.id.etUpdateMealTime);
        this.etUpdateMealIngredients = (EditText) this.findViewById(R.id.etUpdateMealIngredients);
        this.etUpdateMealDirections= (EditText) this.findViewById(R.id.etUpdateMealDirections);
        this.bUpdate = (Button) this.findViewById(R.id.bUpdate);

        Intent updateMealIntent = this.getIntent();
        selectedId = updateMealIntent.getStringExtra(MainActivity.MEAL_ID);
        mealDayUpdate = updateMealIntent.getStringExtra(MainActivity.DAY_UPDATE);
        mealNameUpdate = updateMealIntent.getStringExtra(MainActivity.NAME_UPDATE);
        mealDifficultyUpdate = updateMealIntent.getStringExtra(MainActivity.DIFFICULTY_UPDATE);
        mealTimeUpdate = updateMealIntent.getStringExtra(MainActivity.TIME_UPDATE);
        mealIngredientsUpdate = updateMealIntent.getStringExtra(MainActivity.INGREDIENTS_UPDATE);
        mealDirectionsUpdate = updateMealIntent.getStringExtra(MainActivity.DIRECTIONS_UPDATE);

        this.etUpdateMealDay.setText(mealDayUpdate);
        this.etUpdateMealName.setText(mealNameUpdate);
        etUpdateMealName.setSelection(etUpdateMealName.length());
        this.etUpdateMealDifficulty.setText(mealDifficultyUpdate);
        this.etUpdateMealTime.setText(mealTimeUpdate);
        this.etUpdateMealIngredients.setText(mealIngredientsUpdate);
        this.etUpdateMealDirections.setText(mealDirectionsUpdate);
        id=Integer.parseInt(selectedId);

        this.bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if(etUpdateMealName.getText().toString().matches("")){
                    Toast.makeText(context,"Nothing to update.", Toast.LENGTH_SHORT).show();
                }else {
                    mealDayUpdate = etUpdateMealDay.getText().toString();
                    mealNameUpdate = etUpdateMealName.getText().toString();
                    mealDifficultyUpdate = etUpdateMealDifficulty.getText().toString();
                    mealTimeUpdate = etUpdateMealTime.getText().toString();
                    mealIngredientsUpdate = etUpdateMealIngredients.getText().toString();
                    mealDirectionsUpdate = etUpdateMealDirections.getText().toString();
                    DBHelper.getInstance(getApplicationContext()).updateMeal(id, mealDayUpdate, mealNameUpdate, mealDifficultyUpdate, mealTimeUpdate, mealIngredientsUpdate, mealDirectionsUpdate);
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
