import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
    private final PriorityBlockingQueue<Task> taskPriorityQueue = new PriorityBlockingQueue<>(1, (o1, o2) -> {
        if (o1.getPriority() == o2.getPriority())
            return o1.getId() - o2.getId();

        return o2.getPriority() - o1.getPriority();
    });

    private boolean joinable = false;
    private long start = 0;

    /**
     *
     */
    @Override
    public void run() {
        while (!joinable) {
            // get task from queue
            if (taskPriorityQueue.isEmpty())
                continue;

            Task currentTask = taskPriorityQueue.peek();

            // save starting time of task
            start = System.currentTimeMillis();

            // run task
            synchronized (this) {
                try {
                    wait(currentTask.getLeft());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // call finish on current task
            currentTask.finish();

            // remove current task from queue
            taskPriorityQueue.remove(currentTask);
            start = 0;
        }
    }

    /**
     * @param task task to be added at this host
     */
    @Override
    public void addTask(Task task) {
        taskPriorityQueue.add(task);
    }

    /**
     * @return
     */
    @Override
    public int getQueueSize() {
        return taskPriorityQueue.size();
    }

    /**
     * @return
     */
    @Override
    public long getWorkLeft() {
        long taskQueueDurationSum = taskPriorityQueue.stream().mapToLong(Task::getDuration).sum();

        if (start == 0) {
            return taskQueueDurationSum;
        } else {
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;

            return taskQueueDurationSum - elapsedTime;
        }
    }

    /**
     *
     */
    @Override
    public void shutdown() {
        joinable = true;
    }
}
