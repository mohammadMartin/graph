package minmin;


import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;


public class Myvm extends Vm {
    private double waitingtime = 0.0D;
    private int state = 0;

    public Myvm(int vmid, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler) {
        super(vmid, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }

    public void setwatingtime(double watingtime) {
        this.waitingtime += watingtime;
    }

    public double getWaitingtime() {
        return this.waitingtime;
    }

    public void setstate() {
        this.state = 1;
    }
}
