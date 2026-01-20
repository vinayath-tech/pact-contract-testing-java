import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import petstore.OrderServiceClient;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "order_provider", pactVersion = PactSpecVersion.V3)
public class DeleteOrderTest {


    @Pact(consumer = "petstore_consumer", provider = "order_provider")
    public RequestResponsePact pactForDeleteOrder(PactDslWithProvider builder) {

        return builder
                .given("Delete order state")
                .uponReceiving("Request to delete an order")
                .path(String.format("/order/%s", OrderId.EXISTING_ORDER_ID))
                .method("DELETE")
                .willRespondWith()
                .status(204)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteOrder")
    public void testFor_Delete_Order_shouldYieldHttp204(MockServer mockServer) {

        OrderServiceClient client = new OrderServiceClient(mockServer.getUrl());
        client.deleteOrder(OrderId.EXISTING_ORDER_ID);
    }
}
