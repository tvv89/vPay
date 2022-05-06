package com.tvv.db.dao;

import com.tvv.db.entity.Card;
import com.tvv.service.exception.AppException;

import java.util.List;

public interface CardDAO {
    Card insertCard(Card card) throws AppException;

    List<Card> findAllCards() throws AppException;

    Card findCardById(Long id) throws AppException;

    List<Card> findCardByAccount(Long accountId) throws AppException;

    List<Card> findCardsByUser(Long userId) throws AppException;

    List<Card> getCards(Long userId, String sqlRequest) throws AppException;

    boolean updateStatusCardById(Long id, int newStatus) throws AppException;
}
