package com.loxy01.firebasechatapp.chat;

public class chatMessage {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    String text;
    String name;
    String imageUrl;
    String sender;
    String recipient;
    boolean isMine;
    boolean isIcon;
    String imgIconAccount;
    String backgroundIconAccount;
    String nameIcon;
    String nameBackground;


    public chatMessage(){
    }

    public chatMessage(String text, String name, String imageUrl, String sender, String recipient, boolean isMine, boolean isIcon, String imgIconAccount, String backgroundIconAccount, String nameIcon, String nameBackground) {
        this.text = text;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sender = sender;
        this.recipient = recipient;
        this.isMine = isMine;
        this.isIcon = isIcon;
        this.imgIconAccount = imgIconAccount;
        this.backgroundIconAccount = backgroundIconAccount;
        this.nameIcon = nameIcon;
        this.nameBackground = nameBackground;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean getIsMine() {
        return isMine;
    }
    public void setIsMine(boolean mineMessage) {
        this.isMine = mineMessage;
    }

    public String getImgIconAccount() {
        return imgIconAccount;
    }

    public void setImgIconAccount(String imgIconAccount) {
        this.imgIconAccount = imgIconAccount;
    }

    public String getBackgroundIconAccount() {
        return backgroundIconAccount;
    }

    public void setBackgroundIconAccount(String backgroundIconAccount) {
        this.backgroundIconAccount = backgroundIconAccount;
    }

    public boolean getIsIcon() {
        return isIcon;
    }

    public void setIsIcon(boolean icon) {
        isIcon = icon;
    }

    public String getNameIcon() {
        return nameIcon;
    }

    public void setNameIcon(String nameIcon) {
        this.nameIcon = nameIcon;
    }

    public String getNameBackground() {
        return nameBackground;
    }

    public void setNameBackground(String nameBackground) {
        this.nameBackground = nameBackground;
    }
}

