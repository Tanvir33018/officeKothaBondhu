package net.islbd.kothabondhu.utility;

import android.content.Context;
import android.content.SharedPreferences;
import net.islbd.kothabondhu.document.docfragment.SelectionFragment;

public class MySharedPreferences {
    SelectionFragment selectedFragment;
    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    MySharedPreferences() {
        sharedPreferences = context.getSharedPreferences("MySharedPref",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String name, String value){
        editor.putString(name,value);
        editor.apply();
    }
    public void saveCheckBoxPref(String name, boolean isChecked){
        if(name != null){
            editor.putBoolean(name,isChecked);
            editor.apply();
        }
    }

    public boolean getCheckBoxValuePref(String name){
        if(name != null)
             return sharedPreferences.getBoolean(name, false);
        return false;
    }
}
