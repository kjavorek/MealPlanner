package hr.ferit.kristinajavorek.mealplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecipesAdapter extends BaseAdapter {

    LayoutInflater inflation=null;
    Context mycontext=null;
    RecipesParser myparseobj=null;
    String[] title_array=null, description_array=null, link_array=null;

    RecipesAdapter(Context c,String url)
    {
        mycontext=c;
        inflation=LayoutInflater.from(mycontext);
        myparseobj=new RecipesParser();
        title_array=myparseobj.xmlParsing(url,"item","title");
        link_array=myparseobj.xmlParsing(url,"item","link");
        description_array=myparseobj.xmlParsing(url,"item","description");
    }

    @Override
    public int getCount() {
        return title_array.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTitle(int position){ return title_array[position]; }

    public String getLink(int position){ return link_array[position]; }

    public String getIngredient(int position){ return description_array[position]; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = new MyHolder();
        convertView=null;
        if (convertView == null) {
            convertView = inflation.inflate(R.layout.recipes_item, null);
            holder.tvRecipes = (TextView) convertView.findViewById(R.id.tvRecipes);
        } else {
            holder.tvRecipes = (TextView) convertView.findViewById(R.id.tvRecipes);
        }
        holder.tvRecipes.setText(title_array[position]);
        return convertView;
    }

    static class MyHolder { TextView tvRecipes=null; }
}

