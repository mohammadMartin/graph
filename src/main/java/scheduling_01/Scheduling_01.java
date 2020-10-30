package scheduling_01;

import common.Constant;
import graph.CloudLet;
import graph.MyVm;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Getter
@Setter
public class Scheduling_01 {
    public void calculateSchedulingOne(List<MyVm> vms, List<CloudLet> cloudLets) {
        List<MyVm> vmList = vms;
        List<CloudLet> cloudLetList = cloudLets;

        for (CloudLet c : cloudLetList) {

            Constant.VmCluster selectedCluster = calculateLessWorkLoadCluster(vmList);

            MyVm selectedVm = vmList
                    .stream()
                    .filter(f -> f.getCluster() == selectedCluster)
                    .filter(f -> f.getVmm().equals(c.getResourceName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            Instant startTime = Instant.now();
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
        double taskSecond = cloudLetLength / mips;
        LocalDateTime taskTime = LocalDateTime.MIN.plusSeconds((long) taskSecond);
        Duration duration = Duration.ZERO;
        duration.plus(taskTime.getSecond(), ChronoUnit.SECONDS);
        duration.plus(taskTime.getMinute(), ChronoUnit.MINUTES);
        duration.plus(taskTime.getHour(), ChronoUnit.HOURS);


        return null;
    }

    private static Integer calculateEst() {
        return 1;
    }

    private static Integer calculateEft() {
        return 2;
    }

    private static Integer calculateCost() {
        return 3;
    }

}
