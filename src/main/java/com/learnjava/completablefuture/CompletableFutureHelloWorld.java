package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {
    public static void main(String[] args) {
        HelloWorldService helloWorldService = new HelloWorldService();
        CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld())
                .thenApply(str -> str.toUpperCase())
                .thenAccept(s -> {
                    log("Result == " + s);
                }).join(); // Blocking - AVOID
        log("Completed!");
        //delay(2000);
    }

    private HelloWorldService helloWorldService;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public CompletableFuture<String> helloWorld() {

        return CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld())
                .thenApply(str -> str.toUpperCase());
    }

    public CompletableFuture<String> helloWorldWithSize() {
        return CompletableFuture.supplyAsync(() -> helloWorldService.helloWorld())
                .thenApply(str -> str.length() + " - " + str.toUpperCase());
    }

    public String helloWorldSyncApproach() {
        startTimer();
        String hello = helloWorldService.hello();
        String world = helloWorldService.world();
        timeTaken();

        return hello + world;
    }

    public String helloWorldAsyncApproach() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());

        String result = hello.thenCombine(world, (h, w) -> (h + w))
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello.thenCombine(world, (h, w) -> (h + w))
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldFourAsyncCalls() {
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });
        CompletableFuture<String> bye = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Bye!";
        });

        String result = hello.thenCombine(world, (h, w) -> (h + w))
                .thenCombine(hi, (prev, curr) -> prev + " " + curr)
                .thenCombine(bye, (prev, curr) -> prev + " " + curr)
                .thenApply(str -> str.toUpperCase())
                .join();

        timeTaken();
        return result;
    }

    public CompletableFuture<String> helloWorldCompose() {
        return CompletableFuture.supplyAsync(() -> helloWorldService.hello())
                .thenCompose((prev) -> helloWorldService.worldFuture(prev))
                .thenApply(str -> str.toUpperCase());
    }
}