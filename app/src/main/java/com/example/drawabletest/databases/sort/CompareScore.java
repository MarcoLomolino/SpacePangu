package com.example.drawabletest.databases.sort;

import com.example.drawabletest.databases.model.ModelScore;

import java.util.Comparator;

public class CompareScore implements Comparator<ModelScore> {
    @Override
    public int compare(ModelScore o1, ModelScore o2) {
        return o2.getScore().compareTo(o1.getScore());
    }
}
