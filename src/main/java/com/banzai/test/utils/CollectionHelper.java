package com.banzai.test.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kuznetsov A.S. 08.10.2018
 */
public class CollectionHelper {
    public static <T> List<List<T>> getChunkList(final List<T> largeList , final int chunkSize) {
        if (largeList==null){
            return Collections.emptyList();
        }
        final List<List<T>> chunkList = new ArrayList<>();
        for (int i = 0 ; i <  largeList.size() ; i += chunkSize) {
            final boolean isLastChunk = (i + chunkSize) >= largeList.size();

            chunkList.add(largeList.subList(i , isLastChunk? largeList.size() : i + chunkSize));
        }
        return chunkList;
    }
}
