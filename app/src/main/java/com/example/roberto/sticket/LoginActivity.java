package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().setTitle("Manutencoop Sticket");
        Button button = (Button) findViewById(R.id.login);
        button.setBackgroundColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void login(View v){
        EditText usernameField = (EditText) findViewById(R.id.username);
        String username = usernameField.getText().toString();
        if (username.equals(null)){
            username = usernameField.getHint().toString();
        }
        EditText passwordField = (EditText) findViewById(R.id.password);
        String password = passwordField.getText().toString();
        if (password.equals(null)){
            password = passwordField.getHint().toString();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERNAME",username);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
    }
}
