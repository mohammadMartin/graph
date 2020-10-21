import algorithm2.CriticalPath;
import graph.CloudLet;
import graph.MyGraph;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import minmin.Test;

import java.util.*;

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

        CriticalPath criticalPath = new CriticalPath();
        criticalPath.calculateCriticalPath(cloudLets, startNode, endNode);


        myGraph.getGraphPath();
    }
}
