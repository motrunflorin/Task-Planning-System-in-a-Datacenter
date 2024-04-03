
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MyDispatcher extends Dispatcher {
    private int hostId = 0;

    /**
     * @param algorithm
     * @param hosts
     */
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    /**
     * @param task task to be scheduled
     */
    @Override
    public void addTask(Task task) {
        if (algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            roundRobin(task);
        } else if (algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
            sizeIntervalTaskAssignment(task);
        } else if (algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
            shortestQueue(task);
        } else if (algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
            leastWorkLeft(task);
        }
    }

    /**
     * @param task
     */
    private synchronized void roundRobin(Task task) {
        hosts.get(hostId).addTask(task);

        // calculate next host id
        hostId++;
        hostId = hostId % hosts.size();
    }

    /**
     * @param task
     */
    private void sizeIntervalTaskAssignment(Task task) {
        if (task.getType() == TaskType.SHORT) {
            hosts.get(0).addTask(task);
        } else if (task.getType() == TaskType.MEDIUM) {
            hosts.get(1).addTask(task);
        } else if (task.getType() == TaskType.LONG) {
            hosts.get(2).addTask(task);
        }
    }

    /**
     * @param task
     */
    private void shortestQueue(Task task) {
        Host hostWithShortestQueueSize = hosts.stream().min(Comparator.comparing(Host::getQueueSize)).orElseThrow(NoSuchElementException::new);
        hostWithShortestQueueSize.addTask(task);
    }

    /**
     * @param task
     */
    private void leastWorkLeft(Task task) {
        Host hostWithLeastWorkLeft = hosts.stream().min(Comparator.comparing(Host::getWorkLeft)).orElseThrow(NoSuchElementException::new);
        hostWithLeastWorkLeft.addTask(task);
    }
}
