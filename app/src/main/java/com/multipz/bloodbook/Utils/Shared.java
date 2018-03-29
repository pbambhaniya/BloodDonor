package com.multipz.bloodbook.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shared {
    SharedPreferences pref;
    Editor edit;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String LOGIN = "LoggedIn";
    private static final String BLOOD_TYPE_ID = "Blood_type_id";
    private static final String USER_ID = "user_id";
    private static final String UserName = "username";
    private static final String ContactNo = "ContactNo";
    private static final String Email = "Email";
    private static final String BirthDate = "BirthDate";
    private static final String UserImage = "UserImage";
    private static final String BloodId = "BloodId";
    private static final String Area = "Area";
    private static final String Address = "Address";
    private static final String city = "city";
    private static final String State = "State";
    private static final String Pincode = "Pincode";

    private static final String SEARCH_TYPE = "search_type";

    public Shared(Context c) {
        // TODO Auto-generated constructor stub
        pref = c.getSharedPreferences("file_pref", Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    public void putBoolean(String key, boolean b) {
        edit.putBoolean(key, b);
        edit.commit();
    }


    public String getlogin() {

        String aut_key = pref.getString(LOGIN, "");
        return aut_key;
    }

    public void setlogin(boolean login) {

        edit.putBoolean(IS_LOGIN, login);
        // commit changes
        edit.commit();
    }

    public int getBloodTypeId() {

        int blood_type_id = pref.getInt(BLOOD_TYPE_ID, 0);
        return blood_type_id;
    }


    public void setBlood_type_id(int id) {
        edit.putInt(BLOOD_TYPE_ID, id);
        edit.commit();
    }

    /******************************Start User Address*****************************************/

    public String getAddress() {

        String add = pref.getString(Address, "");
        return add;
    }

    public void setAddress(String add) {
        edit.putString(Address, add);
        edit.commit();
    }

    public String getCity() {

        String c = pref.getString(city, "");
        return c;
    }

    public void setCity(String c) {
        edit.putString(city, c);
        edit.commit();
    }

    public String getArea() {

        String c = pref.getString(Area, "");
        return c;
    }

    public void setArea(String c) {
        edit.putString(Area, c);
        edit.commit();
    }


    public String getState() {

        String c = pref.getString(State, "");
        return c;
    }

    public void setState(String c) {
        edit.putString(State, c);
        edit.commit();
    }

    /******************************End User Address*****************************************/


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public boolean getBoolean(String key, boolean def) {
        return pref.getBoolean(key, def);
    }

    public void putString(String key, String def) {
        edit.putString(key, def);
        edit.commit();
    }

    public String getString(String key, String def) {
        return pref.getString(key, def);
    }

    public void putInt(String key, int def) {
        edit.putInt(key, def);
        edit.commit();
    }

    /*********************************User Registration Detail*************************************/
    public String getUserId() {

        String user_id = pref.getString(USER_ID, "");
        return user_id;
    }

    public void setUserId(String user_id) {
        edit.putString(USER_ID, user_id);
        edit.commit();
    }

    public String getUserName() {

        String name = pref.getString(UserName, "");
        return name;
    }

    public void setUserName(String user_name) {
        edit.putString(UserName, user_name);
        edit.commit();
    }

    public String getContactNo() {

        String cno = pref.getString(ContactNo, "");
        return cno;
    }

    public void setContactNo(String cno) {
        edit.putString(ContactNo, cno);
        edit.commit();
    }

    public String getEmail() {

        String emailid = pref.getString(Email, "");
        return emailid;
    }

    public void setEmail(String Emailid) {
        edit.putString(Email, Emailid);
        edit.commit();
    }

    public String getBirthDate() {

        String bd = pref.getString(BirthDate, "");
        return bd;
    }

    public void setBirthDate(String db) {
        edit.putString(BirthDate, db);
        edit.commit();
    }

    public String getUserImage() {

        String img = pref.getString(UserImage, "");
        return img;
    }

    public void setUserImage(String img) {
        edit.putString(UserImage, img);
        edit.commit();
    }

    public String getBloodId() {

        String bdid = pref.getString(BloodId, "");
        return bdid;
    }

    public void setBloodId(String bdid) {
        edit.putString(BloodId, bdid);
        edit.commit();
    }

    public String getPincode() {

        String pincode = pref.getString(Pincode, "");
        return pincode;
    }

    public void setPincode(String pcode) {
        edit.putString(Pincode, pcode);
        edit.commit();
    }


    /*********************************End Registration Detail*************************************/
    public String getSearchType() {

        String stype = pref.getString(SEARCH_TYPE, "");
        return stype;
    }

    public void setSearchType(String stype) {
        edit.putString(SEARCH_TYPE, stype);
        edit.commit();
    }

    public void logout() {
        edit.clear();
        edit.commit();
    }


}
