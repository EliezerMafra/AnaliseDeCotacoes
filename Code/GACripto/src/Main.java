import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        //final int k[] = {181};
        final int populationSize = 500;
        final double mutationProbGene = 0.05;
        final double mutationProbIndividual = 0.1;
        final double crossoverRate = 0.3;
        final double elitismCountperCent = 0.2; //taxa
        final int numberOfGenerations = 200;

        int elitismCount = (int) (populationSize * elitismCountperCent);


        int numberOfAssets = 177;

        double percent = 1/(double) numberOfAssets;

        float chro[] = new float[numberOfAssets];

        for (int gene = 0; gene < numberOfAssets; gene++) {
            chro[gene] = (float) percent;
        }

        Individual dummie = new Individual(chro);

        for (int k = 0; k < 191; k++) {
            String filePathReader = "../covarFiltrado/"+k+".csv";
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

            filePathReader = "../covarFiltrado/"+k+"_RetornoFuturo.csv";
            double[] futureReturns = new double[0];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

                String row;

                row = reader.readLine();

                String[] line = row.split(",");

                futureReturns = new double[names.length];

                for(int i = 0; (row = reader.readLine()) !=  null; i++){
                    line = row.split(",");
                    if(!(line[2].equals("NaN") || line[2].equals("Infinity")))
                        futureReturns[i] = Double.valueOf(line[2]);
                    else
                        futureReturns[i] = 0;
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            filePathReader = "../covarFiltrado/"+k+"_Retorno.csv";
            double[] pastReturns = new double[0];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

                String row;

                row = reader.readLine();

                String[] line = row.split(",");

                pastReturns = new double[names.length];

                for(int i = 0; (row = reader.readLine()) !=  null; i++){
                    line = row.split(",");
                    if(!(line[2].equals("NaN") || line[2].equals("Infinity")))
                        pastReturns[i] = Double.valueOf(line[2]);
                    else
                        pastReturns[i] = 0;
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //GA------------------------------------------

            GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, mutationProbGene, mutationProbIndividual, crossoverRate, elitismCount, numberOfGenerations);

            Population population = ga.initPopulation(names.length, covarMatrix);

            ga.evalPopulation(population);

            while (!ga.isTerminationConditionMet(population)){
                population = ga.crossover(population);
                population = ga.mutation(population);
                ga.evalPopulation(population);
            }

            Individual bestInd = population.getFittest(0);

            float bestFitness = bestInd.getFitness();

            double futureReturn = 0;

            for (int p = 0; p < futureReturns.length; p++) {
                futureReturn += bestInd.getGene(p) * futureReturns[p];
            }
            double pastReturn = 0;

            for (int p = 0; p < pastReturns.length; p++) {
                pastReturn += bestInd.getGene(p) * pastReturns[p];
            }


            GeneticAlgorithm aux = new GeneticAlgorithm();

            float fitnessDummie = (float) aux.calcFitness(dummie, covarMatrix);

            double futureReturnDummie = 0;

            for (int p = 0; p < futureReturns.length; p++) {
                futureReturnDummie += dummie.getGene(p) * futureReturns[p];
            }
            double pastReturnDummie = 0;

            for (int p = 0; p < pastReturns.length; p++) {
                pastReturnDummie += dummie.getGene(p) * pastReturns[p];
            }

            /*System.out.println("Fitness Dummie: " + fitnessDummie);
            System.out.println("Retorno Futuro Dummie: " + futureReturnDummie);
            System.out.println("Retorno Passado Dummie: " + pastReturnDummie);
            System.out.println("Best Fitness: " + bestFitness);
            System.out.println("Retorno Futuro: " + futureReturn);
            System.out.println("Retorno Passado: " + pastReturn);*/

            String filePathWriter = "../analiseTodoDia.csv";

            try {
                FileWriter writer = new FileWriter(filePathWriter, true);

                writer.append("\n");
                writer.append(k+","+fitnessDummie+","+futureReturnDummie+","+pastReturnDummie+","+bestFitness+","+futureReturn+","+pastReturn);

                writer.close();


            }catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Dia "+k+" completo");




        }





    }




}
