package order;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrderRepository {

    private final Map<UUID, Order> orders = new HashMap<>();

    public void insert(Order order) {

        orders.put(order.getId(), order);
    }

    public Order getById(UUID id) {

        return orders.get(id);
    }

    public void deleteById(UUID id) {

        orders.remove(id);
    }
}