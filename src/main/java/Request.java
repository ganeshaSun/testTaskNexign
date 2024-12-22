import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter // On my home laptop (version of Java 14) lombok plugin can't be installed so I added Constructors
public class Request {
  public BigDecimal priceTicket;
  public Long dayToDeparture;

  Request() {

  }

  Request(BigDecimal priceTicket, Long dayToDeparture) {
    this.dayToDeparture = dayToDeparture;
    this.priceTicket = priceTicket;
  }
}
