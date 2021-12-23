import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        final int[] populationSize = {1000, 1500};
        final double[] mutationProbGene = {0.02, 0.03};
        final double[] mutationProbIndividual = {0.2, 0.3, 0.4};
        final double[] crossoverRate = {0.3, 0.4, 0.5, 0.6};
        final double[] elitismCountperCent = {0.2, 0.3}; //taxa
        final int[] numberOfGenerations = {500, 1000};
        final int startDay = 80;
        final int endDay = 100;

        final int numberOfDays = endDay - startDay;

        int elitismCount;

        String filePathReader = "../covarFiltrado/0.csv";
        String[] names = new String[0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

            String row;

            row = reader.readLine();

            String[] line = row.split(",");

            names = new String[line.length - 1];


            for (int i = 1; i < line.length; i++) {
                names[i - 1] = line[i];
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        int sizeTableLines = numberOfDays*populationSize.length * mutationProbGene.length * mutationProbIndividual.length * crossoverRate.length * elitismCountperCent.length * numberOfGenerations.length;
        int sizeTableColumns = names.length + 10;

        String[][] table = new String[sizeTableLines][sizeTableColumns];

        table[0] = fillFirstLine();

        int tableLine = 1;

        for (int day = startDay; day < endDay; day++) {

            double[][] covarMatrix = new double[0][0];

            filePathReader = "../covarFiltrado/" + day + ".csv";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

                String row;

                row = reader.readLine();

                String[] line = row.split(",");

                covarMatrix = new double[names.length][names.length];

                for (int i = 0; (row = reader.readLine()) != null; i++) {
                    line = row.split(",");
                    for (int j = 0; j < names.length; j++) {
                        covarMatrix[i][j] = Double.valueOf(line[j + 1]);
                    }
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            filePathReader = "../covarFiltrado/" + day + "_RetornoFuturo.csv";
            double[] futureReturn = new double[0];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

                String row;

                row = reader.readLine();

                String[] line = row.split(",");

                futureReturn = new double[names.length];

                for (int i = 0; (row = reader.readLine()) != null; i++) {
                    line = row.split(",");
                    futureReturn[i] = Double.valueOf(line[2]);
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            filePathReader = "../covarFiltrado/" + day + "_Retorno.csv";
            double[] pastReturn = new double[0];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

                String row;

                row = reader.readLine();

                String[] line;

                pastReturn = new double[names.length];

                for (int i = 0; (row = reader.readLine()) != null; i++) {
                    line = row.split(",");
                    pastReturn[i] = Double.valueOf(line[2]);
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < populationSize.length; i++) {
                for (int j = 0; j < mutationProbGene.length; j++) {
                    for (int l = 0; l < mutationProbIndividual.length; l++) {
                        for (int m = 0; m < crossoverRate.length; m++) {
                            for (int n = 0; n < elitismCountperCent.length; n++) {
                                for (int o = 0; o < numberOfGenerations.length; o++) {
                                    elitismCount = (int) (populationSize[i] * elitismCountperCent[n]);

                                    //GA------------------------------------------
                                    GeneticAlgorithm ga = new GeneticAlgorithm(populationSize[i], mutationProbGene[j], mutationProbIndividual[l], crossoverRate[m], elitismCount, numberOfGenerations[o]);

                                    Population population = ga.initPopulation(names.length, covarMatrix);

                                    ga.evalPopulation(population);

                                    while (!ga.isTerminationConditionMet(population)) {
                                        population = ga.crossover(population);
                                        population = ga.mutation(population);
                                        ga.evalPopulation(population);
                                    }
                                    //----------------------------------------------

                                    Individual bestInd = population.getFittest(0);
                                    double futureReturnSum = 0;

                                    for (int p = 0; p < futureReturn.length; p++) {
                                        if((!Double.isNaN(futureReturn[p])) && (!Double.isInfinite(futureReturn[p])))
                                            futureReturnSum += bestInd.getGene(p) * futureReturn[p];
                                    }

                                    double pastReturnSum = 0;

                                    for (int p = 0; p < pastReturn.length; p++) {
                                        if((!Double.isNaN(pastReturn[p])) && (!Double.isInfinite(pastReturn[p])))
                                            pastReturnSum += bestInd.getGene(p) * pastReturn[p];
                                    }

                                    table[tableLine][0] = String.valueOf(day);
                                    table[tableLine][1] = String.valueOf(populationSize[i]);
                                    table[tableLine][2] = String.valueOf(mutationProbGene[j]);
                                    table[tableLine][3] = String.valueOf(mutationProbIndividual[l]);
                                    table[tableLine][4] = String.valueOf(crossoverRate[m]);
                                    table[tableLine][5] = String.valueOf(elitismCountperCent[n]);
                                    table[tableLine][6] = String.valueOf(numberOfGenerations[o]);
                                    table[tableLine][7] = String.valueOf(pastReturnSum);
                                    table[tableLine][8] = String.valueOf(bestInd.getFitness());
                                    table[tableLine][9] = String.valueOf(futureReturnSum);

                                    String filePathWriter = "../tableAG_SUPERDAY.csv";

                                    for (int k = 10, p = 0; k < names.length + 10; k++, p++) {
                                        table[tableLine][k] = String.valueOf(bestInd.getGene(p));
                                    }

                                    try {
                                        FileWriter writer = new FileWriter(filePathWriter);

                                        for (int a = 0; a < sizeTableLines; a++) {
                                            for (int b = 0; b < sizeTableColumns; b++) {
                                                writer.append(table[a][b]+",");
                                            }

                                            writer.append("\n");
                                        }

                                        writer.close();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("Dia: "+day+"\t"+tableLine + " Completo de " + sizeTableLines/numberOfDays);

                                    tableLine++;
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Dia " + day + " de " + numberOfDays + " completo---------------------------------------------");
        }
    }

    public static String[] fillFirstLine(){

        return new String[]
                {
                        "Day",
                        "PopSize",
                        "MutGene",
                        "MutInd",
                        "Crossover",
                        "Elite",
                        "Generations",
                        "PastReturn",
                        "BestFitness",
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
