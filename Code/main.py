# IMPORTS
import pandas as pd
import math
import os.path
import time
from keys import api_key, api_secret
from binance.client import Client
from datetime import timedelta, datetime
from dateutil import parser
from tqdm import tqdm_notebook #(Optional, used for progress-bars)

### API
binance_api_key = api_key    #Enter your own API-key here
binance_api_secret = api_secret #Enter your own API-secret here

### CONSTANTS
binsizes = {"1m": 1, "5m": 5, "1h": 60, "1d": 1440}
batch_size = 750
binance_client = Client(api_key=binance_api_key, api_secret=binance_api_secret)


### FUNCTIONS
def minutes_of_new_data(symbol, kline_size, data, source):
    if len(data) > 0: 

        old = parser.parse(data["timestamp"].iloc[-1])

    elif source == "binance":

        old = datetime.strptime('1 Jan 2017', '%d %b %Y')

    if source == "binance":

        new = pd.to_datetime(binance_client.get_klines(symbol=symbol, interval=kline_size)[-1][0], unit='ms')
        
    return old, new

def get_all_binance(symbol, kline_size, save = False):

    filename = '%s-%s-data.csv' % (symbol, kline_size)

    if os.path.isfile(filename):
        data_df = pd.read_csv(filename)
    else:
        data_df = pd.DataFrame()

    oldest_point, newest_point = minutes_of_new_data(symbol, kline_size, data_df, source = "binance")

    delta_min = (newest_point - oldest_point).total_seconds()/60

    available_data = math.ceil(delta_min/binsizes[kline_size])

    if oldest_point == datetime.strptime('1 Jan 2017', '%d %b %Y'):
        print('Downloading all available %s data for %s. Be patient..!' % (kline_size, symbol))
    else:
        print('Downloading %d minutes of new data available for %s, i.e. %d instances of %s data.' % (delta_min, symbol, available_data, kline_size))
    
    klines = binance_client.get_historical_klines(symbol, kline_size, oldest_point.strftime("%d %b %Y %H:%M:%S"), newest_point.strftime("%d %b %Y %H:%M:%S"))
    
    data = pd.DataFrame(klines, columns = ['timestamp', 'open', 'high', 'low', 'close', 'volume', 'close_time', 'quote_av', 'trades', 'tb_base_av', 'tb_quote_av', 'ignore' ])
    
    data['timestamp'] = pd.to_datetime(data['timestamp'], unit='ms')

    if len(data_df) > 0:
        temp_df = pd.DataFrame(data)
        data_df = data_df.append(temp_df)
    else:
        data_df = data

    data_df.set_index('timestamp', inplace=True)

    if save:
        data_df.to_csv(filename)
    
    print('All caught up..!')
    return data_df

def main():
    symbols = [ "1INCHUSDT",
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
                "BNBBULLUSDT", #foi ate aqui na primeira
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
    ]

    for symbol in symbols:
        get_all_binance(symbol, "1d", save = True)
        get_all_binance(symbol, "1m", save = True)

if __name__ == "__main__":
    main()