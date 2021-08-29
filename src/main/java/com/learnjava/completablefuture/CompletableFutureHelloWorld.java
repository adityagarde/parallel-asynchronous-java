package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;

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
}