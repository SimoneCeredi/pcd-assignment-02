package pcd.assignment.tasks.executors.model.data.monitor;

public class CounterImpl implements Counter {
    private int value;

    @Override
    public synchronized void inc() {
        this.value++;
    }

    @Override
    public synchronized int getValue() {
        return this.value;
    }
}
