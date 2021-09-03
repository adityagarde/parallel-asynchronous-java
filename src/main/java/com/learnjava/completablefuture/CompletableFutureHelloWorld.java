package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public String helloWorldThreeAsyncCallsLog() {
        // Adding loggers to the method to understand internal functioning.
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine (h+w)");
                    return (h + w);
                })
                .thenCombine(hi, (prev, curr) -> {
                    log("thenCombine (prev, curr)");
                    return prev + " " + curr;
                })
                .thenApply(str -> {
                    log("thenApply str");
                    return str.toUpperCase();
                })
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls_CustomThreadPool() {
        startTimer();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world(), executorService);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        }, executorService);

        String result = hello
                .thenCombine(world, (h, w) -> {
                    log("thenCombine (h+w)");
                    return (h + w);
                })
                .thenCombine(hi, (prev, curr) -> {
                    log("thenCombine (prev, curr)");
                    return prev + " " + curr;
                })
                .thenApply(str -> {
                    log("thenApply str");
                    return str.toUpperCase();
                })
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCallsLog_AsyncFunctions() {
        // Adding loggers to the method to understand internal functioning.
        startTimer();
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello());
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world());
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        });

        String result = hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine (h+w)");
                    return (h + w);
                })
                .thenCombineAsync(hi, (prev, curr) -> {
                    log("thenCombine (prev, curr)");
                    return prev + " " + curr;
                })
                .thenApplyAsync(str -> {
                    log("thenApply str");
                    return str.toUpperCase();
                })
                .join();
        timeTaken();
        return result;
    }

    public String helloWorldThreeAsyncCalls_CustomThreadPool_AsyncFunctions() {
        startTimer();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> helloWorldService.hello(), executorService);
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> helloWorldService.world(), executorService);
        CompletableFuture<String> hi = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "Hi, Inside Completable Future!";
        }, executorService);

        String result = hello
                .thenCombineAsync(world, (h, w) -> {
                    log("thenCombine (h+w)");
                    return (h + w);
                }, executorService)
                .thenCombineAsync(hi, (prev, curr) -> {
                    log("thenCombine (prev, curr)");
                    return prev + " " + curr;
                }, executorService)
                .thenApplyAsync(str -> {
                    log("thenApply str");
                    return str.toUpperCase();
                }, executorService)
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

    public String anyOf() {
        // DB fetch call
        CompletableFuture<String> db = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            log("Response from DB");
            return "Hello, World!";
        });

        // REST API Call
        CompletableFuture<String> rest = CompletableFuture.supplyAsync(() -> {
            delay(2000);
            log("Response from REST");
            return "HHello, World!";
        });

        // SOAP API Call
        CompletableFuture<String> soap = CompletableFuture.supplyAsync(() -> {
            delay(3000);
            log("Response from SOAP");
            return "Hello, World!";
        });

        List<CompletableFuture<String>> completableFutureList = List.of(db, rest, soap);

        CompletableFuture<Object> anyOfObj = CompletableFuture.anyOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));

        String result = (String) anyOfObj.thenApply(v -> {
            if (v instanceof String)
                return v;
            return null;
        }).join();

        return result;
    }
}