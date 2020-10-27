package calculatealgorithm;

import graph.*;
import minmin.Myvm;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

public class MyAlgorithm {

    public static void main(String[] args) {
        Log.printLine("Starting CloudSimExample3...");

        try {
            int num_user = 1;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            CloudSim.init(num_user, calendar, trace_flag);

            createDatacenter("DataCenter_0");

            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            List<MyVm> vms = MyVm.produceVm(brokerId);
            broker.submitVmList(vms);

            CloudLetGraph cloudLetGraph = new CloudLetGraph();
            List<CloudLet> cloudLetList = cloudLetGraph.produceGraph();
            broker.submitCloudletList(cloudLetList);


            calculateCloudLetFactorOnVm(vms, cloudLetList);


//            broker.bindCloudletToVm(cloudlet1.getCloudletId(), vm1.getId());
//            broker.bindCloudletToVm(cloudlet2.getCloudletId(), vm2.getId());

            CloudSim.startSimulation();
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();
            printCloudLetList(newList);
            Log.printLine("CloudSimExample3 finished!");
        } catch (Exception var29) {
            var29.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }

    }

    private static void calculateCloudLetFactorOnVm(List<MyVm> vmList, List<CloudLet> cloudLetList) {

        for (CloudLet cl : cloudLetList) {

            Map<MyVm, CloudLetEstimated> estimatedMapOnVm = new HashMap<>();

            for (MyVm vm : vmList) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                if (cl.getResourceTypes().contains(vm.getType())) {
                    CloudLetEstimated cloudLetEstimated = new CloudLetEstimated();
                    cloudLetEstimated.setEst(calculateEst());
                    cloudLetEstimated.setEft(calculateEft());
                    cloudLetEstimated.setCost(calculateCost());
                    cloudLetEstimated.setEet(calculateEet());
                    estimatedMapOnVm.put(vm, cloudLetEstimated);
                }

            }

            cl.setEstimatedMapOnVm(estimatedMapOnVm);
        }
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

    private static Integer calculateEet() {
        return 4;
    }


    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        int mips = 1000;
        peList.add(new Pe(0, new PeProvisionerSimple((double) mips)));
        int hostId = 0;
        int ram = 2048;
        long storage = 1000000L;
        int bw = 10000;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple((long) bw), storage, peList, new VmSchedulerTimeShared(peList)));
        List<Pe> peList2 = new ArrayList<>();
        peList2.add(new Pe(0, new PeProvisionerSimple((double) mips)));
        hostId = hostId + 1;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple((long) bw), storage, peList2, new VmSchedulerTimeShared(peList2)));
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0D;
        double cost = 3.0D;
        double costPerMem = 0.05D;
        double costPerStorage = 0.001D;
        double costPerBw = 0.0D;
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

    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;

        try {
            broker = new DatacenterBroker("Broker");
            return broker;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static void printCloudLetList(List<Cloudlet> list) {
        int size = list.size();
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
        DecimalFormat dft = new DecimalFormat("###.##");

        for (int i = 0; i < size; ++i) {
            Cloudlet cloudlet = (Cloudlet) list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
            if (cloudlet.getCloudletStatus() == 4) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }
}
