package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements WebServiceResultHandler{

    private EditText userNameEd;
    private EditText passEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ShoppingListApp");
        actionBar.setSubtitle("Login");
        userNameEd = (EditText)findViewById(R.id.uname_ed);
        passEd =(EditText)findViewById(R.id.pass_ed);
        if (Util.getInt(this,Util.USER_ID,Util.NOT_LOGGED_IN) != Util.NOT_LOGGED_IN){
            //logged in already
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onLoginPressed(View view){
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("username",userNameEd.getText().toString());
        builder.appendQueryParameter("password",passEd.getText().toString());
        String payload = builder.build().getEncodedQuery();
        ConnectWebService connectWebService = new ConnectWebService(this,payload);
        connectWebService.execute("http://vasudevkumaran.com/app/login");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onReceiveResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("result").equals("OK")){
            Util.showText(this,"Logged in Successfully");
            JSONArray dataArray = jsonObject.getJSONArray("data");
            JSONObject userObject = dataArray.getJSONObject(0);
            Util.saveInt(this,Util.USER_ID,userObject.getInt("user_id"));
            Util.saveString(this,Util.USER_NAME,userObject.getString("user_name"));
            Util.saveString(this,Util.PASSWORD,userObject.getString("user_password"));
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Util.saveInt(this,Util.USER_ID,Util.NOT_LOGGED_IN);
            Util.saveString(this,Util.USER_NAME,"");
            Util.saveString(this,Util.PASSWORD,"");
            Util.showText(this,"Login Failed");
        }
    }
}
