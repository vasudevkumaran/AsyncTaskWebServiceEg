package com.manappuram.dev.asynctaskwebserviceeg;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("username","dev32424");
        builder.appendQueryParameter("password","12345678");
        builder.appendQueryParameter("firstname","Vasudev");
        builder.appendQueryParameter("lastname","Kumaran");
        builder.appendQueryParameter("gender","1");
        builder.appendQueryParameter("is_business","1");
        builder.appendQueryParameter("is_travel","2");
        builder.appendQueryParameter("is_holidays","1");
        String payload = builder.build().getEncodedQuery();
        CallWebService callWebService = new CallWebService(payload);
        callWebService.execute("http://vasudevkumaran.com/app/registration");

    }

    private class CallWebService extends AsyncTask<String,Void,String>{
        private String mPayload;
        public CallWebService(String payload){
                mPayload = payload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(mPayload);
                bufferedWriter.flush();
                outputStream.flush();
                outputStreamWriter.flush();
                outputStream.close();

                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line).append("\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.w("WSRESPONSE",result);
        }
    }
}
