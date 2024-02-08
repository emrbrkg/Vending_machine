package VendingMachineExample;
import java.util.*;

// The implementation class.
public class VendingMachineImpl implements VendingMachine {
    private Inventory<Coin> coinInventory = new Inventory<>();
    private Inventory<Product> productInventory = new Inventory<>();
    private long totalSalesAmount;
    private  Product currentProduct;
    private long currentBalanceAmount;

    public VendingMachineImpl(){
        initialize();
    }


    //initialize machine with 5 coins of each denomination
    //and 5 cans of each Item
    private void initialize(){
        for(Coin c : Coin.values()){
            coinInventory.put(c, 5);
        }

        for(Product p : Product.values()){
            productInventory.put(p, 5);
        }
    }

    @Override
    public long selectItemAndGetPrice(Product product) {
        if (productInventory.hasItem(product)){
            currentProduct = product;
            return currentProduct.getPrice().amount;
        }
        throw new SoldOutException("Sold out, buy another product");
    }

    @Override
    public void insertCoin(Coin coin) {
        currentBalanceAmount += coin.getCoinValue();
        coinInventory.add(coin);
    }

    private Product collectProduct() throws NotSufficientChangeException, NotFullPaidException{
        if (isFullPaid()){
            if (hasSufficientChange()){
                updateProductInventory(currentProduct);
                return currentProduct;
            }
            throw new NotSufficientChangeException("Not sufficient change in inventory");
        }
        if (currentProduct != null) {
            long remaining = currentProduct.getPrice().amount - currentBalanceAmount;
            throw new NotFullPaidException("Not fully paid. Remaining is :", new Money((int) remaining, "TL"));
        }
        return null;
    }



    @Override
    public ArrayList<Coin> refund() {
        ArrayList<Coin> refund = new ArrayList<>();
        if (currentBalanceAmount<currentProduct.getPrice().amount)
            refund = getChange(currentBalanceAmount);

        else
            refund = getChange(currentBalanceAmount-currentProduct.getPrice().amount);
        updateCoinInventory(refund);
        currentBalanceAmount = 0;
        currentProduct = null;
        return refund;
    }


    private void updateProductInventory(Product product) {
        productInventory.deduct(product);
    }

