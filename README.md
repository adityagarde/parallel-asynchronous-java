## Parallel and Asynchronous Programming

Hardware Side -> Multiple Cores -> Better utilization -> Parallel Programming with Streams Software Side -> Blocking I/O
calls -> Increased latency -> Asynchronous Programming with CompletableFuture

### Concurrency and Parallelism

- Concurrency
    - Concept where two or more tasks can run simultaneously.
    - Can be achieved using Threads out of the box.
    - Ex. Multiple threads running in a single core.
    - Ex. Multiple threads running in multiple cores of the CPU.
    - Thus, concurrency can be implemented in both single and multiple cores.

- Concurrency is about correctly and efficiently controlling access to shared resources.

- Parallelism
    - Tasks are going to be literally going to be run in parallel. (Fork-Join)
        - Forking - Decomposing the task into sub tasks.
        - Process / Execute the subtask sequentially.
        - Join - Joining the results of the subtasks.
    - Parallelism can be implemented in a multi-core environment only.

- Parallelism is out using ore resources to access the result faster.

### Before Streams - Threads, Executor Service, Fork Join APIs

- Threads - Java 1
    - Threads are used to offload the blocking tasks as background tasks.
    - Help to write asynchronous style of code.
    - Low level API, need manual starting / joining etc.
    - Threads are expensive, they need their own runtime-stack, memory, registers etc.

- ThreadPool - Java 5
    - Thread Pool is a group of threads created and readily available.
    - For CPU Intensive Tasks - Computational tasks
        - The number of cores = ThreadPool size so that every core gets a single task.
    - I/O Tasks - DB Calls, REST API Calls
        - Thread Pool Size > Number of cores.
    - Benefits of Thread Pool
        - No need to manually create, start and join threads.
        - Achieving Concurrency in your application.

- Executor Service - Java 5
    - Executor Service is an Asynchronous Task Execution Engine.
    - It provides a way to asynchronously execute tasks and provides the results in a much simpler way compare to
      thread.
    - This enabled coarse-grained task based parallelism in Java.

    - Executor Service = WorkQueue + Completion Queue + Thread Pool
        - Any time client sends a task it is stored in the WorkQueue (blocking).
        - Tasks completed by the Thread Pool are placed in the CompletionQueue.

    - Client / Code gives task to the Executor Service which is queued in the WorkQueue and in return the ES return an
      object of Future - which is a promise.
    - A thread from the Thread pool picks the task, executes it and places the result in the CompletionFuture.
    - ES returns this result back to the code.

- Fork Join Framework - Java 7
    - Extension of the Executor Service.
    - Fork Join Framework is designed to achieve Data Parallelism.
    - ExecutorService is designed to achieve Task Based Parallelism.
    - Fork Join Framework has **ForkJoin Pool** to support Data Parallelism.

- Data Parallelism -
    - It is a concept where a given Task is recursively split into possible size and execute those tasks in parallel.

- ForkJoin Pool
    - Clients submit tasks to Shared Work Queue.
    - Worker Threads
    - Each Worker thread has a double ended Work Queue OR Deck.
    - Client submits a ForkJoinTask to the Shared Work Queue.
    - Every thread in the Work Queue continuously polls the Shared Work Queue for tasks.
    - This task is picked up by one available thread and placed in its Work Queue / Deck.
    - The tasks are consumed in LIFO order.
    - The task in the deck is further divided in subtasks (if possible) and placed in the same thread's work queue.
    - Work Stealing - Other available threads check each other's work queue and steal it from the other end of the
      queue.
    - Once executed - the result is returned to the client.

- ForkJoin Task
    - represents part of the data and its computation.
    - Recursive Task - task that returns a value.
    - Recursive Action - Task that does not return a value.

- Implementation
    - Required class can be extended with RecursiveTask< T >
    - forkJoinPool.invoke(ForkJoinTask_Object); - After this the task object becomes available for all the polling threads.
    - protected List< T > compute() {} - Logic is written here.

### Streams API

- Streams API - Java 8
    - Processing a Collection of Objects
    - Stream is created by using the stream() method.
    - By default - Stream API is sequential.
    
- Parallel Stream
    - Implementing Data Parallelism.
    - Invoke the .parallelStream() method.
    
- sequential() and parallel()
    - sequential() - executes the stream in sequential format.
    - parallel() - Executes the stream in parallel.
    - These two when called i between - change the flow of the whole pipeline.
    - The last function call - determines whether it will be sequential or parallel execution.
```
       namesList.parallelStream()
                .map(this::addNameLengthTransform)
                .sequential()
                .parallel()
                .collect(Collectors.toList());
```

- Parallel Streams - Under the hood
  - .parallelStream() has 3 main steps.
    - Split the data in chunks
    - Execute the data chunks
    - Combine the result
  - Split
    - Split the data in smaller chunks
      - Ex. ArrayList is split in chunks of size 1.
    - This is done using the **Spliterator**.
      - Ex for ArrayList the spliterator is ArrayListSpliterator.
  - Execute
    - Data chunks are applied to the stream pipeline and the intermediate operations executed in a Command ForkJoin Pool.
  - Combine
    - Combine the executed results into a final result - terminal operations.
    - Uses collect() and reduce() functions.
  

- ArrayList and LinkedList Comparison of Spliterator
  - For ArrayList the parallel execution is slightly better than Sequential Execution. 
  - Because ArrayList is an Indexed Collection, and the Spliterator can slice the data easily in smaller chunks.
  - For LinkedList the Parallel execution is considerably slow, and the LinkedList can't be split easily.
  
  - Invoking parallelStream() does not guarantee faster performance.
  - As the API performs lot of additional steps (splitting, executing etc.) compared to Sequential.


- How does Parallel Stream maintain Final Result Order ?
  - Depends on Type of Collection
  - And on spliterator implementation of the collection.
  - Example -
    - ArrayList - Ordered Collection and has Ordered Spliterator Impl.
    - Set - Unordered Collection and has Unordered Spliterator Impl.
  - For Unordered Collections - Order is **NOT** maintained in the result post applying stream - so we have to careful when choosing the Collection on which we have to apply ParallelStreams.

```java
[main] - inputList == [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[main] - resultList == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
[main] - inputSet == [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
[main] - resultSet == [16, 18, 2, 20, 4, 6, 8, 10, 12, 14]```