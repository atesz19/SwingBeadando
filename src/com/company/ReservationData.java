package com.company;

import java.util.LinkedList;

public class ReservationData {
    int userID;
    String date;
    int time;
    int count;
    LinkedList<FoodItem> foods_list = new LinkedList<>();
}
