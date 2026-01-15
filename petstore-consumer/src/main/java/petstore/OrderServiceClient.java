package petstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    public OrderServiceClient(@Value("${order_provider.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplateBuilder()
                .errorHandler(new OrderErrorHandler())
                .rootUri(baseUrl)
                .defaultHeader("Connection", "close")
                .build();
    }

    public Order getOrder(String orderId) {
        return restTemplate.getForObject(String.format("/order/%s", orderId), Order.class);
    }

    public void deleteOrder(String orderId) {
        restTemplate.delete(String.format("/order/%s", orderId));
    }
}
