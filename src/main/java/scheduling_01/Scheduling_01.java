package scheduling_01;

import common.Constant;
import graph.CloudLet;
import graph.CloudLetEstimated;
import graph.Edge;
import graph.MyVm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sun.util.BuddhistCalendar;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Getter
@Setter
public class Scheduling_01 {
    public void calculateSchedulingOne(List<MyVm> vms, List<CloudLet> cloudLets, CloudLet startNode, CloudLet endNode) {

        CloudLet stNode = startNode;
        CloudLet edNode = endNode;

        while (stNode != edNode && startNode.getEdges().isEmpty()) {

            Constant.VmCluster selectedCluster = calculateLessWorkLoadCluster(vms);

            MyVm selectedVm = vms
                    .stream()
                    .filter(f -> f.getCluster() == selectedCluster && f.getVmm().equals(stNode.getResourceName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            CloudLetEstimated cloudLetEstimated = new CloudLetEstimated();
            cloudLetEstimated.setEt(calculateEt(stNode.getCloudletLength(), selectedVm.getMips()));
            cloudLetEstimated.setEft(calculateEft());
            cloudLetEstimated.setCost(calculateCost());
            cloudLetEstimated.setEst(calculateEst());

            Map<MyVm, CloudLetEstimated> estimatedCloudLetMap = new HashMap<>();
            estimatedCloudLetMap.put(selectedVm, cloudLetEstimated);

            stNode.setEstimatedMapOnVm(estimatedCloudLetMap);

            Set<Edge> edges = stNode.getEdges();
        }

        for (CloudLet c : cloudLets) {

            Constant.VmCluster selectedCluster = calculateLessWorkLoadCluster(vms);

            MyVm selectedVm = vms
                    .stream()
                    .filter(f -> f.getCluster() == selectedCluster)
                    .filter(f -> f.getVmm().equals(c.getResourceName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            Duration startTime = Duration.ofSeconds(0);

            calculateEt(c.getCloudletLength(), selectedVm.getMips());


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


    private static Duration calculateEt(long cloudLetLength, double mips) {
        if (cloudLetLength == 0 || mips == 0)
            return Duration.ofSeconds(0);

        return Duration.ofSeconds(Math.round(cloudLetLength / mips));
    }

    private static Duration calculateEst() {
        return Duration.ofSeconds(0);
    }

    private static Duration calculateEft() {
        return Duration.ofSeconds(0);
    }

    private static Duration calculateCost() {
        return Duration.ofSeconds(0);
    }

}
