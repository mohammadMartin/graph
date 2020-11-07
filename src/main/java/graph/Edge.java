package graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Edge implements Comparable<Edge> {
    private CloudLet destination;
    private Double weight;


    @Override
    public int compareTo(Edge o) {
        return this.getDestination().getIndex() - o.getDestination().getIndex();
    }
}
