package order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order getOrder(Integer orderId) {

        return orderRepository.getById(orderId);
    }

    public void deleteOrder(Integer orderId) {

        orderRepository.deleteById(orderId);
    }
}
