package com.example.android.abhishek.flikr.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Abhishek on 26/09/16.
 */
public class Photos extends SugarRecord {

    public int page;
    public int pages;
    public int perpage;
    public int total;
    public List<Photo> photo;

}
