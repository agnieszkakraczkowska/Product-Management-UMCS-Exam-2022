import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Product {
    protected String name;
    private static List<Product> products = new ArrayList<>();
    public String getName() {
        return name;
    }

    public void clearProducts() {
        products.clear();
    }

    public static void addProduct(Function<Path, ? extends Product> factoryMethod, Path productsDirectoryPath) {
        try {
            Files.walk(productsDirectoryPath,1)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(path -> {
                        Product product = factoryMethod.apply(path);
                        if(product != null)
                            products.add(product);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Product getProducts(String prefix) throws AmbigiousProductException {
        List<Product> productsStartingWithPrefix = new ArrayList<>();
        for(var product : products) {
            if(product.getName().startsWith(prefix))
                productsStartingWithPrefix.add(product);
        }

        if(productsStartingWithPrefix.size() == 0)
            throw new IndexOutOfBoundsException("Prefix " + prefix + " doesn't refer to any product");
        if(productsStartingWithPrefix.size() > 1) {
            List<String> productsNames = new ArrayList<>();
            for (var product : productsStartingWithPrefix) {
                productsNames.add(product.getName());
            }
            throw new AmbigiousProductException(productsNames);
        }
        else return productsStartingWithPrefix.get(0);
    }

    public abstract double getPrice(int year, int month);

    protected static void checkYearAndMonth(int year, int month) {
       if(year < 2010 || year > 2022)
                throw new IndexOutOfBoundsException("Year must be between 2010 and 2022");
            if(month < 1 || month > 12)
                throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        if(year == 2022 && month > 3)
            throw new IndexOutOfBoundsException("Month must be between 1 and 3 for year 2022");
    }

}
