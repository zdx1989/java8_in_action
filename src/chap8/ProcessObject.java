package chap8;

public abstract class ProcessObject<T> {

    private ProcessObject<T> successor;

    public void setSuccessor(ProcessObject<T> successor) {
        this.successor = successor;
    }

    public T handle(T input) {
        T r = handleWork(input);
        if (successor != null) {
            return successor.handle(r);
        }
        return r;
    }

    abstract T handleWork(T input);
}
