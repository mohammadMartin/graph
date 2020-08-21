import graph.MyGraph;

public class MainClass {
    public static void main(String[] args) {
        MyGraph myGraph = new MyGraph();
        myGraph.produceGraph();
        System.out.println("The current graph:" + myGraph);
        System.out.println();
        System.out.println("InDegree Of Nodes:" + myGraph.inDegreePrint());
        System.out.println();
        System.out.println("OutDegree Of Nodes:" + myGraph.outDegreePrint());
        System.out.println();


        myGraph.getGraphPath();
    }
}
