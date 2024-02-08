package VendingMachineExample;

// Coins were defined as constants.
// Was defined as enum because by this way we can reduce the mistake possibility since we don't need to control if the input is equal to our variable.
public enum Coin {
    NOCOIN(0),
    BIRTL(1),
    BESTL(5),
    ONTL(10),
    YIRMITL(20),
    ELLITL(50);

    private long coinValue;

    Coin(int coinValue){
        this.coinValue = coinValue;
    }

    public long getCoinValue(){
        return this.coinValue;
    }



}
