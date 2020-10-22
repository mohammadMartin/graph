package graph;

import common.Constant;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Consts;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class MyVmGraph {
    private List<MyVm> vms = new ArrayList<>();

    public List<MyVm> produceVm(Integer brokerId) {

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

    private MyVm createVm(Integer vmid, Integer brokerId, Constant.VMInfo vmInfo) {

        MyVm myVm = new MyVm(vmid,
                brokerId,
                (double) vmInfo.getMips(),
                vmInfo.getPesNumber(),
                vmInfo.getRam(),
                vmInfo.getBw(),
                vmInfo.getSize(),
                vmInfo.getName(),
                new CloudletSchedulerTimeShared());

        myVm.setType(vmInfo.getType());
        myVm.setWorkLoad(ThreadLocalRandom.current().nextDouble(100));
        myVm.setLabel(vmid % 2 == 0 ? Constant.VmLabel.LABEL_1.getValue() : Constant.VmLabel.LABEL_2.getValue());
        return myVm;
    }
}
