public class DualPivotQuickSort {

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void dualPivotQuickSort(int[] arr, int low, int high) {
        if (low < high) {

            swap(arr, low, low + (high - low) / 3);
            swap(arr, high, high - (high - low) / 3);


            int[] piv;
            piv = partition(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }

    static int[] partition(int[] arr, int low, int high) {
        if (arr[low] > arr[high])
            swap(arr, low, high);

        // p er venstre pivot, q er høyre pivot
        int j = low + 1;
        int g = high - 1, k = low + 1;
        int p = arr[low], q = arr[high];

        while (k <= g) {
            // Hvis elementet er mindre enn venstre pivot
            if (arr[k] < p) {
                swap(arr, k, j);
                j++;
            }

            // Hvis elementet er større enn eller lik høyre pivot
            else if (arr[k] >= q) {
                while (arr[g] > q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (arr[k] < p) {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        swap(arr, low, j);
        swap(arr, high, g);

        // Sjekker om pivoter er like for å unngå unødvendig rekursjon
        if (arr[j] == arr[g]) {
            return new int[] { j, g }; // Rekursjon på midterste segment unngås
        }

        // Returnerer indekser til pivotene
        return new int[] { j, g };
    }

    public static void main(String[] args) {
        int[] arr = {15, 22, 73, 89, 46, 5, 87, 14, 99, 34, 21, 55, 67, 12, 30, 85, 44, 18, 93, 60, 77, 28, 41, 63, 90, 54, 32, 79, 11, 50};

        dualPivotQuickSort(arr, 0, arr.length - 1);

        System.out.print("Sortert array: ");
        for (int i = 0; i < arr.length; i++)
            System.out.print(arr[i] + " ");

        System.out.println();
    }
}
