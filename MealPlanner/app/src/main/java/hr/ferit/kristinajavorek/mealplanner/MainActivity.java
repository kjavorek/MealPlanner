package hr.ferit.kristinajavorek.mealplanner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.String.valueOf;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MEAL_ID = "Id";
    public static final String DAY_UPDATE = "Day";
    public static final String NAME_UPDATE = "Name";
    public static final String DIFFICULTY_UPDATE = "Difficulty";
    public static final String TIME_UPDATE = "Time";
    public static final String INGREDIENTS_UPDATE = "Ingredients";
    public static final String DIRECTIONS_UPDATE = "Directions";

    ListView lvMealList;
    String mealDay, mealName, mealDifficulty, mealTime, mealIngredients, mealDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.lvMealList= (ListView) findViewById(R.id.lvMealList);
        final ArrayList<Meal> meals = this.loadMeals();
        final Adapter mealAdapter = new Adapter (meals);
        this.lvMealList.setAdapter(mealAdapter);

        Intent addedMealIntent = this.getIntent();
        if(addedMealIntent.hasExtra(AddMealActivity.MEAL_NAME)){
            mealDay = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DAY);
            mealName = addedMealIntent.getStringExtra(AddMealActivity.MEAL_NAME);
            mealDifficulty = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DIFFICULTY);
            mealTime = addedMealIntent.getStringExtra(AddMealActivity.MEAL_TIME);
            mealIngredients = addedMealIntent.getStringExtra(AddMealActivity.MEAL_INGREDIENTS);
            mealDirections = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DIRECTIONS);

            Meal meal = new Meal(7, mealDay, mealName, mealDifficulty, mealTime, mealIngredients, mealDirections);
            long id = DBHelper.getInstance(getApplicationContext()).insertMeal(meal);
            meal.setmId((int)id);
            Adapter adapter = (Adapter) lvMealList.getAdapter();
            adapter.insert(meal);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Clicked", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent();
                addIntent.setClass(getApplicationContext(), AddMealActivity.class);
                startActivity(addIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);  //enable colored icons in drawer
        navigationView.setNavigationItemSelectedListener(this);

        this.lvMealList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), UpdateMealActivity.class);
                Meal meal = (Meal)mealAdapter.getItem(position);
                intent.putExtra(MEAL_ID,valueOf(meal.getmId()));
                intent.putExtra(DAY_UPDATE,meal.getmDay());
                intent.putExtra(NAME_UPDATE,meal.getmName());
                intent.putExtra(DIFFICULTY_UPDATE,meal.getmDifficulty());
                intent.putExtra(TIME_UPDATE,valueOf(meal.getmTime()));
                intent.putExtra(INGREDIENTS_UPDATE,meal.getmIngredients());
                intent.putExtra(DIRECTIONS_UPDATE,meal.getmDirections());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.today_meals) {
            Toast.makeText(getApplicationContext(),"today_meals", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.today_groceries) {
            Toast.makeText(getApplicationContext(),"today_groceries", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.week_meals) {
            Toast.makeText(getApplicationContext(),"week_meals", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.week_groceries) {
            Toast.makeText(getApplicationContext(),"week_groceries", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<Meal> loadMeals(){
        return DBHelper.getInstance(this).getAllMeals();
    }
}
