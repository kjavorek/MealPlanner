package hr.ferit.kristinajavorek.mealplanner;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class NotificationActivity extends Activity {

    public static final String NOTIFICATION_HOUR= "hour";
    public static final String NOTIFICATION_MINUTE= "minute";

    Button btnNotification, bBack;
    TimePicker timePicker;
    int alarmHour, alarmMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        btnNotification= (Button) findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alarmHour = timePicker.getCurrentHour();
                alarmMinute = timePicker.getCurrentMinute();
                Intent mainActivityIntent = new Intent();
                mainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra(NOTIFICATION_HOUR, String.valueOf(alarmHour));
                mainActivityIntent.putExtra(NOTIFICATION_MINUTE, String.valueOf(alarmMinute));
                startActivity(mainActivityIntent);
            }
        });
        bBack= (Button) findViewById(R.id.bBack);
        bBack.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent();
                mainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
    }
}
