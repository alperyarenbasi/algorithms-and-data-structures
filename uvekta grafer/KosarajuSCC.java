import java.io.*;
import java.util.*;

class Kant {
    Kant neste;
    Node til;
    public Kant(Node til, Kant neste) {
        this.til = til;
        this.neste = neste;
    }
}

class Node {
    Kant kant1;
    int id;
    public Node(int id) {
        this.id = id;
    }
}

class Graf {
    int N;
    Node[] node;

    public Graf(int N) {
        this.N = N;
        node = new Node[N];
        for (int i = 0; i < N; i++) {
            node[i] = new Node(i);
        }
    }

    public void addEdge(int fra, int til) {
        Kant k = new Kant(node[til], node[fra].kant1);
        node[fra].kant1 = k; // Legg til kanten i noden
    }

    public void dfs(Node n, boolean[] visited, Stack<Node> stack) {
        visited[n.id] = true;
        for (Kant k = n.kant1; k != null; k = k.neste) {
            if (!visited[k.til.id]) {
                dfs(k.til, visited, stack);
            }
        }
        stack.push(n);
    }

    // DFS på omvendt graf for å finne sterkt sammenhengende komponenter
    public void reverseDfs(Node n, boolean[] visited, List<Node> component) {
        visited[n.id] = true;
        component.add(n);
        for (Kant k = n.kant1; k != null; k = k.neste) {
            if (!visited[k.til.id]) {
                reverseDfs(k.til, visited, component);
            }
        }
    }

    // Funksjon for å transponere grafen (lage omvendt graf)
    public Graf transpose() {
        Graf reverseGraph = new Graf(N);
        for (int i = 0; i < N; i++) {
            for (Kant k = node[i].kant1; k != null; k = k.neste) {
                reverseGraph.addEdge(k.til.id, i); // Reverser kantene
            }
        }
        return reverseGraph;
    }

    // Kosaraju's algoritme for å finne sterkt sammenhengende komponenter
    public List<List<Node>> findSCCs() {
        Stack<Node> stack = new Stack<>();
        boolean[] visited = new boolean[N];

        // Kjør DFS på originalgrafen og finn ferdig-tidene
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                dfs(node[i], visited, stack);
            }
        }

        // Lag omvendt graf (transponert graf)
        Graf reverseGraph = transpose();

        // Kjør DFS på den transponerte grafen i sortert rekkefølge
        Arrays.fill(visited, false);
        List<List<Node>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            Node v = stack.pop();
            if (!visited[v.id]) {
                List<Node> component = new ArrayList<>();
                reverseGraph.reverseDfs(reverseGraph.node[v.id], visited, component);
                components.add(component);
            }
        }
        return components;
    }
}

public class KosarajuSCC {
    // Funksjon for å lese inn en graf fra fil. Opprinnelig kode hentet fra Boka med noen endringer
    public static Graf readGraphFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] input = br.readLine().trim().split("\\s+");
        int N = Integer.parseInt(input[0]);
        int K = Integer.parseInt(input[1]);

        Graf graph = new Graf(N);
        for (int i = 0; i < K; i++) {
            input = br.readLine().trim().split("\\s+");
            int fra = Integer.parseInt(input[0]);
            int til = Integer.parseInt(input[1]);
            graph.addEdge(fra, til);
        }
        br.close();
        return graph;
    }

    public static void main(String[] args) throws IOException {
        String[] files = {"ø5g1.txt", "ø5g6.txt", "ø5g2.txt", "ø5g5.txt"};

        for (String file : files) {
            System.out.println("Løser for fil: " + file);
            Graf g = readGraphFromFile(file);
            List<List<Node>> components = g.findSCCs();
            System.out.println("Grafen " + file + " har " + components.size() + " sterkt sammenhengende komponenter.");
            int componentNumber = 1;
            for (List<Node> component : components) {
                System.out.print("Komponent " + componentNumber + ": ");
                for (Node n : component) {
                    System.out.print(n.id + " ");
                }
                System.out.println();
                componentNumber++;
            }
            System.out.println("\n \n");
        }
    }
}
