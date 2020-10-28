package graph;

import calculatealgorithm.MyAlgorithm;
import common.Constant;
import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class MyVm extends Vm {

    private Byte label;
    private String type;
    private Double workLoad;

    public MyVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }

    // create 9 Vms have 4 type
    public static List<MyVm> produceVm(Integer brokerId) {
        List<MyVm> vms = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (i < 2) {
                vms.add(createVm(i, brokerId, Constant.VMInfo.TYPE_A));
            } else if (i < 4) {
                vms.add(createVm(i, brokerId, Constant.VMInfo.TYPE_B));
            } else if (i < 6) {
                vms.add(createVm(i, brokerId, Constant.VMInfo.TYPE_C));
            } else {
                vms.add(createVm(i, brokerId, Constant.VMInfo.TYPE_D));
            }
        }

        return vms;
    }

    private static MyVm createVm(Integer vmid, Integer brokerId, Constant.VMInfo vmInfo) {

        MyVm myVm = new MyVm(vmid,
                brokerId,
                vmInfo.getMips(),
                vmInfo.getPesNumber(),
                vmInfo.getRam(),
                vmInfo.getBw(),
                vmInfo.getSize(),
                vmInfo.getName(),
                new CloudletSchedulerTimeShared());

        // every vm have a type for work on a special subject
        myVm.setType(vmInfo.getType());

        // every vm have work load for test that define how many work are in the vm
        myVm.setWorkLoad(ThreadLocalRandom.current().nextDouble(100));

        // every vm have label for clustering
        myVm.setLabel(vmid % 2 == 0 ? Constant.VmLabel.LABEL_1.getValue() : Constant.VmLabel.LABEL_2.getValue());

        return myVm;
    }
}
