package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    private HelloWorldService helloWorldService;

    public CompletableFutureHelloWorldException(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    /**
     * Expected :HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!
     * Actual   :RECOVER WORLD HI, INSIDE COMPLETABLE FUTURE!
     * <p>
     * Test Fails as handle() is invoked even when there is no exception.
     * We get NullPointer because we are trying to log ex.getMessage();
     * Solution => Null Check in the handle() method.
     */

    public String helloWorldThreeAsyncCalls_Handle() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .handle((res, ex) -> {
                    if (null != ex) {
                        log("Exception in hello() is == " + ex.getMessage());
                        return "Recover Hello";
                    } else
                        return res;
                })
                .thenCombine(world, (h, w) -> (h + w))
                .handle((res, ex) -> {
                    if (null != ex) {
                        log("Exception in world() is == " + ex.getMessage());
                        return "Recover World";
                    } else
                        return res;
                })
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls_Exceptionally() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .exceptionally(ex -> {
                    log("Exception in hello() is == " + ex.getMessage());
                    return "Recover Hello";
                })
                .thenCombine(world, (h, w) -> (h + w))
                .exceptionally(ex -> {
                    log("Exception in world() is == " + ex.getMessage());
                    return "Recover World";
                })
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls_WhenComplete() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .whenComplete((res, ex) -> {
                    if (null != ex) {
                        log("Exception in hello() is == " + ex.getMessage());
                    }
                })
                .thenCombine(world, (h, w) -> (h + w))
                .whenComplete((res, ex) -> {
                    if (null != ex) {
                        log("Exception in world() is == " + ex.getMessage());
                    }
                })
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls_WhenComplete2() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .whenComplete((res, ex) -> {
                    if (null != ex) {
                        log("Exception in hello() is == " + ex.getMessage());
                    }
                })
                .thenCombine(world, (h, w) -> (h + w))
                .whenComplete((res, ex) -> {
                    if (null != ex) {
                        log("Exception in world() is == " + ex.getMessage());
                    }
                })
                .exceptionally(ex -> {
                    log("Exception after thenCombine() is == " + ex.getMessage());
                    return "Recovering with Exceptionally!";
                })
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

}