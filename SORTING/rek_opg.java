import java.util.Date;

public class rek_opg {
    public static void main(String[] args) {
        //System.out.println("Metode 3 (Math.pow): "+Math.pow(5, 11));
        //System.out.println("Metode 1: "+powMethod1(5, 11));
        //System.out.println("Metode 2: "+powMethod2(5, 11));

        //Kjører ikke samtidig, gir feil tidsmåling da

        //testMethod2(1.002, 50000);
        testMethod1(1.002, 50000);


    }

    public static double powMethod1(double x, int n){
        if(n == 1){return x;}
        return powMethod1(x, n-1) * x;
    }

    public static double powMethod2(double x, int n) {
        if (n == 1) {
            return x;
        }
        if ((n & 1) == 0) { //partall
            return powMethod2(x*x, n/2);
        } else { //oddetall
            return powMethod2(x*x, (n-1)/2) *x;
        }
    }

    public static void testMethod1(double x, int n){
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            double xn = powMethod1(x, n);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        System.out.println("Millisekund pr. runde M1:" + tid);

    }

    public static void testMethod2(double x, int n){
        Date start = new Date();
        int runder = 0;
        double tid;
        Date slutt;
        do {
            double xn = powMethod2(x, n);
            slutt = new Date();
            ++runder;
        } while (slutt.getTime()-start.getTime() < 1000);
        tid = (double)
                (slutt.getTime()-start.getTime()) / runder;
        System.out.println("Millisekund pr. runde M2:" + tid);

    }
}

