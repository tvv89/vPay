package com.tvv.utils;

import com.tvv.db.entity.Card;

import java.time.LocalDate;
import java.util.Random;

public class UtilsGenerator {

    public static Card getGenerateCard(){
        Card card = new Card();
        StringBuilder number = new StringBuilder("4125");
        for (int i = 0; i < 12; i++) {
            if (i%4==0) number.append(" ");
            number.append(new Random().nextInt(10));
        }
        StringBuilder expDate = new StringBuilder();
        LocalDate currenDate = LocalDate.now();
        String mm = ""+(currenDate.getMonth().getValue()+1);
        String yy = ""+(currenDate.getYear()-96);
        if (currenDate.getMonth().getValue()<9) mm = "0" + (currenDate.getMonth().getValue()+1);
        expDate.append(mm)
                .append("/")
                .append(yy);
        card.setId(1L);
        card.setName("");
        card.setStatus(false);
        card.setNumber(number.toString());
        card.setExpDate(expDate.toString());
        return card;
    }

    public static String getGUID (){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(new Random().nextInt(10));
        }
        return result.toString();
    }

    public static String getAccountUID() {
        int startSymbol = 48;
        int endSymbol = 122;
        int targetStringLength = 25;
        Random random = new Random();

        String result = random.ints(startSymbol, endSymbol + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return result;
    }

}
