package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class reportActivity extends Activity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getActionBar().setTitle("Manutencoop Sticket");
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        Button button = (Button) findViewById(R.id.internet);
        button.setBackgroundColor(Color.TRANSPARENT);

        Button button1 = (Button) findViewById(R.id.plumbing);
        button1.setBackgroundColor(Color.TRANSPARENT);

        Button button2 = (Button) findViewById(R.id.lighting);
        button2.setBackgroundColor(Color.TRANSPARENT);

        Button button3 = (Button) findViewById(R.id.temperature);
        button3.setBackgroundColor(Color.TRANSPARENT);

        Button button4 = (Button) findViewById(R.id.wc);
        button4.setBackgroundColor(Color.TRANSPARENT);

        Button button5 = (Button) findViewById(R.id.entryways);
        button5.setBackgroundColor(Color.TRANSPARENT);

        Button button6 = (Button) findViewById(R.id.electrical);
        button6.setBackgroundColor(Color.TRANSPARENT);

        Button button7 = (Button) findViewById(R.id.janitorial);
        button7.setBackgroundColor(Color.TRANSPARENT);

        Button button8 = (Button) findViewById(R.id.building);
        button8.setBackgroundColor(Color.TRANSPARENT);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        Button button = (Button) v;
        int category = Integer.parseInt((String)button.getTag());
        Intent intent = new Intent(this,selectSticketActivity.class);
        intent.putExtra("CATEGORY", category);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}
