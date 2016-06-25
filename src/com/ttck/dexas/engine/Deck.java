package com.ttck.dexas.engine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private Card[] cards;
    
    private int nextCardIndex = 0;
    private Random random = new SecureRandom();
    
	public Deck() {
		// initialize the deck
		cards = new Card[52];
        int index = 0;
        for (int suit = 4 - 1; suit >= 0; suit--) {
            for (int rank = 13 - 1; rank >= 0 ; rank--) {
                cards[index++] = new Card(rank, suit);
            }
        }
	}

    public void shuffle() {
        for (int oldIndex = 0; oldIndex < 52; oldIndex++) {
            int newIndex = random.nextInt(52);
            Card tempCard = cards[oldIndex];
            cards[oldIndex] = cards[newIndex];
            cards[newIndex] = tempCard;
        }
        nextCardIndex = 0;
    }
    
    public void reset() {
        nextCardIndex = 0;
    }
    
    public Card deal() {
        if (nextCardIndex + 1 >= 52) {
            throw new IllegalStateException("No cards left in deck");
        }
        return cards[nextCardIndex++];
    }
    
    public List<Card> deal(int noOfCards) {
        if (noOfCards < 1) {
            throw new IllegalArgumentException("noOfCards < 1");
        }
        if (nextCardIndex + noOfCards >= 52) {
            throw new IllegalStateException("No cards left in deck");
        }
        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < noOfCards; i++) {
            dealtCards.add(cards[nextCardIndex++]);
        }
        return dealtCards;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

}
