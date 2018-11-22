package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements WebServiceResultHandler, AdapterView.OnItemClickListener {

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
        itemListView.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllItems();
    }

    private void loadAdapter(){
        SimpleAdapter adapter = new SimpleAdapter(this,items,
                R.layout.item_cell,
                new String[]{Util.ITEM_NAME,Util.ITEM_QTY,Util.ITEM_PRICE},
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
                map.put(Util.ITEM_NAME,itemsJsonArray.getJSONObject(i).getString("item_name"));
                map.put(Util.ITEM_QTY,itemsJsonArray.getJSONObject(i).getString("item_qty"));
                map.put(Util.ITEM_PRICE,itemsJsonArray.getJSONObject(i).getString("item_price"));
                map.put(Util.ITEM_ID,itemsJsonArray.getJSONObject(i).getString("item_id"));
                items.add(map);
            }
        }
        loadAdapter();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> item = items.get(position);
        Intent intent = new Intent(this,AddEditActivity.class);
        intent.putExtra(Util.ITEM_ID,item.get(Util.ITEM_ID));
        intent.putExtra(Util.ITEM_NAME,item.get(Util.ITEM_NAME));
        intent.putExtra(Util.ITEM_QTY,item.get(Util.ITEM_QTY));
        intent.putExtra(Util.ITEM_PRICE,item.get(Util.ITEM_PRICE));
        startActivity(intent);
    }
}
