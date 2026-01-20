import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.*;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import petstore.BadRequestException;
import petstore.NotFoundException;
import petstore.Order;
import petstore.OrderServiceClient;

import java.util.Map;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "order_provider", pactVersion = PactSpecVersion.V3)
public class GetOrderTest {

    @Pact(consumer = "petstore_consumer", provider = "order_provider")
    public RequestResponsePact pactToGetExistingOrderId(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .integerType("id", Integer.parseInt(OrderId.EXISTING_ORDER_ID))
                .integerType("petId", 789)
                .stringType("petName", "Dog")
                .stringType("status", "delivered")
                .booleanType("complete", true)
        ).build();

        Map<String, Object> providerStateParams = Map.of("orderId", OrderId.EXISTING_ORDER_ID);

        return builder
                .given("Order exists", providerStateParams)
                .uponReceiving("Retrieving an existing order ID")
                .path(String.format("/order/%s", OrderId.EXISTING_ORDER_ID))
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Pact(consumer = "petstore_consumer", provider = "order_provider")
    public RequestResponsePact pactToGetNonExistentOrderId(PactDslWithProvider builder) {
        Map<String, Object> providerStateParams = Map.of("orderId", OrderId.NON_EXISTING_ORDER_ID);

        return builder
                .given("Order not exists", providerStateParams)
                .uponReceiving("Retriving an Non existent oder ID")
                .path(String.format("/order/%s", OrderId.NON_EXISTING_ORDER_ID))
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Pact(consumer = "petstore_consumer", provider = "order_provider")
    public RequestResponsePact pactToGetInvalidOrderId(PactDslWithProvider builder) {

        return builder
                .given("Order ID is invalid")
                .uponReceiving("Retrieving an Invalid order ID")
                .path(String.format("/order/%s", OrderId.INVALID_ORDER_ID))
                .method("GET")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactToGetExistingOrderId")
    public void testFor_GET_existingOrderId_shouldYieldHTTP200(MockServer mockServer) {

        OrderServiceClient client = new OrderServiceClient(mockServer.getUrl());
        Order order = client.getOrder(OrderId.EXISTING_ORDER_ID);

        Assertions.assertEquals(Integer.parseInt(OrderId.EXISTING_ORDER_ID), order.getId());
    }

    @Test
    @PactTestFor(pactMethod = "pactToGetNonExistentOrderId")
    public void testFor_GET_nonExistentOrderId_shouldYieldHTTP404(MockServer mockServer) {

        OrderServiceClient client = new OrderServiceClient(mockServer.getUrl());

        Assertions.assertThrows(NotFoundException.class, () -> {
            client.getOrder(OrderId.NON_EXISTING_ORDER_ID);
        });
    }

    @Test
    @PactTestFor(pactMethod = "pactToGetInvalidOrderId")
    public void testFor_GET_invalidOrderId_shouldYieldHTTP400(MockServer mockServer) {

        OrderServiceClient client = new OrderServiceClient(mockServer.getUrl());
        Assertions.assertThrows(BadRequestException.class, () -> client.getOrder(OrderId.INVALID_ORDER_ID));
    }
}
