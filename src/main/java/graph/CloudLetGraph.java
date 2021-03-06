package graph;


import common.Constant;
import distribution.StdRandom;
import org.cloudbus.cloudsim.UtilizationModelFull;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CloudLetGraph {
    // Used Tree Map For Sort By Index
    private Set<CloudLet> neighbors = new TreeSet<>();

    public List<CloudLet> produceGraph() {

        // get Random Number vertex between 4 and 13
        int vertex = getRandomVertex();

        // add CloudLet to graph
        for (int i = 1; i <= vertex; i++)
            add(i);

        // loop on nodes and get random edge between 1 and 4 and set to node
        for (CloudLet cloudLet : neighbors)
            setRandomEdge(cloudLet, getRandomEdge());

        // set InDegree Node Of Vertex
        neighbors.forEach(f -> f.setInDegree(new TreeSet<>(getInDegreeNodeIndex(f.getIndex()))));


        return new ArrayList<>(neighbors);
    }

    // حداقل 4 نود باید تولید شود
    private int getRandomVertex() {
        int i = new Random().nextInt(13);
        return i < 4 ? 4 : i;
    }

    // اضافه کردن نود ها به گراف
    private void add(int vertex) {
        // Create CloudLet
        CloudLet cloudLet = new CloudLet(vertex, 400000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
        cloudLet.setIndex(vertex);
        cloudLet.setName("CloudLet-" + vertex);
        cloudLet.setWeight(StdRandom.pareto());
        cloudLet.setEdges(new TreeSet<>());
        cloudLet.setMark(CloudLet.MARK.NO);
        cloudLet.setResourceName(Constant.VMInfo.randomType());
        cloudLet.setCloudLetEstimated(new CloudLetEstimated());
        cloudLet.setInDegree(new TreeSet<>());
        cloudLet.setOutDegree(new TreeSet<>());

        // if cloudLet has in graph return else add to Graph
        if (neighbors.contains(cloudLet)) return;

        // add cloudLet to neighbors
        neighbors.add(cloudLet);
    }

    // اگر کمتر از 1 داد +1 میکند یعنی هر نود حداقل به 1 نود دیگر وصل شند
    //درجه خروجی هر نود از بازه 1 تا 4 می باشد
    private int getRandomEdge() {
        int i = new Random().nextInt(4);
        return i < 1 ? 1 : i;
    }

    // مراحل انجام اضافه کردن یال های رندم به گراف
    private void setRandomEdge(CloudLet cloudLet, int edgRandomNumber) {
        Set<CloudLet> edges = new TreeSet<>();

        Integer cloudLetIndex = cloudLet.getIndex();

        // اکر نود در حال پیمایش نود اخر و یکی مانده به اخر نباشد عملیات زیر انجام می شود
        if (cloudLetIndex != neighbors.size() && cloudLetIndex != neighbors.size() - 1) {
            // به شماره نود در حال پیمایش یک واحد اضافه می شود
            int min = cloudLetIndex + 1;

            // سایز نود های موجود در کل گراف
            int max = neighbors.size();

            // اگر نود در حال پیمایش را 6 واحد اضافه کردیم و از تعداد نود های بزرگ تر شد سایز تعداد نود ها به عنوان رنج مجسوب می شود
            int size = min + 6 > max ? max : min + 6;

            // عددی بین index+1 تا 6 تای بعدی اگر از کل نودها بزرگتر نباشد
            int[] randomEdge = new Random().ints(edgRandomNumber, min, size).toArray();

            // اضافه کردن یال ها به لیست
            for (Integer i : randomEdge)
                edges.addAll(neighbors.stream().filter(f -> f.getIndex().equals(i)).collect(Collectors.toSet()));


            // اضافه کردن یال ها به نود در حال پیمایش
            edges.forEach(m -> setEdg(cloudLet, m));
        }

        // اگر یال یکی مونده به اخر بود به یال اخر وصل شود
        else if (cloudLetIndex == neighbors.size() - 1)
            setEdg(cloudLet, neighbors.stream().filter(f -> f.getIndex().equals(neighbors.size())).findFirst().orElseThrow(RuntimeException::new));

        // به غیر از نود اول تمام نود ها باید ورودی داشته باشند اکر نودی ورودی نداشته باشد باید به نود قبل از خودش وصل شود
        if (cloudLetIndex != 1 && cloudLet.getInDegree().size() == 0)
            setEdg(neighbors.stream().filter(f -> f.getIndex() == cloudLetIndex - 1).findFirst().orElseThrow(RuntimeException::new), cloudLet);
    }

    // اضافه کردن یال به نود
    private void setEdg(CloudLet from, CloudLet to) {
        from.getEdges().add(new Edge(to, StdRandom.pareto()));
        from.getOutDegree().add(to.getIndex());
    }

    public void getGraphPath() {
        CloudLet startNode = getStartNode();
        System.out.println("Start CloudLet is: " + startNode.getName());

        CloudLet endNode = getEndNode();
        System.out.println("End CloudLet is: " + endNode.getName());
    }

    public CloudLet getStartNode() {
        return neighbors
                .stream()
                .filter(f -> f.getInDegree().size() == 0).findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public CloudLet getEndNode() {
        return neighbors
                .stream()
                .filter(f -> f.getOutDegree().size() == 0)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }


    private List<CloudLet> getInDegreeNodeIndex(int index) {
        return neighbors
                .stream()
                .filter(f -> f.getEdges().stream().anyMatch(f1 -> f1.getDestination().getIndex().equals(index)))
                .collect(toList());
    }

    public String inDegreePrint() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        neighbors.forEach(f -> stringBuilder.append("Node-").append(f.getIndex()).append("=").append(f.getInDegree().size()).append(", "));
        return stringBuilder.append("}").toString();
    }

    public String outDegreePrint() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        neighbors.forEach(f -> stringBuilder.append("Node-").append(f.getIndex()).append("=").append(f.getOutDegree().size()).append(", "));
        return stringBuilder.append("}").toString();
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (CloudLet v : neighbors) {
            s.append("\n").append(v.getIndex()).append(" -> ").append(v.getEdges().stream().map(e -> e.getDestination().getIndex()).collect(toList()));
        }
        return s.toString();
    }
}
