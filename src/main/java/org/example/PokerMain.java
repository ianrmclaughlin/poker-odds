package org.example;

import java.util.*;


public class PokerMain {

    final Integer TWO_CARD_COMBOS = 2652;
    final Integer LOWEST_VALUE = 2;
    final Integer HIGHEST_VALUE = 14;
    final Integer COMMUNITY_CARD_COUNT = 5;
    final Integer HAND_SIZE = 5;
    final Integer START_VALUE = 0;
    final Integer START_SUIT = 2;
    final Integer STRING_END = 3;
    final Integer CARD_FIVE = 4;
    final Integer CARD_FOUR = 3;
    final Integer CARD_THREE = 2;
    final Integer CARD_TWO = 1;
    final Integer CARD_ONE = 0;

    LinkedList<PlayingCard> cardDeck;

    public HashMap<String, Integer> pokerMain() {
        HashMap<String, Integer> winCountMap = new HashMap<>();
        // TODO replace with calls to real methods
        winCountMap.put("14C14D", 1);
        winCountMap.put("14C14H", 1);
        winCountMap.put("14C14S", 1);
        winCountMap.put("03S02H", 1);

        Integer n = 1;
        while (n <= TWO_CARD_COMBOS - 4) {
            winCountMap.put(n.toString(), n);
            n++;
        }
        return winCountMap;
    }

    public LinkedList<PlayingCard> resetCardDeck() {

        cardDeck = new LinkedList<PlayingCard>();
        LinkedList<PlayingCard> cards;

        cards = generateCards("C");
        cardDeck.addAll(cards);
        cards = generateCards("D");
        cardDeck.addAll(cards);
        cards = generateCards("H");
        cardDeck.addAll(cards);
        cards = generateCards("S");
        cardDeck.addAll(cards);

        return cardDeck;


    }

    private LinkedList<PlayingCard> generateCards(String cardSuit) {

        LinkedList<PlayingCard> cards = new LinkedList<>();

        for (Integer n = LOWEST_VALUE; n <= HIGHEST_VALUE; n++) {
            PlayingCard card = new PlayingCard();
            card.value = n;
            card.suit = cardSuit;
            cards.add(card);
        }

        return cards;
    }

    public PlayingCard dealCard() {
        Random rand = new Random();
        Integer cardNumber = rand.nextInt(cardDeck.size());
        PlayingCard card = cardDeck.get(cardNumber);
        cardDeck.remove(card);
        return card;
    }

    public Integer getCardDeckCount() {
        return cardDeck.size();
    }

    public LinkedList<PlayingCard> dealCommunityCards() {
        LinkedList<PlayingCard> communityCards = new LinkedList<PlayingCard>();
        for (Integer n = 1; n <= COMMUNITY_CARD_COUNT; n++) {
            PlayingCard card = dealCard();
            communityCards.add(card);
        }
        sortCards(communityCards);

        return communityCards;
    }

    public void sortCards(List<PlayingCard> cards) {
        CardSort cs = new CardSort();
        Collections.sort(cards, cs);
    }

    public List<PlayingCard> dealHoleCards() {
        List<PlayingCard> holeCards = new LinkedList<>();

        LinkedList<PlayingCard> cards = new LinkedList<>();

        cards.add(dealCard());
        cards.add(dealCard());

        sortCards(cards);

        holeCards.add(cards.get(0));
        holeCards.add(cards.get(1));

        return holeCards;

    }

    public String getWinner(List<PlayingCard> holeCardsAdam, List<PlayingCard> holeCardsEve, List<PlayingCard> communityCards) {

        PokerHand bestAdam = getBestHand(holeCardsAdam, communityCards);
        PokerHand bestEve = getBestHand(holeCardsEve, communityCards);
        //String winner = getWinningHand(bestAdam, bestEve);
        return null;
    }

