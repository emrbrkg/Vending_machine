package VendingMachineExample;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;


// The test class to test some senarios.
public class VendingMachineTest {

    static VendingMachineImpl vm = new VendingMachineImpl();

    @AfterClass
    public static void tearDown(){
        vm = null;
    }

    @Test
    public void testBuyProductWithExactPrice() {
        long priceAmount = vm.selectItemAndGetPrice(Product.LAYS);
        assertEquals(Product.LAYS.getPrice().amount, priceAmount);

        vm.insertCoin(Coin.YIRMITL);
        vm.insertCoin(Coin.ONTL);
        Bucket<Product, ArrayList<Coin>> bucket = vm.collectProductAndChange();
        Product item = bucket.getFirst();
        ArrayList<Coin> change = bucket.getSecond();
        assertEquals(Product.LAYS, item);
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithMorePrice(){
        long price = vm.selectItemAndGetPrice(Product.SODA);
        assertEquals(Product.SODA.getPrice().amount, price);

        vm.insertCoin(Coin.ELLITL);

        Bucket<Product, ArrayList<Coin>> bucket = vm.collectProductAndChange();
        Product item = bucket.getFirst();
        ArrayList<Coin> change = bucket.getSecond();
        //should be Coke
        assertEquals(Product.SODA, item);
        //there should not be any change
        assertFalse(change.isEmpty());
        //comparing change
        assertEquals(Coin.ELLITL.getCoinValue() - Product.SODA.getPrice().amount, getTotal(change));
    }

    @Test
    public void testRefund(){
        long price = vm.selectItemAndGetPrice(Product.PEPSI);
        assertEquals(Product.PEPSI.getPrice().amount, price);
        vm.insertCoin(Coin.ONTL);
        vm.insertCoin(Coin.BIRTL);
        vm.insertCoin(Coin.BESTL);
        vm.insertCoin(Coin.YIRMITL);

        assertEquals(1, getTotal(vm.refund()));
    }

    @Test(expected=SoldOutException.class)
    public void testSoldOut(){
        for (int i = 0; i <= 5; i++) {
            vm.selectItemAndGetPrice(Product.COKE);
            vm.insertCoin(Coin.YIRMITL);
            vm.insertCoin(Coin.ONTL);
            vm.insertCoin(Coin.BESTL);
            vm.collectProductAndChange();
        }
    }


    @Test(expected=NotSufficientChangeException.class)
    public void testNotSufficientChangeException(){
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Product.SODA);
            vm.insertCoin(Coin.ELLITL);
            vm.collectProductAndChange();

            vm.selectItemAndGetPrice(Product.PEPSI);
            vm.insertCoin(Coin.ELLITL);
            vm.collectProductAndChange();

        }
    }

    @Test(expected=SoldOutException.class)
    public void testReset(){
        VendingMachine vmachine = new VendingMachineImpl();
        vmachine.reset();
        vmachine.selectItemAndGetPrice(Product.COKE);
    }

    @Ignore
    public void testVendingMachineImpl(){
        VendingMachineImpl vm = new VendingMachineImpl();
    }


    private long getTotal(List<Coin> change){
        long total = 0;
        for(Coin c : change){
            total = total + c.getCoinValue();
        }
        return total;
    }

}
