import java.util.List;

public class AmbigiousProductException extends Exception {
    public AmbigiousProductException(List<String> products) {
        super("Ambiguous product(s) name: " + products);
    }
}
