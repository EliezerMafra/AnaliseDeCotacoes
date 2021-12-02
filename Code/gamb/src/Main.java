import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {

        String filePathReader = "../sout3.txt";

        final int combNumber = 11;

        String table[][] = new String[combNumber][8];

        table[0][0] = "populationSize";
        table[0][1] = "mutationProbGene";
        table[0][2] = "mutationProbIndividual";
        table[0][3] = "crossoverRate";
        table[0][4] = "elitismCountperCent";
        table[0][5] = "numberOfGenerations";
        table[0][6] = "Best Fitness";
        table[0][7] = "RealReturn";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

            String row;

            String[] line;

            int counter = 1;

            for(int i = 0; (row = reader.readLine()) !=  null; i++){
                line = row.split(": ");

                if (line[0].equals("---------------------------------------------------")){
                    counter++;
                }else if(line[0].equals("populationSize")){
                    table[counter][0] = line[1];
                }else if(line[0].equals("mutationProbGene")){
                    table[counter][1] = line[1];
                }else if(line[0].equals("mutationProbIndividual")){
                    table[counter][2] = line[1];
                }else if(line[0].equals("crossoverRate")){
                    table[counter][3] = line[1];
                }else if(line[0].equals("elitismCountperCent")){
                    table[counter][4] = line[1];
                }else if(line[0].equals("numberOfGenerations")){
                    table[counter][5] = line[1];
                }else if(line[0].equals("Best Fitness")){
                    table[counter][6] = line[1];
                }else if(line[0].equals("RealReturn")){
                    table[counter][7] = line[1];
                }


            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String filePathWriter = "../tableAG3.csv";

        try {
            FileWriter writer = new FileWriter(filePathWriter);

            for (int i = 0; i < combNumber; i++) {
                for (int j = 0; j < 8; j++) {
                    writer.append(String.valueOf(table[i][j]));
                    writer.append(",");
                }
                writer.append("\n");
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
