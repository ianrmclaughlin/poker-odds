package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class PokerMainTest {

    final Integer TWO_CARD_COMBOS = 2652;
    final Integer CARD_DECK = 52;
    final String CARD_SUIT_REGEX = "C|D|H|S";
    final int COMMUNITY_CARD_COUNT = 5;

    @Test
    public void pokerMainTest() {
        PokerMain pokerMain = new PokerMain();
        HashMap<String, Integer> winCountMap = pokerMain.pokerMain();
        Assertions.assertTrue(winCountMap.get("14C14D") > 0, "14C14D > 0");
        Assertions.assertTrue(winCountMap.get("14C14H") > 0, "14C14H > 0");
        Assertions.assertTrue(winCountMap.get("14C14S") > 0, "14C14S > 0");
        Assertions.assertTrue(winCountMap.get("03S02H") > 0, "03S02H > 0");
        Assertions.assertEquals(TWO_CARD_COMBOS, winCountMap.size());
    }

    @Test
    public void resetCardDeckTest() {
        PlayingCard card;
        PokerMain pokerMain = new PokerMain();
        LinkedList<PlayingCard> cardDeck = pokerMain.resetCardDeck();
        Assertions.assertEquals(CARD_DECK, cardDeck.size());
        card = cardDeck.get(0);
        Assertions.assertEquals(2, card.value);
        Assertions.assertEquals("C", card.suit);
        card = cardDeck.get(CARD_DECK - 1);
        Assertions.assertEquals(14, card.value);
        Assertions.assertEquals("S", card.suit);
    }

    @Test
    public void dealCardTest() {
        PokerMain pokerMain = new PokerMain();
        pokerMain.resetCardDeck();

        PlayingCard card = pokerMain.dealCard();
        Integer cardCount = pokerMain.getCardDeckCount();
        Assertions.assertTrue(card.value >= 2 && card.value <= 14);
        Assertions.assertTrue(card.suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(cardCount == CARD_DECK - 1);

        card = pokerMain.dealCard();
        cardCount = pokerMain.getCardDeckCount();
        Assertions.assertTrue(card.value >= 2 && card.value <= 14);
        Assertions.assertTrue(card.suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(cardCount == CARD_DECK - 2);

        card = pokerMain.dealCard();
        cardCount = pokerMain.getCardDeckCount();
        Assertions.assertTrue(card.value >= 2 && card.value <= 14);
        Assertions.assertTrue(card.suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(cardCount == CARD_DECK - 3);
    }

    @Test
    public void dealCommunityCardsTest() {

        PokerMain pokerMain = new PokerMain();
        pokerMain.resetCardDeck();

        LinkedList<PlayingCard> communityCards = pokerMain.dealCommunityCards();
        Assertions.assertEquals(COMMUNITY_CARD_COUNT, communityCards.size());

        Integer cardCount = pokerMain.getCardDeckCount();
        Assertions.assertEquals(CARD_DECK - 5, cardCount);

        Integer minValue = 99;
        Integer maxValue = 0;

        for (Integer n = 0; n < COMMUNITY_CARD_COUNT; n++) {
            PlayingCard card = communityCards.get(n);
            if (card.value < minValue) minValue = card.value;
            if (card.value > maxValue) maxValue = card.value;

            Assertions.assertTrue(card.value >= 2 && card.value <= 14);
            Assertions.assertTrue(card.suit.matches(CARD_SUIT_REGEX));
        }

        Assertions.assertEquals(communityCards.get(0).value, minValue);
        Assertions.assertEquals(communityCards.get(COMMUNITY_CARD_COUNT - 1).value, maxValue);
    }

    @Test
    public void dealHoleCardsTest() {

        PokerMain pokerMain = new PokerMain();
        pokerMain.resetCardDeck();

        List<PlayingCard> holeCardsAdam = pokerMain.dealHoleCards();
        List<PlayingCard> holeCardsEve = pokerMain.dealHoleCards();

        Integer cardCount = pokerMain.getCardDeckCount();
        Assertions.assertEquals(CARD_DECK - 4, cardCount);

        Assertions.assertTrue(holeCardsAdam.get(0).value <= holeCardsAdam.get(1).value);
        Assertions.assertTrue(holeCardsEve.get(0).value <= holeCardsEve.get(1).value);

        Assertions.assertTrue(holeCardsAdam.get(0).suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(holeCardsAdam.get(1).suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(holeCardsEve.get(0).suit.matches(CARD_SUIT_REGEX));
        Assertions.assertTrue(holeCardsEve.get(1).suit.matches(CARD_SUIT_REGEX));
    }

    @Test
    public void getWinnerTest() {

        PokerMain pokerMain = new PokerMain();
        pokerMain.resetCardDeck();

        // pair of 14s beats a pair of 13s

        List<PlayingCard> holeCardsAdam = new LinkedList<>();
        pokerMain.addCards(holeCardsAdam, "13C", "13D");

        List<PlayingCard> holeCardsEve = new LinkedList<>();
        pokerMain.addCards(holeCardsAdam, "14C", "14D");


        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(holeCardsAdam, "02C", "03C", "04C", "05C", "06C");

        String winner = pokerMain.getWinner(holeCardsAdam, holeCardsEve, communityCards);

        // TODO enable
        //Assertions.assertEquals("Eve", winner);

    }

    @Test
    public void getBestHandTest_royalFlush() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "13C", "14C");
        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "08C", "09C", "10C", "11C", "12C");
        LinkedList<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "10C", "11C", "12C", "13C", "14C");

        // WHEN
        PokerHand bestHand = pokerMain.getBestHand(holeCards, communityCards);

        // THEN
        Assertions.assertEquals("Straight Flush", bestHand.handType);
        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);
        Assertions.assertTrue(isMatch);
    }

    @Test
    public void getBestLowValueHandTest_highCard() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "03D");
        List<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "04H", "05S", "07C", "08D", "09H");
        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "04H", "05S", "07C", "08D", "09H");

        // WHEN
        PokerHand bestHand = pokerMain.getBestLowValueHand(holeCards, communityCards);

        // THEN
        Assertions.assertEquals("High Card", bestHand.handType);
        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);
        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestLowValueHandTest_onePair() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "03D");
        List<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "02H", "05S", "07C", "08D", "09H");
        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "02C", "02H", "07C", "08D", "09H");

        // WHEN
        PokerHand bestHand = pokerMain.getBestLowValueHand(holeCards, communityCards);

        // THEN
        Assertions.assertEquals("One Pair", bestHand.handType);
        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);
        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestLowValueHandTest_twoPair() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "03D");
        List<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "02H", "03S", "07C", "08D", "09H");
        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "03D", "03S", "02C", "02H","09H" );

        // WHEN
        PokerHand bestHand = pokerMain.getBestLowValueHand(holeCards, communityCards);

        // THEN
        Assertions.assertEquals("Two Pair", bestHand.handType);
        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);
        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestLowValueHandTest_threeOfAKind() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "02D");
        List<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "02H", "03S", "07C", "08D", "09H");
        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "02C", "02D","02H","08D","09H" );

        // WHEN
        PokerHand bestHand = pokerMain.getBestLowValueHand(holeCards, communityCards);

        // THEN
        Assertions.assertEquals("Three Of A Kind", bestHand.handType);
        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);
        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestHighValueHandTest_straightFlush() {

        PokerMain pokerMain = new PokerMain();

        LinkedList<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "13C", "14C");

        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "08C", "09C", "10C", "11C", "12C");

        PokerHand bestHand = pokerMain.getBestHighValueHand(holeCards, communityCards);

        Assertions.assertEquals("Straight Flush", bestHand.handType);

        LinkedList<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "10C", "11C", "12C", "13C", "14C");

        Boolean isMatch = pokerMain.compareCards(bestHand.cards, expectedCards);

        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestHighValueHandTest_fourAces() {

        PokerMain pokerMain = new PokerMain();

        LinkedList<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "14C", "14D");

        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "14H", "14S", "02C", "04D", "06H");

        PokerHand bestHand = pokerMain.getBestHighValueHand(holeCards, communityCards);

        Assertions.assertEquals("Four Of A Kind", bestHand.handType);

        LinkedList<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "14C", "14D", "14H", "14S", "06H");
    }

    @Test
    public void getBestHighValueHandTest_fullHouse() {

        PokerMain pokerMain = new PokerMain();

        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "14C", "14D");

        List<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "14H", "13S", "13C", "04D", "06H");

        PokerHand bestHand = pokerMain.getBestHighValueHand(holeCards, communityCards); // null pointer

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "14C", "14D", "14H", "13S", "13C");

        Assertions.assertEquals("Full House", bestHand.handType);

        Boolean isMatch = pokerMain.compareCards(bestHand.cards, expectedCards);

        Assertions.assertTrue(isMatch);
    }

    @Test
    public void getBestHighValueHandTest_flush() {

        PokerMain pokerMain = new PokerMain();

        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "04C");

        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "06C", "08C", "10C", "12D", "14H");

        LinkedList<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "02C", "04C", "06C", "08C", "10C");

        PokerHand bestHand = pokerMain.getBestHighValueHand(holeCards, communityCards);

        Assertions.assertEquals("Flush", bestHand.handType);

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);

        Assertions.assertTrue(isMatch);
    }

    @Test
    public void getBestHighValueHandTest_straight() {

        PokerMain pokerMain = new PokerMain();

        List<PlayingCard> holeCards = new LinkedList<>();
        pokerMain.addCards(holeCards, "02C", "03D");

        LinkedList<PlayingCard> communityCards = new LinkedList<>();
        pokerMain.addCards(communityCards, "04H", "05S", "06C", "13D", "14H");

        LinkedList<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "02C", "03D", "04H", "05S", "06C");

        PokerHand bestHand = pokerMain.getBestHighValueHand(holeCards, communityCards);

        Assertions.assertEquals("Straight", bestHand.handType);

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestHand.cards);

        Assertions.assertTrue(isMatch);
    }


    @Test
    public void getBestStraightTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "07C", "08D", "09H", "10S", "11C", "12D", "13H");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertEquals(9, bestStraight.get(0).value);
        Assertions.assertEquals(10, bestStraight.get(1).value);
        Assertions.assertEquals(11, bestStraight.get(2).value);
        Assertions.assertEquals(12, bestStraight.get(3).value);
        Assertions.assertEquals(13, bestStraight.get(4).value);

        Assertions.assertEquals("H", bestStraight.get(0).suit);
        Assertions.assertEquals("S", bestStraight.get(1).suit);
        Assertions.assertEquals("C", bestStraight.get(2).suit);
        Assertions.assertEquals("D", bestStraight.get(3).suit);
        Assertions.assertEquals("H", bestStraight.get(4).suit);


    }

    @Test
    public void getBestStraightTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "14C", "02C", "03C", "04C", "05C", "12C", "13C");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertEquals(14, bestStraight.get(0).value);
        Assertions.assertEquals(2, bestStraight.get(1).value);
        Assertions.assertEquals(3, bestStraight.get(2).value);
        Assertions.assertEquals(4, bestStraight.get(3).value);
        Assertions.assertEquals(5, bestStraight.get(4).value);

        Assertions.assertEquals("C", bestStraight.get(0).suit);
        Assertions.assertEquals("C", bestStraight.get(1).suit);
        Assertions.assertEquals("C", bestStraight.get(2).suit);
        Assertions.assertEquals("C", bestStraight.get(3).suit);
        Assertions.assertEquals("C", bestStraight.get(4).suit);

    }

    @Test
    public void getBestStraightTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "14C", "05C", "06C", "07C", "08C", "09C", "13C");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertEquals(5, bestStraight.get(0).value);
        Assertions.assertEquals(6, bestStraight.get(1).value);
        Assertions.assertEquals(7, bestStraight.get(2).value);
        Assertions.assertEquals(8, bestStraight.get(3).value);
        Assertions.assertEquals(9, bestStraight.get(4).value);

        Assertions.assertEquals("C", bestStraight.get(0).suit);
        Assertions.assertEquals("C", bestStraight.get(1).suit);
        Assertions.assertEquals("C", bestStraight.get(2).suit);
        Assertions.assertEquals("C", bestStraight.get(3).suit);
        Assertions.assertEquals("C", bestStraight.get(4).suit);

    }

    @Test
    public void getBestStraightTest4() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "04C", "05C", "06C", "08C", "09C", "10C", "11C");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertTrue(bestStraight == null);


    }

    @Test
    public void getBestStraightTest5() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();

        sevenCards.clear();
        pokerMain.addCards(sevenCards, "14C", "14S", "02C", "03C", "04C", "05C", "05S");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertEquals(14, bestStraight.get(0).value);
        Assertions.assertEquals(2, bestStraight.get(1).value);
        Assertions.assertEquals(3, bestStraight.get(2).value);
        Assertions.assertEquals(4, bestStraight.get(3).value);
        Assertions.assertEquals(5, bestStraight.get(4).value);

        Assertions.assertEquals("S", bestStraight.get(0).suit);
        Assertions.assertEquals("C", bestStraight.get(1).suit);
        Assertions.assertEquals("C", bestStraight.get(2).suit);
        Assertions.assertEquals("C", bestStraight.get(3).suit);
        Assertions.assertEquals("S", bestStraight.get(4).suit);
    }

    @Test
    public void getBestStraightTest6() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraight;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "07C", "08D", "09H", "10S", "11C", "11D", "11H");

        bestStraight = pokerMain.getBestStraight(sevenCards);

        Assertions.assertEquals(7, bestStraight.get(0).value);
        Assertions.assertEquals(8, bestStraight.get(1).value);
        Assertions.assertEquals(9, bestStraight.get(2).value);
        Assertions.assertEquals(10, bestStraight.get(3).value);
        Assertions.assertEquals(11, bestStraight.get(4).value);

        Assertions.assertEquals("C", bestStraight.get(0).suit);
        Assertions.assertEquals("D", bestStraight.get(1).suit);
        Assertions.assertEquals("H", bestStraight.get(2).suit);
        Assertions.assertEquals("S", bestStraight.get(3).suit);
        Assertions.assertEquals("H", bestStraight.get(4).suit);
    }

    @Test
    public void getBestFlushTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "04C", "06C", "08C", "10C", "12C", "14C");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertTrue(bestFlush != null, "bestFlush != null");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "06C", "08C", "10C", "12C", "14C");

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestFlush);

        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestFlushTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02D", "04D", "06D", "08D", "10D", "12D", "14D");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertTrue(bestFlush != null, "bestFlush != null");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "06D", "08D", "10D", "12D", "14D");

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestFlush);

        Assertions.assertTrue(isMatch);

    }

    @Test
    public void getBestFlushTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02H", "04H", "06H", "08H", "10H", "12H", "14H");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertTrue(bestFlush != null, "bestFlush != null");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "06H", "08H", "10H", "12H", "14H");

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestFlush);

        Assertions.assertTrue(isMatch);
    }

    @Test
    public void getBestFlushTest4() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02S", "04S", "06S", "08S", "10S", "12S", "14S");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertTrue(bestFlush != null, "bestFlush != null");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "06S", "08S", "10S", "12S", "14S");

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestFlush);

        Assertions.assertTrue(isMatch);
    }

    @Test
    public void getBestFlushTest5() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "04D", "06H", "08S", "10C", "12C", "14C");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertEquals(null, bestFlush);

    }

    @Test
    public void getBestFlushTest6() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "04D", "06C", "08S", "10C", "12C", "14C");

        bestFlush = pokerMain.getBestFlush(sevenCards);

        Assertions.assertTrue(bestFlush != null, "bestFlush != null");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "02C", "06C", "10C", "12C", "14C");

        Boolean isMatch = pokerMain.compareCards(expectedCards, bestFlush);

        Assertions.assertTrue(isMatch);
    }

    @Test
    public void addCardsTest() {

        PokerMain pokerMain = new PokerMain();

        LinkedList<PlayingCard> cards = new LinkedList<>();

        pokerMain.addCards(cards, "02C", "03H", "14D", "10S");
        Assertions.assertEquals(4, cards.size());

        pokerMain.addCards(cards, "13S", "12D");
        Assertions.assertEquals(6, cards.size());

        Assertions.assertEquals(2, cards.get(0).value);
        Assertions.assertEquals("C", cards.get(0).suit);
        Assertions.assertEquals(3, cards.get(1).value);
        Assertions.assertEquals("H", cards.get(1).suit);
        Assertions.assertEquals(14, cards.get(2).value);
        Assertions.assertEquals("D", cards.get(2).suit);
        Assertions.assertEquals(10, cards.get(3).value);
        Assertions.assertEquals("S", cards.get(3).suit);
        Assertions.assertEquals(13, cards.get(4).value);
        Assertions.assertEquals("S", cards.get(4).suit);
        Assertions.assertEquals(12, cards.get(5).value);
        Assertions.assertEquals("D", cards.get(5).suit);

    }

    @Test
    public void getContiguousSequenceListTest1() {

        PokerMain pokerMain = new PokerMain();

        Set<Integer> integerList = new HashSet<>(Arrays.asList(1, 2, 3));

        List<List<Integer>> contiguousSeqList = pokerMain.getContiguousSequences(integerList);

        List<Integer> exp1 = Arrays.asList(1, 2, 3);

        List<List<Integer>> expected = Arrays.asList(exp1);

        Assertions.assertEquals(expected, contiguousSeqList);
    }

    @Test
    public void getContiguousSequenceListTest2() {

        PokerMain pokerMain = new PokerMain();

        Set<Integer> integerList = new HashSet<>(Arrays.asList(1, 2, 3, 5, 6, 7, 9, 10, 11));

        List<List<Integer>> contiguousSeqList = pokerMain.getContiguousSequences(integerList);

        List<Integer> exp1 = Arrays.asList(1, 2, 3);
        List<Integer> exp2 = Arrays.asList(5, 6, 7);
        List<Integer> exp3 = Arrays.asList(9, 10, 11);

        List<List<Integer>> expected = Arrays.asList(exp1, exp2, exp3);

        Assertions.assertEquals(expected, contiguousSeqList);
    }

    @Test
    public void getContiguousSequenceListTest3() {

        PokerMain pokerMain = new PokerMain();

        Set<Integer> integerList = new HashSet<>(Arrays.asList(1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14));

        List<List<Integer>> contiguousSeqList = pokerMain.getContiguousSequences(integerList);

        List<Integer> exp1 = Arrays.asList(1, 2, 3);
        List<Integer> exp2 = Arrays.asList(5, 6, 7);
        List<Integer> exp3 = Arrays.asList(9, 10, 11, 12, 13, 14);

        List<List<Integer>> expected = Arrays.asList(exp1, exp2, exp3);

        Assertions.assertEquals(expected, contiguousSeqList);
    }

    @Test
    public void getContiguousSequenceListTest4() {

        PokerMain pokerMain = new PokerMain();

        Set<Integer> integerList = new HashSet<>(Arrays.asList(1, 3, 5, 7, 9, 11, 13));

        List<List<Integer>> contiguousSeqList = pokerMain.getContiguousSequences(integerList);

        List<Integer> exp1 = Arrays.asList(1);
        List<Integer> exp2 = Arrays.asList(3);
        List<Integer> exp3 = Arrays.asList(5);
        List<Integer> exp4 = Arrays.asList(7);
        List<Integer> exp5 = Arrays.asList(9);
        List<Integer> exp6 = Arrays.asList(11);
        List<Integer> exp7 = Arrays.asList(13);

        List<List<Integer>> expected = Arrays.asList(exp1, exp2, exp3, exp4, exp5, exp6, exp7);

        Assertions.assertEquals(expected, contiguousSeqList);
    }

    @Test

    public void getBestStraightFlushTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraightFlush;
        List<PlayingCard> expectedCards;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "07C", "08C", "09C", "10C", "11C", "12D", "13D");

        expectedCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "13D", "12D", "11C", "10C", "09C");

        bestStraightFlush = pokerMain.getBestStraightFlush(sevenCards);


        Assertions.assertEquals(7, bestStraightFlush.get(0).value);
        Assertions.assertEquals(8, bestStraightFlush.get(1).value);
        Assertions.assertEquals(9, bestStraightFlush.get(2).value);
        Assertions.assertEquals(10, bestStraightFlush.get(3).value);
        Assertions.assertEquals(11, bestStraightFlush.get(4).value);

        Assertions.assertEquals("C", bestStraightFlush.get(0).suit);
        Assertions.assertEquals("C", bestStraightFlush.get(1).suit);
        Assertions.assertEquals("C", bestStraightFlush.get(2).suit);
        Assertions.assertEquals("C", bestStraightFlush.get(3).suit);
        Assertions.assertEquals("C", bestStraightFlush.get(4).suit);


    }

    @Test
    public void getBestStraightFlushTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraightFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "07C", "08C", "09D", "10D", "11D", "12D", "13D");

        bestStraightFlush = pokerMain.getBestStraightFlush(sevenCards);

        Assertions.assertEquals(9, bestStraightFlush.get(0).value);
        Assertions.assertEquals(10, bestStraightFlush.get(1).value);
        Assertions.assertEquals(11, bestStraightFlush.get(2).value);
        Assertions.assertEquals(12, bestStraightFlush.get(3).value);
        Assertions.assertEquals(13, bestStraightFlush.get(4).value);

        Assertions.assertEquals("D", bestStraightFlush.get(0).suit);
        Assertions.assertEquals("D", bestStraightFlush.get(1).suit);
        Assertions.assertEquals("D", bestStraightFlush.get(2).suit);
        Assertions.assertEquals("D", bestStraightFlush.get(3).suit);
        Assertions.assertEquals("D", bestStraightFlush.get(4).suit);


    }

    @Test
    public void getBestStraightFlushTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraightFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02H", "03H", "04H", "05H", "06S", "07S", "14H");

        bestStraightFlush = pokerMain.getBestStraightFlush(sevenCards);

        Assertions.assertEquals(14, bestStraightFlush.get(0).value);
        Assertions.assertEquals(2, bestStraightFlush.get(1).value);
        Assertions.assertEquals(3, bestStraightFlush.get(2).value);
        Assertions.assertEquals(4, bestStraightFlush.get(3).value);
        Assertions.assertEquals(5, bestStraightFlush.get(4).value);

        Assertions.assertEquals("H", bestStraightFlush.get(0).suit);
        Assertions.assertEquals("H", bestStraightFlush.get(1).suit);
        Assertions.assertEquals("H", bestStraightFlush.get(2).suit);
        Assertions.assertEquals("H", bestStraightFlush.get(3).suit);
        Assertions.assertEquals("H", bestStraightFlush.get(4).suit);
    }

    @Test
    public void getBestStraightFlushTest4() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> bestStraightFlush;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "08C", "09C", "10S", "11S", "12S", "13S", "14S");

        bestStraightFlush = pokerMain.getBestStraightFlush(sevenCards);

        Assertions.assertEquals(10, bestStraightFlush.get(0).value);
        Assertions.assertEquals(11, bestStraightFlush.get(1).value);
        Assertions.assertEquals(12, bestStraightFlush.get(2).value);
        Assertions.assertEquals(13, bestStraightFlush.get(3).value);
        Assertions.assertEquals(14, bestStraightFlush.get(4).value);

        Assertions.assertEquals("S", bestStraightFlush.get(0).suit);
        Assertions.assertEquals("S", bestStraightFlush.get(1).suit);
        Assertions.assertEquals("S", bestStraightFlush.get(2).suit);
        Assertions.assertEquals("S", bestStraightFlush.get(3).suit);
        Assertions.assertEquals("S", bestStraightFlush.get(4).suit);
    }

    @Test
    public void getBestFourOfAKindTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fourOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "14C", "14D", "14H", "14S", "02S", "03S", "04S");

        fourOfAKind = pokerMain.getBestFourOfAKind(sevenCards);

        Assertions.assertTrue(fourOfAKind != null, "fourOfAKind!=null");

        Assertions.assertEquals(14, fourOfAKind.get(0).value);
        Assertions.assertEquals(14, fourOfAKind.get(1).value);
        Assertions.assertEquals(14, fourOfAKind.get(2).value);
        Assertions.assertEquals(14, fourOfAKind.get(3).value);
        Assertions.assertEquals(4, fourOfAKind.get(4).value);

        Assertions.assertEquals("C", fourOfAKind.get(0).suit);
        Assertions.assertEquals("D", fourOfAKind.get(1).suit);
        Assertions.assertEquals("H", fourOfAKind.get(2).suit);
        Assertions.assertEquals("S", fourOfAKind.get(3).suit);
        Assertions.assertEquals("S", fourOfAKind.get(4).suit);
    }

    @Test
    public void getBestFourOfAKindTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fourOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "02H", "02S", "03S", "04S", "05S");

        fourOfAKind = pokerMain.getBestFourOfAKind(sevenCards);

        Assertions.assertTrue(fourOfAKind != null, "fourOfAKind!=null");

        Assertions.assertEquals(2, fourOfAKind.get(0).value);
        Assertions.assertEquals(2, fourOfAKind.get(1).value);
        Assertions.assertEquals(2, fourOfAKind.get(2).value);
        Assertions.assertEquals(2, fourOfAKind.get(3).value);
        Assertions.assertEquals(5, fourOfAKind.get(4).value);

        Assertions.assertEquals("C", fourOfAKind.get(0).suit);
        Assertions.assertEquals("D", fourOfAKind.get(1).suit);
        Assertions.assertEquals("H", fourOfAKind.get(2).suit);
        Assertions.assertEquals("S", fourOfAKind.get(3).suit);
        Assertions.assertEquals("S", fourOfAKind.get(4).suit);
    }

    @Test
    public void getBestFourOfAKindTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fourOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "02H", "14S", "03S", "04S", "05S");

        fourOfAKind = pokerMain.getBestFourOfAKind(sevenCards);

        Assertions.assertTrue(fourOfAKind == null, "fourOfAKind == null");

    }

    @Test
    public void getBestThreeOfAKindTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> threeOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "14C", "14D", "14H", "02S", "03S", "04S", "05S");

        threeOfAKind = pokerMain.getBestThreeOfAKind(sevenCards);

        Assertions.assertTrue(threeOfAKind != null, "threeOfAKind!=null");

        Assertions.assertEquals(14, threeOfAKind.get(0).value);
        Assertions.assertEquals(14, threeOfAKind.get(1).value);
        Assertions.assertEquals(14, threeOfAKind.get(2).value);
        Assertions.assertEquals(4, threeOfAKind.get(3).value);
        Assertions.assertEquals(5, threeOfAKind.get(4).value);

        Assertions.assertEquals("C", threeOfAKind.get(0).suit);
        Assertions.assertEquals("D", threeOfAKind.get(1).suit);
        Assertions.assertEquals("H", threeOfAKind.get(2).suit);
        Assertions.assertEquals("S", threeOfAKind.get(3).suit);
        Assertions.assertEquals("S", threeOfAKind.get(4).suit);
    }

    @Test
    public void getBestThreeOfAKindTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> threeOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "02H", "11S", "12S", "13S", "14S");

        threeOfAKind = pokerMain.getBestThreeOfAKind(sevenCards);

        Assertions.assertTrue(threeOfAKind != null, "threeOfAKind!=null");

        Assertions.assertEquals(2, threeOfAKind.get(0).value);
        Assertions.assertEquals(2, threeOfAKind.get(1).value);
        Assertions.assertEquals(2, threeOfAKind.get(2).value);
        Assertions.assertEquals(13, threeOfAKind.get(3).value);
        Assertions.assertEquals(14, threeOfAKind.get(4).value);

        Assertions.assertEquals("C", threeOfAKind.get(0).suit);
        Assertions.assertEquals("D", threeOfAKind.get(1).suit);
        Assertions.assertEquals("H", threeOfAKind.get(2).suit);
        Assertions.assertEquals("S", threeOfAKind.get(3).suit);
        Assertions.assertEquals("S", threeOfAKind.get(4).suit);
    }

    @Test
    public void getBestThreeOfAKindTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> threeOfAKind;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "03H", "11S", "12S", "13S", "14S");

        threeOfAKind = pokerMain.getBestThreeOfAKind(sevenCards);

        Assertions.assertTrue(threeOfAKind == null, "threeOfAKind == null");

    }

    @Test
    public void getBestFullHouseTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fullHouse;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "02H", "13S", "14C", "14D", "14H");

        fullHouse = pokerMain.getBestFullHouse(sevenCards);

        Assertions.assertTrue(fullHouse != null, "threeOfAKind!=null");

        Assertions.assertEquals(14, fullHouse.get(0).value);
        Assertions.assertEquals(14, fullHouse.get(1).value);
        Assertions.assertEquals(14, fullHouse.get(2).value);
        Assertions.assertEquals(2, fullHouse.get(3).value);
        Assertions.assertEquals(2, fullHouse.get(4).value);

        Assertions.assertEquals("C", fullHouse.get(0).suit);
        Assertions.assertEquals("D", fullHouse.get(1).suit);
        Assertions.assertEquals("H", fullHouse.get(2).suit);
        Assertions.assertEquals("C", fullHouse.get(3).suit);
        Assertions.assertEquals("D", fullHouse.get(4).suit);
    }

    @Test
    public void getBestFullHouseTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fullHouse;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "05C", "05D", "06H", "07S", "07C", "07D", "14H");

        fullHouse = pokerMain.getBestFullHouse(sevenCards);

        Assertions.assertTrue(fullHouse != null, "threeOfAKind!=null");

        Assertions.assertEquals(7, fullHouse.get(0).value);
        Assertions.assertEquals(7, fullHouse.get(1).value);
        Assertions.assertEquals(7, fullHouse.get(2).value);
        Assertions.assertEquals(5, fullHouse.get(3).value);
        Assertions.assertEquals(5, fullHouse.get(4).value);

        Assertions.assertEquals("C", fullHouse.get(0).suit);
        Assertions.assertEquals("D", fullHouse.get(1).suit);
        Assertions.assertEquals("S", fullHouse.get(2).suit);
        Assertions.assertEquals("C", fullHouse.get(3).suit);
        Assertions.assertEquals("D", fullHouse.get(4).suit);
    }

    @Test
    public void getBestFullHouseTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> fullHouse;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "05C", "05D", "06H", "07S", "07C", "08D", "14H");

        fullHouse = pokerMain.getBestFullHouse(sevenCards);

        Assertions.assertTrue(fullHouse == null, "fullHouse == null");

    }

    @Test
    public void getBestOnePairHandTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> pair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "04H", "06S", "08C", "10D", "12H");

        pair = pokerMain.getBestOnePairHand(sevenCards);

        Assertions.assertTrue(pair != null, "pair != null");

        Assertions.assertEquals(2, pair.get(0).value);
        Assertions.assertEquals(2, pair.get(1).value);
        Assertions.assertEquals(8, pair.get(2).value);
        Assertions.assertEquals(10, pair.get(3).value);
        Assertions.assertEquals(12, pair.get(4).value);

        Assertions.assertEquals("C", pair.get(0).suit);
        Assertions.assertEquals("D", pair.get(1).suit);
        Assertions.assertEquals("C", pair.get(2).suit);
        Assertions.assertEquals("D", pair.get(3).suit);
        Assertions.assertEquals("H", pair.get(4).suit);
    }

    @Test
    public void getBestOnePairHandTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> pair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "04D", "06H", "08S", "10C", "14D", "14H");

        pair = pokerMain.getBestOnePairHand(sevenCards);

        Assertions.assertTrue(pair != null, "pair != null");

        Assertions.assertEquals(14, pair.get(0).value);
        Assertions.assertEquals(14, pair.get(1).value);
        Assertions.assertEquals(6, pair.get(2).value);
        Assertions.assertEquals(8, pair.get(3).value);
        Assertions.assertEquals(10, pair.get(4).value);

        Assertions.assertEquals("D", pair.get(0).suit);
        Assertions.assertEquals("H", pair.get(1).suit);
        Assertions.assertEquals("H", pair.get(2).suit);
        Assertions.assertEquals("S", pair.get(3).suit);
        Assertions.assertEquals("C", pair.get(4).suit);
    }

    @Test
    public void getBestOnePairHandTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> pair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "04D", "06H", "08S", "10C", "13D", "14H");

        pair = pokerMain.getBestOnePairHand(sevenCards);

        Assertions.assertTrue(pair == null, "pair == null");
    }

    @Test
    public void getBestTwoPairHandTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> expectedCards;
        List<PlayingCard> twoPair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "02D", "04H", "04S", "08C", "10D", "12H");

        expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "04H", "04S", "02C", "02D", "12H");

        twoPair = pokerMain.getBestTwoPairHand(sevenCards);

        Boolean isMatch = pokerMain.compareCards(twoPair, expectedCards);
        Assertions.assertTrue(isMatch, "isMatch == true");

    }

    @Test
    public void getBestTwoPairHandTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> expectedCards;
        List<PlayingCard> twoPair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "03D", "04H", "13C", "13D", "14H", "14S");

        expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "14H", "14S", "13C", "13D", "04H");

        twoPair = pokerMain.getBestTwoPairHand(sevenCards);

        Boolean isMatch = pokerMain.compareCards(twoPair, expectedCards);
        Assertions.assertTrue(isMatch, "isMatch == true");
    }

    @Test
    public void getBestTwoPairHandTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> twoPair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "03D", "04H", "05C", "13D", "14H", "14S");

        twoPair = pokerMain.getBestTwoPairHand(sevenCards);

        Assertions.assertTrue(twoPair == null, "pair == null");
    }

    @Test
    public void getBestTwoPairHandTest4() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> sevenCards;
        List<PlayingCard> twoPair;

        sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "03D", "04H", "05S", "07C", "08D", "09H");

        twoPair = pokerMain.getBestTwoPairHand(sevenCards);

        Assertions.assertTrue(twoPair == null, "pair == null");
    }

    @Test
    public void compareCardsTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> cards1 = new LinkedList<>();
        List<PlayingCard> cards2 = new LinkedList<>();

        pokerMain.addCards(cards1, "02C", "03C", "04C");
        pokerMain.addCards(cards2, "02C", "03C", "04C");

        Boolean isMatch = pokerMain.compareCards(cards1, cards2);

        Assertions.assertTrue(isMatch);

    }

    @Test
    public void compareCardsTest2() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> cards1 = new LinkedList<>();
        List<PlayingCard> cards2 = new LinkedList<>();

        pokerMain.addCards(cards1, "02C", "03C", "04C");
        pokerMain.addCards(cards2, "02C", "03C", "04D");

        Boolean isMatch = pokerMain.compareCards(cards1, cards2);

        Assertions.assertTrue(!isMatch);

    }

    @Test
    public void compareCardsTest3() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> cards1 = new LinkedList<>();
        List<PlayingCard> cards2 = new LinkedList<>();

        pokerMain.addCards(cards1, "02C", "03C", "04C");
        pokerMain.addCards(cards2, "02C", "03C", "05C");

        Boolean isMatch = pokerMain.compareCards(cards1, cards2);

        Assertions.assertTrue(!isMatch);

    }

    @Test
    public void compareCardsTest4() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> cards1 = new LinkedList<>();
        List<PlayingCard> cards2 = new LinkedList<>();

        pokerMain.addCards(cards1, "02C", "03C", "04C");
        pokerMain.addCards(cards2, "02C", "03C", "04C", "05C");

        Boolean isMatch = pokerMain.compareCards(cards1, cards2);

        Assertions.assertTrue(!isMatch);

    }

    @Test
    public void compareCardsTest5() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> cards1 = new LinkedList<>();
        List<PlayingCard> cards2 = new LinkedList<>();

        pokerMain.addCards(cards1, "02C", "03C", "04C", "05C");
        pokerMain.addCards(cards2, "02C", "03C", "04C");

        Boolean isMatch = pokerMain.compareCards(cards1, cards2);

        Assertions.assertTrue(!isMatch);

    }

    @Test
    public void getBestHighCardHandTest1() {

        PokerMain pokerMain = new PokerMain();
        List<PlayingCard> highCardHand;

        List<PlayingCard> sevenCards = new LinkedList<>();
        pokerMain.addCards(sevenCards, "02C", "03D", "04H", "06C", "08D", "13H", "14S");

        List<PlayingCard> expectedCards = new LinkedList<>();
        pokerMain.addCards(expectedCards, "04H", "06C", "08D", "13H", "14S");

        highCardHand = pokerMain.getBestHighCardHand(sevenCards);

        Boolean isMatch = pokerMain.compareCards(highCardHand, expectedCards);
        Assertions.assertTrue(isMatch, "getBestHighCardHandTest1, isMatch == TRUE");

    }

    @Test
    public void getWinningHandTest_highCard5Eve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "06H", "08S", "10C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "11C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);

    }

    @Test
    public void getWinningHandTest_highCard5Adam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "06H", "08S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "11C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);

    }
    @Test
    public void getWinningHandTest_highCard4Eve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "06H", "08S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);

    }
    @Test
    public void getWinningHandTest_highCard4Adam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "06H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "08S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);

    }
    @Test
    public void getWinningHandTest_highCard3Eve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "06H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);

    }
    @Test
    public void getWinningHandTest_highCard3Adam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "06H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);

    }
    @Test
    public void getWinningHandTest_highCard2Eve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "04D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_highCard2Adam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "05D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "04D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_highCard1Eve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "05D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_highCard1Adam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "03C", "05D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "02C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_highCardSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "03C", "05D", "07H", "09S", "12C");
        bestAdam.handType = "High Card";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "05D", "07H", "09S", "12C");
        bestEve.handType = "High Card";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_pairAdamAces() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "02H", "03S", "04C");
        bestAdam.handType = "One Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13C", "13D", "05H", "06S", "07C");
        bestEve.handType = "One Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_pairEveAces() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13C", "13D", "02H", "03S", "04C");
        bestAdam.handType = "One Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14C", "14D", "05H", "06S", "07C");
        bestEve.handType = "One Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_pairEve3s() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "02C", "02D", "02H", "03S", "04C");
        bestAdam.handType = "One Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "03C", "03D", "05H", "06S", "07C");
        bestEve.handType = "One Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_pairAdam3s() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "03C", "03D", "02H", "03S", "04C");
        bestAdam.handType = "One Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "02C", "02D", "05H", "06S", "07C");
        bestEve.handType = "One Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_pairSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "02H", "03S", "04C");
        bestAdam.handType = "One Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "02S", "03H", "04S");
        bestEve.handType = "One Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_twoPairAdamAceKing() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "13H", "13S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13H", "13S", "12S", "12H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_twoPairAdamAceKingEveAceQueen() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "13H", "13S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "12S", "12H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_twoPairEveAceKingAdamKingQueen() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13C", "13D", "12H", "12S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "13S", "13H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_twoPairEveAceKingAdamAceQueen() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "12H", "12S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "13S", "13H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_twoPairAdamKicker() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "13H", "13S", "05C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "13S", "13H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_twoPairEveKicker() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "13H", "13S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "13S", "13H", "05S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_twoPairSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "13H", "13S", "04C");
        bestAdam.handType = "Two Pair";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14H", "14S", "13S", "13H", "04S");
        bestEve.handType = "Two Pair";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_threeOfAKindAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "14H", "02S", "02C");
        bestAdam.handType = "Three Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13H", "13S", "13S", "02H", "02S");
        bestEve.handType = "Three Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_threeOfAKindEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "12C", "12D", "12H", "02S", "02C");
        bestAdam.handType = "Three Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13H", "13S", "13S", "02H", "02S");
        bestEve.handType = "Three Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_threeOfAKindSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "12C", "12D", "12H", "02S", "02C");
        bestAdam.handType = "Three Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "12C", "12D", "12H", "02S", "02C");
        bestEve.handType = "Three Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_straightAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "12C", "11D", "10H", "09S", "08C");
        bestAdam.handType = "Straight";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "11H", "10S", "09S", "08H", "07S");
        bestEve.handType = "Straight";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_straightEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "11H", "10S", "09S", "08H", "07S");
        bestAdam.handType = "Straight";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "12C", "11D", "10H", "09S", "08C");
        bestEve.handType = "Straight";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_straightSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "11H", "10S", "09S", "08H", "07S");
        bestAdam.handType = "Straight";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "11H", "10S", "09S", "08H", "07S");
        bestEve.handType = "Straight";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_flushEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "11H", "10H", "09H", "08H", "02H");
        bestAdam.handType = "Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "12C", "11C", "10C", "09C", "02C");
        bestEve.handType = "Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_flushAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13H", "10H", "09H", "08H", "02H");
        bestAdam.handType = "Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "12C", "11C", "10C", "09C", "02C");
        bestEve.handType = "Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_flushSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13H", "10H", "09H", "08H", "02H");
        bestAdam.handType = "Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13H", "10H", "09H", "08H", "02H");
        bestEve.handType = "Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }

    @Test
    public void getWinningHandTest_fullHouseAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "14C", "14D", "14H", "13C", "13D");
        bestAdam.handType = "Full House";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13C", "13D", "13H", "14C", "14D" );
        bestEve.handType = "Full House";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_fullHouseEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13C", "13D", "13H", "14C", "14D");
        bestAdam.handType = "Full House";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14C", "14D", "14H", "13C", "13D" );
        bestEve.handType = "Full House";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_fullHouseSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13C", "13D", "13H", "14C", "14D");
        bestAdam.handType = "Full House";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "13C", "13D", "13H", "14C", "14D" );
        bestEve.handType = "Full House";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_FourOfAKindEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards, "13C", "13D", "13H", "13C", "02D");
        bestAdam.handType = "Four Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards,  "14C", "14D", "14H", "14C", "02D" );
        bestEve.handType = "Four Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_FourOfAKindAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "14C", "14D", "14H", "14C", "02D" );
        bestAdam.handType = "Four Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "13C", "13D", "13H", "13C", "02D");
        bestEve.handType = "Four Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_FourOfAKindSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "14C", "14D", "14H", "14C", "02D" );
        bestAdam.handType = "Four Of A Kind";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "14C", "14D", "14H", "14C", "02D");
        bestEve.handType = "Four Of A Kind";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_StraightFlushAdam() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "14C", "13C", "12C", "11C", "10C" );
        bestAdam.handType = "Straight Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "13C", "12C", "11C", "10C", "09C" );
        bestEve.handType = "Straight Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Adam", winner);
    }
    @Test
    public void getWinningHandTest_StraightFlushEve() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "13C", "12C", "11C", "10C", "09C" );
        bestAdam.handType = "Straight Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "14C", "13C", "12C", "11C", "10C" );
        bestEve.handType = "Straight Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Eve", winner);
    }
    @Test
    public void getWinningHandTest_StraightFlushSplitPot() {

        // GIVEN
        PokerMain pokerMain = new PokerMain();
        String winner;

        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "14C", "13C", "12C", "11C", "10C" );
        bestAdam.handType = "Straight Flush";

        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "14C", "13C", "12C", "11C", "10C" );
        bestEve.handType = "Straight Flush";

        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);

        // THEN
        Assertions.assertEquals("Split Pot", winner);
    }
    @Test
    public void getWinningHandTest_AllCombos() {

        PokerMain pokerMain = new PokerMain();
        String winner;
        PokerHand bestAdam = new PokerHand();
        bestAdam.cards = new LinkedList<>();
        pokerMain.addCards(bestAdam.cards,  "14C", "14C", "14C", "14C", "14C" );
        PokerHand bestEve = new PokerHand();
        bestEve.cards = new LinkedList<>();
        pokerMain.addCards(bestEve.cards, "14C", "14C", "14C", "14C", "14C" );

        // GIVEN
        bestAdam.handType = "High Card";
        bestEve.handType = "Pair";
        // WHEN
        winner = pokerMain.getWinningHand(bestAdam, bestEve);
        // THEN
        Assertions.assertEquals("Eve", winner);
    }



}
