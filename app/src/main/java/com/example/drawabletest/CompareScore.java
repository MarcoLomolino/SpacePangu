package com.example.drawabletest;

import java.util.Comparator;

public class CompareScore implements Comparator<CustomerModel> {
    @Override
    public int compare(CustomerModel o1, CustomerModel o2) {
        return o2.getScore().compareTo(o1.getScore());
    }
}
