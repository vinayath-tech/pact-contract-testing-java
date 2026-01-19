package order;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Order {

    private int id;
    private int petId;
    private String petName;
    private String status;
    private boolean complete;
}
