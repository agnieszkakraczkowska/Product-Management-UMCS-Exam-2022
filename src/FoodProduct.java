import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FoodProduct extends Product {
    private HashMap<String, ArrayList<Double>> pricesInProvinces;
    
    private FoodProduct(String name, HashMap<String, ArrayList<Double>> prices) {
        this.name = name;
        this.pricesInProvinces = prices;
    }
    public static FoodProduct fromCsv(Path path) {
        String name;
        HashMap<String, ArrayList<Double>> provincePrices = new HashMap<>();
        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine();
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] prices = scanner.nextLine().split(";");
                String provinceName = prices[0];
                ArrayList<Double> pricesList = Arrays.stream(prices)
                        .skip(1)
                        .map(price -> price.replace(",", "."))
                        .mapToDouble(Double::valueOf)
                        .boxed()
                        .collect(Collectors.toCollection(ArrayList::new));

                provincePrices.put(provinceName, pricesList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new FoodProduct(name, provincePrices);
    }


    @Override
    public double getPrice(int year, int month) {
        checkYearAndMonth(year, month);

        double sum = 0;
        int count = 0;
        for(var entryKey : pricesInProvinces.keySet()) {
            sum += pricesInProvinces.get(entryKey).get((year - 2010) * 12 + month - 1);
            count++;
        }
        return sum / count;
    }

    public double getPrice(int year, int month, String province) {
        checkYearAndMonth(year, month);

        boolean found = false;
        double result = 0;
        for(var entryKey : pricesInProvinces.keySet()) {
            if(entryKey.equals(province)) {
                found = true;
                result = pricesInProvinces.get(entryKey).get((year - 2010) * 12 + month - 1);
            }
        }

        if(found)
            return result;
        else
            throw new IndexOutOfBoundsException("Province not found");
    }
}
