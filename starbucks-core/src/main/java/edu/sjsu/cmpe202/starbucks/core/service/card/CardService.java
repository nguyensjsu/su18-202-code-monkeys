package edu.sjsu.cmpe202.starbucks.core.service.card;

import edu.sjsu.cmpe202.starbucks.beans.Card;

import java.util.List;

public interface CardService {
    public boolean addCard(Card card) throws Exception;
    public boolean updateCard(Card card);
    public boolean deleteCard(Card card);
    public boolean exists(Card card);
    public Card getCard(String card, String user);
    public List<Card> getCards(String user);
}
