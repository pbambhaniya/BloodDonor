package com.multipz.bloodbook.Utils;

/**
 * Created by Admin on 18-11-2017.
 */

public class Config {
//    public static final String BASE_URL = "http://192.168.0.116:1417/Api/";
    public static final String BASE_URL = "http://blooddonor.webenger.com/Api/";

    public static final String VERIFY_USER = BASE_URL + "Authentication/VerifyUser";
    public static final String SIGN_UP = BASE_URL + "Authentication/SignUp";
    public static final String BLOOD_REQUEST = BASE_URL + "BloodRequest/GetAllDonnerList";
    public static final String REACT_ON_NOTIFICATION = BASE_URL + "BloodRequest/ReactOnNotification";
    public static final String GET_DONATE_ACCEPT = BASE_URL + "BloodRequest/GetDonateBloodView";
    public static final String currentblood = BASE_URL + "BloodRequest/GetCurrentBloodReuestList";
    public static final String getRequestAcceptedDonerList = BASE_URL + "BloodRequest/GetRequestAcceptedDonnerList";
    public static final String ForgetPassword = BASE_URL + "Authentication/ForgotPassword";
    public static final String MyDonateHistory = BASE_URL + "BloodRequest/GetDonateHistoryList";
    public static final String TotalDonateHistory = BASE_URL + "BloodRequest/GetBloodReuestHistoryList";
    public static final String GetRequestAccepterList = BASE_URL + "BloodRequest/GetRequestAccepterList";
    public static final String GetDonationAmount = BASE_URL + "BloodRequest/AddDonationAmount";
    public static final String GetUserDetail = BASE_URL + "BloodRequest/GetUserDetailView";
    public static final String EditUser = BASE_URL + "Authentication/EditUser";
    public static final String Feedback = BASE_URL + "Authentication/Feedback";
    public static final String ChangePassword = BASE_URL + "Authentication/ChangePassword";
    public static final String ReactOnSuccefullyBloodDonation = BASE_URL + "BloodRequest/ReactOnSuucefullyBloodDonation";
    //BloodRequest/ReactOnSuucefullyBloodDonation
    //Authentication/EditUser

//    public static final String TermsAndPrivacyPolicy = "http://192.168.0.116:1417/Api/Pages/GetPage";
      public static final String TermsAndPrivacyPolicy = "http://blooddonor.webenger.com/Api/Pages/GetPage";


    public static final int API_VERIFY_USER = 1;
    public static final int API_SIGN_UP = 2;
    public static final int API_BLOOD_REQUEST = 3;
    public static final int API_REACT_ON_NOTIFICATION = 4;
    public static final int API_GET_DONATE_ACCEPT = 5;
    public static final int API_GET_FORGET_PASSWORD = 6;
    public static final int API_MY_DONATION_HISTORY = 7;
    public static final int API_GetRequestAccepterList = 8;
    public static final int API_GetDonationAmount = 9;
    public static final int API_EditUser = 10;
    public static final int API_ReactOnSuccefullyBloodDonation = 11;
    public static final String Logout = BASE_URL + "Authentication/LogOut";
    public static final String Contactus = BASE_URL + "Authentication/ContactUs";
    public static final String GetActiveDonnerList = BASE_URL + "BloodRequest/GetActiveDonnerList";
    public static final String DoneateBloodList = BASE_URL + "BloodRequest/GetDonateBloodList";
    public static final String SaveBlood = BASE_URL + "BloodRequest/GetSavedBloodRequestList";

//    public static final String Img = "http://192.168.0.116:1417";
    public static final String Img = "http://blooddonor.webenger.com";
}
