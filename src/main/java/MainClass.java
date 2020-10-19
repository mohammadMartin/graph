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

        @AllArgsConstructor
        @Getter
        @Setter
        class Person {
            String name;
            Integer age;
        }

        Person alex = new Person("Alex", 23);
        Person john = new Person("John", 40);
        Person peter = new Person("Peter", 32);
        List<Person> people = Arrays.asList(alex, john, peter);

        // then
        Person minByAge = people
                .stream()
                .min(Comparator.comparing(Person::getAge))
                .orElseThrow(NoSuchElementException::new);


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


        Test.main(args);

        myGraph.getGraphPath();
    }
}
