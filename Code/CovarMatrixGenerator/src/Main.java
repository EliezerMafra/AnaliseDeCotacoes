import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        //counter: conta as linhas que estão sendo analisadas
        /*refSave: referência para salvar os arquivos
        Exemplo:
        um arquivo de nome i.csv contendo a matriz de covariância e o arquivo i_retorno correspondem ao mesmo step
         */
        int counter = 0, refSave = 0;
        //papersQuant: quantidade de ativos que estão sendo analisados
        final int papersQuant = 259;
        //l: janela de tempo de cálculo
        final int l = 30;
        //delta: janela de tempo de salvamento, step
        final int delta = 7;

        //covarMatrix: matriz de covariâncias entre papeis
        double[][] covarMatrix = new double[papersQuant][papersQuant];
        //values: medias das cotações de cada papel na janela de tempo l
        double[][] values = new double[l][papersQuant];

        String[] names = new String[papersQuant];

        boolean[] hasStarted = new boolean[papersQuant];
        boolean[] eliminate = new boolean[papersQuant];

        //pathData: base de dados
        String pathData = "../dataOutput.csv";
        //folderSave: pasta de salvamento dos arquivos gerados
        String folderSave = "../covarMatrixes/";
        
        //csvReader: arquivo para leitura da base de Dados
        BufferedReader csvReader;

        /*
        auxSum: variavel auxiliar para soma das cotações dos papeis
        mean: media movel das cotações em um periodo l
        lastValue: valor da cotação anterior
        currentValue: valor da cotação atual
        auxSum: variavel auxiliar para soma das cotações dos papeis para calculo do retorno futuro
         */
        double[] mean = new double[papersQuant], lastValue,
                currentValue = new double[papersQuant], meanGran = new double[papersQuant],
                lastValueGran, currentValueGran = new double[papersQuant];

        //preenche mean e values com zeros
        Arrays.fill(mean,0.0);
        Arrays.fill(meanGran,0.0);
        for (double[] value:values) {
            Arrays.fill(value, 0.0);
        }
        Arrays.fill(hasStarted, false);
        Arrays.fill(eliminate, false);


        try {
            //leitura da base de dados
            csvReader = new BufferedReader(new FileReader(pathData));
            //row: String da linha que está sendo lida
            String row;
            //le a primeira linha
            row = csvReader.readLine();
            //names: guarda a primeira linha que contem os nomes dos papeis
            names = row.split(",");

            //usado para ignorar as duas primeiras colunas que possuem o timeStamp e a data
            System.arraycopy(names, 2, names, 0, 259);

            //loop de leitura de cada uma das linhas da base de dados
            while (((row = csvReader.readLine()) != null)) {
                //line: contem a linha que está sendo lida, separando pelas ','
                //cada posição de line é uma coluna da linha que esta sendo lida da base de dados
                String[] line = row.split(",");

                for (int i = 0; i < papersQuant; i++) {
                    if(Double.valueOf(line[i + 2]) != 0.0){
                        hasStarted[i] = true;
                    }
                }

                for (int i = 0; i < papersQuant; i++) {
                    if(hasStarted[i] && (line[i + 2].equals("0.0"))){
                        eliminate[i] = true;
                    }
                }

                //salva arquivos quando ja estiverem passados os primeiros 30 dias e com um step de delta
                if ((counter >= l)&&(counter % delta == 0)){
                    rotateBy1(papersQuant, l, values);
                    fillLastValue(papersQuant, l, values, line);

                    meanLCalc(papersQuant, l, values, mean);

                    meanGranCalc(papersQuant, l, delta, values, meanGran);

                    //lastValue pega os valores de currentValue
                    lastValue = currentValue.clone();

                    //currentValue pega os valores das medias
                    currentValue = mean.clone();

                    //lastValue pega os valores de currentValue
                    lastValueGran = currentValueGran.clone();

                    //currentValue pega os valores das medias
                    currentValueGran = meanGran.clone();

                    try {

                        covarMatrixCalc(papersQuant, l, covarMatrix, values, mean);

                        covarFileSave(String.valueOf(refSave), papersQuant, covarMatrix, folderSave, names);

                        returnFileSave(String.valueOf(refSave), papersQuant, folderSave, lastValue, currentValue, names);

                        futureReturnFileSave(String.valueOf(refSave - 1), papersQuant, folderSave, lastValueGran, currentValueGran, names);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //incremento da referencia de salvamento
                    refSave++;

                    System.out.println("Dia: "+line[1]+" calculado");
                }
                //se ainda não foram lidos os primeiros 30 dias
                //somente salva as cotações de cada papel
                else if (counter < l){
                    fillDay(counter, papersQuant, values, line);
                }
                //se já foram lidos os primeiros 30 dias e não está na janela de tempo de salvamento
                //somente rotaciona o vetor de cotações
                else{
                    rotateBy1(papersQuant, l, values);
                    fillLastValue(papersQuant, l, values, line);
                }

                //incremento de counter para se controlar os dias lidos
                counter++;

            }

            //fecha o arquivo da base de dados
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileWriter delete;

        try {
            delete = new FileWriter(folderSave+"PapeisEliminados.csv");
            delete.append("ativo, pos\n");
            for (int i = 0; i < papersQuant; i++) {
                if(eliminate[i]){
                    delete.append(names[i]+","+i+"\n");
                }
            }
            delete.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void fillDay(int counter, int papersQuant, double[][] values, String[] line) {
        for (int j = 0; j < papersQuant; j++) {
                values[counter][j] = Double.valueOf(line[j + 2]);
        }
    }

    private static void rotateBy1(int papersQuant, int l, double[][] values) {
        //rotação do vetor de cotações para cada papel, para calculo da media movel
        for (int i = 0; i < l - 1; i++) {
            for (int j = 0; j < papersQuant; j++) {
                values[i][j] = values[i + 1][j];
            }
        }
    }

    private static void futureReturnFileSave(String refSave, int papersQuant, String folderSave, double[] lastValueGran, double[] currentValueGran, String[] names) throws IOException {
        // dataReturn: arquivo dos retornos
        FileWriter dataFutureReturn = null;

        //dataReturn é instanciado para salvar o retorno passado
        dataFutureReturn = new FileWriter(folderSave +refSave+"_RetornoFuturo.csv");

        //define os nomes das colunas
        dataFutureReturn.append("ativo,");
        dataFutureReturn.append("retornoBruto,");
        dataFutureReturn.append("retornoPercentual,");
        dataFutureReturn.append("LN\n");


        //loop para calculo e salvamento dos retornos
        for (int j = 0; j < papersQuant; j++) {
            dataFutureReturn.append(names[j]);
            dataFutureReturn.append(",");
            //retorno bruto
            dataFutureReturn.append(String.valueOf(currentValueGran[j]- lastValueGran[j]));
            dataFutureReturn.append(",");
            //retorno percentual
            dataFutureReturn.append(String.valueOf((currentValueGran[j]/ lastValueGran[j])-1));
            dataFutureReturn.append(",");
            //log do retorno
            dataFutureReturn.append(String.valueOf(Math.log1p((currentValueGran[j]/ lastValueGran[j])-1))); //ESTÁ CERTO?????
            dataFutureReturn.append("\n");
        }

        //fecha arquivo do retorno
        dataFutureReturn.close();
    }

    private static void returnFileSave(String refSave, int papersQuant, String folderSave, double[] lastValue, double[] currentValue, String[] names) throws IOException {
        // dataReturn: arquivo dos retornos
        FileWriter dataReturn;

        //dataReturn é instanciado para salvar o retorno passado
        dataReturn = new FileWriter(folderSave + refSave +"_Retorno.csv");

        //define os nomes das colunas
        dataReturn.append("ativo,");
        dataReturn.append("retornoBruto,");
        dataReturn.append("retornoPercentual,");
        dataReturn.append("LN\n");

        double[][] assetsReturn = new double[papersQuant][3];

        calcReturn(papersQuant,lastValue,currentValue,assetsReturn);

        //loop para calculo e salvamento dos retornos
        for (int j = 0; j < papersQuant; j++) {
            dataReturn.append(names[j]);
            dataReturn.append(",");
            //retorno bruto
            dataReturn.append(String.valueOf(assetsReturn[j][0]));
            dataReturn.append(",");
            //retorno percentual
            dataReturn.append(String.valueOf(assetsReturn[j][1]));
            dataReturn.append(",");
            //log do retorno
            dataReturn.append(String.valueOf(assetsReturn[j][2])); //ESTÁ CERTO?????
            dataReturn.append("\n");
        }

        //fecha arquivo do retorno
        dataReturn.close();
    }

    private static void calcReturn( int papersQuant, double[] lastValue, double[] currentValue, double[][] assetsReturn){
        for (int i = 0; i < papersQuant; i++) {
            assetsReturn[i][0] = currentValue[i]-lastValue[i];
            assetsReturn[i][1] = (currentValue[i]/lastValue[i]) - 1;
            assetsReturn[i][2] = Math.log1p((currentValue[i]/ lastValue[i])-1);
        }
    }

    private static void covarFileSave(String refSave, int papersQuant, double[][] covarMatrix, String folderSave, String[] names) throws IOException {
        //dataCovar: arquivo da matriz de covariância
        FileWriter dataCovar;

        //DecimalFormat df = new DecimalFormat("#.####");

        //dataCovar é instanciado para salvar a matriz de covariância gerado
        dataCovar = new FileWriter(folderSave + refSave +".csv");

        //pula a primeira coluna
        dataCovar.append(" ,");

        //loop para escrita da primeira linha com os nomes dos papeis
        for (int j = 0; j < papersQuant; j++) {
            dataCovar.append(names[j]);
            dataCovar.append(",");
        }

        //pula para proxima linha
        dataCovar.append("\n");

        //loop para salvamento da matriz no arquivo
        for (int x = 0; x < papersQuant; x++) {
            //a primeira coluna sempre é o nome do papel na posição x correspondente
            dataCovar.append(names[x]);
            dataCovar.append(",");
            //loop para escrita de cada uma das covariâncias em covarMatrix
            for (int y = 0; y < papersQuant; y++) {
                //dataCovar.append(String.valueOf(df.format(covarMatrix[x][y])).replace(",","."));
                dataCovar.append(String.valueOf(covarMatrix[x][y]));
                dataCovar.append(",");
            }

            dataCovar.append("\n");
        }

        //sempre fecha o arquivo
        dataCovar.close();
    }

    private static void covarMatrixCalc(int papersQuant, int l, double[][] covarMatrix, double[][] values, double[] mean) {
        //calculo da matriz de covariância
        //loop que controla o ativo x
        //loop que controla o ativo y
        for (int x = 0; x < papersQuant; x++)
            for (int y = 0; y < papersQuant; y++) {
                //covarSum: para se calcular o somatorio da covariância
                double covarSum = 0;
                //loop que controla os dias, ou seja, a janela de calculo das covariancias
                for (int i = 0; i < l; i++) { //controla dias
                    covarSum += (values[i][x] - mean[x]) * (values[i][y] - mean[y]);
                }
                /*covariancia é calculada para papeis diferentes,
                 uma covariancia de um mesmo papel para com ele mesmo é sempre 0.0
                 */
                if (x != y)
                    covarMatrix[x][y] = covarSum / (Double.valueOf(l)-1);
                else
                    covarMatrix[x][y] = 0;
            }
    }

    private static void meanGranCalc(int papersQuant, int l, int gran, double[][] values, double[] meanGran) {

        double auxSum;
        //loop para calculo das medias dos valores na janela de gran para cada um dos papeis
        for (int j = 0; j < papersQuant; j++) {
            //preenche auxSum com zeros para calculo da próxima média
            auxSum = 0.0;
            for (int i = l - 1, k = 0; k < gran; i--, k++) {
                auxSum += values[i][j];
            }

            meanGran[j]= auxSum/ gran;
        }
    }

    private static void meanLCalc(int papersQuant, int l, double[][] values, double[] mean) {

        double auxSum;
        //loop para calculo das medias dos valores na janela de l para cada um dos papeis
        for (int j = 0; j < papersQuant; j++) {
            //preenche auxSum com zeros para calculo da próxima média
            auxSum = 0.0;
            for (int i = 0; i < l; i++) {
                auxSum += values[i][j];
            }

            mean[j]= auxSum/ l;
        }
    }

    private static void fillLastValue(int i, int size, double[][] values, String[] line) {
        //o vetor de valores foi rotacionado e é preciso salvar o valor atual
        //preenchimento do ultimo valor de cada papel
        for (int j = 0; j < i; j++) {
            values[size -1][j] = Double.parseDouble(line[j+2]);
        }
    }
}
