package minmin;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Test {
    private static List<MyCloudlet> cloudletList;
    private static List<Myvm> vmlist;

    public Test() {
    }

    public static void main(String[] args) {
        Log.printLine("Starting Test...");

        try {
            int num_user = 1;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;
            CloudSim.init(num_user, calendar, trace_flag);
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            MyDataCenterBroker broker = createBroker();
            int brokerId = broker.getId();
            vmlist = new ArrayList();
            int vmid = 0;
            long size = 10000L;
            int ram = 2048;
            long bw = 1000L;
            int pesNumber = 1;
            String vmm = "Xen";
            Myvm vm1 = new Myvm(vmid, brokerId, 500.0D, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            vmid = vmid + 1;
            Myvm vm2 = new Myvm(vmid, brokerId, 1000.0D, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            ++vmid;
            new Myvm(vmid, brokerId, 2000.0D, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            ++vmid;
            new Myvm(vmid, brokerId, 800.0D, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            ++vmid;
            vmlist.add(vm1);
            vmlist.add(vm2);
            broker.submitVmList(vmlist);
            cloudletList = new ArrayList();
            int id = 0;
            long fileSize = 300L;
            long outputSize = 300L;
            UtilizationModel utilizationModel = new UtilizationModelFull();
            MyCloudlet cloudlet1 = new MyCloudlet(id, 500L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet1.setUserId(brokerId);
            id = id + 1;
            MyCloudlet cloudlet2 = new MyCloudlet(id, 1000L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet2.setUserId(brokerId);
            ++id;
            MyCloudlet cloudlet3 = new MyCloudlet(id, 1000L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet3.setUserId(brokerId);
            ++id;
            MyCloudlet cloudlet4 = new MyCloudlet(id, 400L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet4.setUserId(brokerId);
            ++id;
            MyCloudlet cloudlet5 = new MyCloudlet(id, 100L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet5.setUserId(brokerId);
            ++id;
            MyCloudlet cloudlet6 = new MyCloudlet(id, 1500L, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet6.setUserId(brokerId);
            cloudletList.add(cloudlet1);
            cloudletList.add(cloudlet2);
            cloudletList.add(cloudlet3);
            cloudletList.add(cloudlet4);
            broker.submitCloudletList(cloudletList);
            CloudSim.startSimulation();
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();
            printCloudletList(newList);
            Log.printLine("stoping Test finished!");
        } catch (Exception var33) {
            var33.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }

    }

    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList();
        List<Pe> peList1 = new ArrayList();
        int mips = 1000;
        peList1.add(new Pe(0, new PeProvisionerSimple((double) mips)));
        peList1.add(new Pe(1, new PeProvisionerSimple((double) mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple((double) mips)));
        peList1.add(new Pe(3, new PeProvisionerSimple((double) mips)));
        List<Pe> peList2 = new ArrayList();
        peList2.add(new Pe(0, new PeProvisionerSimple((double) mips)));
        peList2.add(new Pe(1, new PeProvisionerSimple((double) mips)));
        int hostId = 0;
        int ram = 2048;
        long storage = 1000000L;
        int bw = 10000;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple((long) bw), storage, peList1, new VmSchedulerTimeShared(peList1)));
        hostId = hostId + 1;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple((long) bw), storage, peList2, new VmSchedulerTimeShared(peList2)));
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0D;
        double cost = 3.0D;
        double costPerMem = 0.05D;
        double costPerStorage = 0.1D;
        double costPerBw = 0.1D;
        LinkedList<Storage> storageList = new LinkedList();
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
        Datacenter datacenter = null;

        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0.0D);
        } catch (Exception var27) {
            var27.printStackTrace();
        }

        return datacenter;
    }

    private static MyDataCenterBroker createBroker() {
        MyDataCenterBroker broker = null;

        try {
            broker = new MyDataCenterBroker("Broker");
            return broker;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
        DecimalFormat dft = new DecimalFormat("###.##");

        for (int i = 0; i < size; ++i) {
            Cloudlet cloudlet = (Cloudlet) list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
            if (cloudlet.getCloudletStatus() == 4) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() + indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }
}
