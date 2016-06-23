package com.ttck.dexas;

public class Card implements Comparable<Card> {

    // The ranks.
    public static final int ACE      = 12;
    public static final int KING     = 11;
    public static final int QUEEN    = 10;
    public static final int JACK     = 9;
    public static final int TEN      = 8;
    public static final int NINE     = 7;
    public static final int EIGHT    = 6;
    public static final int SEVEN    = 5;
    public static final int SIX      = 4;
    public static final int FIVE     = 3;
    public static final int FOUR     = 2;
    public static final int THREE    = 1;
    public static final int TWO    = 0;
    
    // The suits.
    public static final int SPADES   = 3;
    public static final int HEARTS   = 2;
    public static final int CLUBS    = 1;
    public static final int DIAMONDS = 0;
    
    /** The rank symbols. */
    public static final String[] RANK_SYMBOLS = {
        "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"
    };
    
    /** The suit symbols. */
    public static final char[] SUIT_SYMBOLS = { 'd', 'c', 'h', 's' };

    /** The rank. */
    private final int rank;
    
    /** The suit. */
    private final int suit;
    
    
    public Card(int rank, int suit) {
        if (rank < 0 || rank > 13) {
            throw new IllegalArgumentException("Invalid rank");
        }
        if (suit < 0 || suit > 4) {
            throw new IllegalArgumentException("Invalid suit");
        }
        this.rank = rank;
        this.suit = suit;
    }
    
    //Generate Card from string like Kh Tc 7S
    public Card(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null string or of invalid length");
        }
        s = s.trim();
        if (s.length() != 2) {
            throw new IllegalArgumentException("Empty string or invalid length");
        }
        
        // Parse the rank character.
        String rankSymbol = s.substring(0, 1);
        char suitSymbol = s.charAt(1);
        int rank = -1;
        for (int i = 0; i < 13; i++) {
            if (rankSymbol.equals(RANK_SYMBOLS[i])) {
                rank = i;
                break;
            }
        }
        if (rank == -1) {
            throw new IllegalArgumentException("Unknown rank: " + rankSymbol);
        }
        // Parse the suit character.
        int suit = -1;
        for (int i = 0; i < 4; i++) {
            if (suitSymbol == SUIT_SYMBOLS[i]) {
                suit = i;
                break;
            }
        }
        if (suit == -1) {
            throw new IllegalArgumentException("Unknown suit: " + suitSymbol);
        }
        this.rank = rank;
        this.suit = suit;
    }

	public int getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}
    
	/** {@inheritDoc} */
    @Override
    public int hashCode() {
        return (rank * 4 + suit);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card && obj.hashCode() == hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Card card) {
        int thisValue = hashCode();
        int otherValue = card.hashCode();
        if (thisValue < otherValue) {
            return -1;
        } else if (thisValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return RANK_SYMBOLS[rank] + SUIT_SYMBOLS[suit];
    }
    

}
