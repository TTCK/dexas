package com.ttck.dexas.engine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Random;

public class Player {

	/** Name. */
    private final String name;
    
	/** 
	 * Type. 
	 * 0 -- always check or call
	 * 1 -- random bet
	 * 2 -- smart AI
	 */
    private final int type; 

    /** Hand of cards. */
    private final Hand hand;

    /** Current amount of cash. */
    private BigDecimal cash;

    /** Whether the player has hole cards. */
    private boolean hasCards;

    /** Current bet. */
    private BigDecimal bet;

    /** Last action performed. */
    private Action action;
    
    public Player(String name, BigDecimal cash, int type) {
        this.name = name;
        this.cash = cash;
        this.type = type;
        
        hand = new Hand();

        resetHand();
    }
    
    /**
     * Prepares the player for another hand.
     */
    public void resetHand() {
        hasCards = false;
        hand.removeAllCards();
        resetBet();
    }

    /**
     * Resets the player's bet.
     */
    public void resetBet() {
        bet = BigDecimal.ZERO;
        if (hasCards() && BigDecimal.ZERO.equals(cash)){
        	action = new Action(ActionType.ALL_IN,null);
        }
        else
        	action = null;        
    }

    /**
     * Sets the hole cards.
     */
    public void setCards(List<Card> cards) {
        hand.removeAllCards();
        if (cards != null) {
            if (cards.size() == 2) {
                hand.addCards(cards);
                hasCards = true;
                System.out.format("%s's cards:\t%s\n", name, hand);
            } else {
                throw new IllegalArgumentException("Invalid number of cards");
            }
        }
    }
    
    /**
     * Returns whether the player has his hole cards dealt.
     * 
     * @return True if the hole cards are dealt, otherwise false.
     */
    public boolean hasCards() {
        return hasCards;
    }

    /**
     * Returns the player's name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's current amount of cash.
     * 
     * @return The amount of cash.
     */
    public BigDecimal getCash() {
        return cash;
    }

    /**
     * Returns the player's current bet.
     * 
     * @return The current bet.
     */
    public BigDecimal getBet() {
        return bet;
    }
    
    /**
     * Sets the player's current bet.
     * 
     * @param bet
     *            The current bet.
     */
    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }

    /**
     * Returns the player's most recent action.
     * 
     * @return The action.
     */
    public Action getAction() {
        return action;
    }
    
    /**
     * Sets the player's most recent action.
     * 
     * @param action
     *            The action.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Indicates whether this player is all-in.
     * 
     * @return True if all-in, otherwise false.
     */
    public boolean isAllIn() {
        return hasCards() && (BigDecimal.ZERO.equals(cash));
    }

    /**
     * Returns the player's hole cards.
     * 
     * @return The hole cards.
     */
    public Card[] getCards() {
        return hand.getCards();
    }
    
    
    /**
     * Action
     * 
     */
    public Action act(BigDecimal minBet, BigDecimal currentBet, Set<ActionType> allowedActions) {
    	
    	Action act = null;
    	
    	// 0 -- always check or call
   	    // 1 -- random bet
   	    // 2 -- smart AI
    	if(type == 0){  
	    	if(allowedActions.size() > 0){
	    		if(allowedActions.contains(ActionType.CALL)){
	    			act = new Action(ActionType.CALL, null);
	    		}
	    		else if(allowedActions.contains(ActionType.CHECK)){
	    			act = new Action(ActionType.CHECK,null);
	    		}	    		
	    	}
    	}else if(type == 1){ 
    		if(allowedActions.size() > 0){
    	   		Random random = new Random();
    	   		int index = random.nextInt(allowedActions.size());
    	   		
    	   		ActionType type = (ActionType)allowedActions.toArray()[index];
    	   		
    	   		if(type == ActionType.RAISE || type == ActionType.BET){
    	   			act = new Action(type, cash.compareTo(BigDecimal.valueOf(4)) > 0 ? BigDecimal.valueOf(4) : cash);
    	   		} 
    	   		else{
    	   			act = new Action(type, null);
    	   		}    	   	    		
	    	}
	    }
    	
    	this.action = act;
    	return act;
    }

    /**
     * Posts the small blind.
     * 
     * @param blind
     *            The small blind.
     */
    public void postSmallBlind(BigDecimal blind) {
        action = new Action(ActionType.SMALL_BLIND, blind);
        cash = cash.subtract(blind);
        bet = bet.add(blind);
    }
    
    /**
     * Posts the big blinds.
     * 
     * @param blind
     *            The big blind.
     */
    public void postBigBlind(BigDecimal blind) {
        action = new Action(ActionType.BIG_BLIND,blind);
        cash = cash.subtract(blind);
        bet = bet.add(blind);
    }
    
    /**
     * Pays an amount of cash.
     * 
     * @param amount
     *            The amount of cash to pay.
     */
    public void payCash(BigDecimal amount) {
        if (amount.compareTo(cash) > 0) {
            throw new IllegalStateException("Player asked to pay more cash than he owns!");
        }
        cash = cash.subtract(amount);
    }
    
    /**
     * Wins an amount of money.
     * 
     * @param amount
     *            The amount won.
     */
    public void win(BigDecimal amount) {
        cash = cash.add(amount);
    }
    
    @Override
    public String toString() {
        return name;
    }


}
