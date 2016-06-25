package com.ttck.test;

import junit.framework.Assert;
import org.junit.Test;
import com.ttck.dexas.*;
import com.ttck.dexas.engine.Card;
import com.ttck.dexas.engine.Hand;
import com.ttck.dexas.engine.HandEval;
import com.ttck.dexas.engine.ValueType;

public class HandTest {

	@Test
	public void BasicTest() {
		Hand hand = new Hand();
        Assert.assertNotNull(hand);
        Assert.assertEquals(0, hand.size());
        
        Card[] cards = hand.getCards();
        Assert.assertNotNull(cards);
        Assert.assertEquals(0, cards.length);
        
        hand.addCard(new Card("Th"));
        Assert.assertEquals(1, hand.size());
        cards = hand.getCards();
        Assert.assertNotNull(cards);
        Assert.assertEquals(1, cards.length);
        Assert.assertNotNull(cards[0]);
        Assert.assertEquals("Th", cards[0].toString());
        
        hand.addCards(new Card[]{new Card("2d"), new Card("Jc")});
        Assert.assertEquals(3, hand.size());
        cards = hand.getCards();
        Assert.assertNotNull(cards);
        Assert.assertEquals(3, cards.length);
        Assert.assertEquals("Jc", cards[0].toString());
        Assert.assertEquals("Th", cards[1].toString());
        Assert.assertEquals("2d", cards[2].toString());
        
        hand.removeAllCards();
        Assert.assertEquals(0, hand.size());
	}
	
