import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

        csvWriter.append("timestamp");
        csvWriter.append(',');

        csvWriter.append("Date");
        csvWriter.append(',');

        for (int j = 0; j < coinsNames.length; j++) {
            csvWriter.append(coinsNames[j]);
            if (j < coinsNames.length - 1)
                csvWriter.append(',');
        }

        csvWriter.append('\n');

        int timestampCounter = 0;
        List<Long> timestamps = Utils.getSortedTimestamps();



        for (int i = 0; i < 1409; i++) {
            csvWriter.append(String.valueOf(timestamps.get(timestampCounter)));

            csvWriter.append(',');

            Date dateAux = new Date(timestamps.get(timestampCounter));

            LocalDate local = dateAux.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            csvWriter.append(String.valueOf(local.getYear()+"-"+ local.getMonthValue()+"-"+local.getDayOfMonth()));

            csvWriter.append(',');

            timestampCounter++;

            for (int j = 0; j < coinsNames.length; j++) {
                csvWriter.append(String.valueOf(coinsQuotes1D[j][i]));
                if (j < coinsNames.length - 1) {
                    csvWriter.append(',');
                }
            }
            csvWriter.append('\n');
        }
        csvWriter.close();
    }


}

