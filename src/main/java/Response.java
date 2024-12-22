import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // On my home laptop (version of Java 14) lombok plugin can't be installed so I added getters
public class Response {
  String percent;
  BigDecimal cashback;

  public String getPercent() {
    return  this.percent;
  }

  public BigDecimal getCashback() {
    return this.cashback;
  }
}
