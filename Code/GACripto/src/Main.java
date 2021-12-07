import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        final int k[] = {181};
        final int[] populationSize = {500,1000,2000};
        final double[] mutationProbGene = {0.01, 0.03, 0.05, 0.08, 0.1, 0.2, 0.3, 0.5};
        final double[] mutationProbIndividual = {0.05, 0.1, 0.15, 0.3, 0.5, 0.8};
        final double[] crossoverRate = {0.1, 0.2, 0.3, 0.6, 0.8, 0.9};
        final double[] elitismCountperCent = {0.1, 0.15, 0.2, 0.5, 0.8}; //taxa
        final int[] numberOfGenerations = {200, 500, 1000};

        int elitismCount;

        int sizeTable = populationSize.length*mutationProbGene.length*mutationProbIndividual.length*crossoverRate.length*elitismCountperCent.length*numberOfGenerations.length;

        String[][] table = new String[sizeTable + 1][8];

        table[0] = new String[]{"PopSize", "MutationProbgene", "MutationProbIndividual", "CrossoverProb", "ElitismProb", "NumberOfGenerations", "BestFitness", "RealReturn"};

        //Carregamento dos arquivos----------------------------------

        String filePathReader = "../covarFiltrado/"+k[0]+".csv";
        String[] names = new String[0];

        double[][] covarMatrix = new double[0][0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

            String row;

            row = reader.readLine();

            String[] line = row.split(",");

            names = new String[line.length - 1];

            covarMatrix = new double[names.length][names.length];

            for (int i = 1; i < line.length; i++) {
                names[i - 1] = line[i];
            }

            for(int i = 0; (row = reader.readLine()) !=  null; i++){
                line = row.split(",");
                for (int j = 0; j < names.length; j++) {
                    covarMatrix[i][j] = Double.valueOf(line[j + 1]);
                }
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        filePathReader = "../covarFiltrado/"+k[0]+"_RetornoFuturo.csv";
        double[] futureReturn = new double[0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

            String row;

            row = reader.readLine();

            String[] line = row.split(",");

            futureReturn = new double[names.length];

            for(int i = 0; (row = reader.readLine()) !=  null; i++){
                line = row.split(",");
                futureReturn[i] = Double.valueOf(line[2]);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        int tableLine = 1;

        for (int i = 0; i < populationSize.length; i++) {
            for (int j = 0; j < mutationProbGene.length; j++) {
                for (int l = 0; l < mutationProbIndividual.length; l++) {
                    for (int m = 0; m < crossoverRate.length; m++) {
                        for (int n = 0; n < elitismCountperCent.length; n++) {
                            for (int o = 0; o < numberOfGenerations.length; o++) {
                                 elitismCount = (int) (populationSize[i] * elitismCountperCent[n]);

                                //GA------------------------------------------

                                //System.out.println("populationSize: " +populationSize[i]);
                                //System.out.println("mutationProbGene: " +mutationProbGene[j]);
                                //System.out.println("mutationProbIndividual: " +mutationProbIndividual[l]);
                                //System.out.println("crossoverRate: " +crossoverRate[m]);
                                //System.out.println("elitismCountperCent: " +elitismCountperCent[n]);
                                //System.out.println("numberOfGenerations: " +numberOfGenerations[o]);

                                GeneticAlgorithm ga = new GeneticAlgorithm(populationSize[i], mutationProbGene[j], mutationProbIndividual[l], crossoverRate[m], elitismCount, numberOfGenerations[o]);

                                Population population = ga.initPopulation(names.length, covarMatrix);

                                ga.evalPopulation(population);

                                while (!ga.isTerminationConditionMet(population)){
                                    population = ga.crossover(population);
                                    population = ga.mutation(population);
                                    ga.evalPopulation(population);
                                    //System.out.println(population.getGeneration() + "\t"+population.getFittest(0).getFitness());
                                }

                                //System.out.println("Best Fitness: "+population.getFittest(0).getFitness());

                                Individual bestInd = population.getFittest(0);
                                double realReturn = 0;

                                for (int p = 0; p < futureReturn.length; p++) {
                                    realReturn += bestInd.getGene(p) * futureReturn[p];
                                }

                                //System.out.println("RealReturn: "+realReturn);


                                table[tableLine][0] = String.valueOf(populationSize[i]);
                                table[tableLine][1] = String.valueOf(mutationProbGene[j]);
                                table[tableLine][2] = String.valueOf(mutationProbIndividual[l]);
                                table[tableLine][3] = String.valueOf(crossoverRate[m]);
                                table[tableLine][4] = String.valueOf(elitismCountperCent[n]);
                                table[tableLine][5] = String.valueOf(numberOfGenerations[o]);
                                table[tableLine][6] = String.valueOf(bestInd.getFitness());
                                table[tableLine][7] = String.valueOf(realReturn);

                                tableLine++;

                                String filePathWriter = "../tableAG_SUPER.csv";

                                try {
                                    FileWriter writer = new FileWriter(filePathWriter);

                                    for (int a = 0; a < sizeTable + 1; a++) {
                                        for (int b = 0; b < 8; b++) {
                                            writer.append(table[a][b]);
                                            writer.append(",");
                                        }
                                        writer.append("\n");
                                    }

                                    writer.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println(tableLine + " Completo de \t" + sizeTable);

                            }
                            }
                        }
                    }
                }
            }
        }


}
