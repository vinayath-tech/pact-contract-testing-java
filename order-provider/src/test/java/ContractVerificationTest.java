import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import order.Order;
import order.OrderRepository;
import order.OrderServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = OrderServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("order_provider")
@PactBroker(url= "http://localhost:8000")
public class ContractVerificationTest {

    @LocalServerPort
    int port;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Order exists")
    public void orderWithIdExists(Map<String, Object> params) {
//        Integer orderId = Integer.parseInt(params.get("orderId").toString());
        String orderId = params.get("orderId").toString();

        System.out.println("Pact state params: " + params);
        System.out.println("Setting up state for order ID: " + orderId);

        Order order = new Order();
//        order.setId(Integer.parseInt(orderId));
        order.setId(12345);
        order.setPetId(789);
        order.setPetName("Dog");
        order.setStatus("delivered");
        order.setComplete(true);

        when(orderRepository.getById(Integer.parseInt(orderId))).thenReturn(order);

        // Here you would set up the state in your system to ensure that an order with the given ID exists.
        // This could involve inserting a record into a test database or configuring a mock service.
    }

    @State("Order not exists")
    public void orderWithIdNotExists(Map<String, Object> params) {
        String orderId = params.get("orderId").toString();
        System.out.println("Pact state params: " + params);

        when(orderRepository.getById(Integer.parseInt(orderId))).thenReturn(null);
    }
}
