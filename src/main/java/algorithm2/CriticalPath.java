package algorithm2;

import graph.CloudLet;
import graph.Edge;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class CriticalPath {

    List<List<CloudLet>> totalCP = new ArrayList<>();

    public void calculateCriticalPath(List<CloudLet> cloudLets, CloudLet startNode, CloudLet endNode) {
        // محاسبه LFT
        List<CloudLet> current = cloudLets.stream().map(this::calculateLFT).collect(Collectors.toList());

        // New WorkFlow
        List<CloudLet> CP = new ArrayList<>();

        // آخرین نود
        CloudLet Vr = endNode;
        CP.add(Vr);

        // تا زمانی که آخرین نود به نود اول نرسد
        while (!Vr.equals(startNode)) {

            // لیست تمام نودهایی که به نود ما وارد می شود
            CloudLet VrPrim = Vr;

            // بعد چرخش تمام نودها تمام یال هایی که آن نود دارد را محاسبه می کنیم اگر به آن نود آخر وارد شده بود به عنوان درجه ورودی آن نود محسوب میشود
            List<CloudLet> inDegreeNodes = current.stream()
                    .filter(f -> f.getEdges().stream().anyMatch(f1 -> f1.getDestination().getIndex().equals(VrPrim.getIndex())))
                    .collect(Collectors.toList());

            // درجه ورودی هر نود را کسانی که درحه LFT بیشتری دارند به لیست اضافه می شودند
            CloudLet Vs = inDegreeNodes.stream().max(Comparator.comparing(CloudLet::getLFT)).orElseThrow(NoSuchElementException::new);

            // آنهایی که انتخاب شده اند به عنوان نود بهرانی مارک yes میخوردند
            current.stream()
                    .filter(f -> f.getIndex().equals(Vs.getIndex()) && !f.getIndex().equals(startNode.getIndex()) && !f.getIndex().equals(endNode.getIndex()))
                    .forEach(m -> m.setMark(CloudLet.MARK.YES));

            //اضافه کردن به CP
            CP.add(Vs);

            Vr = Vs;

            System.out.println();
        }

        totalCP.add(CP);

        List<CloudLet> newCurrent = current.stream()
                .filter(f -> f.getMark() == CloudLet.MARK.NO)
                .collect(Collectors.toList());

        CloudLet stNode = newCurrent.stream().filter(f -> f.getIndex().equals(startNode.getIndex())).findFirst().orElseThrow(RuntimeException::new);
        CloudLet enNode = newCurrent.stream().filter(f -> f.getIndex().equals(endNode.getIndex())).findFirst().orElseThrow(RuntimeException::new);

//        while ()



    }

    private CloudLet calculateLFT(CloudLet cloudLet) {
        cloudLet.setLFT(new Random().nextDouble());
        return cloudLet;
    }


}
