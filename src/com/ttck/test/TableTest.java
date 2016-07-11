package com.ttck.test;

import junit.framework.Assert;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import com.ttck.dexas.engine.*;

public class TableTest {

    private static final BigDecimal BIG_BLIND = BigDecimal.valueOf(5);

    private static final BigDecimal STARTING_CASH = BigDecimal.valueOf(200);
    

	
	@Test
	public void TableTest() {		
	    /* The players at the table. */
	    Map<String, Player> players = new LinkedHashMap<>();

	    players.put("Thomas", new Player("Thomas", STARTING_CASH));
	    players.put("Terry", new Player("Terry", STARTING_CASH));
	    players.put("Cosmos", new Player("Cosmos", STARTING_CASH));
	    players.put("Kemp", new Player("Kemp", STARTING_CASH));
	    
	    /* The table. */
        Table table = new Table(BIG_BLIND);
        for (Player player : players.values()) {
            table.addPlayer(player);
        }
        
        // Start the game.
        table.run();
		
	}

}
