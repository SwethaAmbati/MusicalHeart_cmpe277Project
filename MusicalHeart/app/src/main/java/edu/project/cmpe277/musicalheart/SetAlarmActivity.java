package edu.project.cmpe277.musicalheart;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class SetAlarmActivity extends Activity {



    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    Intent mainintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        setup();
    }

    private void setup() {
        mainintent = new Intent(SetAlarmActivity.this,AlarmService.class);
        am = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
    }

    @Override
    public void onResume(){
        super.onResume();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_alarm, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
            TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            cal.set(Calendar.MONTH, datePicker.getMonth());
            cal.set(Calendar.YEAR, datePicker.getYear());
            cal.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

            long time = cal.getTimeInMillis();
            String displayTime = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString() + " on " + datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();

            pi = PendingIntent.getBroadcast(this, 0, mainintent,  0 );
            am.set( AlarmManager.RTC_WAKEUP, time, pi);

            SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);

            String datetime = DateFormat.getDateTimeInstance().format(new Date());
            try
            {
                db.execSQL("CREATE TABLE IF NOT EXISTS ReminderMessage(ID INTEGER PRIMARY KEY , Message VARCHAR, Time VARCHAR, Category VARCHAR); ");
                db.execSQL("INSERT INTO ReminderMessage (Time, Category)  VALUES ('"+datetime+"','"+displayTime+"');");
                Toast.makeText(SetAlarmActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
            catch(Exception e)
            {
                Toast.makeText(SetAlarmActivity.this, "Error in saving message", Toast.LENGTH_LONG).show();
            }
            SetAlarmActivity.this.finish();

            return true;
        }

        else if (id == R.id.action_cancel) {
            SetAlarmActivity.this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




}

