package order;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/order/{orderId}", produces = "application/json")
    public Order getOrder(@PathVariable Integer orderId) {

        Order order = orderService.getOrder(orderId);

        if (order == null) {
            throw new NotFoundException();
        }

        return order;
    }

    @DeleteMapping("/order/{orderId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Integer orderId) {

        orderService.deleteOrder(orderId);
    }
}
