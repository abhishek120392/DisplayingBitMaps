package com.example.android.abhishek.flikr.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Abhishek on 26/09/16.
 */
public class Photo extends SugarRecord {

    @Unique
    public long photo_id;
    public String owner;
    public String secret;
    public int server;
    public int farm;
    public String title;
    public boolean ispublic;
    public boolean isfriend;
    public boolean isfamily;
    public String url_n;
    public int height_n;
    public int width_n;

}
