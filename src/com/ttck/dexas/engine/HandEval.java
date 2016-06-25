
package com.ttck.dexas.engine;

public class HandEval {    
   
    private static final int NO_OF_RANKINGS  = 6;    
    
    private static final int MAX_NO_OF_PAIRS = 2;    
    
    private static final int[] RANKING_FACTORS = {371293, 28561, 2197, 169, 13, 1};
    
    private ValueType type;    
    
    private int value = 0;    
    
    private final Card[] cards;    
    
    private int[] rankDist = new int[13];    
    
    private int[] suitDist = new int[4];    
   
    private int noOfPairs = 0;    
    
    private int[] pairs = new int[MAX_NO_OF_PAIRS];    
    
    private int flushSuit = -1;    
    
    private int flushRank = -1;    
    
    private int straightRank = -1;    
    
    private boolean wheelingAce = false;    
   
    private int tripleRank = -1;    
    
    private int quadRank = -1;    
    
    private int[] rankings = new int[NO_OF_RANKINGS];
    
    public HandEval(Hand hand) {
        cards = hand.getCards();
        
        // Find patterns.
        calculateDistributions();
        findStraight();
        findFlush();
        findDuplicates();
        
        // Find special values.
        boolean isSpecialValue =
                (isStraightFlush() ||
                 isFourOfAKind()   ||
                 isFullHouse()     ||
                 isFlush()         ||
                 isStraight()      ||
                 isThreeOfAKind()  ||
                 isTwoPairs()      ||
                 isOnePair());
        if (!isSpecialValue) {
            calculateHighCard();
        }
        
        // Calculate value.
        for (int i = 0; i < NO_OF_RANKINGS; i++) {
            value += rankings[i] * RANKING_FACTORS[i];
        }
    }
    
    /**
     * Returns the hand value type.
     *
     * @return  the hand value type
     */
    public ValueType getType() {
        return type;
    }
    
    /**
     * Returns the hand value as an integer.
     * 
     * This method should be used to compare hands.
     *
     * @return  the hand value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Calculates the rank and suit distributions.
     */
    private void calculateDistributions() {
        for (Card card : cards) {
            rankDist[card.getRank()]++;
            suitDist[card.getSuit()]++;
        }
    }
    
