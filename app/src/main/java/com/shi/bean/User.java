package com.shi.bean;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Author: Yunr
 * Date: 2018-04-19 17:39
 */
public class User implements Serializable {

    private int id;
    private String userName;
    private String password;
    private int age;
    private Bitmap head;
    private byte[] headData;
    private int unit ;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public byte[] getHeadData() {
        return headData;
    }

    public void setHeadData(byte[] headData) {
        this.headData = headData;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getHeadByte() {
        if (headData == null) {
            if (head != null) {
                return img(head);
            }
        }
        return headData;
    }

    public byte[] img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", head=" + head +
                ", headData=" + Arrays.toString(headData) +
                '}';
    }
}
