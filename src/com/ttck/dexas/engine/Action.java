package com.ttck.dexas.engine;

import java.math.BigDecimal;

public class Action {

    private ActionType type;

    private final BigDecimal amount;
    
	public Action(ActionType type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
	}
	
	public ActionType getActionType() {
		return type;
	}
    
    public BigDecimal getAmount() {
        return amount;
    }

}
