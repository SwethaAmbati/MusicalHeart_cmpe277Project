package edu.project.cmpe277.musicalheart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class AlarmActivity extends Activity {
String delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_createalarm) {

            Intent intent = new Intent(AlarmActivity.this,SetAlarmActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public StringBuilder getSQLMessage(){
        try
        {
            StringBuilder sql = new StringBuilder();
            SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM ReminderMessage", null);
            if(cursor!= null){
                if (cursor.moveToFirst()) {
                    do {
                        String alarmlist = cursor.getString(cursor.getColumnIndex("Category"));
                    }while(cursor.moveToNext());
                }
            }
            return sql;
        }
        catch(Exception e)
        {
            return new StringBuilder("No SQL");
        }
    }
    ArrayList<HashMap<String, String>> mylist ;
    View temp;
    Button temp1;
    Button temp2;
    @Override
    protected void onResume(){
        super.onResume();
        ListView list = (ListView) findViewById(R.id.reminder);
        mylist = new ArrayList<HashMap<String, String>>();
        try
        {
            HashMap<String, String> map= new HashMap<String, String>();
         //   map.put("category", "Reminders");
            mylist.add(map);
            SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM ReminderMessage", null);
            if(cursor!= null){
                if (cursor.moveToFirst()) {
                    do {

                        String alarmlist = cursor.getString(cursor.getColumnIndex("Category"));
                        String id = cursor.getString(cursor.getColumnIndex("ID"));
                        map = new HashMap<String, String>();
                        map.put("id",id);
                        map.put("category", alarmlist);

                        mylist.add(map);
                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Exception e)
        {

        }



        SimpleAdapter mReminder = new SimpleAdapter(this, mylist, R.layout.alarm_list,
                new String[] {"category"}, new int[] {R.id.category});
        list.setAdapter(mReminder);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                temp= view;
                delete = mylist.get(position).get("id");
                Button b = (Button)view.findViewById(R.id.button);
                b.setVisibility(View.VISIBLE);
                temp1 = b;
                Button c = (Button)view.findViewById(R.id.button5);
                c.setVisibility(View.VISIBLE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = openOrCreateDatabase("AlarmList.db", Context.MODE_PRIVATE,null);
                        int i = db.delete("ReminderMessage", "ID" + "=" + delete, null);
                        if(i >0){
                            temp.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp1.setVisibility(View.GONE);
                        Button c = (Button)v.findViewById(R.id.button5);
                        c.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}
