import java.util.Date;
import java.util.Random;

public class StockTrading {

    public static void main(String[] args) {
        // Generer tilfeldige kursendringer
        int[] changes = generateRandomStock(100000, -10, 10);
        //int[] changes = {-1, 3, -9, 2, 2, -1, 2, -1, -5, };


        int[] prices = generatePrices(changes);

        // Variabel for å holde resultatet
        int[] result = new int[3];

        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            result = findBestTrade(prices);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime() - start.getTime() < 1000);
        tid = (double) (slutt.getTime() - start.getTime()) / runder;

        // Print tid per runde
        System.out.println("Millisekund pr. runde: " + tid);

        // Print de beste kjøps- og salgsdagene og maksimal fortjeneste
        System.out.println("Best day to buy: " + result[0]);
        System.out.println("Best day to sell: " + result[1]);
    }

    // Funksjon for å finne maksimal fortjeneste
    public static int[] findBestTrade(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;
        int bestBuyDay = 0;
        int bestSellDay = 0;

        // Finn maksimal fortjeneste ved å iterere gjennom prisene
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] - minPrice > maxProfit) {
                maxProfit = prices[i] - minPrice;
                bestSellDay = i;
                bestBuyDay = getIndexOfPrice(prices, minPrice);
            }

            if (prices[i] < minPrice) {
                minPrice = prices[i];
            }
        }

        return new int[]{bestBuyDay, bestSellDay, maxProfit};
    }


    public static int[] generatePrices(int[] changes) {
        int[] prices = new int[changes.length + 1];
        prices[0] = 100; // Startprisen er 0

        for (int i = 1; i < prices.length; i++) {
            prices[i] = prices[i - 1] + changes[i - 1];
        }
        return prices;
    }

    private static int getIndexOfPrice(int[] prices, int price) {
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] == price) {
                return i;
            }
        }
        return -1; // Returner -1 hvis prisen ikke finnes (burde ikke skje i denne konteksten)
    }

    public static int[] generateRandomStock(int days, int minChange, int maxChange) {
        Random random = new Random();
        int[] changes = new int[days];

        for (int i = 0; i < days; i++) {
            changes[i] = random.nextInt((maxChange - minChange) + 1) + minChange;
        }

        return changes;
    }

}
