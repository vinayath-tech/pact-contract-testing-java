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
    public Order getOrder(@PathVariable UUID addressId) {

        Order order = orderService.getOrder(addressId);

        if (order == null) {
            throw new NotFoundException();
        }

        return order;
    }

    @DeleteMapping("/address/{orderId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable UUID orderId) {

        orderService.deleteOrder(orderId);
    }
}
