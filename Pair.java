
public class Pair {
    int startNode,endNode;

    public Pair(int startNode, int endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(int endNode) {
        this.endNode = endNode;
    }

    public void printPair(){ // this function is for testing purposes only
        System.out.println(getStartNode()+"-->"+getEndNode());
    }
}
