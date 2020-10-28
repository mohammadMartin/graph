import algorithm2.CriticalPath;
import graph.CloudLet;
import graph.CloudLetGraph;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MainClass {
    public static void main(String[] args) {
        CloudLetGraph cloudLetGraph = new CloudLetGraph();
        List<CloudLet> cloudLets = cloudLetGraph.produceGraph();
        System.out.println("The current graph:" + cloudLetGraph);
        System.out.println();
        System.out.println("InDegree Of Nodes:" + cloudLetGraph.inDegreePrint());
        System.out.println();
        System.out.println("OutDegree Of Nodes:" + cloudLetGraph.outDegreePrint());
        System.out.println();

        CloudLet startNode = cloudLetGraph.getStartNode();
        CloudLet endNode = cloudLetGraph.getEndNode();

        CriticalPath criticalPath = new CriticalPath();
        criticalPath.calculateCriticalPath(cloudLets, startNode, endNode);


        cloudLetGraph.getGraphPath();
    }
}
