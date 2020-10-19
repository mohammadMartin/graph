package algorithm2;

import graph.CloudLet;
import graph.Edge;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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

            List<CloudLet> outDegree = Vr.getEdges().stream().map(Edge::getDestination).collect(Collectors.toList());
            CloudLet maxCloudLet = outDegree.stream().max(Comparator.comparing(CloudLet::getLFT)).orElseThrow(RuntimeException::new);
            CP.add(maxCloudLet);
        }


    }


    private CloudLet calculateLFT(CloudLet cloudLet) {
        cloudLet.setLFT(new Random().nextDouble());
        return cloudLet;
    }


}