	@Test
	public void EvalTest(){
		HandEval evaluator;
        int value1, value2, value3,value4,value5,value6,value7,value8;
        int value9, value10,value11, value12, value13, value14,value15;
        int value16,value17,value18;
        
        // A High!
        evaluator = new HandEval(new Hand("As Qh Tc 8d 5d 4h 2c"));
        Assert.assertEquals(ValueType.HIGH_CARD, evaluator.getType());
        value1 = evaluator.getValue();
        System.out.println(value1);

        // A High!
        evaluator = new HandEval(new Hand("Ac Qd Td 8h 5s 4c 2d"));
        Assert.assertEquals(ValueType.HIGH_CARD, evaluator.getType());
        value2 = evaluator.getValue();
        System.out.println(value2);
        Assert.assertTrue(value1 == value2);

        // K High!
        evaluator = new HandEval(new Hand("Ks Qh Tc 8d 5d 4h 2c"));
        Assert.assertEquals(ValueType.HIGH_CARD, evaluator.getType());
        value2 = evaluator.getValue();
        System.out.println(value2);
        Assert.assertTrue(value1 > value2);

        // K High
        evaluator = new HandEval(new Hand("Ks Qh Tc 8d 4d 3h 2c"));
        Assert.assertEquals(ValueType.HIGH_CARD, evaluator.getType());
        value2 = evaluator.getValue();
        System.out.println(value2);
        Assert.assertTrue(value1 > value2);
        
        // Q pair
        evaluator = new HandEval(new Hand("Qs Qh 9c 7c 5d 3s 2h"));
        Assert.assertEquals(ValueType.ONE_PAIR, evaluator.getType());
        value3 = evaluator.getValue();
        System.out.println(value3);

        // J pair
        evaluator = new HandEval(new Hand("Js Jh 9c 7c 5d 3s 2h"));
        Assert.assertEquals(ValueType.ONE_PAIR, evaluator.getType());
        value4 = evaluator.getValue();
        Assert.assertTrue(value3 > value4); //Q pair > J pair
        Assert.assertTrue(value4 > value2); //J pair > K high
        
        // Q pair, small kicker
        evaluator = new HandEval(new Hand("Qs Qh 8c 7c 5d 3s 2h"));
        Assert.assertEquals(ValueType.ONE_PAIR, evaluator.getType());
        value5 = evaluator.getValue();
        Assert.assertTrue(value3 > value5); //Q pair with kicker 9 > Q pair with kicker 8
        
        // Q pair, equal
        evaluator = new HandEval(new Hand("Qs Qh 9c 7c 5d 4s 2d"));
        Assert.assertEquals(ValueType.ONE_PAIR, evaluator.getType());
        value5 = evaluator.getValue();
        Assert.assertTrue(value3 == value5);
        
        // 5 ,2 two pair with kicker K
        evaluator = new HandEval(new Hand("Ks Qh Tc 5d 5c 2h 2c"));
        Assert.assertEquals(ValueType.TWO_PAIRS, evaluator.getType());
        value6 = evaluator.getValue();
        Assert.assertTrue(value6 > value5);
        
        //4,3 two pair
        evaluator = new HandEval(new Hand("Ks Qh Tc 4d 4d 3h 3c"));
        Assert.assertEquals(ValueType.TWO_PAIRS, evaluator.getType());
        value7 = evaluator.getValue();
        Assert.assertTrue(value6 > value7);  //5,2 > 4,3
        
        //5 , 2 two pair with kicker A
        evaluator = new HandEval(new Hand("As Qh Tc 5d 5d 2h 2c"));
        Assert.assertEquals(ValueType.TWO_PAIRS, evaluator.getType());
        value7 = evaluator.getValue();
        Assert.assertTrue(value6 < value7);
        
        //Q three of a kind
        evaluator = new HandEval(new Hand("Ah Qs Qh Qc Th 8s 6c"));
        Assert.assertEquals(ValueType.THREE_OF_A_KIND, evaluator.getType());
        value8 = evaluator.getValue();
        Assert.assertTrue(value8 > value7);
        
        //J three of a kind
        evaluator = new HandEval(new Hand("Ah Js Jh Jc Th 8s 6c"));
        Assert.assertEquals(ValueType.THREE_OF_A_KIND, evaluator.getType());
        value9 = evaluator.getValue();
        Assert.assertTrue(value8 > value9);
        
        //678910 straight
        evaluator = new HandEval(new Hand("Ks Th 9s 8d 7c 6h 4c"));
        Assert.assertEquals(ValueType.STRAIGHT, evaluator.getType());
        value10 = evaluator.getValue();
        Assert.assertTrue(value10 > value9);
        
        //10JQKA straight
        evaluator = new HandEval(new Hand("As Ks Qs Js Th 4d 2c"));
        Assert.assertEquals(ValueType.STRAIGHT, evaluator.getType());
        value11 = evaluator.getValue();
        Assert.assertTrue(value10 < value11);
        
        //12345 straight
        evaluator = new HandEval(new Hand("Ad Qc Th 5s 4d 3h 2c"));
        Assert.assertEquals(ValueType.STRAIGHT, evaluator.getType());
        value11 = evaluator.getValue();
        Assert.assertTrue(value10 > value11);
        
        //spade flush with A
        evaluator = new HandEval(new Hand("As Qs Ts 8s 6s 4d 2c"));
        Assert.assertEquals(ValueType.FLUSH, evaluator.getType());
        value12 = evaluator.getValue();
        Assert.assertTrue(value12 > value11);
        
        //flush with K
        evaluator = new HandEval(new Hand("Kh Jh Jd 8h 6d 5h 3h"));
        Assert.assertEquals(ValueType.FLUSH, evaluator.getType());
        value13 = evaluator.getValue();
        Assert.assertTrue(value12 > value13);
        
        //Q full house with T pair
        evaluator = new HandEval(new Hand("As Qs Qh Qc Tc Td 4c"));
        Assert.assertEquals(ValueType.FULL_HOUSE, evaluator.getType());
        value14 = evaluator.getValue();
        Assert.assertTrue(value14 > value13);
        
        //J full house with K pair
        evaluator = new HandEval(new Hand("As Js Jh Jc Kc Kd 4c"));
        Assert.assertEquals(ValueType.FULL_HOUSE, evaluator.getType());
        value15 = evaluator.getValue();
        Assert.assertTrue(value14 > value15);
        
        //A zha!
        evaluator = new HandEval(new Hand("As Ah Ac Ad Qs Th 8c"));
        Assert.assertEquals(ValueType.FOUR_OF_A_KIND, evaluator.getType());
        value16 = evaluator.getValue();
        Assert.assertTrue(value16 > value15);
        
        //K zha!
        evaluator = new HandEval(new Hand("Ks Kh Kc Kd Qs Th 8c"));
        Assert.assertEquals(ValueType.FOUR_OF_A_KIND, evaluator.getType());
        value17 = evaluator.getValue();
        Assert.assertTrue(value16 > value17);
        
        //Royal flush!!!
        evaluator = new HandEval(new Hand("As Ks Qs Js Ts 9s 4d"));
        Assert.assertEquals(ValueType.STRAIGHT_FLUSH, evaluator.getType());
        value18 = evaluator.getValue();
        Assert.assertTrue(value18 > value17);
        System.out.println(value18);
	}

}
