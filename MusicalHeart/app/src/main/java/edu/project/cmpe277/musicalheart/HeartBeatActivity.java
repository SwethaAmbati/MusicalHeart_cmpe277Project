package edu.project.cmpe277.musicalheart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HeartBeatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_beat);

        boolean isReminderIntent = getIntent().getBooleanExtra("reminder", false);
        if(isReminderIntent)
            showReminder();
    }

    protected void showReminder(){
        String message = getIntent().getStringExtra("message");
        Intent local = new Intent(this,AlarmNotification.class);
        local.putExtra("message", message);
        startService(local);
    }

    @Override
    public void onResume(){
        boolean isReminderIntent = getIntent().getBooleanExtra("reminder", false);
        if(isReminderIntent)
            showReminder();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_heart_beat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_alarm) {
            Intent intent = new Intent(HeartBeatActivity.this,AlarmActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
