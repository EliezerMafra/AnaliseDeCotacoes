import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Utils {

    public static String[] coinsInitializer(){

        String[] coins = {"1INCHUSDT",
                "AAVEUSDT",
                "ADAUSDC",
                "ADAUSDT",
                "AIONUSDT",
                "AKROUSDT",
                "ALGOUSDC",
                "ALGOUSDT",
                "ALPHAUSDT",
                "ANKRUSDC",
                "ANKRUSDT",
                "ANTUSDT",
                "ARDRUSDT",
                "ARPAUSDT",
                "ASRUSDT",
                "ATMUSDT",
                "ATOMUSDC",
                "ATOMUSDT",
                "AUDIOUSDT",
                "AUDUSDT",
                "AVAUSDT",
                "AVAXUSDT",
                "AXSUSDT",
                "BALUSDT",
                "BANDUSDT",
                "BATUSDC",
                "BATUSDT",
                "BCCUSDT",
                "BCHABCUSDC",
                "BCHABCUSDT",
                "BCHSVUSDC",
                "BCHSVUSDT",
                "BCHUSDC",
                "BCHUSDT",
                "BCPTUSDC",
                "BEAMUSDT",
                "BEARUSDT",
                "BELUSDT",
                "BGBPUSDC",
                "BKRWUSDT",
                "BLZUSDT",
                "BNBBEARUSDT",
                "BNBBULLUSDT",
                "BNBUSDC",
                "BNBUSDT",
                "BNTUSDT",
                "BTCSTUSDT",
                "BTCUSDC",
                "BTCUSDT",
                "BTSUSDT",
                "BTTUSDC",
                "BTTUSDT",
                "BULLUSDT",
                "BUSDUSDT",
                "BZRXUSDT",
                "CELOUSDT",
                "CELRUSDT",
                "CHRUSDT",
                "CHZUSDT",
                "CKBUSDT",
                "COCOSUSDT",
                "COMPUSDT",
                "COSUSDT",
                "COTIUSDT",
                "CRVUSDT",
                "CTKUSDT",
                "CTSIUSDT",
                "CTXCUSDT",
                "CVCUSDT",
                "DAIUSDT",
                "DASHUSDT",
                "DATAUSDT",
                "DCRUSDT",
                "DENTUSDT",
                "DGBUSDT",
                "DIAUSDT",
                "DNTUSDT",
                "DOCKUSDT",
                "DOGEUSDC",
                "DOGEUSDT",
                "DOTUSDT",
                "DREPUSDT",
                "DUSKUSDC",
                "DUSKUSDT",
                "EGLDUSDT",
                "ENJUSDT",
                "EOSBEARUSDT",
                "EOSBULLUSDT",
                "EOSUSDC",
                "EOSUSDT",
                "ERDUSDC",
                "ERDUSDT",
                "ETCUSDC",
                "ETCUSDT",
                "ETHBEARUSDT",
                "ETHBULLUSDT",
                "ETHUSDC",
                "ETHUSDT",
                "EURUSDT",
                "FETUSDT",
                "FILUSDT",
                "FIOUSDT",
                "FIROUSDT",
                "FLMUSDT",
                "FTMUSDC",
                "FTMUSDT",
                "FTTUSDT",
                "FUNUSDT",
                "GBPUSDT",
                "GRTUSDT",
                "GTOUSDC",
                "GTOUSDT",
                "GXSUSDT",
                "HARDUSDT",
                "HBARUSDT",
                "HCUSDT",
                "HIVEUSDT",
                "HNTUSDT",
                "HOTUSDT",
                "ICXUSDT",
                "INJUSDT",
                "IOSTUSDT",
                "IOTAUSDT",
                "IOTXUSDT",
                "IRISUSDT",
                "JSTUSDT",
                "JUVUSDT",
                "KAVAUSDT",
                "KEYUSDT",
                "KMDUSDT",
                "KNCUSDT",
                "KSMUSDT",
                "LENDUSDT",
                "LINKUSDC",
                "LINKUSDT",
                "LITUSDT",
                "LRCUSDT",
                "LSKUSDT",
                "LTCUSDC",
                "LTCUSDT",
                "LTOUSDT",
                "LUNAUSDT",
                "MANAUSDT",
                "MATICUSDT",
                "MBLUSDT",
                "MCOUSDT",
                "MDTUSDT",
                "MFTUSDT",
                "MITHUSDT",
                "MKRUSDT",
                "MTLUSDT",
                "NANOUSDT",
                "NBSUSDT",
                "NEARUSDT",
                "NEOUSDC",
                "NEOUSDT",
                "NKNUSDT",
                "NMRUSDT",
                "NPXSUSDC",
                "NPXSUSDT",
                "NULSUSDT",
                "OCEANUSDT",
                "OGNUSDT",
                "OGUSDT",
                "OMGUSDT",
                "ONEUSDC",
                "ONEUSDT",
                "ONGUSDT",
                "ONTUSDC",
                "ONTUSDT",
                "ORNUSDT",
                "OXTUSDT",
                "PAXGUSDT",
                "PAXUSDT",
                "PERLUSDC",
                "PERLUSDT",
                "PHBUSDC",
                "PNTUSDT",
                "PSGUSDT",
                "QTUMUSDT",
                "REEFUSDT",
                "RENUSDT",
                "REPUSDT",
                "RIFUSDT",
                "RLCUSDT",
                "ROSEUSDT",
                "RSRUSDT",
                "RUNEUSDT",
                "RVNUSDT",
                "SANDUSDT",
                "SCUSDT",
                "SFPUSDT",
                "SKLUSDT",
                "SNXUSDT",
                "SOLUSDT",
                "SRMUSDT",
                "STMXUSDT",
                "STORJUSDT",
                "STORMUSDT",
                "STPTUSDT",
                "STRATUSDT",
                "STRAXUSDT",
                "STXUSDT",
                "SUNUSDT",
                "SUSDUSDT",
                "SUSHIUSDT",
                "SXPUSDT",
                "TCTUSDT",
                "TFUELUSDC",
                "TFUELUSDT",
                "THETAUSDT",
                "TOMOUSDC",
                "TOMOUSDT",
                "TRBUSDT",
                "TROYUSDT",
                "TRUUSDT",
                "TRXUSDC",
                "TRXUSDT",
                "TUSDUSDT",
                "TWTUSDT",
                "UMAUSDT",
                "UNFIUSDT",
                "UNIUSDT",
                "USDCUSDT",
                "USDSBUSDT",
                "USDSUSDC",
                "USDSUSDT",
                "UTKUSDT",
                "VENUSDT",
                "VETUSDT",
                "VITEUSDT",
                "VTHOUSDT",
                "WANUSDT",
                "WAVESUSDC",
                "WAVESUSDT",
                "WINGUSDT",
                "WINUSDC",
                "WINUSDT",
                "WNXMUSDT",
                "WRXUSDT",
                "WTCUSDT",
                "XEMUSDT",
                "XLMUSDC",
                "XLMUSDT",
                "XMRUSDT",
                "XRPBEARUSDT",
                "XRPBULLUSDT",
                "XRPUSDC",
                "XRPUSDT",
                "XTZUSDT",
                "XVSUSDT",
                "XZCUSDT",
                "YFIIUSDT",
                "YFIUSDT",
                "ZECUSDC",
                "ZECUSDT",
                "ZENUSDT",
                "ZILUSDT",
                "ZRXUSDT"
        };

        return coins;
    }

    public static double[][] hashInitializer1D(){

        double coins[][] = new double[259][1409];

        for (int i = 0; i < 259; i++) {
            for (int j = 0; j < 1409; j++) {
                coins[i][j] = 0;
            }
        }

        return coins;
    }

    public static HashSet<Long> getHashSet() {

        HashSet<Long> timestamps = new HashSet<>();

        String coinName = "BTCUSDT";

        String path;

        long timestamp;

        path = "../historical_candle_data/historical_candle_data/" + coinName + "-1d-data.csv";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            String row;
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                timestamp = Long.parseLong(data[6]);

                timestamps.add(timestamp);
            }
            csvReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Size timestamps: " + timestamps.size());

        return timestamps;
    }

    public static double[][] getHash1D(){

        System.out.println("Inicializando hash 1D...");

        double coinsQuotes[][] = Utils.hashInitializer1D();
        HashSet<Long> timestamps = Utils.getHashSet();
        List<Long> k = new ArrayList<Long>(timestamps);
        Collections.sort(k);

        System.out.println("Preenchendo hash 1D...");

        String[] coinsNames = Utils.coinsInitializer();
        String path;

        for (int i = 0; i < coinsNames.length; i++) {

            HashMap<Long, Double> coinAux = new HashMap<Long, Double>();

            path = "../historical_candle_data/historical_candle_data/" + coinsNames[i] + "-1d-data.csv";

            try {
                BufferedReader csvReader = new BufferedReader(new FileReader(path));
                String row;
                csvReader.readLine();
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    long timestampAux = Long.parseLong(data[6]);
                    double quoteAux = Double.parseDouble(data[4]);
                    coinAux.put(timestampAux, quoteAux);
                }
                csvReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int j = 0; j < k.size(); j++){

                if (coinAux.containsKey(k.get(j))){
                    coinsQuotes[i][j] = coinAux.get(k.get(j));
                }
            }
        }

        System.out.println("Hash 1D concluída!!!");

        return coinsQuotes;
    }


}
