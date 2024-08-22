package org.example;

import java.util.Comparator;

class CardSort implements Comparator<PlayingCard> {
    public int compare(PlayingCard a, PlayingCard b) {

        int compareSuit = a.suit.compareTo(b.suit);

        if (a.value > b.value) {
            return +1; // order correct
        }
        if ((a.value == b.value) && (compareSuit > 0)) {
            return +1; // order correct
        }
        return -1; // order wrong
    }
}

