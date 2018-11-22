package com.manappuram.dev.asynctaskwebserviceeg;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEditActivity extends AppCompatActivity implements WebServiceResultHandler{

    private EditText itemNameEd;
    private EditText itemPriceEd;
    private EditText itemQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add");
        actionBar.setDisplayHomeAsUpEnabled(true);
        itemNameEd = (EditText)findViewById(R.id.itemNameEd);
        itemPriceEd = (EditText)findViewById(R.id.itemPriceEd);
        itemQty = (EditText)findViewById(R.id.itemQtyEd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("itemname",itemNameEd.getText().toString());
            builder.appendQueryParameter("itemprice",itemPriceEd.getText().toString());
            builder.appendQueryParameter("itemqty",itemQty.getText().toString());
            builder.appendQueryParameter("username",Util.getString(this,Util.USER_NAME,""));
            builder.appendQueryParameter("password",Util.getString(this,Util.PASSWORD,""));
            String payload = builder.build().getEncodedQuery();
            ConnectWebService connectWebService = new ConnectWebService(this,payload);
            connectWebService.execute("http://vasudevkumaran.com/app/additem");

        }else{
            finish();
        }
        return true;
    }

    @Override
    public void onReceiveResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("result").equals("OK")){
            Util.showText(this,"Item Added Successfully");
            finish();
        }else{
            Util.showText(this,"Item Could not be added or already existing");
        }
    }
}