    public String getWinningHand(PokerHand bestAdam, PokerHand bestEve) {

        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Pair")) return "Eve";
        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Two Pair")) return "Eve";
        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Three Of A Kind")) return "Eve";
        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Straight")) return "Eve";
        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Two Pair")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Three Of A Kind")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Straight")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Two Pair")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Three Of A Kind")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Straight")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("Pair") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Pair")) return "Adam";
        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Three Of A Kind")) return "Eve";
        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Straight")) return "Eve";
        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Pair")) return "Adam";
        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Two Pair")) return "Adam";
        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Straight")) return "Eve";
        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Pair")) return "Adam";
        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Two Pair")) return "Adam";
        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Three Of A Kind")) return "Adam";
        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Flush")) return "Eve";
        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Pair")) return "Adam";
        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Two Pair")) return "Adam";
        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Three Of A Kind")) return "Adam";
        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Straight")) return "Adam";
        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Straight Flush")) return "Eve";

        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("High Card")) return "Adam";
        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Pair")) return "Adam";
        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Two Pair")) return "Adam";
        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Three Of A Kind")) return "Adam";
        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Straight")) return "Adam";
        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Flush")) return "Adam";

        if (bestAdam.handType.equals("High Card") && bestEve.handType.equals("High Card")) {
            for (Integer cardNumber = 5; cardNumber >= 1; cardNumber--) {
                if (bestAdam.cards.get(cardNumber - 1).value < bestEve.cards.get(cardNumber - 1).value) return "Eve";
                if (bestAdam.cards.get(cardNumber - 1).value > bestEve.cards.get(cardNumber - 1).value) return "Adam";
            }
            return "Split Pot";
        }

        if (bestAdam.handType.equals("One Pair") && bestEve.handType.equals("One Pair")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Two Pair") && bestEve.handType.equals("Two Pair")) {
            int adamPair1 = bestAdam.cards.get(0).value;
            int adamPair2 = bestAdam.cards.get(2).value;
            int adamKicker = bestAdam.cards.get(4).value;
            int evePair1 = bestEve.cards.get(0).value;
            int evePair2 = bestEve.cards.get(2).value;
            int eveKicker = bestEve.cards.get(4).value;
            if (adamPair1 > evePair1) return "Adam";
            if (adamPair1 < evePair1) return "Eve";
            if ((adamPair1 == evePair1) && (adamPair2 > evePair2)) return "Adam";
            if ((adamPair1 == evePair1) && (adamPair2 < evePair2)) return "Eve";
            if ((adamPair1 == evePair1) && (adamPair2 == evePair2) && (adamKicker > eveKicker)) return "Adam";
            if ((adamPair1 == evePair1) && (adamPair2 == evePair2) && (adamKicker < eveKicker)) return "Eve";
            if ((adamPair1 == evePair1) && (adamPair2 == evePair2) && (adamKicker == eveKicker)) return "Split Pot";
        }

        if (bestAdam.handType.equals("Three Of A Kind") && bestEve.handType.equals("Three Of A Kind")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Straight") && bestEve.handType.equals("Straight")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Flush") && bestEve.handType.equals("Flush")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Full House") && bestEve.handType.equals("Full House")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Four Of A Kind") && bestEve.handType.equals("Four Of A Kind")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value == bestEve.cards.get(0).value) return "Split Pot";
        }

        if (bestAdam.handType.equals("Straight Flush") && bestEve.handType.equals("Straight Flush")) {
            if (bestAdam.cards.get(0).value > bestEve.cards.get(0).value) return "Adam";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Eve";
            if (bestAdam.cards.get(0).value < bestEve.cards.get(0).value) return "Split Pot";
        }



        // TODO - Add code for other winning hands

        return null;
    }

    public PokerHand getBestHand(List<PlayingCard> holeCards, List<PlayingCard> communityCards) {
        PokerHand bestHand = getBestHighValueHand(holeCards, communityCards);
        if (bestHand == null) {
            bestHand = getBestLowValueHand(holeCards, communityCards);
        }
        return bestHand;
    }


    public PokerHand getBestHighValueHand(List<PlayingCard> holeCards, List<PlayingCard> communityCards) {

        PokerHand bestHand = new PokerHand();
        bestHand.cards = new LinkedList<>();
        List<PlayingCard> playerHand;

        List<PlayingCard> cardsIn = new LinkedList<>();
        cardsIn.addAll(holeCards);
        cardsIn.addAll(communityCards);

        playerHand = getBestStraightFlush(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Straight Flush";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestFourOfAKind(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Four Of A Kind";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestFullHouse(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Full House";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestFlush(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Flush";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestStraight(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Straight";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        return null;

    }


    public List<PlayingCard> getBestStraightFlush(List<PlayingCard> sevenCards) {

        List<PlayingCard> clubs = new LinkedList<>();
        List<PlayingCard> diamonds = new LinkedList<>();
        List<PlayingCard> hearts = new LinkedList<>();
        List<PlayingCard> spades = new LinkedList<>();
        List<PlayingCard> bestStraightFlush = new LinkedList<>();

        // CLUBS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("C")) {
                clubs.add(card);
            }
        }
        if (clubs.size() >= 5) {
            bestStraightFlush = getBestStraight(clubs);
        }

        if (bestStraightFlush == null) bestStraightFlush = new LinkedList<>();

        if (bestStraightFlush.size() == 5) {
            return bestStraightFlush;
        } else {
            bestStraightFlush.clear();
        }

        // DIAMONDS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("D")) {
                diamonds.add(card);
            }
        }
        if (diamonds.size() >= 5) {
            bestStraightFlush = getBestStraight(diamonds);
        }

        if (bestStraightFlush == null) bestStraightFlush = new LinkedList<>();

        if (bestStraightFlush.size() == 5) {
            return bestStraightFlush;
        } else {
            bestStraightFlush.clear();
        }

        // HEARTS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("H")) {
                hearts.add(card);
            }
        }
        if (hearts.size() >= 5) { // TODO not needed?
            bestStraightFlush = getBestStraight(hearts);
        }

        if (bestStraightFlush == null) bestStraightFlush = new LinkedList<>();

        if (bestStraightFlush.size() == 5) {
            return bestStraightFlush;
        } else {
            bestStraightFlush.clear();
        }

        // SPADES

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("S")) {
                spades.add(card);
            }
        }
        if (spades.size() >= 5) { // TODO not needed?
            bestStraightFlush = getBestStraight(spades);
        }

        if (bestStraightFlush == null) bestStraightFlush = new LinkedList<>();

        if (bestStraightFlush.size() == 5) {
            return bestStraightFlush;
        } else {
            bestStraightFlush.clear();
        }

        if (bestStraightFlush.size() > 0) {
            return bestStraightFlush;
        } else {
            return null;
        }
    }

    public List<PlayingCard> getBestStraight(List<PlayingCard> sevenCards) {

        List<PlayingCard> cards = new LinkedList<>();

        for (PlayingCard card : sevenCards) {
            cards.add(card);
            if (card.value == 14) {
                PlayingCard pc = new PlayingCard();
                pc.value = 1;
                pc.suit = card.suit;
                cards.add(pc);
            }
        }

        sortCards(cards);

        Set<Integer> integerValues = new HashSet<>();

        for (PlayingCard card : cards) {
            integerValues.add(card.value);
        }

        List<List<Integer>> contiguousSeqList = getContiguousSequences(integerValues);

        List<Integer> straightValues = null;
        for (List<Integer> integerList : contiguousSeqList) {
            if (integerList.size() >= 5) {
                straightValues = integerList;
            }
        }

        if (straightValues == null) return null;

        List<PlayingCard> straight = new LinkedList();

        for (Integer n = straightValues.size() - 1; n >= 0; n--) {
            PlayingCard card = new PlayingCard();
            card.value = straightValues.get(n);
            card.suit = " ";
            straight.add(card);
            if ((straightValues.size() - n) >= 5) break;
        }

        sortCards(straight);

        if (straight.get(0).value == 1) {
            PlayingCard card = new PlayingCard();
            card.value = 14;
            card.suit = straight.get(0).suit;
            straight.set(0, card);
        }

        // Populate suits

        for (PlayingCard straightCard : straight) {
            for (PlayingCard card : sevenCards) {
                if (straightCard.value == card.value) {
                    straightCard.suit = card.suit;
                }
            }
        }

        if (straight.size() > 0) {
            return straight;
        } else {
            return null;
        }
    }

    public List<PlayingCard> getBestFlush(List<PlayingCard> sevenCards) {

        List<PlayingCard> clubs = new LinkedList<>();
        List<PlayingCard> diamonds = new LinkedList<>();
        List<PlayingCard> hearts = new LinkedList<>();
        List<PlayingCard> spades = new LinkedList<>();
        List<PlayingCard> bestFlush = new LinkedList<>();

        // CLUBS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("C")) {
                clubs.add(card);
            }
        }

        sortCards(clubs);
        Collections.reverse(clubs);
        Integer count = 0;
        for (PlayingCard card : clubs) {
            count++;
            bestFlush.add(card);
            if (count >= 5) break;

        }

        if (bestFlush.size() == 5) {
            Collections.reverse(bestFlush);
            return bestFlush;
        } else {
            bestFlush.clear();
        }

        // DIAMONDS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("D")) {
                diamonds.add(card);
            }
        }

        sortCards(diamonds);
        Collections.reverse(diamonds);
        count = 0;
        for (PlayingCard card : diamonds) {
            count++;
            bestFlush.add(card);
            if (count >= 5) break;

        }

        if (bestFlush.size() == 5) {
            Collections.reverse(bestFlush);
            return bestFlush;
        } else {
            bestFlush.clear();
        }

        // HEARTS

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("H")) {
                hearts.add(card);
            }
        }

        sortCards(hearts);
        Collections.reverse(hearts);
        count = 0;
        for (PlayingCard card : hearts) {
            count++;
            bestFlush.add(card);
            if (count >= 5) break;

        }

        if (bestFlush.size() == 5) {
            Collections.reverse(bestFlush);
            return bestFlush;
        } else {
            bestFlush.clear();
        }

        // SPADES

        for (PlayingCard card : sevenCards) {
            if (card.suit.equals("S")) {
                spades.add(card);
            }
        }

        sortCards(spades);
        Collections.reverse(spades);
        count = 0;
        for (PlayingCard card : spades) {
            count++;
            bestFlush.add(card);
            if (count >= 5) break;

        }

        if (bestFlush.size() == 5) {
            Collections.reverse(bestFlush);
            return bestFlush;
        } else {
            bestFlush.clear();
        }

        if (bestFlush.size() > 0) {
            Collections.reverse(bestFlush);
            return bestFlush;
        } else {
            return null;
        }
    }

    public PokerHand getBestLowValueHand(List<PlayingCard> holeCards, List<PlayingCard> communityCards) {

        PokerHand bestHand = new PokerHand();
        bestHand.cards = new LinkedList<>();
        List<PlayingCard> playerHand;

        List<PlayingCard> cardsIn = new LinkedList<>();
        cardsIn.addAll(holeCards);
        cardsIn.addAll(communityCards);

        playerHand = getBestThreeOfAKind(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Three Of A Kind";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestTwoPairHand(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "Two Pair";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestOnePairHand(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "One Pair";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        playerHand = getBestHighCardHand(cardsIn);

        if (playerHand != null) {
            bestHand.handType = "High Card";
            bestHand.cards.addAll(playerHand);
            return bestHand;
        }

        return null;
    }

    public void addCards(List<PlayingCard> cards, String... cardList) {

        for (String s : cardList) {
            PlayingCard card = new PlayingCard();
            card.value = Integer.parseInt(s.substring(START_VALUE, START_SUIT));
            card.suit = s.substring(START_SUIT, STRING_END);
            cards.add(card);
        }
    }

    public List<List<Integer>> getContiguousSequences(Set<Integer> integers) {

        List<List<Integer>> contiguousSequences = new LinkedList<>();
        List<Integer> contIntegerList = new LinkedList<>();
        Integer lastValue = -1;
        for (Integer currentValue : integers) {

            if (currentValue - lastValue > 1) {
                contIntegerList = new LinkedList<>();
                contiguousSequences.add(contIntegerList);
            }
            contIntegerList.add(currentValue);
            lastValue = currentValue;
        }
        return contiguousSequences;
    }

    public List<PlayingCard> getBestFourOfAKind(List<PlayingCard> sevenCards) {

        List<PlayingCard> cardsIn = new LinkedList<>();
        List<PlayingCard> fourOfAKind = new LinkedList<>();

        for (Integer n = 2; n <= 14; n++) {

            fourOfAKind.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == n) {
                    cardCount++;
                    fourOfAKind.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 4) {
                sortCards(cardsIn);
                Collections.reverse(cardsIn);
                fourOfAKind.add(cardsIn.get(0));
                return fourOfAKind;
            }
        }
        return null;
    }

    public List<PlayingCard> getBestThreeOfAKind(List<PlayingCard> sevenCards) {

        // Cant have more than one 3 of a kind: that would be a full house

        List<PlayingCard> cardsIn = new LinkedList<>();
        List<PlayingCard> threeOfAKind = new LinkedList<>();

        for (Integer n = 2; n <= 14; n++) {

            threeOfAKind.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == n) {
                    cardCount++;
                    threeOfAKind.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 3) {
                sortCards(cardsIn);
                Collections.reverse(cardsIn);
                threeOfAKind.add(cardsIn.get(1));
                threeOfAKind.add(cardsIn.get(0));
                return threeOfAKind;
            }
        }
        return null;
    }

    public List<PlayingCard> getBestFullHouse(List<PlayingCard> sevenCards) {
        List<PlayingCard> cardsIn = new LinkedList<>();
        List<PlayingCard> fullHouse = new LinkedList<>();

        for (Integer n = 14; n >= 2; n--) {

            fullHouse.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == n) {
                    cardCount++;
                    fullHouse.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 3) {

                sortCards(fullHouse);

                // Look for a pair in remaining four cards

                PlayingCard card1 = cardsIn.get(0);
                PlayingCard card2 = cardsIn.get(1);
                PlayingCard card3 = cardsIn.get(2);
                PlayingCard card4 = cardsIn.get(3);

                if (card1.value == card2.value) {
                    fullHouse.add(card1);
                    fullHouse.add(card2);
                    return fullHouse;
                }
                if (card1.value == card3.value) {
                    fullHouse.add(card1);
                    fullHouse.add(card3);
                    return fullHouse;
                }
                if (card1.value == card4.value) {
                    fullHouse.add(card1);
                    fullHouse.add(card4);
                    return fullHouse;
                }
                if (card2.value == card3.value) {
                    fullHouse.add(card2);
                    fullHouse.add(card3);
                    return fullHouse;
                }
                if (card2.value == card4.value) {
                    fullHouse.add(card2);
                    fullHouse.add(card4);
                    return fullHouse;
                }
                if (card3.value == card4.value) {
                    fullHouse.add(card3);
                    fullHouse.add(card4);
                    return fullHouse;
                }
            }
        }
        return null;
    }

    public List<PlayingCard> getBestOnePairHand(List<PlayingCard> sevenCards) {

        List<PlayingCard> cardsIn = new LinkedList<>();
        List<PlayingCard> pair = new LinkedList<>();

        for (Integer n = 2; n <= 14; n++) {

            pair.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == n) {
                    cardCount++;
                    pair.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 2) {
                sortCards(cardsIn);
                Collections.reverse(cardsIn);
                pair.add(cardsIn.get(2));
                pair.add(cardsIn.get(1));
                pair.add(cardsIn.get(0));
                return pair;
            }
        }
        return null;
    }

    public List<PlayingCard> getBestTwoPairHand(List<PlayingCard> cards) {

        List<PlayingCard> sevenCards = new LinkedList<>();
        sevenCards.addAll(cards);

        List<PlayingCard> cardsIn = new LinkedList<>();

        // pair #1

        List<PlayingCard> pair1 = new LinkedList<>();

        for (Integer cardValue = LOWEST_VALUE; cardValue <= HIGHEST_VALUE; cardValue++) {

            pair1.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == cardValue) {
                    cardCount++;
                    pair1.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 2) {
                sevenCards.remove(pair1.get(0));
                sevenCards.remove(pair1.get(1));
                break;
            }
        }

        // pair #2

        List<PlayingCard> pair2 = new LinkedList<>();

        for (Integer cardValue = LOWEST_VALUE; cardValue <= HIGHEST_VALUE; cardValue++) {

            pair2.clear();
            cardsIn.clear();
            cardsIn.addAll(sevenCards);

            Integer cardCount = 0;

            for (PlayingCard card : sevenCards) {
                if (card.value == cardValue) {
                    cardCount++;
                    pair2.add(card);
                    cardsIn.remove(card);
                }
            }

            if (cardCount == 2) {
                break;
            }
        }
        if (pair1.size() == 2 && pair2.size() == 2) {
            sevenCards.remove(pair2.get(0));
            sevenCards.remove(pair2.get(1));
            sortCards(sevenCards);
            Collections.reverse(sevenCards);
            List<PlayingCard> twoPair = new LinkedList();
            twoPair.addAll(pair2);
            twoPair.addAll(pair1);
            twoPair.add(sevenCards.get(0));
            return twoPair;
        }
        return null;
    }

    public List<PlayingCard> getBestHighCardHand(List<PlayingCard> sevenCards) {

        sortCards(sevenCards);

        List<PlayingCard> highestCardHand = new LinkedList<PlayingCard>();

        highestCardHand.addAll(sevenCards);

        highestCardHand.remove(0);
        highestCardHand.remove(0);

        return highestCardHand;
    }

    public Boolean compareCards(List<PlayingCard> cards1, List<PlayingCard> cards2) {

        if (cards1.size() != cards2.size()) return false;

        for (Integer n = 0; n < cards1.size(); n++) {
            if (cards1.get(n).value != cards2.get(n).value) return false;
            if (!cards1.get(n).suit.equals(cards2.get(n).suit)) return false;
        }

        return true;

    }
}
