package com.multipz.bloodbook.Model;

/**
 * Created by Admin on 23-11-2017.
 */

public class CurrentBloodModel {

    String Id;
    String UserId;
    String UserName;
    String ContactNo;
    String City;
    String Area;
    String Address;
    String Pincode;
    String UserImage;
    String BloodType;
    String Status;
    String IsRequetClear;

    public String getRequireDate() {
        return RequireDate;
    }

    public void setRequireDate(String requireDate) {
        RequireDate = requireDate;
    }

    String RequireDate;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getBloodType() {
        return BloodType;
    }

    public void setBloodType(String bloodType) {
        BloodType = bloodType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIsRequetClear() {
        return IsRequetClear;
    }

    public void setIsRequetClear(String isRequetClear) {
        IsRequetClear = isRequetClear;
    }
}
