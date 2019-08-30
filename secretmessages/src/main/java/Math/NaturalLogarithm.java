//2.7182818284

package Math;

import static Math.Mathematics.pow;

public class NaturalLogarithm {

    //private double e = 2.7182818284;
    private double[] logList;
    private double[] logList2;

    public NaturalLogarithm() {

        logList = new double[Short.MAX_VALUE];
        logList2 = new double[Short.MAX_VALUE];
        logList[0] = 0.0;
        logList[1] = 0.0;
        logList[2] = 0.6931471805599453;
        logList2[0] = 0.0;
        logList2[1] = 0.0;
        logList2[2] = 0.6931471805599453;
        // ln[2] = 0,693147181

        long start = System.currentTimeMillis();
        for (int i = 3; i < logList.length; i++) {
            logList2[i] = ln(i);

        }
        System.out.println("natural with double in " + (System.currentTimeMillis() - start) + "ms");
        /*long start2 = System.currentTimeMillis();
        for (int i = 3; i < logList.length; i++) {
            logList[i] = logList[i - 1] + 2.0 * getAdditionToN(i);
        }
        System.out.println("natural with int in " + (System.currentTimeMillis() - start) + "ms");
*/

        for (int i = 1; i < 20; i++) {
            System.out.println("" + logList[i] + ", " + logList2[i] + ", " + Math.log((double) i));
        }

    }

    /*private double sigmaNotation(int n) {
        double res = 0;
        for (int k = 0; k < 100; k++) {
            res += (1.0 / (2.0 * k + 1.0)) * pow((n - 1.0) / (n + 1.0),(2 * k + 1));
        }
        return res * 2;
    }*/

    public static double ln(double n) {
        double res = 0;
        for (int k = 0; k < 100; k++) {
            res += (1.0 / (2.0 * k + 1.0)) * pow((n - 1.0) / (n + 1.0),(2 * k + 1));
        }
        return res * 2;
    }

    /*private double getAdditionToN(int n) {
        double res = 0.0;
        for (int k = 0; k < 100; k++) {
            res += (1.0 / (2.0 * (double) k + 1.0)) * pow(1.0 / (2.0 * (double) n + 1.0),(2 * k + 1));
        }
        return res;
    }*/
}