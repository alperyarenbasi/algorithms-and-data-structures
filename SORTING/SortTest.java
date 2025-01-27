import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class SortTest {

    // Sjekksumtest: Summer alle elementer i arrayet før og etter sortering
    public static boolean sjekksumTest(int[] original, int[] sorted) {
        int sumOriginal = Arrays.stream(original).sum();
        int sumSorted = Arrays.stream(sorted).sum();
        return sumOriginal == sumSorted;
    }

    // Rekkefølgetest: Sjekk at arrayet er sortert
    public static boolean rekkefolgeTest(int[] sorted) {
        for (int i = 0; i < sorted.length - 1; i++) { // Sjekk fra 0 til length - 1
            if (sorted[i + 1] < sorted[i]) { // Skal være < for at det skal være feil.
                return false;
            }
        }
        return true;
    }

    // Generer tilfeldige data
    public static int[] genererTilfeldigeData(int n) {
        Random rand = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = rand.nextInt(n);
        }
        return arr;
    }

    // Generer data med mange duplikater (annenhver lik)
    public static int[] genererDuplikater(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (i % 2 == 0) ? i / 2 : i / 2;
        }
        return arr;
    }

    // Generer sorterte data
    public static int[] genererSorterteData(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        return arr;
    }

    // Generer baklengs sorterte data
    public static int[] genererBaklengsSorterteData(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = n - i;
        }
        return arr;
    }


    public static void testQS(int[] data, String typeOfData){
        int[] original = Arrays.copyOf(data, data.length);
        System.out.println(typeOfData);

        //TID
        long startTime = System.nanoTime();
        Quicksort.quicksort(data, 0, data.length - 1);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Millisekunder
        System.out.println("sorteringstid: " + duration + " ms");


        if (sjekksumTest(original, data)) {
            System.out.println("Sjekksumtest bestått.");
        } else {
            System.out.println("Sjekksumtest FEILET.");
        }

        if (rekkefolgeTest(data)) {  // Endret til å teste sortert array
            System.out.println("Rekkefølgetest bestått.");
        } else {
            System.out.println("Rekkefølgetest FEILET.");
        }

        System.out.println(" ");
    }
    public static void testDPQS(int[] data, String typeOfData){
        int[] original = Arrays.copyOf(data, data.length);

        System.out.println(typeOfData);
        //TID
        long startTime = System.nanoTime();
        DualPivotQuickSort.dualPivotQuickSort(data, 0, data.length - 1);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Millisekunder
        System.out.println("sorteringstid: " + duration + " ms");


        if (sjekksumTest(original, data)) {
            System.out.println("Sjekksumtest bestått.");
        } else {
            System.out.println("Sjekksumtest FEILET.");
        }

        if (rekkefolgeTest(data)) {  // Endret til å teste sortert array
            System.out.println("Rekkefølgetest bestått.");
        } else {
            System.out.println("Rekkefølgetest FEILET.");
        }
        System.out.println(" ");

    }



    public static void main(String[] args) {
        int n = 50_000_000; // Mindre størrelse for enklere diagnostisering

        // Test med  data
        int[] tilfeldigData = genererBaklengsSorterteData(n);
        int[] dupliData = genererDuplikater(n);
        int[] sortertData = genererSorterteData(n);
        int[] baklengsSorterteData = genererBaklengsSorterteData(n);


        testQS(tilfeldigData, "Tilfeldig");
        testQS(dupliData, "dupliData");
        testQS(sortertData, "sortertData");
        testQS(baklengsSorterteData, "baklengsSorterteData");



        /*
        testDPQS(tilfeldigData, "Tilfeldig");
        testDPQS(dupliData, "dupliData");
        testDPQS(sortertData, "sortertData");
        testDPQS(baklengsSorterteData, "baklengsSorterteData");


         */








    }


}
