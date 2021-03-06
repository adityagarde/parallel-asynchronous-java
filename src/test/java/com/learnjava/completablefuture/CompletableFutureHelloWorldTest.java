package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService helloWorldService = new HelloWorldService();
    CompletableFutureHelloWorld completableFutureHelloWorld = new CompletableFutureHelloWorld(helloWorldService);

    @Test
    void helloWorld() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorld();

        completableFuture
                .thenAccept(str -> assertEquals("HELLO WORLD", str))
                .join(); // join is required here, because without join() the test is not executed only
    }

    @Test
    void helloWorldWithSize() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldWithSize();

        completableFuture
                .thenAccept(str -> assertEquals("11 - HELLO WORLD", str))
                .join(); // join is required here, because without join() the test is not executed only
    }

    @Test
    void helloWorldAsyncApproach() {
        String helloWorld = completableFutureHelloWorld.helloWorldAsyncApproach();

        assertEquals("HELLO WORLD!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCalls() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCalls();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCallsLog() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCallsLog();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCalls_CustomThreadPool() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCalls_CustomThreadPool();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCallsLog_AsyncFunctions() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCallsLog_AsyncFunctions();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", helloWorld);
    }

    @Test
    void helloWorldThreeAsyncCalls_CustomThreadPool_AsyncFunctions() {
        String helloWorld = completableFutureHelloWorld.helloWorldThreeAsyncCalls_CustomThreadPool_AsyncFunctions();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", helloWorld);
    }

    @Test
    void helloWorldFourAsyncCalls() {
        String helloWorld = completableFutureHelloWorld.helloWorldFourAsyncCalls();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE! BYE!", helloWorld);
    }

    @Test
    void helloWorldCompose() {
        startTimer();
        CompletableFuture<String> helloWorld = completableFutureHelloWorld.helloWorldCompose();

        helloWorld.thenAccept(str -> assertEquals("HELLO WORLD!", str))
                .join();
        timeTaken();
    }

    @Test
    void anyOf() {
        startTimer();
        String helloWorld = completableFutureHelloWorld.anyOf();

        assertEquals("Hello, World!", helloWorld); // We get from the DB - the least delay
        timeTaken();
    }
}