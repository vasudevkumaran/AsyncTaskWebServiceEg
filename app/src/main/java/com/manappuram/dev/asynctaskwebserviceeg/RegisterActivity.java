package com.manappuram.dev.asynctaskwebserviceeg;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements WebServiceResultHandler {


    private EditText unameEd;
    private EditText pwdEd;
    private EditText firstNameEd;
    private EditText lastNameEd;
    private RadioButton maleRd;
    private RadioButton femaleRd;
    private CheckBox bizCb;
    private CheckBox travelCb;
    private CheckBox holidaysCb;
    private int formType = Util.REGISTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register");
        actionBar.setDisplayHomeAsUpEnabled(true);
        unameEd = (EditText)findViewById(R.id.uname_ed);
        pwdEd = (EditText)findViewById(R.id.pass_ed);
        firstNameEd = (EditText)findViewById(R.id.fname_ed);
        lastNameEd = (EditText)findViewById(R.id.lname_ed);
        maleRd = (RadioButton)findViewById(R.id.male_rb);
        femaleRd = (RadioButton)findViewById(R.id.female_rb);
        bizCb = (CheckBox)findViewById(R.id.biz_cb);
        travelCb = (CheckBox)findViewById(R.id.travel_cb);
        holidaysCb = (CheckBox)findViewById(R.id.holidays_cb);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            formType = bundle.getInt(Util.TYPE);
            if (formType == Util.UPDATE){
                actionBar.setTitle("My Account");
                //restore all values from preference
                unameEd.setText(Util.getString(this,Util.USER_NAME,""));
                unameEd.setEnabled(false);
                pwdEd.setText(Util.getString(this,Util.PASSWORD,""));
                firstNameEd.setText(Util.getString(this,Util.USER_FULL_NAME,""));
                lastNameEd.setText(Util.getString(this,Util.USER_LAST_NAME,""));
                if (Util.getString(this,Util.USER_GENDER,"1").equals("1")){
                    maleRd.setChecked(true);
                }else{
                    femaleRd.setChecked(true);
                }

                if (Util.getString(this,Util.IS_TRAVEL,"2").equals("1")){
                    travelCb.setChecked(true);
                }

                if (Util.getString(this,Util.IS_BUSINESS,"2").equals("1")){
                    bizCb.setChecked(true);
                }

                if (Util.getString(this,Util.IS_HOLIDAYS,"2").equals("1")){
                    holidaysCb.setChecked(true);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.action_save:
                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("username",unameEd.getText().toString());
                builder.appendQueryParameter("password",pwdEd.getText().toString());
                builder.appendQueryParameter("firstname",firstNameEd.getText().toString());
                builder.appendQueryParameter("lastname",lastNameEd.getText().toString());
                if (maleRd.isChecked()){
                    builder.appendQueryParameter("gender","1");
                }else{
                    builder.appendQueryParameter("gender","2");
                }
                if (bizCb.isChecked()){
                    builder.appendQueryParameter("is_business","1");
                }else{
                    builder.appendQueryParameter("is_business","2");
                }
                if (travelCb.isChecked()){
                    builder.appendQueryParameter("is_travel","1");
                }else{
                    builder.appendQueryParameter("is_travel","2");
                }
                if (holidaysCb.isChecked()){
                    builder.appendQueryParameter("is_holidays","1");
                }else{
                    builder.appendQueryParameter("is_holidays","2");
                }
                String payload = builder.build().getEncodedQuery();
                ConnectWebService connectWebService = null;
                if (formType == Util.REGISTER){
                    connectWebService = new ConnectWebService(this,payload,Util.OTHER);
                    connectWebService.execute("http://vasudevkumaran.com/app/registration");
                }else{
                    //update
                    connectWebService = new ConnectWebService(this,payload,Util.UPDATE);
                    connectWebService.execute("http://vasudevkumaran.com/app/registrationupdate");
                }

                break;
        }
        return true;
    }

    @Override
    public void onReceiveResult(int who, String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (who == Util.OTHER) {
            if (jsonObject.getString("result").equals("OK")) {
                Util.showText(this, jsonObject.getString("message"));
                finish();
            } else {
                Util.showText(this, jsonObject.getString("message"));
            }
        }else if (who == Util.UPDATE){
            if (jsonObject.getString("result").equals("OK")) {
                Util.showText(this, jsonObject.getString("message"));
                //update local preference values
                JSONArray dataArray = jsonObject.getJSONArray("data");
                JSONObject userObject = dataArray.getJSONObject(0);
                Util.saveInt(this,Util.USER_ID,userObject.getInt("user_id"));
                Util.saveString(this,Util.USER_NAME,userObject.getString("user_name"));
                Util.saveString(this,Util.PASSWORD,userObject.getString("user_password"));
                Util.saveString(this,Util.USER_FULL_NAME,userObject.getString("user_full_name"));
                Util.saveString(this,Util.USER_LAST_NAME,userObject.getString("user_last_name"));
                Util.saveString(this,Util.USER_GENDER,userObject.getString("user_gender"));
                Util.saveString(this,Util.IS_TRAVEL,userObject.getString("is_travel"));
                Util.saveString(this,Util.IS_BUSINESS,userObject.getString("is_business"));
                Util.saveString(this,Util.IS_HOLIDAYS,userObject.getString("is_holidays"));
                finish();
            } else {
                Util.showText(this, jsonObject.getString("message"));
            }
        }

    }
}
