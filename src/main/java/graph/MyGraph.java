package graph;

import distribution.StdRandom;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyGraph {
    // Used Tree Map For Sort By Index
    private Set<CloudLet> neighbors = new TreeSet<>();

    public List<CloudLet> produceGraph() {
        // get Random Number vertex between 4 and 13
        int vertex = getRandomVertex();

//        vertex = 10;
        // add CloudLet to graph
        for (int i = 1; i <= vertex; i++) {
            add(i);
        }

        // loop on nodes and get random edge between 1 and 4 and set to node
        for (CloudLet cloudLet : neighbors) {
            setRandomEdge(cloudLet, getRandomEdge());
        }

        return new ArrayList<>(neighbors);
    }

    public void getGraphPath() {
        List<CloudLet> startNode = inDegree().keySet().stream().filter(f -> inDegree().get(f) == 0).collect(Collectors.toList());
        if (startNode.size() != 1)
            throw new RuntimeException("Start Node must be once in a graph");
        CloudLet cloudLet = startNode.stream().findFirst().get();
        System.out.println("Start CloudLet is: " + cloudLet.getName());

        List<CloudLet> endNodes = outDegree().keySet().stream().filter(f -> outDegree().get(f) == 0).collect(Collectors.toList());
        if (endNodes.size() != 1)
            throw new RuntimeException("End Node must be once in a graph");
        CloudLet cloudLet1 = endNodes.stream().findFirst().get();
        System.out.println("End CloudLet is: " + cloudLet1.getName());
    }


    // درجه وردی هر نود
    private Map<CloudLet, Integer> inDegree() {
        Map<CloudLet, Integer> result = new TreeMap<>();
        for (CloudLet i : neighbors)
            result.put(i, 0);
        for (CloudLet nodes : neighbors) {
            for (Edge c : nodes.getEdges()) {
                result.put(c.getDestination(), result.get(c.getDestination()) + 1);
            }
        }
        return result;
    }

    // درجه وردی هر نود
    private Integer inDegree(int index) {
        Integer value = 0;
        for (CloudLet nodes : neighbors) {
            for (Edge c : nodes.getEdges()) {
                if (c.getDestination().getIndex() == index) {
                    value++;
                }
            }
        }
        return value;
    }

    // درجه خروحی هر نود
    private Map<CloudLet, Integer> outDegree() {
        Map<CloudLet, Integer> outDegree = new TreeMap<>();
        for (CloudLet i : neighbors) {
            outDegree.put(i, i.getEdges().size());
        }
        return outDegree;
    }

    // مراحل انجام اضافه کردن یال های رندم به گراف
    private void setRandomEdge(CloudLet cloudLet, int edgRandomNumber) {

        Set<CloudLet> edges = new TreeSet<>();

        Integer index = cloudLet.getIndex();

        // اکر نود در حال پیمایش نود اخر و یکی مانده به اخر نباشد عملیات زیر انجام می شود
        if (index != neighbors.size() && index != neighbors.size() - 1) {
            // به شماره نود در حال پیمایش یک واحد اضافه می شود
            int min = index + 1;

            // سایز نود های موجود در کل گراف
            int max = neighbors.size();

            // اگر نود در حال پیمایش را 6 واحد اضافه کردیم و از تعداد نود های بزرگ تر شد سایز تعداد نود ها به عنوان رنج مجسوب می شود
            int size = min + 6 > max ? max : min + 6;

            // عددی بین index+1 تا 6 تای بعدی اگر از کل نودها بزرگتر نباشد
            Random random = new Random();
            int[] randomEdge = random.ints(edgRandomNumber, min, size).toArray();

            // اضافه کردن یال ها به لیست
            for (Integer i : randomEdge) {
                edges.addAll(neighbors
                        .stream()
                        .filter(f -> f.getIndex().equals(i))
                        .collect(Collectors.toSet()));
            }

            // اضافه کردن یال ها به نود در حال پیمایش
            edges.forEach(m -> setEdg(cloudLet, m));
        }
        // اگر یال یکی مونده به اخر بود به یال اهر وصل شود
        else if (index == neighbors.size() - 1) {
            // اضافه کردن نود یکی مونده به اخر به نود اخر
            setEdg(cloudLet, neighbors.stream().filter(f -> f.getIndex().equals(neighbors.size())).findFirst().orElseThrow(RuntimeException::new));
        }

        // به غیر از نود اول تمام نود ها باید ورودی داشته باشند اکر نودی ورودی نداشته باشد باید به نود قبل از خودش وصل شود
        if (index != 1) {
            if (inDegree(index) == 0) {
                setEdg(neighbors.stream().filter(f -> f.getIndex() == index - 1).findFirst().orElseThrow(RuntimeException::new), cloudLet);
            }
        }
    }

    // اضافه کردن یال به نود
    private void setEdg(CloudLet from, CloudLet to) {
        from.getEdges().add(new Edge(to, StdRandom.pareto()));
    }

    // اضافه کردن نود ها به گراف
    private void add(int vertex) {
        // Create CloudLet
        UtilizationModel utilizationModel = new UtilizationModelFull();
        CloudLet cloudLet = new CloudLet(vertex,400000,1,300,300,utilizationModel,utilizationModel,utilizationModel);
        cloudLet.setIndex(vertex);
        cloudLet.setName("CloudLet_" + vertex);
        cloudLet.setWeight(StdRandom.pareto());
        cloudLet.setEdges(new HashSet<>());

        // if cloudLet has in graph return else add to graph
        if (neighbors.contains(cloudLet)) return;
        neighbors.add(cloudLet);
    }

    // حداقل 4 نود باید تولید شود
    private int getRandomVertex() {
        int i = new Random().nextInt(13);
        return i < 4 ? 4 : i;
    }

    // اگر کمتر از 1 داد +1 میکند یعنی هر نود حداقل به 1 نود دیگر وصل شند
    //درجه خروجی هر نود از بازه 1 تا 4 می باشد
    private int getRandomEdge() {
        int i = new Random().nextInt(4);
        return i < 1 ? 1 : i;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (CloudLet v : neighbors) {
            List<Integer> edges = new ArrayList<>();
            for (Edge i : v.getEdges()) {
                edges.add(i.getDestination().getIndex());
            }
            s.append("\n").append(v.getIndex()).append(" -> ").append(edges);
        }
        return s.toString();
    }

    public String inDegreePrint() {
        Map<CloudLet, Integer> result = inDegree();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (CloudLet i : result.keySet()) {
            stringBuilder.append("Node:").append(i.getIndex()).append("=").append(result.get(i).toString()).append(", ");
        }
        return stringBuilder.append("}").toString();
    }

    public String outDegreePrint() {
        Map<CloudLet, Integer> result = outDegree();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        for (CloudLet i : result.keySet())
            stringBuilder.append("Node:").append(i.getIndex()).append("=").append(result.get(i).toString()).append(", ");

        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
