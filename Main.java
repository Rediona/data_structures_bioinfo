
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("\nThis is an app that answers if " +
                "two users are connected in a network" +
                " (e.g if they are friends) \nand if they are " +
                "it calculates their distance.");

        System.out.println("\nPlease wait while a graph representation is being created from your input file\n ");

        long startTimeR = System.currentTimeMillis();

        int[] users = new int[2];// this is where user input will be stored
        int max = 0;// variable for finding max userId
        ArrayList<Pair> pairs = new ArrayList<>();//create array that will store user pairs
        ArrayList<Pair> reversePairs = new ArrayList<>();//create array that will store user pairs in reverse order
        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj); // create scanner obj to grab .txt file
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.startsWith("#")) { // discard lines that begin with '#'
                    String[] s = data.split("\t"); // split lines in start node and end node
                    int start = Integer.parseInt(s[0].trim()); // transform string to int
                    int end = Integer.parseInt(s[1].trim());
                    if (start > end) {//this is for finding max userId
                        if (start > max)
                            max = start;
                    } else if (end > max) {
                        max = end;
                    }
                    Pair p = new Pair(start, end); // create pair using Pair class
                    Pair reverseP = new Pair(end, start); // create reverse pair
                    insertWithOrder(pairs,p);
                    insertWithOrder(reversePairs,reverseP);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        ArrayList<Integer> edges = createEdgesArray(pairs); // use function to create edges array
        ArrayList<Integer> reverseEdges = createEdgesArray(reversePairs);

        ArrayList<Integer> vertices = createVerticesArray(max, pairs); // use function to create vertices array
        ArrayList<Integer> reverseVertices = createVerticesArray(max, reversePairs);

        long endTimeR = System.currentTimeMillis();

        System.out.println("Time spent on graph representation: " +((endTimeR-startTimeR)/1000)+" seconds\n");

        String answer = "y";
        Scanner scanner = new Scanner(System.in);
        while (answer.equals("y")) {

            for (int i = 0; i <= 1; i++) {
                while (true) {
                    System.out.println("Please enter user no" + (i + 1) + " (integer up to " + max + ",zero included!): ");
                    boolean hasNextInt = scanner.hasNextInt(); //handle character input instead of int
                    if (hasNextInt) {
                        users[i] = scanner.nextInt();
                        if (users[i] >= 0 && users[i] <= (vertices.size() - 1)) {
                            break;
                        } else {
                            System.out.println("Input must be between 0 and " + max + "!");
                        }
                    } else
                        System.out.println("Input must be an integer!");
                    scanner.nextLine(); // handle next line character
                }
            }

            System.out.println("BFS results: ");
            long startTime1 = System.currentTimeMillis();
            int BFS = breadthFirstSearch(vertices, edges, users[0], users[1]); // BFS implementation
            long endTime1 = System.currentTimeMillis();
            if (BFS != 0) {
                System.out.println("Users " + users[0] + " and " + users[1] + " are connected. Their distance is: " + BFS);
            } else
                System.out.println("Users not connected");
            System.out.println("Search time was: " + (endTime1 - startTime1) + " milliseconds");

            System.out.println("Bidirectional BFS results: ");
            long startTime2 = System.currentTimeMillis();
            int biDirBFS = biDirBFS(vertices, edges, reverseVertices, reverseEdges, users[0], users[1]);
            long endTime2 = System.currentTimeMillis();
            if (biDirBFS != 0) {
                System.out.println("Users " + users[0] + " and " + users[1] + " are connected. Their distance is: " + biDirBFS);
            } else
                System.out.println("Users not connected");
            System.out.println("Search time was: " + (endTime2 - startTime2) + " milliseconds");
            System.out.println("\nPress \"y\" to continue search or anything else to exit.");
            Scanner scannerNew = new Scanner(System.in);
            answer = scannerNew.next();

        }
    }

    public static void insertWithOrder(ArrayList<Pair> myList, Pair x){

        int lo=0;
        int mid;
        int hi = myList.size();

        if (hi!=0){
            while (lo < hi){
                mid = (lo+hi)/2;
                if (x.getStartNode() < myList.get(mid).getStartNode()){
                    hi = mid;
                }else
                    lo = mid+1;
            }
            myList.add(lo, x);
        }else
            myList.add(x);
    }

    // create and return edges array function (must be used AFTER pairs array has been sorted!)
    public static ArrayList<Integer> createEdgesArray(ArrayList<Pair> pairs) {
        ArrayList<Integer> edges = new ArrayList<>();
        for (int i = 0; i < pairs.size(); i++) { // edges array size equals pairs array size
            edges.add(i, pairs.get(i).getEndNode()); // elements are end users of all pairs
        }
        return edges;
    }

    // function that returns vertices array
    public static ArrayList<Integer> createVerticesArray(int max, ArrayList<Pair> pairs) {
        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(0); // first element is zero because that is the index in edges array it points to
        int cnt = 0; // this counter is used for calculation of neighbours of every user
        for (int i = 0; i <= max; i++) { // vertices size must be equal to max userId
            for (int j = 0; j < pairs.size(); j++) {
                if (pairs.get(j).getStartNode() == i)
                    j = cnt++;
            }
            vertices.add(cnt);
        }
        vertices.add(cnt + 1);
        return vertices;
    }

    public static int breadthFirstSearch(ArrayList<Integer> vertices, ArrayList<Integer> edges, int start, int end) {

        int[] distance = new int[vertices.size()];// array to store distances
        Arrays.fill(distance, -1);//initialize all values to -1
        Queue<Integer> verticesQueue = new LinkedList<>();// create Queue

        verticesQueue.add(start); // add start user
        distance[start] = 0;

        while (!verticesQueue.isEmpty()) { // while queue is not empty
            int parent = verticesQueue.peek(); // store head of queue
            int indexFirstNeighbor = vertices.get(parent);
            int indexLastNeighbor = vertices.get(parent + 1);
            int numberOfNeighbors = indexLastNeighbor - indexFirstNeighbor;
            if (numberOfNeighbors != 0) { // if a user has no neighbours go to next one
                for (int i = 0; i < numberOfNeighbors; i++) {
                    if (distance[edges.get(indexFirstNeighbor + i)] == -1) {//if neighbor's distance is not equal to -1, he has been visited
                        distance[edges.get(indexFirstNeighbor + i)] = distance[parent] + 1; // increment distance from parent
                        verticesQueue.add(edges.get(indexFirstNeighbor + i)); // add neighbor to queue
                        if (edges.get(indexFirstNeighbor + i) == end) { //if end user is found return his distance
                            return distance[end];
                        }
                    }
                }
            }
            verticesQueue.remove();//remove head of queue
        }
        return 0;
    }

    public static int biDirBFS(ArrayList<Integer> vertices1, ArrayList<Integer> edges1,
                               ArrayList<Integer> vertices2, ArrayList<Integer> edges2, int start, int end) {
        int[] sourceDistance = new int[vertices1.size()];
        int[] targetDistance = new int[vertices2.size()];
        Arrays.fill(sourceDistance, -1);
        Arrays.fill(targetDistance, -1);
        Queue<Integer> sourceQueue = new LinkedList<>();
        Queue<Integer> targetQueue = new LinkedList<>();

        sourceQueue.add(start);
        targetQueue.add(end);
        sourceDistance[start] = 0;
        targetDistance[end] = 0;

        while (!sourceQueue.isEmpty() && !targetQueue.isEmpty()) {
            int parent1 = sourceQueue.peek();
            int indexFirstNeighbor1 = vertices1.get(parent1);
            int indexLastNeighbor1 = vertices1.get(parent1 + 1);
            int numberOfNeighbors1 = indexLastNeighbor1 - indexFirstNeighbor1;

            if (numberOfNeighbors1 != 0) {
                for (int i = 0; i < numberOfNeighbors1; i++) {
                    if (sourceDistance[edges1.get(indexFirstNeighbor1 + i)] == -1) {
                        sourceDistance[edges1.get(indexFirstNeighbor1 + i)] = (sourceDistance[parent1] + 1);
                        sourceQueue.add(edges1.get(indexFirstNeighbor1 + i));
                        if (targetDistance[edges1.get(indexFirstNeighbor1 + i)] != -1) {//check if node has been visited by the opposite search front
                            return (sourceDistance[edges1.get(indexFirstNeighbor1 + i)] +//if node has been visited return the sum of its distances
                                    targetDistance[edges1.get(indexFirstNeighbor1 + i)]);
                        }
                    }
                }
            }
            sourceQueue.remove();

            int parent2 = targetQueue.peek();
            int indexFirstNeighbor2 = vertices2.get(parent2);
            int indexLastNeighbor2 = vertices2.get(parent2 + 1);
            int numberOfNeighbors2 = indexLastNeighbor2 - indexFirstNeighbor2;

            if (numberOfNeighbors2 != 0) {
                for (int i = 0; i < numberOfNeighbors2; i++) {
                    if (targetDistance[edges2.get(indexFirstNeighbor2 + i)] == -1) {
                        targetDistance[edges2.get(indexFirstNeighbor2 + i)] = (targetDistance[parent2] + 1);
                        targetQueue.add(edges2.get(indexFirstNeighbor2 + i));
                        if (sourceDistance[edges2.get(indexFirstNeighbor2 + i)] != -1) {//check if node has been visited by the opposite search front
                            return (targetDistance[edges2.get(indexFirstNeighbor2 + i)] +//if node has been visited return the sum of its distances
                                    sourceDistance[edges2.get(indexFirstNeighbor2 + i)]);
                        }
                    }
                }
            }
            targetQueue.remove();
        }
        return 0;
    }
}
