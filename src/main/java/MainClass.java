import algorithm2.CriticalPath;
import graph.CloudLet;
import graph.MyGraph;
import minmin.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        MyGraph myGraph = new MyGraph();
        List<CloudLet> cloudLets = myGraph.produceGraph();
        System.out.println("The current graph:" + myGraph);
        System.out.println();
        System.out.println("InDegree Of Nodes:" + myGraph.inDegreePrint());
        System.out.println();
        System.out.println("OutDegree Of Nodes:" + myGraph.outDegreePrint());
        System.out.println();

        CloudLet startNode = myGraph.getStartNode();
        CloudLet endNode = myGraph.getEndNode();

        CriticalPath criticalPath=new CriticalPath();
        criticalPath.calculateCriticalPath(cloudLets,startNode,endNode);


        Test.main(args);

        myGraph.getGraphPath();
    }
}
