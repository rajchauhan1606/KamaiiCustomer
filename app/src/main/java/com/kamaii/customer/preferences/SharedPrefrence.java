package com.kamaii.customer.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.customer.DTO.TaxiRouteData;
import com.kamaii.customer.DTO.UserDTO;
import com.kamaii.customer.interfacess.Consts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static SharedPrefrence myObj;

    private SharedPrefrence() {

    }

    public void clearAllPreferences() {
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return myPrefs.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }






    public String getValue(String Tag) {
        if (Tag.equalsIgnoreCase(Consts.LATITUDE))
            return myPrefs.getString(Tag, "");
        else if (Tag.equalsIgnoreCase(Consts.LONGITUDE))
            return myPrefs.getString(Tag, "");
        else if (Tag.equalsIgnoreCase(Consts.WORK_ADDRESS_LATI))
            return myPrefs.getString(Tag, myObj.getValue(Consts.LATITUDE));
        else if (Tag.equalsIgnoreCase(Consts.WORK_ADDRESS_LONGI))
            return myPrefs.getString(Tag, myObj.getValue(Consts.LONGITUDE));
        else if (Tag.equalsIgnoreCase(Consts.HOME_ADDRESS_LATI))
            return myPrefs.getString(Tag, myObj.getValue(Consts.LATITUDE));
        else if (Tag.equalsIgnoreCase(Consts.HOME_ADDRESS_LONGI))
            return myPrefs.getString(Tag, myObj.getValue(Consts.LONGITUDE));
        return myPrefs.getString(Tag, "");
    }


    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public void setParentUser(UserDTO userDTO, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public UserDTO getParentUser(String tag) {

        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            UserDTO testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }

    public void setRoute(List<TaxiRouteData> taxiRouteData, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(taxiRouteData);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public List<TaxiRouteData> getRouteUser(String tag)
    {

        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new ArrayList<TaxiRouteData>();
        }
        else
        {
            String storedHashMapString = myPrefs.getString(tag, "");
            Type arrayListType = new TypeToken<ArrayList<TaxiRouteData>>(){}.getType();
            Gson gson = new Gson();
            List<TaxiRouteData> yourList = gson.fromJson(storedHashMapString, arrayListType);
            return yourList;
        }
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public static void set(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

}
