package VendingMachineExample;

import java.util.ArrayList;
import java.util.List;

// Declared as interface since all the vending machine must have this functions and implement them.
public interface VendingMachine {
    public long selectItemAndGetPrice(Product product);
    public void insertCoin(Coin coin);
    public List<Coin> refund();
    public void reset();
    public Bucket<Product, ArrayList<Coin>> collectProductAndChange();

}
