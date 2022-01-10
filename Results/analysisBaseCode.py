import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import pandas_datareader as web

#  Assets to be included in the portfolio
tickers = ['BND', 'VB', 'VEA', 'VOO', 'VWO']

# Asset weights
wts = [0.1,0.2,0.25,0.25,0.2]

price_data = web.get_data_yahoo(tickers,
                               start = '2013-01-01',
                               end = '2018-03-01')


price_data = price_data['Adj Close']

print(price_data)

ret_data = price_data.pct_change()[1:]


weighted_returns = (wts * ret_data)

port_ret = weighted_returns.sum(axis=1)
# axis =1 tells pandas we want to add
# the rows

print(port_ret)
print(type(port_ret))

'''cumulative_ret = (port_ret + 1).cumprod()

fig = plt.figure()
ax1 = fig.add_axes([0.1,0.1,0.8,0.8])
ax1.plot(cumulative_ret)
ax1.set_xlabel('Date')
ax1.set_ylabel("Cumulative Returns")
ax1.set_title("Portfolio Cumulative Returns")
plt.show()'''
