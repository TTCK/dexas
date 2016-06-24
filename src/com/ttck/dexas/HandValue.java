package com.ttck.dexas;

public class HandValue implements Comparable<HandValue> {

    /**
     * The hand.
     */
    private final Hand hand;

    /**
     * The hand value type.
     */
    private final ValueType type;

    /**
     * The exact, numeric hand value.
     */
    private final int value;

    /**
     * Constructor.
     *
     * @param hand The hand.
     */
    public HandValue(Hand hand) {
        this.hand = hand;
        HandEval evaluator = new HandEval(hand);
        type = evaluator.getType();
        value = evaluator.getValue();
    }

    /**
     * Returns the hand.
     *
     * @return The hand.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the hand value type.
     *
     * @return The hand value type.
     */
    public ValueType getType() {
        return type;
    }

    /**
     * Returns a description of the hand value type.
     *
     * @return The description of the hand value type.
     */
    public String getDescription() {
        return type.getDescription();
    }

    /**
     * Returns the exact, numeric hand value.
     *
     * @return The exact, numeric hand value.
     */
    public int getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof HandValue && ((HandValue) obj).getValue() == value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(HandValue handValue) {
        if (value > handValue.getValue()) {
            return -1;
        } else if (value < handValue.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s (%d)", type.getDescription(), value);
    }
}
