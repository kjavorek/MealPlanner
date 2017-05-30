package hr.ferit.kristinajavorek.mealplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;


public class IngredientsAdapter extends BaseAdapter {
    private static final int TYPE_NAME = 0;
    private static final int TYPE_INGREDIENT = 1;

    private List<String> mIngredients;
    private List<Integer> mMealName;
    private List<String> mIngredientsIsChecked;

    public IngredientsAdapter (List<String> ingredients, List<Integer> mealName, List<String> isChecked) {
        mIngredients=ingredients; mMealName=mealName; mIngredientsIsChecked=isChecked;
    }

    @Override
    public int getCount() { return this.mIngredients.size(); }

    @Override
    public Object getItem(int position) { return this.mIngredients.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getViewTypeCount() { return 2; }

    @Override
    public int getItemViewType(int position) { return mMealName.contains(position) ? TYPE_NAME : TYPE_INGREDIENT; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder ingredientViewHolder;
        int type = getItemViewType(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ingredientViewHolder=new ViewHolder();
            switch (type) {
                case TYPE_NAME:
                    convertView = inflater.inflate(R.layout.ingredient_group, parent, false);
                    ingredientViewHolder.tvList = (TextView) convertView.findViewById(R.id.tvIngredientGroup);
                    break;
                case TYPE_INGREDIENT:
                    convertView = inflater.inflate(R.layout.ingredient_item, parent, false);
                    ingredientViewHolder.cbList = (CheckBox) convertView.findViewById(R.id.cbIngredientList);
                    break;
            }
            convertView.setTag(ingredientViewHolder);
        }
        else{
            ingredientViewHolder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case TYPE_NAME:
                ingredientViewHolder.tvList.setText(mIngredients.get(position));
                String name = valueOf(position);
                ingredientViewHolder.tvList.setHint(name);
                break;
            case TYPE_INGREDIENT:
                ingredientViewHolder.cbList.setText(mIngredients.get(position));
                String ingredient = valueOf(position);
                ingredientViewHolder.cbList.setHint(ingredient);
                if(mIngredientsIsChecked.get(position).equals("0")) ingredientViewHolder.cbList.setChecked(false);
                else if (mIngredientsIsChecked.get(position).equals("1")) ingredientViewHolder.cbList.setChecked(true);
                break;
        }
        return convertView;
    }
    public static class ViewHolder {
        public TextView tvList;
        public CheckBox cbList;
    }
}
