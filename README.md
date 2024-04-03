###### Copyright of Motrun Florin

# Tema 2

### MyDispatcher :


    - Method "roundRobin" is synchronized to ensure that multiple threads(tasks) 
    don't interfere with each other while updating the shared state(hostId) :

      * add task to queue: to avoid race conditions it is used an atomic operation;

      * update hostId: to ensure that roundRobin is consistent and avoids conflicts
      between different threads, the next host for the next task is determined in a
      thread-safe manner;

    - Method "sizeIntervalTaskAssignment" assigns tasks to nodes based on their types;

    - Method "shortestQueue" finds the host with the shortest task queue size;

    - Method "leastWorkLeft" finds the host with the least remaining workload.

### MyHost :


    - Method "run" is the main execution loop for a host thread:
      
      * "while (!joinable)": ensures the loop continues until the host is flagged
      for shutdown;

      * "taskPriorityQueue.peek()": retrieves the highest-priority task from the
        priority queue without removing it;

      * "start = System.currentTimeMillis()": records the start time of the task
        for workload calculation;

      * "wait(currentTask.getLeft())": simulates task execution time using the 
        wait method - releases the lock on the host object, allowing other 
        threads to access it while waiting;

      * "currentTask.finish()": marks the task as finished;

      * "taskPriorityQueue.remove(currentTask)": removes the completed task from 
        the priority queue;

      * start = 0: resets the start time for the next task.

    - Method "addTask": adds a task to the priority queue:

        * "taskPriorityQueue.add(task)": adds the task to the priority queue: the 
        priority queue automatically orders tasks based on their priority and arrival order.

    - Method "getWorkLeft": calculates the remaining workload of the host:

        * "taskQueueDurationSum - elapsedTime": calculates the remaining workload by subtracting
        the elapsed time (time spent on the currently executing task) from the total duration
        of tasks in the queue.

    - Method "shutdown": flags the host for shutdown:

        * "joinable = true": flags the host for shutdown: the run method's loop condition 
        (while (!joinable)) will exit, terminating the host thread.