    /**
     * Looks for a flush
     */
    private void findFlush() {
        for (int i = 0; i < 4; i++) {
            if (suitDist[i] >= 5) {
                flushSuit = i;
                for (Card card : cards) {
                    if (card.getSuit() == flushSuit) {
                        if (!wheelingAce || card.getRank() != Card.ACE) {
                            flushRank = card.getRank();
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

   
    private void findStraight() {
        boolean inStraight = false;
        int rank = -1;
        int count = 0;
        for (int i = 13 - 1; i >= 0 ; i--) {
            if (rankDist[i] == 0) {
                inStraight = false;
                count = 0;
            } else {
                if (!inStraight) {
                    // First card of the potential Straight.
                    inStraight = true;
                    rank = i;
                }
                count++;
                if (count >= 5) {
                    // Found a Straight!
                    straightRank = rank;
                    break;
                }
            }
        }
       
        if ((count == 4) && (rank == Card.FIVE) && (rankDist[Card.ACE] > 0)) {
            wheelingAce = true;
            straightRank = rank;
        }
    }
    
    private void findDuplicates() {
        // Find quads, triples and pairs.
        for (int i = 13 - 1; i >= 0 ; i--) {
            if (rankDist[i] == 4) {
                quadRank = i;
            } else if (rankDist[i] == 3) {
                tripleRank = i;
            } else if (rankDist[i] == 2) {
                if (noOfPairs < MAX_NO_OF_PAIRS) {
                    pairs[noOfPairs++] = i;
                }
            }
        }
    }

    /**
     * Calculates the hand value based on the highest ranks.
     */
    private void calculateHighCard() {
        type = ValueType.HIGH_CARD;
        rankings[0] = type.getValue();
        // Get the five highest ranks.
        int index = 1;
        for (Card card : cards) {
            rankings[index++] = card.getRank();
            if (index > 5) {
                break;
            }
        }
    }
    
    private boolean isOnePair() {
        if (noOfPairs == 1) {
            type = ValueType.ONE_PAIR;
            rankings[0] = type.getValue();
            // Get the rank of the pair.
            int pairRank = pairs[0];
            rankings[1] = pairRank;
            // Get the three kickers.
            int index = 2;
            for (Card card : cards) {
                int rank = card.getRank();
                if (rank != pairRank) {
                    rankings[index++] = rank;
                    if (index > 4) {
                        // We don't need any more kickers.
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
   
    private boolean isTwoPairs() {
        if (noOfPairs == 2) {
            type = ValueType.TWO_PAIRS;
            rankings[0] = type.getValue();
            // Get the value of the high and low pairs.
            int highRank = pairs[0];
            int lowRank  = pairs[1];
            rankings[1] = highRank;
            rankings[2] = lowRank;
            // Get the kicker card.
            for (Card card : cards) {
                int rank = card.getRank();
                if ((rank != highRank) && (rank != lowRank)) {
                    rankings[3] = rank;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }
   
    private boolean isThreeOfAKind() {
        if (tripleRank != -1) {
            type = ValueType.THREE_OF_A_KIND;
            rankings[0] = type.getValue();
            rankings[1] = tripleRank;
            // Get the remaining two cards as kickers.
            int index = 2;
            for (Card card : cards) {
                int rank = card.getRank();
                if (rank != tripleRank) {
                    rankings[index++] = rank;
                    if (index > 3) {                        
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    
    private boolean isStraight() {
        if (straightRank != -1) {
            type = ValueType.STRAIGHT;
            rankings[0] = type.getValue();
            rankings[1] = straightRank;
            return true;
        } else {
            return false;
        }
    }

   
    private boolean isFlush() {
        if (flushSuit != -1) {
            type = ValueType.FLUSH;
            rankings[0] = type.getValue();
            int index = 1;
            for (Card card : cards) {
                if (card.getSuit() == flushSuit) {
                    int rank = card.getRank();
                    if (index == 1) {
                        flushRank = rank;
                    }
                    rankings[index++] = rank;
                    if (index > 5) {
                        // We don't need more kickers.
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

   
    private boolean isFullHouse() {
        if ((tripleRank != -1) && (noOfPairs > 0)) {
            type = ValueType.FULL_HOUSE;
            rankings[0] = type.getValue();
            rankings[1] = tripleRank;
            rankings[2] = pairs[0];
            return true;
        } else {
            return false;
        }
    }
    
    
    private boolean isFourOfAKind() {
        if (quadRank != -1) {
            type = ValueType.FOUR_OF_A_KIND;
            rankings[0] = type.getValue();
            rankings[1] = quadRank;
            // Get the remaining card as kicker.
            int index = 3; 
            for (Card card : cards) {
                int rank = card.getRank();
                if (rank != quadRank) {
                    rankings[index] = rank;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    
    private boolean isStraightFlush() {
        if (straightRank != -1 && flushRank == straightRank) {
            // Flush and Straight (possibly separate); check for Straight Flush.
            int straightRank2 = -1;
            int lastSuit = -1;
            int lastRank = -1;
            int inStraight = 1;
            int inFlush = 1;
            for (Card card : cards) {
                int rank = card.getRank();
                int suit = card.getSuit();
                if (lastRank != -1) {
                    int rankDiff = lastRank - rank;
                    if (rankDiff == 1) {                        
                        inStraight++;
                        if (straightRank2 == -1) {
                            straightRank2 = lastRank;
                        }
                        if (suit == lastSuit) {
                            inFlush++;
                        } else {
                            inFlush = 1;
                        }
                        if (inStraight >= 5 && inFlush >= 5) {
                            // Straight!
                            break;
                        }
                    } else if (rankDiff == 0) {
                        // Duplicate rank; skip.
                    } else {                        
                        straightRank2 = -1;
                        inStraight = 1;
                        inFlush = 1;
                    }
                }
                lastRank = rank;
                lastSuit = suit;
            }
            
            if (inStraight >= 5 && inFlush >= 5) {
                if (straightRank == Card.ACE) {
                    // Royal Flush.
                    type = ValueType.ROYAL_FLUSH;
                    rankings[0] = type.getValue();
                    return true;
                } else {
                    // Straight Flush.
                    type = ValueType.STRAIGHT_FLUSH;
                    rankings[0] = type.getValue();
                    rankings[1] = straightRank2;
                    return true;
                }
            } else if (wheelingAce && inStraight >= 4 && inFlush >= 4) {
                
                type = ValueType.STRAIGHT_FLUSH;
                rankings[0] = type.getValue();
                rankings[1] = straightRank2;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
}
