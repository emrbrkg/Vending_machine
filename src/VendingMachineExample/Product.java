package VendingMachineExample;


// Products were defined as constants.
// We reduce the mistake possibility.
public enum Product {
    COKE("Coke", new Money(35,"TL")), PEPSI("Pepsi", new Money(35,"TL")), SODA("Soda", new Money(45,"TL")),
    KINDER("Kinder", new Money(20, "TL")), LAYS("Lays", new Money(30,"TL")), GUM("Gum", new Money(5,"TL"));
    private String productName;
    private Money price;

    private Product( String productName, Money price){
        this.productName = productName;
        this.price = price;
    }



    public String getProductName(){
        return this.productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }

    public Money getPrice(){
        return this.price;
    }
    public void setPrice(Money newPrice){
        this.price = newPrice;
    }



}

// Money is not just a digit value. It also has currency. Its better to define as class. It is called value object in domain driven design.
class Money{
    int amount;
    String currency;

    public Money(int amount, String currency){
        this.amount = amount;
        this.currency = currency;
    }

    public void displayMoney(){
        System.out.println(amount + " " + currency);
    }
}

