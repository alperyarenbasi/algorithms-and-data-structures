public class doubleHash {
    private int[] table;
    private int size;
    private int totalElements = 0;
    private int totalCollisions = 0;

    public doubleHash(int size) {
        this.size = size;
        table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = -1;  // -1 for ledig plass
        }
    }


    private int h1(int key) {
        return key % size;
    }

    //Fra bokas/forelesning
    private int h2(int key) {
        return 1 + (key % (size - 1));
    }


    // Tra ikke hensyn til at tabell er full.
    public void insert(int key) {
        int index = h1(key);
        if (table[index] == -1) {
            table[index] = key;
            totalElements++;
            return;
        }

        int stepSize = h2(key);
        totalCollisions++;

        while (table[index] != -1) {
            index = (index + stepSize) % size;
        }

        table[index] = key;
        totalElements++;
    }



    public double loadFactor() {
        return (double) totalElements / size;
    }

    public int getTotalCollisions() {
        return totalCollisions;
    }

    //kun for meg selv til å visuelt debugge (bruker da lav size på hash og tall)
    public void printTable() {
        for(int num : table) {
            System.out.print(num + "  ");
        }
    }


}
