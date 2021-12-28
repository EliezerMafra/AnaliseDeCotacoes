import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Main {
    public static final int[] populationSize = {1000, 1500};
    public static final double[] mutationProbGene = {0.02, 0.03};
    public static final double[] mutationProbIndividual = {0.2, 0.3, 0.4};
    public static final double[] crossoverRate = {0.3, 0.4, 0.5, 0.6};
    public static final double[] elitismCountperCent = {0.2, 0.3}; //taxa
    public static final int[] numberOfGenerations = {500, 1000};
    public static final int startDay = 80;
    public static final int endDay = 100;
    public static final int bestSelector = 50;

    public static void main(String[] args) throws Exception{
                final int numberOfDays = endDay - startDay;

        int numberOfFiles = populationSize.length * mutationProbGene.length * mutationProbIndividual.length
                * crossoverRate.length * elitismCountperCent.length * numberOfGenerations.length;

        ArrayList<Parameter> parameters = new ArrayList<>();

        int countFile = 0;

        for (int i = 0; i < populationSize.length; i++)
        {
            for (int j = 0; j < mutationProbGene.length; j++)
            {
                for (int k = 0; k < mutationProbIndividual.length; k++)
                {
                    for (int l = 0; l < crossoverRate.length; l++)
                    {
                        for (int m = 0; m < elitismCountperCent.length; m++)
                        {
                            for (int n = 0; n < numberOfGenerations.length; n++)
                            {

                                Parameter p = new Parameter(populationSize[i], mutationProbGene[j], mutationProbIndividual[k]
                                        , crossoverRate[l], elitismCountperCent[m], numberOfGenerations[n]);

                                String fileReaderPath = "../ConfParam/PopSize=" + populationSize[i] + "_MutGene=" + mutationProbGene[j]
                                        + "_MutInd=" + mutationProbIndividual[k] + "_Crossover=" + crossoverRate[l] + "_Elit=" + elitismCountperCent[m]
                                        + "_Gen=" + numberOfGenerations[n] + ".csv";

                                BufferedReader reader = new BufferedReader(new FileReader(fileReaderPath));

                                reader.readLine();

                                String row;

                                String[] line;

                                double futureReturnSum = 0;

                                double[] futureReturn = new double[numberOfDays];

                                int countDays = 0;

                                while ((row = reader.readLine()) != null)
                                {
                                    line = row.split(",");
                                    futureReturn[countDays] = Double.parseDouble(line[6]);
                                    futureReturnSum += Double.parseDouble(line[6]);

                                    countDays++;
                                }

                                reader.close();

                                p.setFutureReturnMean(futureReturnSum / numberOfDays);

                                double stdDeviationSum = 0;

                                for (int o = 0; o < futureReturn.length; o++) {
                                    stdDeviationSum += Math.pow(futureReturn[o] - p.getFutureReturnMean(), 2);
                                }

                                p.setStdDeviation(Math.sqrt(stdDeviationSum / futureReturn.length));

                                double maxReturn = Arrays.stream(futureReturn).max().getAsDouble();
                                double minReturn = Arrays.stream(futureReturn).min().getAsDouble();

                                p.setMaxDrawDown(minReturn);

                                parameters.add(p);
                            }
                        }
                    }
                }
            }
        }

        Collections.sort(parameters, ((o1, o2) -> {
            if (o1.getFutureReturnMean() > o2.getFutureReturnMean())
                return -1;
            else
                return 1;
        }));

        ArrayList<Parameter> bestFutureReturnMean = new ArrayList<Parameter>(parameters.subList(0,bestSelector));

        Collections.sort(parameters, ((o1, o2) -> {
            if (o1.getStdDeviation() < o2.getStdDeviation())
                return -1;
            else
                return 1;
        }));

        ArrayList<Parameter> bestStdDeviation = new ArrayList<Parameter>(parameters.subList(0,bestSelector));


        BufferedWriter writer = new BufferedWriter(new FileWriter("../paramsBestFutureReturnMean.csv"));

        writer.append("popSize,mutGen,mutInd,cross,elit,nGenerations\n");

        for (Parameter p: bestFutureReturnMean) {
            writer.append(p.getPopSize()+","+p.getMutGen()+","+p.getMutInd()+","+p.getCross()+","+p.getElit()+","+p.getGens()+"\n");
        }

        writer.close();

        writer = new BufferedWriter(new FileWriter("../paramsBestStdDeviation.csv"));

        writer.append("popSize,mutGen,mutInd,cross,elit,nGenerations\n");

        for (Parameter p: bestStdDeviation) {
            writer.append(p.getPopSize()+","+p.getMutGen()+","+p.getMutInd()+","+p.getCross()+","+p.getElit()+","+p.getGens()+"\n");
        }

        writer.close();



    }
}
