package minmin;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.VmList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDataCenterBroker extends DatacenterBroker {
    public int step = 0;

    public MyDataCenterBroker(String name) throws Exception {
        super(name);
    }

    public List<Myvm> getVmsCreatedList() {
        return (List<Myvm>) this.vmsCreatedList;
    }

    public List<MyCloudlet> getCloudletList() {
        return (List<MyCloudlet>) this.cloudletList;
    }

    protected void submitCloudlets() {
        int vmIndex;
        for (vmIndex = 0; vmIndex < this.getCloudletList().size(); ++vmIndex) {
            this.MinMin_algorithm();
        }

        Log.printLine("====================================");
        vmIndex = 0;
        Iterator var2 = this.getCloudletList().iterator();

        while (true) {
            while (var2.hasNext()) {
                MyCloudlet cloudlet = (MyCloudlet) var2.next();
                Myvm vm;
                if (cloudlet.getVmId() == -1) {
                    vm = (Myvm) this.getVmsCreatedList().get(vmIndex);
                } else {
                    vm = (Myvm) VmList.getById(this.getVmsCreatedList(), cloudlet.getVmId());
                    if (vm == null) {
                        Log.printLine(CloudSim.clock() + ":" + this.getName() + ":postponin execution" + cloudlet.getCloudletId() + ":bount VM not available");
                        continue;
                    }
                }

                Log.printLine(CloudSim.clock() + ":" + this.getName() + ":sending cloudlet " + cloudlet.getCloudletId() + " to VM #" + vm.getId());
                cloudlet.setVmId(vm.getId());
                vm.setstate();
                this.sendNow((Integer) this.getVmsToDatacentersMap().get(vm.getId()), 21, cloudlet);
                ++this.cloudletsSubmitted;
                this.getCloudletSubmittedList().add(cloudlet);
                vmIndex = (vmIndex + 1) % this.getVmsCreatedList().size();
            }

            var2 = this.getCloudletSubmittedList().iterator();

            while (var2.hasNext()) {
                Cloudlet cloudlet = (Cloudlet) var2.next();
                this.getCloudletList().remove(cloudlet);
            }

            return;
        }
    }

    public void MinMin_algorithm() {
        int vm_index = 0;
        int task_index = 0;
        double[] waiting_time = new double[this.cloudletList.size()];
        ArrayList<double[]> run_Time = new ArrayList();
        ArrayList<double[]> temp = new ArrayList();
        double[] inf = new double[2];

        int i;
        for (i = 0; i < this.cloudletList.size(); ++i) {
            temp.add(inf);
        }

        for (i = 0; i < this.vmList.size(); ++i) {
            Vm vm = (Vm) this.vmList.get(i);
            waiting_time[i] = this.calculatewaitingtimeofvm(i);
            double[] rt = new double[this.cloudletList.size()];

            for (int j = 0; j < this.cloudletList.size(); ++j) {
                Cloudlet cloudlet = (Cloudlet) this.cloudletList.get(j);
                rt[j] = (double) cloudlet.getCloudletLength() / vm.getHost().getTotalAllocatedMipsForVm(vm);
            }

            run_Time.add(rt);
        }

        double min_completiontime;
        for (i = 0; i < this.cloudletList.size(); ++i) {
            double minvalue = 500.0D;
            min_completiontime = 0.0D;

            for (int j = 0; j < this.vmList.size(); ++j) {
                min_completiontime = ((double[]) run_Time.get(j))[i];
                if (min_completiontime < minvalue) {
                    minvalue = min_completiontime;
                    double[] temp1 = new double[]{(double) j, min_completiontime};
                    temp.set(i, temp1);
                }
            }
        }

        double min_completiontimeoftask = 90.0D;

        for (i = 0; i < this.cloudletList.size(); ++i) {
            if (((MyCloudlet) this.getCloudletList().get(i)).select_task == 0 && ((double[]) temp.get(i))[1] < min_completiontimeoftask) {
                min_completiontimeoftask = ((double[]) temp.get(i))[1];
                vm_index = (int) ((double[]) temp.get(i))[0];
                task_index = i;
                ((Cloudlet) this.cloudletList.get(i)).setVmId(vm_index);
            }
        }

        Myvm vm = (Myvm) this.getVmsCreatedList().get(vm_index);
        min_completiontime = (double) ((Cloudlet) this.cloudletList.get(task_index)).getCloudletLength() / vm.getHost().getTotalAllocatedMipsForVm(vm);
        ((MyCloudlet) this.getCloudletList().get(task_index)).select_task = 1;
        vm.setwatingtime(min_completiontime);
        ++this.step;
        Log.printLine("=================minmin_algorithm=================");
        Log.printLine("for stage " + this.step + " task" + task_index + " assign to vm " + vm_index + " time " + min_completiontime);
    }

    public double calculatewaitingtimeofvm(int vmid) {
        double waitingtime = 0.0D;
        waitingtime = ((Myvm) this.getVmsCreatedList().get(vmid)).getWaitingtime();
        return waitingtime;
    }
}
