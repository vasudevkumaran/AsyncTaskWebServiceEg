package com.manappuram.dev.asynctaskwebserviceeg;

import android.content.Context;
import android.widget.Toast;

public class Util {

    public static void showText(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
