package graph;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyGraph {
    // Used Tree Map For Sort By Index
    private Set<CloudLet> neighbors = new TreeSet<>();

    public void produceGraph() {
        // get Random Number vertex between 4 and 13
        int vertex = getRandomVertex();

        vertex = 15;
        // add CloudLet to graph
        for (int i = 1; i <= vertex; i++) {
            add(i);
        }

        // loop on nodes and get random edge between 1 and 4 and set to node
        for (CloudLet cloudLet : neighbors) {
            setRandomEdge(cloudLet, getRandomEdge());
        }

        inDegree();
        outDegree();
        System.out.println();
    }

    /*public void getGraphPath() {
        List<CloudLet> startNode = inDegree().keySet().stream().filter(f -> inDegree().get(f) == 0).collect(Collectors.toList());
        if (startNode.size() != 1)
            throw new RuntimeException("Start Node must be once in a graph");
        System.out.println("Start CloudLet is :" + startNode.stream().findFirst().get());

        List<CloudLet> endNodes = outDegree().keySet().stream().filter(f -> outDegree().get(f) == 0).collect(Collectors.toList());
        if (endNodes.size() != 1)
            throw new RuntimeException("End Node must be once in a graph");
        System.out.println("End CloudLet is :" + endNodes.stream().findFirst().get());
    }*/

    // درجه وردی هر نود
    public Map<CloudLet, Integer> inDegree() {
        Map<CloudLet, Integer> result = new HashMap<>();
        for (CloudLet i : neighbors)
            result.put(i, 0);
        for (CloudLet nodes : neighbors) {
            for (CloudLet c : nodes.getDestination()) {
                result.put(c, result.get(c) + 1);
            }
        }
        return result;
    }

    // درجه خروحی هر نود
    public Map<CloudLet, Integer> outDegree() {
        Map<CloudLet, Integer> outDegree = new HashMap<>();
        for (CloudLet i : neighbors) {
            outDegree.put(i, i.getDestination().size());
        }
        return outDegree;
    }

    // مراحل انجام اضافه کردن یال های رندم به گراف
    private void setRandomEdge(CloudLet cloudLet, int edgRandomNumber) {

        Set<CloudLet> edges = new TreeSet<>();

//        Edge edge = new Edge();

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
            setEdg(cloudLet, edges);
        }
        // اگر یال یکی مونده به اخر بود به یال اهر وصل شود
        else if (index == neighbors.size() - 1) {
            edges.addAll(neighbors
                    .stream()
                    .filter(f -> f.getIndex().equals(neighbors.size()))
                    .collect(Collectors.toSet()));

            // اضافه کردن نود یکی مونده به اخر به نود اخر
            setEdg(cloudLet, edges);
        }
    }

    // اضافه کردن یال به نود
    private void setEdg(CloudLet from, Set<CloudLet> to) {
        from.setDestination(to);
    }

    // اضافه کردن نود ها به گراف
    private void add(int vertex) {
        // Create CloudLet
        CloudLet cloudLet = new CloudLet();
        cloudLet.setIndex(vertex);
        cloudLet.setName("CloudLet - " + vertex);
        cloudLet.setDestination(new HashSet<>());

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
            for (CloudLet i : v.getDestination()) {
                edges.add(i.getIndex());
            }
            s.append("\n").append(v.getIndex()).append(" -> ").append(edges);
        }
        return s.toString();
    }
}
