package graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
public class CloudLetEstimated {
    private Duration est;
    private Duration eft;
    private Duration cost;
    private Duration et;
    private MyVm vm;

    public CloudLetEstimated() {
        this.est = Duration.ZERO;
        this.eft = Duration.ZERO;
        this.cost = Duration.ZERO;
        this.et = Duration.ZERO;
    }
}
