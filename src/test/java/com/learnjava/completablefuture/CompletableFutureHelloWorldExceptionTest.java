package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @Mock
    HelloWorldService helloWorldService = mock(HelloWorldService.class);

    @InjectMocks
    CompletableFutureHelloWorldException helloWorldExceptionCf;

    @Test
    void helloWorldThreeAsyncCalls_Handle0() {
        // Mocking a POSITIVE scenario for BOTH hello() and world() calls.
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_Handle();
        /**
         * Expected :HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!
         * Actual   :RECOVER WORLD HI, INSIDE COMPLETABLE FUTURE!
         *
         * Test Fails as handle() is invoked even when there is no exception.
         * We get NullPointer because we are trying to log ex.getMessage();
         * Solution => Null Check in the handle() method.
         */

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_Handle1() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_Handle();

        assertEquals("RECOVER WORLD HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_Handle2() {
        // Mocking a Runtime Exception for hello() call, and normal positive scenario for world() call.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        when(helloWorldService.world()).thenCallRealMethod();

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_Handle();

        assertEquals("RECOVERY VALUE WORLD! HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_Exceptionally0() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call.
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_Exceptionally();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_Exceptionally2() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_Exceptionally();

        assertEquals("RECOVER WORLD HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_WhenComplete0() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call.
        when(helloWorldService.hello()).thenCallRealMethod();
        when(helloWorldService.world()).thenCallRealMethod();

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_WhenComplete();

        assertEquals("HELLO WORLD! HI, INSIDE COMPLETABLE FUTURE!", result);
    }

    @Test
    void helloWorldThreeAsyncCalls_WhenComplete1() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_WhenComplete();

        // assertEquals("", result);
        // java.util.concurrent.CompletionException: java.lang.RuntimeException: Exception Occurred! - AS we do not recover from the exception
    }

    @Test
    void helloWorldThreeAsyncCalls_WhenComplete2() {
        // Mocking a Runtime Exception for BOTH hello() call and world() call - but with helloWorldThreeAsyncCalls_WhenComplete2() method.
        when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String result = helloWorldExceptionCf.helloWorldThreeAsyncCalls_WhenComplete2();

        assertEquals("RECOVERING WITH EXCEPTIONALLY! HI, INSIDE COMPLETABLE FUTURE!", result);
    }
}