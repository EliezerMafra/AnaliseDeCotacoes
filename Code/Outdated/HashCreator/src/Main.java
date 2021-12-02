import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws IOException {

        double[][] coinsQuotes1D = Utils.getHash1D();

        String[] coinsNames = Utils.coinsInitializer();

        String path = "../dataOutput.csv";

        FileWriter csvWriter = null;

        //HashSet<Long> timestamps = Utils.getHashSet();

        try {
            csvWriter = new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < coinsNames.length; i++) {
            csvWriter.append(coinsNames[i]);

            for (int j = 0; j < 1409; j++) {
                csvWriter.append(',');
                csvWriter.append(String.valueOf(coinsQuotes1D[i][j]));
            }
            csvWriter.append('\n');
        }
        csvWriter.close();


    }
}
