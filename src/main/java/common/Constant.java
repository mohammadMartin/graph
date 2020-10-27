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
        TYPE_A("A", 250.0, 1000L, 2048, 1000L, 1, "XEN"),
        TYPE_B("B", 250.0, 1000L, 2048, 1000L, 1, "XEN"),
        TYPE_C("C", 250.0, 1000L, 2048, 1000L, 1, "XEN"),
        TYPE_D("D", 250.0, 1000L, 2048, 1000L, 1, "XEN");

        private String type;
        private Double mips;
        private Long size;
        private Integer ram;
        private Long bw;
        private Integer pesNumber;
        private String name;

        public static Set<String> randomType() {
            Constant.VMInfo[] vmInfoValues = Constant.VMInfo.values();

            int[] vmInfoArray = new Random().ints(2, 0, vmInfoValues.length).toArray();
            return Arrays.stream(vmInfoArray).mapToObj(m -> vmInfoValues[m].getType()).collect(Collectors.toSet());
        }
    }

    @AllArgsConstructor
    @Getter
    public enum VmLabel {
        LABEL_1((byte) 1, "Label_1"),
        LABEL_2((byte) 2, "Label_2");

        private byte value;
        private String title;
    }
}
