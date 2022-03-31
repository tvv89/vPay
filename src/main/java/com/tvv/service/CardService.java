package com.tvv.service;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Card;
import com.tvv.service.exception.AppException;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;

import java.util.Map;

public class CardService {
    public static void createCard(Map<String,String> cardData) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        ErrorString error = new ErrorMessageEN();

        if (errorMessage.length()==0) {

            Card card = new Card();
            card.setId(1L);
            card.setName(cardData.get("name"));
            card.setNumber(CardService.formatCard(cardData.get("cardnumber")));
            card.setExpDate(cardData.get("expMM")+"/"+cardData.get("expYY"));
            Long userId = Long.parseLong(cardData.get("ownerUser"));

            card.setUser(UserDAO.findUserById(userId));
            card.setStatus(true);

            CardDAO.insertCard(card);
        }
        else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }

    public static String formatCard (String cardNumber) {
        if (cardNumber==null) return "";
        cardNumber.replace(" ","");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cardNumber.toCharArray().length; i++) {
            if (i%4==0) result.append(' ');
            result.append(cardNumber.toCharArray()[i]);
        }
        return result.toString().trim();

    }


}
