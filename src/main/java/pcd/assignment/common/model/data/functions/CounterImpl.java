package pcd.assignment.common.model.data.functions;

public class CounterImpl implements Counter {
    private int value;

    public CounterImpl(int value) {
        this.value = value;
    }

    public CounterImpl() {
        this.value = 0;
    }

    @Override
    public synchronized void inc() {
        this.value++;
    }

    @Override
    public synchronized int getValue() {
        return this.value;
    }
}
