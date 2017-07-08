package hr.ferit.kristinajavorek.mealplanner;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RecipesActivity extends Activity {

    private static final String RSSFeed = "http://www.dietsinreview.com/recipes.rss";
    public static final String TITLE = "Title";
    public static final String LINK = "Link";
    public static final String INGREDIENTS= "Ingredients";
    public static final String WEEK_DAY = "This day";
    public static final String WEEK_NUM = "Week number";

    ListView listcomp=null;
    RecipesAdapter adapt_obj=null;
    Context myref=null;
    private SwipeRefreshLayout swipeContainer;
    boolean created=false;
    Button bBackRecipes;
    String weekDay="", weekNum="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        listcomp = (ListView) findViewById(R.id.mylistview);
        bBackRecipes= (Button) findViewById(R.id.bBackRecipes);
        bBackRecipes.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent();
                mainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });

        Intent mainMealIntent = this.getIntent();
        if(mainMealIntent.hasExtra(MainActivity.WEEK_NUM)) {
            weekDay = mainMealIntent.getStringExtra(MainActivity.WEEK_DAY);
            weekNum = mainMealIntent.getStringExtra(MainActivity.WEEK_NUM);
        }

        if(isNetworkAvailable()) {
            myref = RecipesActivity.this;
            new MyAsyncTask().execute(); // Call the Async Task
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchTimelineAsync(0);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
        else{
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.recipesLayout);
            TextView tvInternet = new TextView(RecipesActivity.this);
            tvInternet.setText("There is no internet connection.");
            tvInternet.setTextColor(Color.LTGRAY);
            tvInternet.setTextSize(20);
            tvInternet.setWidth(750);
            tvInternet.setHeight(300);
            tvInternet.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            layout.addView(tvInternet);
        }
    }

    public void fetchTimelineAsync(int page) {
        created=true;
        new MyAsyncTask().execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        private final ProgressDialog dialog=new ProgressDialog(RecipesActivity.this);

        @Override
        protected Void doInBackground(Void... params) {
            adapt_obj=new RecipesAdapter(myref, RSSFeed);
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            if(!created) {
                dialog.setMessage("Loading ...");
                dialog.show();
                dialog.setCancelable(false);
            }
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if(dialog.isShowing() == true)
            {
                dialog.dismiss();
            }
            else swipeContainer.setRefreshing(false);

            listcomp.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listcomp.setAdapter(adapt_obj);
                    view.getResources();
                    String link = adapt_obj.getLink(position);
                    Uri uri = Uri.parse(link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            listcomp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String title, link, ingredients, ingredient, temp, ingredientsString="";
                    Integer ingredientsCounter = 0, lastIndex=0;
                    title = adapt_obj.getTitle(position);
                    link = adapt_obj.getLink(position);
                    ingredients = adapt_obj.getIngredient(position);
                    temp = ingredients;

                    while(lastIndex != -1){
                        lastIndex = ingredients.indexOf("<li>",lastIndex);
                        if(lastIndex != -1){
                            ingredientsCounter ++;
                            ingredient = temp.substring(temp.indexOf("<li>")+4, temp.indexOf("</li>"));
                            temp = temp.substring(temp.indexOf("</li>")+5);
                            if(ingredientsString=="") ingredientsString=ingredient;
                            else ingredientsString += "," + ingredient;
                            lastIndex += "<li>".length();
                        }
                    }

                    Intent addIntent = new Intent();
                    addIntent.setClass(getApplicationContext(), AddMealActivity.class);
                    addIntent.putExtra(TITLE, title);
                    addIntent.putExtra(LINK, link);
                    addIntent.putExtra(INGREDIENTS, ingredientsString);
                    addIntent.putExtra(WEEK_DAY, weekDay);
                    addIntent.putExtra(WEEK_NUM, weekNum);
                    startActivity(addIntent);
                    return true;
                }
            });

            listcomp.setAdapter(adapt_obj);
            adapt_obj.notifyDataSetChanged();
        }
    }



}

