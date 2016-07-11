package com.ttck.dexas.engine;

public enum ActionType {

    ALL_IN("All-in"),
    BET("Bet"),
    SMALL_BLIND("Small Blind"),
    BIG_BLIND("Big Blind"),
    CALL("Call"),
    CHECK("Check"),
    FOLD("Fold"),
    RAISE("Raise"),   
    ;
    
    /** Display name. */
    private String name;
    
	private ActionType(String name) {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Returns the display name.
     * 
     * @return The display name.
     */
    public String getName() {
        return name;
    }

}
