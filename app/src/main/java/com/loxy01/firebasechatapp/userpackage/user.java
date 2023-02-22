package com.loxy01.firebasechatapp.userpackage;

import android.net.Uri;

public class user {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    String userId;
    String userName;
    String userEmail;

    public user() {
    }

    public user(String userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
