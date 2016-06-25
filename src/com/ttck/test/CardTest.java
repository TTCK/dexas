package com.ttck.test;

import junit.framework.Assert;
import org.junit.Test;
import com.ttck.dexas.*;
import com.ttck.dexas.engine.Card;

public class CardTest {

	@Test
	public void basictest() {
		Card card = new Card(Card.FOUR, Card.HEARTS);
        Assert.assertNotNull(card);
        Assert.assertEquals(Card.NINE, card.getRank());
        Assert.assertEquals(Card.SPADES, card.getSuit());
        Assert.assertEquals("9s", card.toString());
        card = new Card("   As "); 
        Assert.assertNotNull(card);
        Assert.assertEquals(Card.ACE, card.getRank());
        Assert.assertEquals(Card.SPADES, card.getSuit());
        Assert.assertEquals("As", card.toString());
	}

}
