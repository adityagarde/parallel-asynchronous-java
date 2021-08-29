package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

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
}