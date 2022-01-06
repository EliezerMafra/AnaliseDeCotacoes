import java.io.*;
import java.util.ArrayList;

public class Main {
    public static final int startDay = 101;
    public static final int endDay = 196;

    public static void main(String[] args) throws Exception{

        ArrayList<Parameter> paramsReturn = new ArrayList<>();
        ArrayList<Parameter> paramsStD = new ArrayList<>();

        String filePathReader = "../paramsBestFutureReturnMean.csv";

        BufferedReader reader = new BufferedReader(new FileReader(filePathReader));

        String row;

        reader.readLine();

        String[] line;

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

        reader.readLine();

        while (((row = reader.readLine()) != null))
        {
            line = row.split(",");
            Parameter p = new Parameter(Integer.parseInt(line[0]), Double.parseDouble(line[1]), Double.parseDouble(line[2])
                    , Double.parseDouble(line[3]), Double.parseDouble(line[4]), Integer.parseInt(line[5]));

            paramsStD.add(p);
        }

        reader.close();

        filePathReader = "../covarFiltrado/0.csv";
        String[] names;
        int assetsNumber;

        reader = new BufferedReader(new FileReader(filePathReader));

        row = reader.readLine();

        line = row.split(",");

        names = new String[line.length - 1];


        System.arraycopy(line, 1, names, 0, line.length - 1);

        reader.close();

        assetsNumber = names.length;

        ArrayList<Parameter> paramList1 = new ArrayList<>(paramsReturn.subList(0, 7));
        ArrayList<Parameter> paramList2 = new ArrayList<>(paramsReturn.subList(7, 14));
        ArrayList<Parameter> paramList3 = new ArrayList<>(paramsReturn.subList(14, 21));
        ArrayList<Parameter> paramList4 = new ArrayList<>(paramsReturn.subList(21, 28));
        ArrayList<Parameter> paramList5 = new ArrayList<>(paramsReturn.subList(28, 35));
        ArrayList<Parameter> paramList6 = new ArrayList<>(paramsReturn.subList(35, 42));
        ArrayList<Parameter> paramList7 = new ArrayList<>(paramsReturn.subList(42, 50));

        ArrayList<ArrayList<Parameter>> listas = new ArrayList<>();

        listas.add(paramList1);
        listas.add(paramList2);
        listas.add(paramList3);
        listas.add(paramList4);
        listas.add(paramList5);
        listas.add(paramList6);
        listas.add(paramList7);

        for (ArrayList<Parameter> lista : listas) {
            Thread t = new Thread(() -> {
                String filePathReader1;
                BufferedReader reader1;
                String row1;
                String[] line1;
                int counter = 1;

                for (Parameter p : lista) {
                    try {
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

                            filePathReader1 = "../covarFiltrado/" + day + ".csv";


                            reader1 = new BufferedReader(new FileReader(filePathReader1));

                            row1 = reader1.readLine();

                            line1 = row1.split(",");

                            covarMatrix = new double[assetsNumber][assetsNumber];

                            for (int x = 0; (row1 = reader1.readLine()) != null; x++) {
                                line1 = row1.split(",");
                                for (int y = 0; y < assetsNumber; y++) {
                                    covarMatrix[x][y] = Double.valueOf(line1[y + 1]);
                                }
                            }

                            reader1.close();


                            filePathReader1 = "../covarFiltrado/" + day + "_RetornoFuturo.csv";
                            double[] futureReturn = new double[0];

                            reader1 = new BufferedReader(new FileReader(filePathReader1));


                            row1 = reader1.readLine();

                            line1 = row1.split(",");

                            futureReturn = new double[assetsNumber];

                            for (int x = 0; (row1 = reader1.readLine()) != null; x++) {
                                line1 = row1.split(",");
                                if ((!Double.isNaN(Double.valueOf(line1[2]))) && (!Double.isInfinite(Double.valueOf(line1[2]))))
                                    futureReturn[x] = Double.valueOf(line1[2]);
                                else
                                    futureReturn[x] = 0;

                            }

                            reader1.close();


                            filePathReader1 = "../covarFiltrado/" + day + "_Retorno.csv";
                            double[] pastReturn = new double[0];

                            reader1 = new BufferedReader(new FileReader(filePathReader1));

                            row1 = reader1.readLine();


                            pastReturn = new double[assetsNumber];

                            for (int x = 0; (row1 = reader1.readLine()) != null; x++) {
                                line1 = row1.split(",");
                                if ((!Double.isNaN(Double.valueOf(line1[2]))) && (!Double.isInfinite(Double.valueOf(line1[2]))))
                                    pastReturn[x] = Double.valueOf(line1[2]);
                                else
                                    pastReturn[x] = 0;
                            }

                            reader1.close();

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
                            writer.append("" + futureReturnSum);

                            for (int x = 0; x < bestInd.getChromossomeLength(); x++) {
                                writer.append("," + bestInd.getGene(x));
                            }

                            writer.close();


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    System.out.println("Parametro "+counter+" de "+paramsReturn.size()+"\t RETORNO");

                    counter++;
                }
            });

            t.start();
        }


        paramList1 = new ArrayList<>(paramsStD.subList(0, 7));
        paramList2 = new ArrayList<>(paramsStD.subList(7, 14));
        paramList3 = new ArrayList<>(paramsStD.subList(14, 21));
        paramList4 = new ArrayList<>(paramsStD.subList(21, 28));
        paramList5 = new ArrayList<>(paramsStD.subList(28, 35));
        paramList6 = new ArrayList<>(paramsStD.subList(35, 42));
        paramList7 = new ArrayList<>(paramsStD.subList(42, 50));

        listas.clear();

        listas.add(paramList1);
        listas.add(paramList2);
        listas.add(paramList3);
        listas.add(paramList4);
        listas.add(paramList5);
        listas.add(paramList6);
        listas.add(paramList7);

        for (ArrayList<Parameter> lista : listas) {
            Thread t = new Thread(() -> {
                String filePathReader12;
                BufferedReader reader12;
                String row12;
                String[] line12;
                int counter = 1;

                for (Parameter p : lista) {
                    try {
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

                            filePathReader12 = "../covarFiltrado/" + day + ".csv";


                            reader12 = new BufferedReader(new FileReader(filePathReader12));

                            row12 = reader12.readLine();

                            line12 = row12.split(",");

                            covarMatrix = new double[assetsNumber][assetsNumber];

                            for (int x = 0; (row12 = reader12.readLine()) != null; x++) {
                                line12 = row12.split(",");
                                for (int y = 0; y < assetsNumber; y++) {
                                    covarMatrix[x][y] = Double.valueOf(line12[y + 1]);
                                }
                            }

                            reader12.close();


                            filePathReader12 = "../covarFiltrado/" + day + "_RetornoFuturo.csv";
                            double[] futureReturn = new double[0];

                            reader12 = new BufferedReader(new FileReader(filePathReader12));


                            row12 = reader12.readLine();

                            line12 = row12.split(",");

                            futureReturn = new double[assetsNumber];

                            for (int x = 0; (row12 = reader12.readLine()) != null; x++) {
                                line12 = row12.split(",");
                                if ((!Double.isNaN(Double.valueOf(line12[2]))) && (!Double.isInfinite(Double.valueOf(line12[2]))))
                                    futureReturn[x] = Double.valueOf(line12[2]);
                                else
                                    futureReturn[x] = 0;

                            }

                            reader12.close();


                            filePathReader12 = "../covarFiltrado/" + day + "_Retorno.csv";
                            double[] pastReturn = new double[0];

                            reader12 = new BufferedReader(new FileReader(filePathReader12));

                            row12 = reader12.readLine();


                            pastReturn = new double[assetsNumber];

                            for (int x = 0; (row12 = reader12.readLine()) != null; x++) {
                                line12 = row12.split(",");
                                if ((!Double.isNaN(Double.valueOf(line12[2]))) && (!Double.isInfinite(Double.valueOf(line12[2]))))
                                    pastReturn[x] = Double.valueOf(line12[2]);
                                else
                                    pastReturn[x] = 0;
                            }

                            reader12.close();

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
                            writer.append("" + futureReturnSum);

                            for (int x = 0; x < bestInd.getChromossomeLength(); x++) {
                                writer.append("," + bestInd.getGene(x));
                            }

                            writer.close();

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    System.out.println("Parametro "+counter+" de "+paramsReturn.size()+"\t STD");

                    counter++;
                }
            });

            t.start();
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
