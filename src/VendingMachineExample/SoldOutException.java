package VendingMachineExample;

// A new exception inheritied from RunTimeException.
// It is occured when the selected product in not contained by the product inventory on the vending machine
public class SoldOutException extends RuntimeException {
    private String message;

    public SoldOutException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage(){
        return message;
    }

}
