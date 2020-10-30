package graph;

import common.Constant;
import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MyVm extends Vm {

    private Constant.VmCluster cluster;
    private Double workLoad = 0.0;

    public MyVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }

    // create 6 Vms and two cluster every cluster have 3 type
    public static List<MyVm> produceVm(Integer brokerId) {
        final int[] i = {0};

        List<MyVm> vms = new ArrayList<>();

        Arrays.stream(Constant.VMInfo.values())
                .forEach(f -> {
                    vms.add(createVm(i[0], brokerId, f, Constant.VmCluster.CLUSTER_1));
                    i[0] += 1;
                    vms.add(createVm(i[0], brokerId, f, Constant.VmCluster.CLUSTER_2));
                    i[0] += 1;
                });

        return vms;
    }

    private static MyVm createVm(Integer vmid, Integer brokerId, Constant.VMInfo vmInfo, Constant.VmCluster vmCluster) {

        MyVm myVm = new MyVm(vmid,
                brokerId,
                vmInfo.getMips(),
                vmInfo.getPesNumber(),
                vmInfo.getRam(),
                vmInfo.getBw(),
                vmInfo.getSize(),
                vmInfo.getName(),
                new CloudletSchedulerTimeShared());

        // every vm have cluster for clustering
        myVm.setCluster(vmCluster);

        return myVm;
    }
}
