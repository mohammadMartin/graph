package graph;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class CloudLetEstimated {
    private Duration est;
    private Duration eft;
    private Duration cost;
    private Duration et;
}
