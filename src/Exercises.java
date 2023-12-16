import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Exercises {

    public static void main(String[] args) {
        Exercises exercises = new Exercises();
        System.out.println(exercises.ex1());
        System.out.println(exercises.ex2());
        System.out.println(exercises.ex3());
        System.out.println(exercises.ex4());
        System.out.println(exercises.ex5());
        System.out.println(exercises.ex6());
        System.out.println(exercises.ex7());
        System.out.println(exercises.ex8());
        System.out.println(exercises.ex9());
        System.out.println(exercises.ex10());
        System.out.println(exercises.ex11());
        System.out.println(exercises.ex12());
        System.out.println(exercises.ex13());
        System.out.println(exercises.ex14());
        System.out.println(exercises.ex15());
    }

    public List<Product> ex1() {
        // Obtain a list of products belonging to category “Books” with price > 100
        ProductRepository productRepository = new ProductRepository();
        return productRepository.getAll().stream()
                .filter(product -> product.getCategory().equals("Books") && product.getPrice() > 100)
                .toList();
    }

    public List<Order> ex2() {
        // Obtain a list of orders with products belonging to category “Baby”
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.getAll().stream()
                .filter(order -> order.getProducts().stream().anyMatch(product -> product.getCategory().equals("Baby")))
                .toList();
    }

    public List<Product> ex3() {
        // Obtain a list of products with category = “Toys” and then apply 10% discount
        ProductRepository productRepository = new ProductRepository();
        return productRepository.getAll().stream()
                .filter(product -> product.getCategory().equals("Toy"))
                .peek(product -> product.setPrice(product.getPrice() * 0.9))
                .toList();
    }

    public List<Product> ex4() {
        // Obtain a list of products ordered by customers of tier 2 between 01-Feb-2021 and 01-Apr-2021
        OrderRepository orderRepository = new OrderRepository();

        List<Order> organizedRepository = orderRepository
                .getAll()
                .stream()
                .filter(order -> order.getCustomer().getTier().equals(2) && order.getDeliveryDate().isAfter(LocalDate.of(2021, Month.FEBRUARY, 1))
                        && order.getDeliveryDate().isBefore(LocalDate.of(2021, Month.APRIL, 1)))
                .toList();

        return organizedRepository
                .stream()
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
    }

    public Optional<Product> ex5() {
        // Obtain the cheapest product from the “Books” category
        ProductRepository productRepository = new ProductRepository();

        return productRepository
                .getAll()
                .stream()
                .filter(product -> product.getCategory().equals("Books"))
                .min(Comparator.comparingDouble(Product::getPrice));
    }

    public List<Order> ex6() {
        // Obtain the 3 most recently placed orders
        // HELPFUL TIP: the method limit() might be useful here ;)
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .limit(3)
                .toList();
    }

    public List<Product> ex7() {
        // Obtain the list of orders which were ordered on 15-Mar-2021,
        // log each order to the console and then return their product list
        // HELPFUL TIP: the methods peek() and flatMap() might be useful here ;)
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .filter(order -> order.getOrderDate().equals(LocalDate.of(2021, Month.MARCH, 15)))
                .peek(System.out::println)
                .flatMap(order -> order.getProducts().stream())
                .toList();
    }

    public Double ex8() {
        // Calculate total sum of all orders placed in Feb 2021
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .filter(order -> order.getDeliveryDate()
                        .isAfter(LocalDate.of(2021, Month.JANUARY, 31))
                        && order.getDeliveryDate().isBefore(LocalDate.of(2021, Month.MARCH, 1)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Double ex9() {
        // Calculate average order payment placed on 14-Mar-2021
        OrderRepository orderRepository = new OrderRepository();
        OptionalDouble average = orderRepository
                .getAll()
                .stream()
                .filter(order -> order.getOrderDate().equals(LocalDate.of(2021, Month.MARCH, 14)))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average();
        return average.isPresent() ? average.getAsDouble() : 0.0;
    }

    public DoubleSummaryStatistics ex10() {
        // Obtain a collection of statistic figures (i.e. sum, average, max, min, count)
        // for all products of category “Books”
        // HELPFUL TIP: the methods mapToDouble() and summaryStatistics() might be useful here ;)
        ProductRepository productRepository = new ProductRepository();
        return productRepository
                .getAll()
                .stream()
                .filter(product -> product.getCategory().equals("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
    }

    public Map<Long, Integer> ex11() {
        // Obtain a map with order id as key and each order’s product count as value
        // HELPFUL TIP: the method Collectors.toMap() might be useful here
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .collect(Collectors
                        .toMap(Order::getId, order -> order.getProducts().size()));
    }

    public Map<Customer, List<Order>> ex12() {
        // Obtain a map with orders grouped by customer
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer));
    }

    public Map<Order, Double> ex13() {
        // Obtain a map with order as key and the order's products total sum as value
        // HELPFUL TIP: the method Collectors.toMap() might be useful here ;)
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository
                .getAll()
                .stream()
                .collect(Collectors.toMap(
                        order -> order,
                        this::calculateOrderTotal
                ));
    }

    private double calculateOrderTotal(Order order) {
        return order.getProducts()
                .stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Map<String, List<String>> ex14() {
        // Obtain a map with list of product names by category
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        ProductRepository productRepository = new ProductRepository();
        return productRepository
                .getAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory, //gets category
                        Collectors.mapping(Product::getName, Collectors.toList()//gets name of all products in the category
                        )));

    }

    public Map<String, Optional<Product>> ex15() {
        // Obtain the most expensive product by category
        // HELPFUL TIP: the method Collectors.groupingBy() might be useful here ;)
        ProductRepository productRepository = new ProductRepository();
        return productRepository
                .getAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.maxBy(Comparator.comparing(Product::getPrice)
                        )));
    }
}
