package com.ttck.test;

import junit.framework.Assert;
import org.junit.Test;
import com.ttck.dexas.*;

public class DeckTest {

	@Test
	public void shuffle() {
		Deck deck = new Deck();
		System.out.println(deck);
		deck.shuffle();
		System.out.println(deck);
		System.out.println(deck.deal());
		System.out.println(deck.deal());
		System.out.println(deck.deal());
		System.out.println(deck);
	}

}
