package scheduling_01;

import common.Constant;
import graph.CloudLet;
import graph.CloudLetEstimated;
import graph.MyVm;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;


@Getter
@Setter
public class Scheduling_01 {
    public void calculateSchedulingOne(List<MyVm> vms, List<CloudLet> cloudLets) {

        for (CloudLet c : cloudLets) {

            MyVm selectedVm = vms
                    .stream()
                    .filter(f -> f.getCluster() == calculateLessWorkLoadCluster(vms) && f.getVmm().equals(c.getResourceName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            Duration ET = calculateEt(c.getCloudletLength(), selectedVm.getMips());
            CloudLetEstimated previousEstimatedCloudLet = maxEstimated(cloudLets, c);
            Duration cost = calculateCost(c.getCloudletFileSize(), selectedVm.getBw());
            Duration EST = calculateEst(previousEstimatedCloudLet.getEft(), cost);
            Duration EFT = calculateEft(EST, ET);


        }


        System.out.println();
    }


    private Constant.VmCluster calculateLessWorkLoadCluster(List<MyVm> vmList) {
        return vmList
                .stream()
                .collect(groupingBy(MyVm::getCluster, Collectors.summingDouble(MyVm::getWorkLoad)))
                .entrySet()
                .stream()
                .max(comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .get();
    }


    private Duration calculateEt(long cloudLetLength, double mips) {
        if (cloudLetLength == 0 || mips == 0)
            return Duration.ZERO;
        return Duration.ofSeconds(Math.round(cloudLetLength / mips));
    }

    private Duration calculateEft(Duration EST, Duration ET) {
        return EST.plusSeconds(ET.getSeconds());
    }

    private Duration calculateEst(Duration eft, Duration cost) {
        return eft.plusSeconds(cost.getSeconds());
    }

    private Duration calculateCost(long cloudLetFileSize, long bw) {
        if (cloudLetFileSize == 0 || bw == 0)
            return Duration.ZERO;
        return Duration.ofSeconds(Math.round(cloudLetFileSize / bw));
    }

    private CloudLetEstimated maxEstimated(List<CloudLet> cloudLets, CloudLet node) {
        return inDegree(cloudLets, node)
                .stream()
                .map(CloudLet::getCloudLetEstimated)
                .max(comparing(CloudLetEstimated::getEft))
                .orElseThrow(RuntimeException::new);
    }

    private List<CloudLet> inDegree(List<CloudLet> cloudLets, CloudLet node) {
        return cloudLets.stream()
                .filter(f -> f.getEdges().stream().anyMatch(e -> e.getDestination().getIndex().equals(node.getIndex())))
                .collect(Collectors.toList());
    }

}
