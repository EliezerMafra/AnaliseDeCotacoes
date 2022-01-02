import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;

public class Main {
    public static final int startDay = 101;
    public static final int endDay = 197;

    public static void main(String[] args) throws Exception{

        ArrayList<Parameter> paramsReturn = new ArrayList<Parameter>();
        ArrayList<Parameter> paramsStD = new ArrayList<Parameter>();

        String filePathReader = "../paramsBestFutureReturnMean.csv";

        BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

        String row;

        row = reader.readLine();

        String[] line = row.split(",");

        while (((row = reader.readLine()) != null))
        {
            line = row.split(",");
            Parameter p = new Parameter(Integer.parseInt(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2])
                    , Double.parseDouble(line[3]), Double.parseDouble(line[4]), Integer.parseInt(line[5]));

            paramsReturn.add(p);
        }

        reader.close();

        filePathReader = "../paramsBestStdDeviation.csv";

        reader = new BufferedReader(new FileReader(filePathReader));

        row = reader.readLine();

        line = row.split(",");

        while (((row = reader.readLine()) != null))
        {
            line = row.split(",");
            Parameter p = new Parameter(Integer.parseInt(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2])
                    , Double.parseDouble(line[3]), Double.parseDouble(line[4]), Integer.parseInt(line[5]));

