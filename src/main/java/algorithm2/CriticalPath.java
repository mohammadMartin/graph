package algorithm2;

import graph.CloudLet;
import graph.Edge;
import lombok.Getter;
import lombok.Setter;
import org.cloudbus.cloudsim.UtilizationModelFull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CriticalPath {

    public void calculateCriticalPath(List<CloudLet> cloudLets, CloudLet startNode, CloudLet endNode) {
        // محاسبه LFT
        List<CloudLet> current = cloudLets.stream().map(this::calculateLFT).collect(Collectors.toList());

        // New WorkFlow
        List<CloudLet> CP = new ArrayList<>();

        // آخرین نود
        CloudLet Vr = endNode;
        Vr.setMark(CloudLet.MARK.YES);
        CP.add(Vr);

        // تا زمانی که آخرین نود به نود اول نرسد
        while (!Vr.equals(startNode)) {

            // لیست تمام نودهایی که به نود ما وارد می شود
            List<CloudLet> inDegreeNodes = new ArrayList<>();

            // بعد چرخش تمام نودها تمام یال هایی که آن نود دارد را محاسبه می کنیم اگر به آن نود آخر وارد شده بود به عنوان درجه ورودی آن نود محسوب میشود
            for (CloudLet c : current) {
                for (Edge ed : c.getEdges()) {
                    if (ed.getDestination().getIndex().equals(Vr.getIndex())) {
                        inDegreeNodes.add(c);
                    }
                }
            }

            // درجه ورودی هر نود را کسانی که درحه LFT بیشتری دارند به لیست اضافه می شودند
            CloudLet Vs = inDegreeNodes.stream().max(Comparator.comparing(CloudLet::getLFT)).orElseThrow(NoSuchElementException::new);
            current.stream().filter(f -> f.getIndex().equals(Vs.getIndex())).forEach(m -> m.setMark(CloudLet.MARK.YES));
            CP.add(Vs);

            Vr = Vs;

            System.out.println();
        }


        System.out.println();
    }

    private CloudLet calculateLFT(CloudLet cloudLet) {
        cloudLet.setLFT(new Random().nextDouble());
        return cloudLet;
    }


}
