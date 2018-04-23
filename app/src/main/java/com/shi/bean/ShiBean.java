package com.shi.bean;

import java.io.Serializable;

/**
 * Author: Yunr
 * Date: 2018-04-20 15:01
 */
public class ShiBean implements Serializable{

    /**
     * "_id integer primary key autoincrement," +
     * "topic text," +
     * "title text,detail text,author text,dynasty text," +
     * "translate text,appreciation text)";
     */

    private int id;
    private String topic;
    private String title;
    private String detail;
    private String author;
    private String dynasty;
    private String translate;
    private String appreciation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic == null ? "" : topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail == null ? "" : detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDynasty() {
        return dynasty == null ? "" : dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getTranslate() {
        return translate == null ? "" : translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getAppreciation() {
        return appreciation == null ? "" : appreciation;
    }

    public void setAppreciation(String appreciation) {
        this.appreciation = appreciation;
    }

    @Override
    public String toString() {
        return "ShiBean{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", author='" + author + '\'' +
                ", dynasty='" + dynasty + '\'' +
                ", translate='" + translate + '\'' +
                ", appreciation='" + appreciation + '\'' +
                '}';
    }
}
