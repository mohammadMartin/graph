package scheduling_01;

import common.Constant;
import graph.CloudLet;
import graph.CloudLetEstimated;
import graph.Edge;
import graph.MyVm;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
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
            // select Vm
            MyVm selectedVm = selectMyVm(vms, c.getResourceName());

            // calculate Execution Time of Node base of  CloudLet Length (MI) / VM Mips (MIPS)
            Duration ET = calculateEt(c.getCloudletLength(), selectedVm.getMips());

            //select parent of cloudLet base of max Execution Finish Time
            CloudLet parentCloudLetMax = maxParentCloudLetByEFT(c);

            // fetch parent of node and weight that is (MB)
            Double weight = parentCloudLetMax
                    .getEdges()
                    .stream()
                    .filter(f -> f.getDestination().equals(c))
                    .map(Edge::getWeight)
                    .findFirst()
                    .orElse(0.0);

            // calculate cost per parentNode weight(MB) and selected VM Band Width (MBPs) Cost(c)=(weight(c,c-1)/Bw(vm))
            Duration cost = calculateCost(weight, selectedVm.getBw());

            // calculate EST Base on Parent EFT(EFT (c-1)) and Communication cost EST(c)=EFT(c-1)+Cost(c)
            Duration EST = calculateEst(parentCloudLetMax.getCloudLetEstimated().getEft(), cost);

            // calculate EFT base on EST(c) and ET(c) EFT(c)=EST(c)+ET(c)
            Duration EFT = calculateEft(EST, ET);

            // Set CloudLet Estimated Object
            c.setCloudLetEstimated(new CloudLetEstimated(EST, EFT, cost, ET, selectedVm));

            // set Vm CloudLet how many time Execute Time
            selectedVm.getCloudLetDuration().put(c, calculateEET(EST, EFT));
        }


        System.out.println();
    }

    // انتخاب منبع ای که در کلاستری است که ورکلود کمتر و همک تایپ خودش باشد
    private MyVm selectMyVm(List<MyVm> vmList, String resourceName) {
        // بر اساس کلاسترها و جمع ورکلود ها گروه بندی کرده و آن کلاستری که ورکلود بیشتری دارد را انتخاب می کند
        Constant.VmCluster vmCluster = vmList
                .stream()
                .collect(groupingBy(MyVm::getCluster, Collectors.summingDouble(MyVm::getWorkLoad)))
                .entrySet()
                .stream()
                .min(comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .get();

        return vmList
                .stream()
                .filter(f -> f.getCluster() == vmCluster && f.getVmm().equals(resourceName))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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

    private Duration calculateCost(Double weight, long bw) {
        if (weight == 0 || bw == 0)
            return Duration.ZERO;
        return Duration.ofSeconds(Math.round(weight / bw));
    }

    private Duration calculateEET(Duration est, Duration eft) {
        return est.plusSeconds(eft.getSeconds());
    }

    private CloudLet maxParentCloudLetByEFT(CloudLet node) {
        CloudLet cloudLet = node;
        Duration eft = Duration.ZERO;
        for (CloudLet inDegreeNode : node.getInDegree()) {
            if (inDegreeNode.getCloudLetEstimated().getEft().compareTo(eft) > 0) {
                cloudLet = inDegreeNode;
                eft = inDegreeNode.getCloudLetEstimated().getEft();
            }
        }
        return cloudLet;

    }

}
