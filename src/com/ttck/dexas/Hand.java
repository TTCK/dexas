package com.ttck.dexas;

import java.util.Collection;

public class Hand {

    /** The maximum number of cards in a hand. */
    private static final int MAX_NO_OF_CARDS = 7;
    
    /** The cards in this hand. */
    private Card[] cards = new Card[MAX_NO_OF_CARDS];
    
    /** The current number of cards in this hand. */
    private int noOfCards = 0;
    
	public Hand() {
		// TODO Auto-generated constructor stub
	}

}
