package com.ttck.dexas;

public enum ValueType {
    
    ROYAL_FLUSH("a Royal Flush", 9),    
  
    STRAIGHT_FLUSH("a Straight Flush", 8),
    
    FOUR_OF_A_KIND("Four of a Kind", 7),    
    
    FULL_HOUSE("a Full House", 6),    
   
    FLUSH("a Flush", 5),    
    
    STRAIGHT("a Straight", 4),
   
    THREE_OF_A_KIND("Three of a Kind", 3),    
    
    TWO_PAIRS("Two Pairs", 2),
    
    ONE_PAIR("One Pair", 1),    
    
    HIGH_CARD("a High Card", 0),
    ;
	
    private String description;
   
    private int value;
    
    
    ValueType(String description, int value) {
        this.description = description;
        this.value = value;
    }
    
    /**
     * Returns the description.
     * 
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the hand value.
     * 
     * @return The hand value.
     */
    public int getValue() {
        return value;
    }
    
}
