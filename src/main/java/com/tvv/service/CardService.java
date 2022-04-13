package com.tvv.service;

import com.tvv.db.dao.CardDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Card;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;

import java.util.Map;

/**
 * Business logic for Cards
 */
public class CardService {

    public UserDAO userDAO;
    public CardDAO cardDAO;

    public CardService() {
        userDAO = new UserDAO();
        cardDAO = new CardDAO();
    }

    public void setDAO(UserDAO userDAO, CardDAO cardDAO){
        this.userDAO = userDAO;
        this.cardDAO = cardDAO;
    }
    /**
     * Create card with checking field. Checking will be developed in the future
     * @param cardData Map - account data
     * @throws AppException
     */
    public void createCard(Map<String,String> cardData) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        String cardCheck = cardData.get("cardnumber");
        cardCheck = cardCheck.replace(" ", "");
        if (!FieldsChecker.checkCardNumber(cardCheck)) errorMessage.append("Bad card number");

        if (errorMessage.length()==0) {
            Card card = new Card();
            card.setId(1L);
            card.setName(cardData.get("name"));
            card.setNumber(CardService.formatCard(cardData.get("cardnumber")));
            card.setExpDate(cardData.get("expMM")+"/"+cardData.get("expYY"));
            Long userId = Long.parseLong(cardData.get("ownerUser"));

            card.setUser(userDAO.findUserById(userId));
            card.setStatus(true);

            cardDAO.insertCard(card);
        }
        else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }

    /**
     * Create card format '#### #### #### ####'
     * @param cardNumber String card number
     * @return String with new format
     */
    public static String formatCard (String cardNumber) {
        if (cardNumber==null) return "";
        String trueCard = cardNumber.replace(" ","");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < trueCard.toCharArray().length; i++) {
            if (i%4==0) result.append(' ');
            result.append(trueCard.toCharArray()[i]);
        }
        return result.toString().trim();
    }

}
