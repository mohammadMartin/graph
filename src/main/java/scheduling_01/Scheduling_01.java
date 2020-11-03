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
            List<CloudLet> inDegreeOfNode = inDegree(cloudLets, c);
            CloudLetEstimated previousEstimatedCloudLet = maxEstimated(cloudLets, c);
            calculateEst(previousEstimatedCloudLet.getEft(),c.getCloudletFileSize(),selectedVm.getBw());
            Duration EFT = calculateEft(selectedVm, ET);


        }


        System.out.println();
    }


    private Constant.VmCluster calculateLessWorkLoadCluster(List<MyVm> vmList) {
        return vmList
                .stream()
                .collect(groupingBy(MyVm::getCluster, Collectors.summingDouble(MyVm::getWorkLoad)))
                .entrySet()
                .stream()
                .max(comparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .get();
    }


    private Duration calculateEt(long cloudLetLength, double mips) {
        if (cloudLetLength == 0 || mips == 0)
            return Duration.ofSeconds(0);
        return Duration.ofSeconds(Math.round(cloudLetLength / mips));
    }

    private Duration calculateEft(MyVm vm, Duration ET) {
        // به نظرم این میشه تخمین زمان شروع
        Duration lastNodeDuration = vm
                .getCloudLetDuration()
                .lastEntry()
                .getValue();

        return lastNodeDuration.plusSeconds(ET.getSeconds());

    }

    private Duration calculateEst(Duration eft, long cloudLetFileSize, long bw) {
        cloudLetFileSize/bw
    }

    private Duration calculateCost() {
        return Duration.ofSeconds(0);
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
