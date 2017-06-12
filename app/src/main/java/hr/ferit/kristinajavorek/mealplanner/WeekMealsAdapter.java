package hr.ferit.kristinajavorek.mealplanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class WeekMealsAdapter extends BaseAdapter {
    private static final int TYPE_DAY = 0;
    private static final int TYPE_MEAL = 1;

    private List<String> mDayMealList;
    private List<Meal> mMealList;
    private List<Integer> mDayList;

    public WeekMealsAdapter (List<String> dayMealList, List<Meal> mealList, List<Integer> dayList) {
        mDayMealList=dayMealList; mMealList=mealList; mDayList=dayList;
    }

    @Override
    public int getCount() { return this.mDayMealList.size(); }

    @Override
    public Object getItem(int position) {
        if(mDayList.contains(position)) return 1;
        else return this.mMealList.get(parseInt(this.mDayMealList.get(position)));
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getViewTypeCount() { return 2; }

    @Override
    public int getItemViewType(int position) {
        if(mDayList.contains(position)) return TYPE_DAY;
        else return TYPE_MEAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeekMealsAdapter.ViewHolder weekMealsViewHolder;
        int type = getItemViewType(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            weekMealsViewHolder=new WeekMealsAdapter.ViewHolder();
            switch (type) {
                case TYPE_DAY:
                    convertView = inflater.inflate(R.layout.ingredient_day, parent, false);
                    weekMealsViewHolder.tvList = (TextView) convertView.findViewById(R.id.tvIngredientDay);
                    break;
                case TYPE_MEAL:
                    convertView = inflater.inflate(R.layout.meal_item, parent, false);
                    weekMealsViewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
                    weekMealsViewHolder.tvItemDifficulty = (TextView) convertView.findViewById(R.id.tvItemDifficulty);
                    weekMealsViewHolder.tvItemTime = (TextView) convertView.findViewById(R.id.tvItemTime);
                    break;
            }
            convertView.setTag(weekMealsViewHolder);
        }
        else{
            weekMealsViewHolder = (WeekMealsAdapter.ViewHolder) convertView.getTag();
        }
        switch (type) {
            case TYPE_DAY:
                weekMealsViewHolder.tvList.setText(mDayMealList.get(position));
                //String day = valueOf(position);
                //weekMealsViewHolder.tvList.setHint(day);
                break;
            case TYPE_MEAL:
                Meal meal = this.mMealList.get(parseInt(mDayMealList.get(position)));
                weekMealsViewHolder.tvItemName.setText(meal.getmName());
                weekMealsViewHolder.tvItemDifficulty.setText(meal.getmDifficulty());
                weekMealsViewHolder.tvItemTime.setText(meal.getmTime());
                //String name = valueOf(position);
                //weekMealsViewHolder.tvList.setHint(name);
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvList, tvItemName, tvItemDifficulty, tvItemTime;
    }

}
