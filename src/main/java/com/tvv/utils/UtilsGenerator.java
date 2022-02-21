package com.tvv.utils;

import com.tvv.db.entity.Card;

import java.util.Date;
import java.util.Random;

public class UtilsGenerator {

    public static Card getGenerateCard(){
        Card card = new Card();
        String number = "4125";
        for (int i = 0; i < 12; i++) {
            if (i%4==0) number = number + " ";
            number = number + new Random().nextInt(10);

        }

        String expDate = "";
        Date currenDate = new Date();
        String mm = ""+(currenDate.getMonth()+1);
        String yy = ""+(currenDate.getYear()-96);
        if (currenDate.getMonth()<9) mm = "0" + (currenDate.getMonth()+1);
        expDate = mm +"/"+yy;

        card.setId(1L);
        card.setName("");
        card.setStatus(false);
        card.setNumber(number);
        card.setExpDate(expDate);
        return card;
    }

    public static String getGUID (){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(new Random().nextInt(10));
        }
        return result.toString();
    }



}
