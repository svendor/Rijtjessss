package com.absontwikkeling.rijtjes;

import android.os.Parcelable;

public class wordList {
    String[] wordAns = new String[] {null};
    String[] wordQue = new String[] {null};
    int wordAmount = 0;

    public void newWord(int i, String ansString, String queString) {
        wordAns[i] = ansString;
        wordQue[i] = queString;
    }


}
