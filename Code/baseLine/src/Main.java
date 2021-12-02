import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
	    int numberOfAssets = 177;
	    int k = 181;

	    double percent = 1/(double) numberOfAssets;

        String filePathReader = "../covarFiltrado/"+k+"_RetornoFuturo.csv";
        double[] futureReturn = new double[0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

            String row;

            row = reader.readLine();

            String[] line = row.split(",");

            futureReturn = new double[numberOfAssets];

            for(int i = 0; (row = reader.readLine()) !=  null; i++){
                line = row.split(",");
                futureReturn[i] = Double.valueOf(line[2]);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        double realReturn = 0;
        for (int i = 0; i < numberOfAssets; i++) {
            realReturn += percent * futureReturn[i];
        }

        System.out.println(realReturn);
    }
}
