public class Quicksort {
    public static void main(String[] args) {
    // Testing av algoritmen
        int[] array = {24, 8, 42, 75, 29, 77, 38, 57, 33, 5, 1, 0, 4, 9, 9 };
        quicksort(array, 0, array.length - 1);
        System.out.println("Sortert array: ");
        for (int i : array) {
            System.out.print(i + " ");
        }
    }

    private static void swap(int[] t, int i, int j) {
        int temp = t[i];
        t[i] = t[j];
        t[j] = temp;
    }

    private static int median3sort(int[] t, int v, int h) {
        int m = (v + h) / 2;
        if (t[v] > t[m]) swap(t, v, m);
        if (t[m] > t[h]) {
            swap(t, m, h);
            if (t[v] > t[m]) swap(t, v, m);
        }
        return m;
    }

    public static void quicksort(int[] t, int v, int h) {
        if (h - v > 2) {
            int delepos = splitt(t, v, h);
            quicksort(t, v, delepos - 1);
            quicksort(t, delepos + 1, h);
        } else {
            median3sort(t, v, h);
        }
    }

    private static int splitt(int[] t, int v, int h) {
        int iv, ih;
        int m = median3sort(t, v, h);
        int dv = t[m];
        swap(t, m, h - 1);
        for (iv = v, ih = h - 1;;) {
            while (t[++iv] < dv);
            while (t[--ih] > dv);
            if (iv >= ih) break;
            swap(t, iv, ih);
        }
        swap(t, iv, h - 1);
        return iv;
    }
}
