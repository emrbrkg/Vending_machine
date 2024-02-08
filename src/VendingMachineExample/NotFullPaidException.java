package VendingMachineExample;

// A new exception inheritied from RunTimeException.
// It is occurued when the customer did not pay enough for selected product.
public class NotFullPaidException extends RuntimeException{
    private final String message;
    private final Money remaining;

    public NotFullPaidException(String message, Money remaining) {
        this.message = message;
        this.remaining = remaining;
    }

    public Money getRemaining(){
        return remaining;
    }

    @Override
    public String getMessage(){
        return message + remaining.amount;
    }
}

