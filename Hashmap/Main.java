import java.util.HashMap;
import java.util.Random;

public class Main {
    // Begge oppgavene kjører under main her
    public static void main(String[] args) {

        //Deloppgave 1: Hashtabell med tekstnøkler
        hashWithStringKeys();

        System.out.println("\n\n"); //dobbel linjeskift for å skape litt avstand mellom oppgavene

        //Deloppgave 2: Hashtabeller med heltallsnøkler – og ytelse
        testPerformance();


    }

    public static int[] generateRandomIntegers(int count, int range) {
        Random random = new Random();
        int[] numbers = new int[count];

        for (int i = 0; i < count; i++) {
            numbers[i] = random.nextInt(range);
        }

        return numbers;
    }

    private static void testPerformance(){
        int tableSize =13_249_997; // primtlall nærme 13.5 mill (35% større enn 10mill). Brukte opprinnelig 13.499.999
        doubleHash hashTable = new doubleHash(tableSize);

        // 10mil int
        int[] randomNumbers = generateRandomIntegers(10_000_000, 100_000_000);



        // måle tid
        long startTime = System.currentTimeMillis();
        for (int num : randomNumbers) {
            hashTable.insert(num);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Custom HashTable: Time taken = " + (endTime - startTime) + " ms");
        System.out.println("Load factor = " + hashTable.loadFactor());
        System.out.println("Total collisions = " + hashTable.getTotalCollisions());
        System.out.println("--------------------------------");

        // Javas hashmap
        HashMap<Integer, Integer> hashMap = new HashMap<>(tableSize);
        long startTimeJava = System.currentTimeMillis();
        for (int num : randomNumbers) {
            hashMap.put(num, num);
        }
        long endTimeJava = System.currentTimeMillis();

        System.out.println("Java HashMap: Time taken = " + (endTimeJava - startTimeJava) + " ms");

    }

    private static void hashWithStringKeys(){
        int tableSize = 256;    //nærmeste 2er potens
        HashTable hashTable = new HashTable(tableSize);

        hashTable.readNamesFromFile("navn.txt");

        System.out.println("--------------------------------");
        hashTable.printStatistics();
        //hashTable.printAllLists();
        hashTable.lookup("Alper Yarenbasi");
        hashTable.lookup("Elper Yarenbasi");

        hashTable.printAllLists();

    }

}
