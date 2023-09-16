import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product, int amount) {
        for(int i = 0; i < amount; i++) {
            products.add(product);
        }
    }

    public double getPrice(int year, int month) {
        double sum = 0;
        for(var product : products) {
            sum += product.getPrice(year, month);
        }
        return sum;
    }

    public double getInflation(int year1, int month1, int year2, int month2) {
        if(year1 < year2 && month1 < month2)
            throw new IllegalArgumentException("First date must be earlier than second date");
        if(year1 < year2)
            throw new IllegalArgumentException("First date must be earlier than second date");
        if(year1 == year2 && month1 < month2)
            throw new IllegalArgumentException("First date must be earlier than second date");
        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);

        return (price2 - price1) / price1 * 100 / monthsBetween(year1, month1, year2, month2) * 12;
    }

    public int monthsBetween(int year1, int month1, int year2, int month2) {
        YearMonth firstDate = YearMonth.of(year1, month1);
        YearMonth secondDate = YearMonth.of(year2, month2);

        long amountOfMonths = ChronoUnit.MONTHS.between(firstDate, secondDate);
        System.out.println(amountOfMonths);
        return (int) amountOfMonths;
    }
}
