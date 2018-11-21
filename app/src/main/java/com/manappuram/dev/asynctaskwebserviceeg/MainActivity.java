package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ShoppingList");
        actionBar.setSubtitle("Utility app to maintain your shopping items");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_profile:

                break;

            case R.id.action_logout:
                Util.saveInt(this,Util.USER_ID,Util.NOT_LOGGED_IN);
                Util.saveString(this,Util.PASSWORD,"");
                Util.saveString(this,Util.USER_NAME,"");
                Util.showText(this,"You are being logged out....");
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }


        return true;
    }
}
