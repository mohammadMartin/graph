package graph;

import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

@Getter
@Setter
public class MyVm extends Vm {

    private Byte label;
    private String type;
    private Double workLoad;

    public MyVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }
}
