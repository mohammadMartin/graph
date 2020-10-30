package common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Constant {

    @Getter
    @AllArgsConstructor
    public enum VMInfo {
        TYPE_A(250.0, 1000L, 1024, 500L, 1, "A"),
        TYPE_B(150.0, 1000L, 2048, 1000L, 1, "B"),
        TYPE_C(350.0, 1000L, 3072, 2000L, 1, "C");

        private Double mips;
        private Long size;
        private Integer ram;
        private Long bw;
        private Integer pesNumber;
        private String name;

        public static String randomType() {
            Constant.VMInfo[] vmInfoValues = Constant.VMInfo.values();
            int randomValue = new Random().nextInt(vmInfoValues.length);
            return vmInfoValues[randomValue].getName();
        }
    }

    @AllArgsConstructor
    @Getter
    public enum VmCluster {
        CLUSTER_1((byte) 1, "CLUSTER_1"),
        CLUSTER_2((byte) 2, "CLUSTER_2");

        private byte value;
        private String title;
    }
}
