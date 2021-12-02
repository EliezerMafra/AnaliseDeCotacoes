import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final int numFiles = 196;
        final int papersQuant = 259;

        String filePathReader;
        String filePathWriter;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("../covarMatrixes/PapeisEliminados.csv"));

            FileWriter writer;

            ArrayList<String> namesDel = new ArrayList<>();
            ArrayList<Integer> posDel = new ArrayList<>();

            String row;

            reader.readLine();

            while((row = reader.readLine()) != null) {
                String[] line = row.split(",");

                namesDel.add(line[0]);
                posDel.add(Integer.valueOf(line[1]));
            }

            reader.close();

            for (int k = 0; k <= numFiles; k++) {
                //FILTRA MATRIZ COVAR
                filePathReader = "../covarMatrixes/"+k+".csv";
                filePathWriter = "../covarFiltrado/"+k+".csv";

                reader = new BufferedReader(new FileReader(filePathReader));
                writer = new FileWriter(filePathWriter);

                while ((row = reader.readLine()) != null){
                    String[] line = row.split(",");

                    List<String> list = new ArrayList<>(Arrays.asList(line));

                    int cont = 1;

                    for (int i = 0; i < posDel.size(); i++) {
                        list.remove(posDel.get(i) + cont--);
                    }

                    line = list.toArray(new String[0]);

                    if(!namesDel.contains(line[0])){
                        for (int i = 0; i < line.length; i++) {
                            writer.append(line[i]);
                            writer.append(",");
                        }
                        writer.append("\n");
                    }

                }

                reader.close();
                writer.close();

                //FILTRA RETORNO

                filePathReader = "../covarMatrixes/"+k+"_Retorno.csv";
                filePathWriter = "../covarFiltrado/"+k+"_Retorno.csv";

                reader = new BufferedReader(new FileReader(filePathReader));
                writer = new FileWriter(filePathWriter);

                while ((row = reader.readLine()) != null){
                    String[] line = row.split(",");

                    if(!namesDel.contains(line[0])){
                        for (int i = 0; i < line.length; i++) {
                            writer.append(line[i]);
                            writer.append(",");
                        }
                        writer.append("\n");
                    }

                }

                reader.close();
                writer.close();

                //FILTRA RETORNO FUTURO

                filePathReader = "../covarMatrixes/"+(k-1)+"_RetornoFuturo.csv";
                filePathWriter = "../covarFiltrado/"+(k-1)+"_RetornoFuturo.csv";

                reader = new BufferedReader(new FileReader(filePathReader));
                writer = new FileWriter(filePathWriter);

                while ((row = reader.readLine()) != null){
                    String[] line = row.split(",");

                    if(!namesDel.contains(line[0])){
                        for (int i = 0; i < line.length; i++) {
                            writer.append(line[i]);
                            writer.append(",");
                        }
                        writer.append("\n");
                    }

                }

                reader.close();
                writer.close();

                System.out.println("Arquivo " + k + " filtrado");


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
