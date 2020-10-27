package graph;

import lombok.*;
import minmin.MyCloudlet;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Set<String> resourceTypes;
    private Map<MyVm, CloudLetEstimated> estimatedMapOnVm;
    private Timestamp startTime;

    public enum MARK {
        YES, NO
    }

    //---------------------------------------------------------------
    public int select_task = 0;//this value for minmin and must delete
    //---------------------------------------------------------------

    public CloudLet(int cloudletId, long cloudletLength, int pesNumber, long cloudletFileSize, long cloudletOutputSize, UtilizationModel utilizationModelCpu, UtilizationModel utilizationModelRam, UtilizationModel utilizationModelBw) {
        super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw);
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
