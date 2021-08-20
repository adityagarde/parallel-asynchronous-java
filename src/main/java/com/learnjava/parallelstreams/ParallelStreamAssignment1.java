package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamAssignment1 {
    public static void main(String[] args) {
        List<String> namesList = DataSet.namesList();
        ParallelStreamAssignment1 parallelStreamAssignment1 = new ParallelStreamAssignment1();

        startTimer();
        List<String> resultList = parallelStreamAssignment1.stringTransform(namesList);
        log("resultList == " + resultList);
        timeTaken();
    }

    public List<String> stringTransform(List<String> namesList) {
        return namesList
                .parallelStream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

}