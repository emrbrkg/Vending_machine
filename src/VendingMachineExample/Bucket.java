package VendingMachineExample;

// It is the bucket that contains both selected product and a list of coin that was inserted to the vending machine.
public class Bucket<T1, T2> {
    private T1 first;
    private T2 second;

    public Bucket(T1 first, T2 second){
        this.first = first;
        this.second =second;
    }
    public T1 getFirst(){
        return first;
    }
    public T2 getSecond(){
        return second;
    }

}
