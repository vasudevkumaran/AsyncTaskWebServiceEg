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
    private String itemId = "-1";


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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //Edit
            itemId = bundle.getString(Util.ITEM_ID);
            itemNameEd.setText(bundle.getString(Util.ITEM_NAME));
            itemPriceEd.setText(bundle.getString(Util.ITEM_PRICE));
            itemQty.setText(bundle.getString(Util.ITEM_QTY));
            actionBar.setTitle("Edit");
        }else{
            //Add
            itemId = "-1";
        }
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
            if (!itemId.equals("-1")){
                //edit
                builder.appendQueryParameter("itemid",itemId);
                String payload = builder.build().getEncodedQuery();
                ConnectWebService connectWebService = new ConnectWebService(this,payload);
                connectWebService.execute("http://vasudevkumaran.com/app/updateitem");
            }else{
                //add
                String payload = builder.build().getEncodedQuery();
                ConnectWebService connectWebService = new ConnectWebService(this,payload);
                connectWebService.execute("http://vasudevkumaran.com/app/additem");
            }




        }else{
            finish();
        }
        return true;
    }

    @Override
    public void onReceiveResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("result").equals("OK")){
            if (itemId.equals("-1")) {
                Util.showText(this, "Item Added Successfully");
            }else{
                Util.showText(this, "Item Edited Successfully");
            }
            finish();
        }else{
            Util.showText(this,"Item Could not be added or already existing");
        }
    }
}
