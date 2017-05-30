package hr.ferit.kristinajavorek.mealplanner;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.TreeSet;

public class Adapter extends BaseAdapter {
    private ArrayList<Meal> mMeals;

    public Adapter(ArrayList<Meal> meals) { mMeals = meals; }
    @Override
    public int getCount() { return this.mMeals.size(); }
    @Override
    public Object getItem(int position) { return this.mMeals.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mealViewHolder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.meal_item, parent, false);
            mealViewHolder = new ViewHolder(convertView);
            convertView.setTag(mealViewHolder);
        }
        else{
            mealViewHolder = (ViewHolder) convertView.getTag();
        }
        Meal meal = this.mMeals.get(position);

        //mealViewHolder.tvItemDay.setText(meal.getmDay());
        mealViewHolder.tvItemName.setText(meal.getmName());
        mealViewHolder.tvItemDifficulty.setText(meal.getmDifficulty());
        mealViewHolder.tvItemTime.setText(meal.getmTime());
        //mealViewHolder.tvItemIngredients.setText(meal.getmIngredients());
        //mealViewHolder.tvItemDirections.setText(meal.getmDirections());
        return convertView;
    }

    public void insert(Meal meal) {
        this.mMeals.add(meal);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView tvItemDay, tvItemName, tvItemDifficulty, tvItemTime, tvItemIngredients, tvItemDirections;
        public ImageView ivPriority, ivPriorityLine;
        public ViewHolder(View mealView) {
            //tvItemDay = (TextView) mealView.findViewById(R.id.tvItemDay);
            tvItemName = (TextView) mealView.findViewById(R.id.tvItemName);
            tvItemDifficulty = (TextView) mealView.findViewById(R.id.tvItemDifficulty);
            tvItemTime = (TextView) mealView.findViewById(R.id.tvItemTime);
            //tvItemIngredients = (TextView) mealView.findViewById(R.id.tvItemIngredients);
            //tvItemDirections = (TextView) mealView.findViewById(R.id.tvItemDirections);
        }
    }
    public void deleteAt(int position) {
        this.mMeals.remove(position);
        this.notifyDataSetChanged();
    }

}