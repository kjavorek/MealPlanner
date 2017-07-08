package hr.ferit.kristinajavorek.mealplanner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver{

    public static final String NOTIFICATION = "Notification";
    int MID=0;
    String ingredientsToReturn, today, thisMondayDate="";;
    ArrayList<Meal> meals=new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(meals.size()>0) meals.clear();
        meals = DBHelper.getInstance(context).getAllMeals();

        //Monday date
        Calendar calendar = Calendar.getInstance();
        String thisDay = getDay();
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
        thisMondayDate = df.format(calendar.getTime());

        long currentTime = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String msgText = "Buy ingredients";
        msgText = allTodayGroceries();

        if(!msgText.equals("Buy ingredients") && !msgText.equals("") && !msgText.equals(null)) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(NOTIFICATION, "reminder");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.muffin)
                .setColor(10388124)
                .setContentTitle("Grocery List")
                .setContentText(msgText)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(currentTime)
                .setContentIntent(pendingIntent)
                .setLights(Color.BLUE, 2000, 1000)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager.notify(MID, notificationBuilder.build());
        MID++;
        }

    }
    private String allTodayGroceries(){
        ingredientsToReturn="";
        List<String> isCheckedIngredient=new ArrayList<String>(), ingredient=new ArrayList<String>();
        if (isCheckedIngredient.size()>0)isCheckedIngredient.clear();
        if(ingredient.size()>0)ingredient.clear();
        today = getDay();
        for(int i=0;i<meals.size();i++){
            if(meals.get(i).getmDay().equals(today) && checkWeek(i)){
                if(meals.get(i).getmIngredientsIsChecked().contains("0")){
                    isCheckedIngredient = Arrays.asList(meals.get(i).getmIngredientsIsChecked().split("\\s*,\\s*"));
                    ingredient = Arrays.asList(meals.get(i).getmIngredients().split("\\s*,\\s*"));
                    for(int j=0;j<isCheckedIngredient.size();j++){
                        if(isCheckedIngredient.get(j).equals("0")){
                            if(ingredientsToReturn.equals("")) ingredientsToReturn = ingredient.get(j);
                            else ingredientsToReturn += ", " + ingredient.get(j);
                        }
                    }
                }
            }
        }
        return ingredientsToReturn;
    }
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
    private boolean checkWeek(int i){
        String mealWeek = meals.get(i).getmWeekFirstDay();
        if(mealWeek.equals(thisMondayDate)) return true;
        else return false;
    }
}
