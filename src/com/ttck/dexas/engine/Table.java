package com.ttck.dexas.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Table {

	/** The size of the big blind. */
    private final BigDecimal bigBlind;

    /** The players at the table. */
    private final List<Player> players;
    
    /** The active players in the current hand. */
    private final List<Player> activePlayers;
    
    /** The deck of cards. */
    private final Deck deck;
    
    /** The community cards on the board. */
    private final List<Card> board;
    
    /** The current dealer position. */
    private int dealerPosition;

    /** The current dealer. */
    private Player dealer;

    /** The position of the acting player. */
    private int actorPosition;
    
    /** The acting player. */
    private Player actor;

    /** The minimum bet in the current hand. */
    private BigDecimal minBet;

    /** The current bet in the current hand. */
    private BigDecimal bet;

    /** All pots in the current hand (main pot and any side pots). */
    private final List<Pot> pots;
    
    /** The player who bet or raised last (aggressor). */
    private Player lastBettor;
    
    /** Number of raises in the current betting round. */
    private int raises;
	
    public Table(BigDecimal bigBlind) {        
        this.bigBlind = bigBlind;
        players = new ArrayList<>();
        activePlayers = new ArrayList<>();
        deck = new Deck();
        board = new ArrayList<>();
        pots = new ArrayList<>();
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * Main game loop.
     */
    public void run() {
        dealerPosition = -1;
        actorPosition = -1;
        while (true) {
            int noOfActivePlayers = 0;
            Boolean stop = false;
            for (Player player : players) {
            	System.out.println(player.getName() + " cash: " + player.getCash().toString());
                if (player.getCash().compareTo(bigBlind) < 0) {
                    stop = true;
                }
            }          
           
            if (!stop) {
            	System.out.println("$$$$$$$$$$$$$  New Game!!!");
                playHand();
            } else {
                break;
            }
        }
        
        // Game over.
        board.clear();
        pots.clear();
        bet = BigDecimal.ZERO;
        
        for (Player player : players) {
            player.resetHand();
        }        
    }
    
    /**
     * Plays a single hand.
     */
    private void playHand() {
        resetHand();
        
        // Small blind.
        if (activePlayers.size() > 2) {
            rotateActor();
        }
        postSmallBlind();
        
        // Big blind.
        rotateActor();
        postBigBlind();
        
        // Pre-Flop.
        dealHoleCards();
        doBettingRound();
        
        // Flop.
        if (activePlayers.size() > 1) {
            bet = BigDecimal.ZERO;
            dealCommunityCards("Flop", 3);
            System.out.println("Flop cards: " + board.get(0).toString() + " " + board.get(1).toString() + " " + board.get(2).toString());
            doBettingRound();

            // Turn.
            if (activePlayers.size() > 1) {
                bet = BigDecimal.ZERO;
                dealCommunityCards("Turn", 1);
                System.out.println("Turn cards: " + board.get(3).toString());
                minBet = bigBlind.add(bigBlind);
                doBettingRound();

                // River.
                if (activePlayers.size() > 1) {
                    bet = BigDecimal.ZERO;
                    dealCommunityCards("River", 1);
                    System.out.println("River cards: " + board.get(4).toString());
                    doBettingRound();

                    // Showdown.
                    if (activePlayers.size() > 1) {
                        bet = BigDecimal.ZERO;
                        doShowdown();
                    }
                }
            }
        }
    }
    
    /**
     * Resets the game for a new hand.
     */
    private void resetHand() {
        // Clear the board.
        board.clear();
        pots.clear();        
        
        // Determine the active players.
        activePlayers.clear();
        for (Player player : players) {
            player.resetHand();
            // Player must be able to afford at least the big blind.
            if (player.getCash().compareTo(bigBlind) >= 0) {
                activePlayers.add(player);
            }
        }
        
        // Rotate the dealer button.
        dealerPosition = (dealerPosition + 1) % activePlayers.size();
        dealer = activePlayers.get(dealerPosition);

        // Shuffle the deck.
        deck.shuffle();

        // Determine the first player to act.
        actorPosition = dealerPosition;
        actor = activePlayers.get(actorPosition);
        
        // Set the initial bet to the big blind.
        minBet = bigBlind;
        bet = minBet;
        
    }
    
    /**
     * Rotates the position of the player in turn (the actor).
     */
    private void rotateActor() {
        actorPosition = (actorPosition + 1) % activePlayers.size();
        actor = activePlayers.get(actorPosition);        
    }
    
    /**
     * Posts the small blind.
     */
    private void postSmallBlind() {
        final BigDecimal smallBlind = bigBlind.divide(BigDecimal.valueOf(2));
        actor.postSmallBlind(smallBlind);
        contributePot(smallBlind);
    }
    
    /**
     * Posts the big blind.
     */
    private void postBigBlind() {
        actor.postBigBlind(bigBlind);
        contributePot(bigBlind);
    }
    
    /**
     * Deals the cards to each player
     */
    private void dealHoleCards() {
        for (Player player : activePlayers) {
            player.setCards(deck.deal(2));
        }
        System.out.println("Deal Cards");
    }
    
    /**
     * Deals a number of community cards.
     * 
     * @param phaseName
     *            The name of the phase.
     * @param noOfCards
     *            The number of cards to deal.
     */
    private void dealCommunityCards(String phaseName, int noOfCards) {
        for (int i = 0; i < noOfCards; i++) {
            board.add(deck.deal());
        }
    }
    
    /**
     * Performs a betting round.
     */
    private void doBettingRound() {
        // Determine the number of active players.
        int playersToAct = activePlayers.size();
        // Determine the initial player and bet size.
        if (board.size() == 0) {
            // Pre-Flop; player left of big blind starts, bet is the big blind.
            bet = bigBlind;
        } else {
            // Otherwise, player left of dealer starts, no initial bet.
            actorPosition = dealerPosition;
            bet = BigDecimal.ZERO;
        }
        
        if (playersToAct == 2) {
            // Heads Up mode; player who is not the dealer starts.
            actorPosition = dealerPosition;
        }
        
        lastBettor = null;
        raises = 0;
        
        while (playersToAct > 0) {
            rotateActor();
            Action action;
            if (actor.isAllIn()) {
                // Player is all-in, so must check.
                action = new Action(ActionType.CHECK,null);
                playersToAct--;
                System.out.println("Player  " + actor.getName() + "  All IN!!!");
            } else {
                // Otherwise allow client to act.
                Set<ActionType> allowedActions = getAllowedActions(actor);
                action = actor.act(minBet, bet, allowedActions);
                
                playersToAct--;
                if (action.getActionType() == ActionType.CHECK) {
                    // Do nothing.
                	System.out.println("Player  " + actor.getName() + " Check");
                } else if (action.getActionType() == ActionType.CALL) {
                    BigDecimal betIncrement = bet.subtract(actor.getBet());
                    if (betIncrement.compareTo(actor.getCash()) > 0) {
                        betIncrement = actor.getCash();
                    }
                    actor.payCash(betIncrement);
                    actor.setBet(actor.getBet().add(betIncrement));
                    contributePot(betIncrement);
                    
                    System.out.println("Player  " + actor.getName() + " call " + betIncrement.toString());
                } else if (action.getActionType() == ActionType.BET) {
                    BigDecimal amount = action.getAmount();
                    if (amount.compareTo(minBet) < 0 && amount.compareTo(actor.getCash()) < 0) {
                        throw new IllegalStateException("Illegal client action: bet less than minimum bet!");
                    }
                    actor.setBet(amount);
                    actor.payCash(amount);
                    contributePot(amount);
                    bet = amount;
                    minBet = amount;
                    lastBettor = actor;
                    playersToAct = activePlayers.size();
                    System.out.println("Player  " + actor.getName() + " Bet " + amount.toString());
                } else if (action.getActionType() == ActionType.RAISE) {
                    BigDecimal amount = action.getAmount();
                    if (amount.compareTo(minBet) < 0 && amount.compareTo(actor.getCash()) < 0) {
                        throw new IllegalStateException("Illegal client action: raise less than minimum bet!");
                    }
                    bet = bet.add(amount);
                    minBet = amount;
                    BigDecimal betIncrement = bet.subtract(actor.getBet());
                    if (betIncrement.compareTo(actor.getCash()) > 0) {
                        betIncrement = actor.getCash();
                    }
                    actor.setBet(bet);
                    actor.payCash(betIncrement);
                    contributePot(betIncrement);
                    lastBettor = actor;
                    raises++;
                    playersToAct = activePlayers.size();
                    
                    System.out.println("Player  " + actor.getName() + "Raise " + betIncrement.toString());
                
                } else if (action.getActionType() == ActionType.FOLD) {
                    actor.setCards(null);
                    activePlayers.remove(actor);
                    actorPosition--;
                    if (activePlayers.size() == 1) {
                        // Only one player left, so he wins the entire pot.
                        Player winner = activePlayers.get(0);
                        BigDecimal amount = getTotalPot();
                        winner.win(amount);
                        playersToAct = 0;
                    }
                } else {
                    // Programming error, should never happen.
                    throw new IllegalStateException("Invalid action: " + action);
                }
            }
            actor.setAction(action);

        }
        
        // Reset player's bets.
        for (Player player : activePlayers) {
            player.resetBet();
        }
    }
    
    /**
     * Returns the allowed actions of a specific player.
     * 
     * @param player
     *            The player.
     * 
     * @return The allowed actions.
     */
    private Set<ActionType> getAllowedActions(Player player) {
        Set<ActionType> actions = new HashSet<>();
        if (player.isAllIn()) {
            actions.add(ActionType.CHECK);
        } else {
            BigDecimal actorBet = actor.getBet();
            if (bet.equals(BigDecimal.ZERO)) {
            	actions.add(ActionType.CHECK);
                actions.add(ActionType.BET);
                
            } else {
                if (actorBet.compareTo(bet) < 0) {
                    actions.add(ActionType.CALL);                  
                    actions.add(ActionType.RAISE);
                    
                } else {
                    actions.add(ActionType.CHECK);
                    actions.add(ActionType.RAISE);                    
                }
            }
            actions.add(ActionType.FOLD);
        }
        return actions;
    }
    
    private void contributePot(BigDecimal amount) {
        for (Pot pot : pots) {
            if (!pot.hasContributer(actor)) {
                BigDecimal potBet = pot.getBet();
                if (amount.compareTo(potBet) >= 0) {
                    // Regular call, bet or raise.
                    pot.addContributer(actor);
                    amount = amount.subtract(pot.getBet());
                } else {
                    // Partial call (all-in); redistribute pots.
                    pots.add(pot.split(actor, amount));
                    amount = BigDecimal.ZERO;
                }
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            Pot pot = new Pot(amount);
            pot.addContributer(actor);
            pots.add(pot);
        }
    }
    
    
    private void doShowdown() {

      // Determine show order; start with all-in players...
      List<Player> showingPlayers = new ArrayList<>();
      for (Pot pot : pots) {
          for (Player contributor : pot.getContributors()) {
              if (!showingPlayers.contains(contributor) && contributor.isAllIn()) {
                  showingPlayers.add(contributor);
              }
          }
      }
      // ...then last player to bet or raise (aggressor)...
      if (lastBettor != null) {
          if (!showingPlayers.contains(lastBettor)) {
              showingPlayers.add(lastBettor);
          }
      }
      //...and finally the remaining players, starting left of the button.
      int pos = (dealerPosition + 1) % activePlayers.size();
      while (showingPlayers.size() < activePlayers.size()) {
          Player player = activePlayers.get(pos);
          if (!showingPlayers.contains(player)) {
              showingPlayers.add(player);
          }
          pos = (pos + 1) % activePlayers.size();
      }
      
      // Players automatically show or fold in order.
      boolean firstToShow = true;
      int bestHandValue = -1;
      for (Player playerToShow : showingPlayers) {
          Hand hand = new Hand(board);
          hand.addCards(playerToShow.getCards());
          HandValue handValue = new HandValue(hand);
          boolean doShow = false;  //todo: implement always show down.
          if (!doShow) {
              if (playerToShow.isAllIn()) {
                  // All-in players must always show.
                  doShow = true;
                  firstToShow = false;
              } else if (firstToShow) {
                  // First player must always show.
                  doShow = true;
                  bestHandValue = handValue.getValue();
                  firstToShow = false;
              } else {
                  // Remaining players only show when having a chance to win.
                  if (handValue.getValue() >= bestHandValue) {
                      doShow = true;
                      bestHandValue = handValue.getValue();
                  }
              }
          }
          if (doShow) {
              // Show hand.
              for (Player player : players) {
                  //show hand
              }
              
          } else {
              // Fold.
              playerToShow.setCards(null);
              activePlayers.remove(playerToShow);
              for (Player player : players) {
                  if (player.equals(playerToShow)) {
                      //show card
                  } else {
                      // Hide secret information to other players.
                      //no show card
                  }
              }              
          }
      }
      
      // Sort players by hand value (highest to lowest).
      Map<HandValue, List<Player>> rankedPlayers = new TreeMap<>();
      for (Player player : activePlayers) {
          // Create a hand with the community cards and the player's hole cards.
          Hand hand = new Hand(board);
          hand.addCards(player.getCards());
          // Store the player together with other players with the same hand value.
          HandValue handValue = new HandValue(hand);
//          System.out.format("[DEBUG] %s: %s\n", player, handValue);
          List<Player> playerList = rankedPlayers.get(handValue);
          if (playerList == null) {
              playerList = new ArrayList<>();
          }
          playerList.add(player);
          rankedPlayers.put(handValue, playerList);
      }

      // Per rank (single or multiple winners), calculate pot distribution.
      BigDecimal totalPot = getTotalPot();
      Map<Player, BigDecimal> potDivision = new HashMap<>();
      for (HandValue handValue : rankedPlayers.keySet()) {
          List<Player> winners = rankedPlayers.get(handValue);
          for (Pot pot : pots) {
              // Determine how many winners share this pot.
              int noOfWinnersInPot = 0;
              for (Player winner : winners) {
                  if (pot.hasContributer(winner)) {
                      noOfWinnersInPot++;
                  }
              }
              if (noOfWinnersInPot > 0) {
                  // Divide pot over winners.
                  BigDecimal potShare = pot.getValue().divide(new BigDecimal(String.valueOf(noOfWinnersInPot)),BigDecimal.ROUND_DOWN); 
                  for (Player winner : winners) {
                      if (pot.hasContributer(winner)) {
                          BigDecimal oldShare = potDivision.get(winner);
                          if (oldShare != null) {
                              potDivision.put(winner, oldShare.add(potShare));
                          } else {
                              potDivision.put(winner, potShare);
                          }
                          
                      }
                  }
                  // Determine if we have any odd chips left in the pot.
                  BigDecimal oddChips = pot.getValue().remainder(new BigDecimal(String.valueOf(noOfWinnersInPot))); //TODO
                  if (oddChips.compareTo(BigDecimal.ZERO) > 0) {
                      // Divide odd chips over winners, starting left of the dealer.
                      pos = dealerPosition;
                      while (oddChips.compareTo(BigDecimal.ZERO) > 0) {
                          pos = (pos + 1) % activePlayers.size();
                          Player winner = activePlayers.get(pos);
                          BigDecimal oldShare = potDivision.get(winner);
                          if (oldShare != null) {
                              potDivision.put(winner, oldShare.add(BigDecimal.ONE));
//                              System.out.format("[DEBUG] %s receives an odd chip from the pot.\n", winner);
                              oddChips = oddChips.subtract(BigDecimal.ONE);
                          }
                      }
                      
                  }
                  pot.clear();
              }
          }
      }
      
      // Divide winnings.
      StringBuilder winnerText = new StringBuilder();
      BigDecimal totalWon = BigDecimal.ZERO;
      for (Player winner : potDivision.keySet()) {
          BigDecimal potShare = potDivision.get(winner);
          winner.win(potShare);
          totalWon = totalWon.add(potShare);
          if (winnerText.length() > 0) {
              winnerText.append(", ");
          }
          winnerText.append(String.format("%s wins $ %s", winner, potShare.toString()));
          
      }
      winnerText.append('.');  
      
      System.out.println(winnerText);
      
      // Sanity check.
      if (!totalWon.equals(totalPot)) {
          throw new IllegalStateException("Incorrect pot division!");
      }
  }
    
    private BigDecimal getTotalPot() {
        BigDecimal totalPot = BigDecimal.ZERO;
        for (Pot pot : pots) {
            totalPot = totalPot.add(pot.getValue());
        }
        return totalPot;
    }

}
