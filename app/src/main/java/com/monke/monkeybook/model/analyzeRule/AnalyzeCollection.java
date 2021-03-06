package com.monke.monkeybook.model.analyzeRule;

import java.util.Iterator;
import java.util.List;

final class AnalyzeCollection {

    private OutAnalyzer mAnalyzer;
    private Iterator mIterator;

    AnalyzeCollection(OutAnalyzer analyzer, List rawList) {
        this.mAnalyzer = analyzer;
        this.mIterator = rawList.iterator();
    }

    boolean hasNext() {
        if (mIterator.hasNext()) {
            Object o = mIterator.next();
            mAnalyzer.setContent(o);
            return true;
        }
        return false;
    }


    OutAnalyzer<?> mutable() {
        return mAnalyzer;
    }
}