            paramsStD.add(p);
        }

        reader.close();

        filePathReader = "../covarFiltrado/0.csv";
        String[] names = new String[0];
        int assetsNumber;

        reader = new BufferedReader(new FileReader(filePathReader));

        row = reader.readLine();

        line = row.split(",");

        names = new String[line.length - 1];


        for (int i = 1; i < line.length; i++) {
            names[i - 1] = line[i];
        }

        reader.close();

        assetsNumber = names.length;

        int counter = 1;

        for (Parameter p : paramsReturn) {
            int elitismCount = (int) (p.getPopulationSize() * p.getElitismCountperCent());

            String filePathWriter = "../OutputExecucaoRetorno/PopSize=" + p.getPopulationSize() + "_MutGene=" + p.getMutationProbGene()
                    + "_MutInd=" + p.getMutationProbIndividual() + "_Crossover=" + p.getCrossoverRate() + "_Elit=" + p.getElitismCountperCent() 
                    + "_Gen=" + p.getNumberOfGenerations() + ".csv";

            try {
                FileWriter writer = new FileWriter(filePathWriter, true);

                for (int x = 0; x < fillFirstLine().length; x++) {
                    writer.append(fillFirstLine()[x] + ",");
                }

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int day = startDay; day < endDay; day++) {

                double[][] covarMatrix = new double[0][0];

                filePathReader = "../covarFiltrado/" + day + ".csv";


                reader = new BufferedReader(new FileReader(filePathReader));

                row = reader.readLine();

                line = row.split(",");

                covarMatrix = new double[assetsNumber][assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    for (int y = 0; y < assetsNumber; y++) {
                        covarMatrix[x][y] = Double.valueOf(line[y + 1]);
                    }
                }

                reader.close();


                filePathReader = "../covarFiltrado/" + day + "_RetornoFuturo.csv";
                double[] futureReturn = new double[0];

                reader = new BufferedReader(new FileReader(filePathReader));


                row = reader.readLine();

                line = row.split(",");

                futureReturn = new double[assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    if ((!Double.isNaN(Double.valueOf(line[2]))) && (!Double.isInfinite(Double.valueOf(line[2]))))
                        futureReturn[x] = Double.valueOf(line[2]);
                    else
                        futureReturn[x] = 0;

                }

                reader.close();


                filePathReader = "../covarFiltrado/" + day + "_Retorno.csv";
                double[] pastReturn = new double[0];

                reader = new BufferedReader(new FileReader(filePathReader));

                row = reader.readLine();


                pastReturn = new double[assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    if ((!Double.isNaN(Double.valueOf(line[2]))) && (!Double.isInfinite(Double.valueOf(line[2]))))
                        pastReturn[x] = Double.valueOf(line[2]);
                    else
                        pastReturn[x] = 0;
                }

                reader.close();

                //GA------------------------------------------
                GeneticAlgorithm ga = new GeneticAlgorithm(p.getPopulationSize(), p.getMutationProbGene(), p.getMutationProbIndividual(), p.getCrossoverRate(), elitismCount, p.getNumberOfGenerations());

                Population population = ga.initPopulation(assetsNumber, covarMatrix);

                ga.evalPopulation(population);

                while (!ga.isTerminationConditionMet(population)) {
                    population = ga.crossover(population);
                    population = ga.mutation(population);
                    ga.evalPopulation(population);
                }
                //----------------------------------------------

                double weightDummie = 1.0 / assetsNumber;

                float[] weightsDummie = new float[assetsNumber];

                double pastReturnDummieSum = 0;

                for (int x = 0; x < assetsNumber; x++) {
                    pastReturnDummieSum += weightDummie * pastReturn[x];
                }

                double futureReturnDummieSum = 0;

                for (int x = 0; x < assetsNumber; x++) {
                    futureReturnDummieSum += weightDummie * futureReturn[x];
                    weightsDummie[x] = (float) weightDummie;
                }

                double fitnessDummie = ga.calcFitness(new Individual(weightsDummie), covarMatrix);

                Individual bestInd = population.getFittest(0);
                double futureReturnSum = 0;

                for (int i = 0; i < futureReturn.length; i++) {
                    futureReturnSum += bestInd.getGene(i) * futureReturn[i];
                }

                double pastReturnSum = 0;

                for (int i = 0; i < pastReturn.length; i++) {
                    pastReturnSum += bestInd.getGene(i) * pastReturn[i];
                }

                FileWriter writer = new FileWriter(filePathWriter, true);

                writer.append("\n");

                writer.append(day + ",");
                writer.append(fitnessDummie + ",");
                writer.append(pastReturnDummieSum + ",");
                writer.append(futureReturnDummieSum + ",");
                writer.append(bestInd.getFitness() + ",");
                writer.append(pastReturnSum + ",");
                writer.append(""+futureReturnSum);

                for (int x = 0; x < bestInd.getChromossomeLength(); x++) {
                    writer.append("," + bestInd.getGene(x));
                }

                writer.close();

            }

            System.out.println("Parametro "+counter+" de "+paramsReturn.size()+"\t RETORNO");

            counter++;
        }

        counter = 1;
        for (Parameter p : paramsStD) {
            int elitismCount = (int) (p.getPopulationSize() * p.getElitismCountperCent());

            String filePathWriter = "../OutputExecucaoStD/PopSize=" + p.getPopulationSize() + "_MutGene=" + p.getMutationProbGene()
                    + "_MutInd=" + p.getMutationProbIndividual() + "_Crossover=" + p.getCrossoverRate() + "_Elit=" + p.getElitismCountperCent()
                    + "_Gen=" + p.getNumberOfGenerations() + ".csv";

            try {
                FileWriter writer = new FileWriter(filePathWriter, true);

                for (int x = 0; x < fillFirstLine().length; x++) {
                    writer.append(fillFirstLine()[x] + ",");
                }

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int day = startDay; day < endDay; day++) {

                double[][] covarMatrix = new double[0][0];

                filePathReader = "../covarFiltrado/" + day + ".csv";


                reader = new BufferedReader(new FileReader(filePathReader));

                row = reader.readLine();

                line = row.split(",");

                covarMatrix = new double[assetsNumber][assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    for (int y = 0; y < assetsNumber; y++) {
                        covarMatrix[x][y] = Double.valueOf(line[y + 1]);
                    }
                }

                reader.close();


                filePathReader = "../covarFiltrado/" + day + "_RetornoFuturo.csv";
                double[] futureReturn = new double[0];

                reader = new BufferedReader(new FileReader(filePathReader));


                row = reader.readLine();

                line = row.split(",");

                futureReturn = new double[assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    if ((!Double.isNaN(Double.valueOf(line[2]))) && (!Double.isInfinite(Double.valueOf(line[2]))))
                        futureReturn[x] = Double.valueOf(line[2]);
                    else
                        futureReturn[x] = 0;

                }

                reader.close();


                filePathReader = "../covarFiltrado/" + day + "_Retorno.csv";
                double[] pastReturn = new double[0];

                reader = new BufferedReader(new FileReader(filePathReader));

                row = reader.readLine();


                pastReturn = new double[assetsNumber];

                for (int x = 0; (row = reader.readLine()) != null; x++) {
                    line = row.split(",");
                    if ((!Double.isNaN(Double.valueOf(line[2]))) && (!Double.isInfinite(Double.valueOf(line[2]))))
                        pastReturn[x] = Double.valueOf(line[2]);
                    else
                        pastReturn[x] = 0;
                }

                reader.close();

                //GA------------------------------------------
                GeneticAlgorithm ga = new GeneticAlgorithm(p.getPopulationSize(), p.getMutationProbGene(), p.getMutationProbIndividual(), p.getCrossoverRate(), elitismCount, p.getNumberOfGenerations());

                Population population = ga.initPopulation(assetsNumber, covarMatrix);

                ga.evalPopulation(population);

                while (!ga.isTerminationConditionMet(population)) {
                    population = ga.crossover(population);
                    population = ga.mutation(population);
                    ga.evalPopulation(population);
                }
                //----------------------------------------------

                double weightDummie = 1.0 / assetsNumber;

                float[] weightsDummie = new float[assetsNumber];

                double pastReturnDummieSum = 0;

                for (int x = 0; x < assetsNumber; x++) {
                    pastReturnDummieSum += weightDummie * pastReturn[x];
                }

                double futureReturnDummieSum = 0;

                for (int x = 0; x < assetsNumber; x++) {
                    futureReturnDummieSum += weightDummie * futureReturn[x];
                    weightsDummie[x] = (float) weightDummie;
                }

                double fitnessDummie = ga.calcFitness(new Individual(weightsDummie), covarMatrix);

                Individual bestInd = population.getFittest(0);
                double futureReturnSum = 0;

                for (int i = 0; i < futureReturn.length; i++) {
                    futureReturnSum += bestInd.getGene(i) * futureReturn[i];
                }

                double pastReturnSum = 0;

                for (int i = 0; i < pastReturn.length; i++) {
                    pastReturnSum += bestInd.getGene(i) * pastReturn[i];
                }

                FileWriter writer = new FileWriter(filePathWriter, true);

                writer.append("\n");

                writer.append(day + ",");
                writer.append(fitnessDummie + ",");
                writer.append(pastReturnDummieSum + ",");
                writer.append(futureReturnDummieSum + ",");
                writer.append(bestInd.getFitness() + ",");
                writer.append(pastReturnSum + ",");
                writer.append(""+futureReturnSum);

                for (int x = 0; x < bestInd.getChromossomeLength(); x++) {
                    writer.append("," + bestInd.getGene(x));
                }

                writer.close();

            }

            System.out.println("Parametro "+counter+" de "+paramsReturn.size()+"\t STD");

            counter++;
        }
    }







    public final static String[] fillFirstLine() {

        return new String[]
                {
                        "Day",
                        "FitnessDummie",
                        "PastReturnDummie",
                        "FutureReturnDummie",
                        "BestFitness",
                        "PastReturn",
                        "FutureReturn",
                        "w1",
                        "w2",
                        "w3",
                        "w4",
                        "w5",
                        "w6",
                        "w7",
                        "w8",
                        "w9",
                        "w10",
                        "w11",
                        "w12",
                        "w13",
                        "w14",
                        "w15",
                        "w16",
                        "w17",
                        "w18",
                        "w19",
                        "w20",
                        "w21",
                        "w22",
                        "w23",
                        "w24",
                        "w25",
                        "w26",
                        "w27",
                        "w28",
                        "w29",
                        "w30",
                        "w31",
                        "w32",
                        "w33",
                        "w34",
                        "w35",
                        "w36",
                        "w37",
                        "w38",
                        "w39",
                        "w40",
                        "w41",
                        "w42",
                        "w43",
                        "w44",
                        "w45",
                        "w46",
                        "w47",
                        "w48",
                        "w49",
                        "w50",
                        "w51",
                        "w52",
                        "w53",
                        "w54",
                        "w55",
                        "w56",
                        "w57",
                        "w58",
                        "w59",
                        "w60",
                        "w61",
                        "w62",
                        "w63",
                        "w64",
                        "w65",
                        "w66",
                        "w67",
                        "w68",
                        "w69",
                        "w70",
                        "w71",
                        "w72",
                        "w73",
                        "w74",
                        "w75",
                        "w76",
                        "w77",
                        "w78",
                        "w79",
                        "w80",
                        "w81",
                        "w82",
                        "w83",
                        "w84",
                        "w85",
                        "w86",
                        "w87",
                        "w88",
                        "w89",
                        "w90",
                        "w91",
                        "w92",
                        "w93",
                        "w94",
                        "w95",
                        "w96",
                        "w97",
                        "w98",
                        "w99",
                        "w100",
                        "w101",
                        "w102",
                        "w103",
                        "w104",
                        "w105",
                        "w106",
                        "w107",
                        "w108",
                        "w109",
                        "w110",
                        "w111",
                        "w112",
                        "w113",
                        "w114",
                        "w115",
                        "w116",
                        "w117",
                        "w118",
                        "w119",
                        "w120",
                        "w121",
                        "w122",
                        "w123",
                        "w124",
                        "w125",
                        "w126",
                        "w127",
                        "w128",
                        "w129",
                        "w130",
                        "w131",
                        "w132",
                        "w133",
                        "w134",
                        "w135",
                        "w136",
                        "w137",
                        "w138",
                        "w139",
                        "w140",
                        "w141",
                        "w142",
                        "w143",
                        "w144",
                        "w145",
                        "w146",
                        "w147",
                        "w148",
                        "w149",
                        "w150",
                        "w151",
                        "w152",
                        "w153",
                        "w154",
                        "w155",
                        "w156",
                        "w157",
                        "w158",
                        "w159",
                        "w160",
                        "w161",
                        "w162",
                        "w163",
                        "w164",
                        "w165",
                        "w166",
                        "w167",
                        "w168",
                        "w169",
                        "w170",
                        "w171",
                        "w172",
                        "w173",
                        "w174",
                        "w175",
                        "w176",
                        "w177"
                };

    }
}
