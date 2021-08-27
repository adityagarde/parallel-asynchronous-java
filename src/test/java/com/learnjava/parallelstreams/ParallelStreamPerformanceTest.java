package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamPerformanceTest {

    ParallelStreamPerformance intStreamExample = new ParallelStreamPerformance();

    @Test
    void sum_using_intstream() {
        int sum = intStreamExample.sum_using_intstream(1000000, false);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_intstream_parallel() {
        int sum = intStreamExample.sum_using_intstream(1000000, true);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_iterate() {
        int sum = intStreamExample.sum_using_iterate(1000000, false);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_iterate_parallel() {
        int sum = intStreamExample.sum_using_iterate(1000000, true);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_list() {
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        int sum = intStreamExample.sum_using_list(inputList, false);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_list_parallel() {
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        int sum = intStreamExample.sum_using_list(inputList, true);
        log("sum : " + sum);

        assertEquals(1784293664, sum);
    }

}