import java.io.*;
import java.util.Scanner;

public class HashTable {
    private LinkedListOwn[] table;
    private int size;
    private int totalElements = 0;
    private int totalCollisions = 0;

    // Constructor
    public HashTable(int size) {
        this.size = size;
        table = new LinkedListOwn[size];
        for (int i = 0; i < size; i++) {
            table[i] = new LinkedListOwn();
        }
    }

    private int stringHash(String s) {
        int hash = 0;
        int prime = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = (prime * hash + s.charAt(i)) % size;
        }
        return hash;
    }

    public void insert(String name) {
        int index = stringHash(name);
        LinkedListOwn list = table[index];

        if (list.contains(name)) {
            System.out.println("Name already exists: " + name);
            return;
        }


        if (list.getNumberOfElements() > 0) {
            totalCollisions++;
            System.out.println("Collision: " + name + " at index " + index);
        }

        list.insertBack(name);
        totalElements++;
    }

    public void readNamesFromFile(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            while (fileScanner.hasNextLine()) {
                String name = fileScanner.nextLine();
                insert(name);  // Insert each name into the hash table
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void printStatistics() {
        double loadFactor = (double) totalElements / size;
        double avgCollisionsPerPerson = (double) totalCollisions / totalElements;
        System.out.println("Load factor: " + loadFactor);
        System.out.println("Total collisions: " + totalCollisions);
        System.out.println("Average collisions per person: " + avgCollisionsPerPerson);
    }

    public void printAllLists() {
        for (int i = 0; i < size; i++) {
            System.out.print("Index " + i + ": ");
            table[i].printList();
        }
    }
    public void lookup(String name) {
        int index = stringHash(name);
        LinkedListOwn list = table[index];
        System.out.println("HashTable contains " + name +": "+ list.contains(name));
    }

}
