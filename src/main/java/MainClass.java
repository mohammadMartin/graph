import graph.MyGraph;

public class MainClass {
    public static void main(String[] args) {
        MyGraph myGraph = new MyGraph();
        myGraph.produceGraph();
        System.out.println("The current graph:" + myGraph);

        System.out.println("InDegree Of Nodes:" + myGraph.inDegree());
        System.out.println("OutDegree Of Nodes:" + myGraph.outDegree());
//        myGraph.getGraphPath();
    }
}
