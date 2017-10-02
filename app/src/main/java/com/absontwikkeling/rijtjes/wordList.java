package com.absontwikkeling.rijtjes;

public class wordList {
    String[] wordAns = new String[] {"test"};
    String[] wordQue = new String[] {"test"};

    public void newWord(int i, String ansString, String queString) {
        wordAns[i] = ansString;
        wordQue[i] = queString;
    }
}
