package hr.ferit.kristinajavorek.mealplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MEAL_ID = "Id";
    public static final String DAY_UPDATE = "Day";
    public static final String NAME_UPDATE = "Name";
    public static final String DIFFICULTY_UPDATE = "Difficulty";
    public static final String TIME_UPDATE = "Time";
    public static final String INGREDIENTS_UPDATE = "Ingredients";
    public static final String INGREDIENTS_ISCHECKED_UPDATE = "IngredientsIsChecked";
    public static final String DIRECTIONS_UPDATE = "Directions";
    public static final String TODAY = "Today";

    ListView lvMealList;
    String mealDay, mealName, mealDifficulty, mealTime, mealIngredients, mealIngredientsIsChecked, mealDirections;
    Boolean todayMeal=false, todayGroceries=false, weekMeal=false, weekGroceries=false;
    ArrayList<Meal> meals=new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        this.lvMealList= (ListView) findViewById(R.id.lvMealList);
        meals = this.loadMeals();
        if (weekMeal) weekMeals();
        else {
            todayMeal=true; todayGroceries=false; weekMeal=false; weekGroceries=false;
            todayMeals();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent();
                addIntent.setClass(getApplicationContext(), AddMealActivity.class);
                if (todayMeal) addIntent.putExtra(TODAY, getDay());
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
        // Handle navigation view item clicks
        int id = item.getItemId();

        if (id == R.id.today_meals) {
            todayMeal=true; todayGroceries=false; weekMeal=false; weekGroceries=false;
            todayMeals();
            //Toast.makeText(getApplicationContext(),"today_meals", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.today_groceries) {
            todayMeal=false; todayGroceries=true; weekMeal=false; weekGroceries=false;
            todayGroceriesFunction();
            //Toast.makeText(getApplicationContext(),"today_groceries", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.week_meals) {
            todayMeal=false; todayGroceries=false; weekMeal=true; weekGroceries=false;
            weekMeals();
            //Toast.makeText(getApplicationContext(),"week_meals", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.week_groceries) {
            todayMeal=false; todayGroceries=false; weekMeal=false; weekGroceries=true;
            Toast.makeText(getApplicationContext(),"week_groceries", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<Meal> loadMeals(){ return DBHelper.getInstance(this).getAllMeals(); }

    private String getDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String today="";
        switch (day) {
            case Calendar.MONDAY:
                today="Monday";
                break;
            case Calendar.TUESDAY:
                today="Tuesday";
                break;
            case Calendar.WEDNESDAY:
                today="Wednesday";
                break;
            case Calendar.THURSDAY:
                today="Thursday";
                break;
            case Calendar.FRIDAY:
                today="Friday";
                break;
            case Calendar.SATURDAY:
                today="Saturday";
                break;
            case Calendar.SUNDAY:
                today="Sunday";
                break;
        }
        return today;
    }
    private void weekMeals(){
        fab.setVisibility(VISIBLE);
        final Adapter mealAdapter = new Adapter (meals);
        this.lvMealList.setAdapter(mealAdapter);
        addNewMeal();
        updateExistingMeal();
        clickListeners(mealAdapter);
    }
    ListAdapter ingredientsAdapter;
    List<String> groceriesList = new ArrayList<String>();
    List<Integer> mealNameList = new ArrayList<Integer>();
    List<String> isChecked = new ArrayList<String>();
    String today;

    private void todayGroceriesFunction(){
        fab.setVisibility(INVISIBLE);
        String ingredients, mealName;
        today = getDay();
        if (groceriesList.size()>0) groceriesList.clear();
        if (mealNameList.size()>0) mealNameList.clear();
        if (isChecked.size()>0) isChecked.clear();
        for(int i=0;i<meals.size();i++){
            if(meals.get(i).getmDay().equals(today)){
                mealName=meals.get(i).getmName();
                groceriesList.add(mealName);
                isChecked.add(mealName);
                mealNameList.add(groceriesList.size()-1);
                isChecked.addAll(Arrays.asList(meals.get(i).getmIngredientsIsChecked().split("\\s*,\\s*")));
                ingredients=meals.get(i).getmIngredients();
                groceriesList.addAll(Arrays.asList(ingredients.split("\\s*,\\s*")));
            }
        }
        ingredientsAdapter = new IngredientsAdapter(groceriesList, mealNameList, isChecked);
        this.lvMealList.setAdapter(ingredientsAdapter);
    }
    public void handleCheckBoxOnClick(View v){
        CheckBox checkBox = (CheckBox) v;
        CharSequence p=checkBox.getHint(); //Item position in a groceries list
        Integer itemPosition = Integer.parseInt(p.toString());
        Integer positionInMealsList = 0;

        String cliked = groceriesList.get(itemPosition);

        //Find meal which ingredient is clicked
        for(int i=0;i<mealNameList.size();i++){
            if(mealNameList.get(i)<itemPosition) positionInMealsList=i;
        }
        Meal checkedMeal = meals.get(positionInMealsList);
        String ingredients=checkedMeal.getmIngredients();

        String setIngredientsIsClicked="";
        List<String> isChecked = Arrays.asList(checkedMeal.getmIngredientsIsChecked().split("\\s*,\\s*"));
        List<String> items = Arrays.asList(ingredients.split("\\s*,\\s*"));

        for(int i=0;i<items.size();i++){
            if(items.get(i).equals(cliked)){
                if(checkBox.isChecked()) {
                    if (setIngredientsIsClicked.equals("")) setIngredientsIsClicked = "1";
                    else setIngredientsIsClicked += ",1";
                }
                else{
                    if (setIngredientsIsClicked.equals("")) setIngredientsIsClicked = "0";
                    else setIngredientsIsClicked += ",0";
                }
            }
            else {
                if(setIngredientsIsClicked.equals("")) setIngredientsIsClicked=isChecked.get(i);
                else setIngredientsIsClicked+=","+isChecked.get(i);
            }
        }
        checkedMeal.setmIngredientsIsChecked(setIngredientsIsClicked);
        //Update ingredients check list in DB
        Integer id=checkedMeal.getmId();
        String mealIngredientsIsCheckedUpdate=setIngredientsIsClicked;
        DBHelper.getInstance(getApplicationContext()).updateIngredientsIsChecked(id, mealIngredientsIsCheckedUpdate);
        meals = this.loadMeals();
    }
    private void todayMeals(){
        fab.setVisibility(VISIBLE);
        final ArrayList<Meal> todayMeals = new ArrayList<Meal>();
        String today = getDay();
        for(int i=0;i<meals.size();i++){
            if(meals.get(i).getmDay().equals(today)){
                todayMeals.add(meals.get(i));
            }
        }
        final Adapter mealAdapter = new Adapter (todayMeals);
        this.lvMealList.setAdapter(mealAdapter);
        clickListeners(mealAdapter);
        addNewMeal();
        updateExistingMeal();
    }
    private void clickListeners(final Adapter mealAdapter){
            this.lvMealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(todayMeal || weekMeal) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), UpdateMealActivity.class);
                        Meal meal = (Meal) mealAdapter.getItem(position);
                        intent.putExtra(MEAL_ID, valueOf(meal.getmId()));
                        intent.putExtra(DAY_UPDATE, meal.getmDay());
                        intent.putExtra(NAME_UPDATE, meal.getmName());
                        intent.putExtra(DIFFICULTY_UPDATE, meal.getmDifficulty());
                        intent.putExtra(TIME_UPDATE, valueOf(meal.getmTime()));
                        intent.putExtra(INGREDIENTS_UPDATE, meal.getmIngredients());
                        intent.putExtra(INGREDIENTS_ISCHECKED_UPDATE, meal.getmIngredientsIsChecked());
                        intent.putExtra(DIRECTIONS_UPDATE, meal.getmDirections());
                        startActivity(intent);
                    }
                }
            });
            this.lvMealList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if(todayMeal || weekMeal) {
                        Meal meal = (Meal) mealAdapter.getItem(position);
                        int mId = meal.getmId();
                        DBHelper.getInstance(getApplicationContext()).deleteMeal(mId);
                        Adapter adapter = (Adapter) lvMealList.getAdapter();
                        adapter.deleteAt(position);
                        meals = loadMeals();
                        return true;
                    }
                    else return false;
                }
            });
    }
    private void addNewMeal(){
        Intent addedMealIntent = this.getIntent();
        if(addedMealIntent.hasExtra(AddMealActivity.MEAL_NAME)){
            mealDay = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DAY);
            mealName = addedMealIntent.getStringExtra(AddMealActivity.MEAL_NAME);
            mealDifficulty = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DIFFICULTY);
            mealTime = addedMealIntent.getStringExtra(AddMealActivity.MEAL_TIME);
            mealIngredients = addedMealIntent.getStringExtra(AddMealActivity.MEAL_INGREDIENTS);
            mealIngredientsIsChecked = addedMealIntent.getStringExtra(AddMealActivity.MEAL_INGREDIENTS_ISCHECKED);
            mealDirections = addedMealIntent.getStringExtra(AddMealActivity.MEAL_DIRECTIONS);

            Meal meal = new Meal(7, mealDay, mealName, mealDifficulty, mealTime, mealIngredients, mealIngredientsIsChecked, mealDirections);
            long id = DBHelper.getInstance(getApplicationContext()).insertMeal(meal);
            meal.setmId((int)id);
            Adapter adapter = (Adapter) lvMealList.getAdapter();
            adapter.insert(meal);
            meals = this.loadMeals();
        }
    }
    private void updateExistingMeal(){
        Intent updatedMealIntent = this.getIntent();
        if(updatedMealIntent.hasExtra(UpdateMealActivity.UPDATE_FINISH)) meals = this.loadMeals();
    }
}
