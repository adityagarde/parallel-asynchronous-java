package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample linkedListSpliteratorExample = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValueSequential() {
        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);

        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValueParallel() {
        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);

        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}