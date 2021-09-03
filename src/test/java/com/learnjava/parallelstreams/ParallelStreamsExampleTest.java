package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.learnjava.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsExampleTest {

    ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();

    @Test
    void stringTransform() {
        List<String> inputList = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform(inputList);
        timeTaken();

        assertEquals(4, resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void stringTransform1(boolean isParallel) {
        List<String> inputList = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform1(inputList, isParallel);
        timeTaken();

        assertEquals(4, resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }
}