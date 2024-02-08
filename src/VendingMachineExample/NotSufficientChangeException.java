package VendingMachineExample;

// A new exception inheritied from RunTimeException.
// It is occurued when the vending machine does not have enough coin to give change to the customer.
public class NotSufficientChangeException extends RuntimeException {
    private String message;

    public NotSufficientChangeException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage(){
        return message;
    }

}
