package graph;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class CloudLet extends Cloudlet implements Comparable<CloudLet> {
    private Integer index;
    private String name;
    private Double weight;
    private Set<Edge> edges;
    private Double LFT;
    private MARK mark;
    private String resourceName;
    private CloudLetEstimated cloudLetEstimated;
    private Set<CloudLet> inDegree;
    private Set<Integer> outDegree;

    public enum MARK {
        YES, NO
    }

    public CloudLet(int cloudLetId, long cloudLetLength, int pesNumber, long cloudLetFileSize, long cloudLetOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw) {
        super(cloudLetId, cloudLetLength, pesNumber, cloudLetFileSize, cloudLetOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw);
    }

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