    private void updateCoinInventory(List<Coin> change) {
        for (Coin c: change)
            coinInventory.deduct(c);
    }

    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalanceAmount-currentProduct.getPrice().amount);
    }

    private boolean hasSufficientChangeForAmount(long amount) {
        boolean hasChange = true;
        
        try{
            getChange(amount);
        } catch (NotSufficientChangeException e){
            hasChange = false;
        }
        
        return hasChange;
    }

    // It looks to the highest valued coin to give a change and as long as highest valued coin can be given as a  change it gives. Then looks the lesser valued coins it goes so on.
    private ArrayList<Coin> getChange(long amount) {
        ArrayList<Coin> changes = new ArrayList<>();

        if (amount > 0){
            if (amount == Coin.NOCOIN.getCoinValue()) {
                throw new NotSufficientChangeException("Not sufficient change, please try another product");
            }

            while (amount >= Coin.ELLITL.getCoinValue() && coinInventory.hasItem(Coin.ELLITL)){
                changes.add(Coin.ELLITL);
                amount -= Coin.ELLITL.getCoinValue();
            }

            while (amount >= Coin.YIRMITL.getCoinValue() && coinInventory.hasItem(Coin.YIRMITL)){
                changes.add(Coin.YIRMITL);
                amount -= Coin.YIRMITL.getCoinValue();
            }

            while (amount >= Coin.ONTL.getCoinValue() && coinInventory.hasItem(Coin.ONTL)){
                changes.add(Coin.ONTL);
                amount -= Coin.ONTL.getCoinValue();
            }

            while (amount >= Coin.BESTL.getCoinValue() && coinInventory.hasItem(Coin.BESTL)){
                changes.add(Coin.BESTL);
                amount -= Coin.BESTL.getCoinValue();
            }

            while (amount >= Coin.BIRTL.getCoinValue() && coinInventory.hasItem(Coin.BIRTL)){
                changes.add(Coin.BIRTL);
                amount -= Coin.BIRTL.getCoinValue();
            }
            // Eventhought all coins were given as change but if still the amount is not zero that means there is no enough coin in inventory to give the change.
            if (amount != 0)
                throw new NotSufficientChangeException("Not sufficient change in inventory!");
        }
    return changes;
    }

    private boolean isFullPaid() {
        try {
            return currentBalanceAmount >= currentProduct.getPrice().amount;
        } catch (NullPointerException e){
            System.out.println("Not a product selected");
            return false;
        }

    }

    @Override
    public void reset() {
        productInventory.clear();
        coinInventory.clear();
        currentProduct = null;
        currentBalanceAmount = 0;
        totalSalesAmount = 0;
    }

    // We both collect the product and the change respectively.
    @Override
    public Bucket<Product, ArrayList<Coin>> collectProductAndChange() {
        Product product = collectProduct();
        totalSalesAmount += product.getPrice().amount;
        ArrayList<Coin> change = refund();
        return new Bucket<>(product, change);
    }

    public void printStats() {
        System.out.println("Total sales : "+getTotalSalesAmount());
        new Money((int) currentBalanceAmount, "TL").displayMoney();
        productInventory.displayInventory();
        coinInventory.displayInventory();
    }

    public long getTotalSalesAmount(){
        return totalSalesAmount;
    }

    // Menu shown in the console.
    public void displayMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("*********************************************");
        System.out.println("press gum to select GUM");
        System.out.println("press pepsi to select PEPSI");
        System.out.println("press coke to select COKE");
        System.out.println("press lays to select LAYS");
        System.out.println("press soda to select SODA");
        System.out.println("press kinder to select KINDER");
        System.out.println("*********************************************");
        System.out.println("press 1 to insert 1 tl");
        System.out.println("press 5 to insert 5 tl");
        System.out.println("press 10 to insert 10 tl");
        System.out.println("press 20 to insert 20 tl");
        System.out.println("press 50 to insert 50 tl");
        System.out.println("---------------------------------------------");
        System.out.println();
    }

    // Handles to input that is given by the user.
    public void handleInput(){
        Scanner keyboard = new Scanner(System.in);

        switch (keyboard.nextLine()) {
            case ("gum") -> {
                selectItemAndGetPrice(Product.GUM);
                System.out.print(Product.GUM + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);


                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");
                        }

                    }

                }
                collectProductAndChange();
            }
            case ("pepsi") -> {
                selectItemAndGetPrice(Product.PEPSI);
                System.out.print(Product.PEPSI + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);

                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");
                        }

                    }

                }
                collectProductAndChange();
            }
            case ("coke") -> {
                selectItemAndGetPrice(Product.COKE);
                System.out.print(Product.COKE + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);

                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");
                        }

                    }

                }
                collectProductAndChange();
            }
            case ("lays") -> {
                selectItemAndGetPrice(Product.LAYS);
                System.out.print(Product.LAYS + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);

                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");
                        }
                    }

                }
                collectProductAndChange();
            }
            case ("soda") -> {
                selectItemAndGetPrice(Product.SODA);
                System.out.print(Product.SODA + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);

                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");
                        }

                    }

                }
                collectProductAndChange();
            }
            case ("kinder") -> {

                selectItemAndGetPrice(Product.KINDER);
                System.out.print(Product.KINDER + " selected "+ "price is : ");
                new Money(currentProduct.getPrice().amount, "TL").displayMoney();
                while (currentProduct.getPrice().amount > currentBalanceAmount) {
                    switch (keyboard.nextLine()) {
                        case ("1") -> insertCoin(Coin.BIRTL);

                        case ("5") -> insertCoin(Coin.BESTL);

                        case ("10") -> insertCoin(Coin.ONTL);

                        case ("20") -> insertCoin(Coin.YIRMITL);

                        case ("50") -> insertCoin(Coin.ELLITL);

                        default -> {
                            System.out.print("unexpected input. Not fully paid! Remanining is : ");
                            new Money((int) (currentProduct.getPrice().amount-currentBalanceAmount), "TL").displayMoney();
                            refund();
                            System.out.print("Your money refunded.");

                        }

                    }

                }
                collectProductAndChange();
            }
        }
    }


    public static void main(String[] args) {
        VendingMachineImpl vm = new VendingMachineImpl();

        vm.printStats();

        vm.displayMenu();
        vm.handleInput();

        vm.printStats();
    }

}
