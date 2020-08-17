package graph;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CloudLet implements Comparable<CloudLet> {
    private Integer index;
    private String name;
    private Integer weight;
    private Set<CloudLet> destination;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        CloudLet cloudLet = (CloudLet) o;
        return index.equals(cloudLet.index) && (name.equals(cloudLet.name));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + index.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(CloudLet o) {
        return this.index - o.index;
    }

}
