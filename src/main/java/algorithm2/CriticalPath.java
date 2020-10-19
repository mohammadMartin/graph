package algorithm2;

import graph.CloudLet;
import graph.Edge;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CriticalPath {

    public void calculateCriticalPath(List<CloudLet> cloudLets, CloudLet startNode, CloudLet endNode) {
        // محاسبه LFT
        List<CloudLet> current = cloudLets.stream().map(this::calculateLFT).collect(Collectors.toList());

        List<CloudLet> CP = null;

        CloudLet Vr = endNode;

        while (!Vr.equals(startNode)) {

            List<CloudLet> inDegreeNodes = new ArrayList<>();

            for (CloudLet c : current) {
                for (Edge ed : c.getEdges()) {
                    if (ed.getDestination().getIndex().equals(Vr.getIndex())) {
                        inDegreeNodes.add(c);
                    }
                }
            }

            CloudLet maxLFTNode = inDegreeNodes.stream().max(Comparator.comparing(CloudLet::getLFT)).orElse(null);

            Vr = maxLFTNode;


            System.out.println();
        }


    }

    private CloudLet calculateLFT(CloudLet cloudLet) {
        cloudLet.setLFT(new Random().nextDouble());
        return cloudLet;
    }


}
