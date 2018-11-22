package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements WebServiceResultHandler{

    private ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String, String>>();
    private ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ShoppingList");
        actionBar.setSubtitle("Utility app to maintain your shopping items");
        itemListView = (ListView) findViewById(R.id.itemListView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllItems();
    }

    private void loadAdapter(){
        SimpleAdapter adapter = new SimpleAdapter(this,items,
                R.layout.item_cell,
                new String[]{"item_name","item_qty","item_price"},
                new int[]{R.id.itemNameTV,R.id.itemQtyTV,R.id.itemPriceTV});
        itemListView.setAdapter(adapter);
    }

    private void loadAllItems(){
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("username",Util.getString(this,Util.USER_NAME,""));
        builder.appendQueryParameter("password",Util.getString(this,Util.PASSWORD,""));
        ConnectWebService connectWebService = new ConnectWebService(this,builder.build().getEncodedQuery());
        connectWebService.execute("http://vasudevkumaran.com/app/getallitems");
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

            case R.id.action_add:
                Intent intentAdd = new Intent(this,AddEditActivity.class);
                startActivity(intentAdd);
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

    @Override
    public void onReceiveResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        items = new ArrayList<HashMap<String, String>>();
        if (jsonObject.getString("result").equals("OK")){
            JSONArray itemsJsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < itemsJsonArray.length(); i++) {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("item_name",itemsJsonArray.getJSONObject(i).getString("item_name"));
                map.put("item_qty",itemsJsonArray.getJSONObject(i).getString("item_qty"));
                map.put("item_price",itemsJsonArray.getJSONObject(i).getString("item_price"));
                items.add(map);
            }
        }
        loadAdapter();

    }
}
