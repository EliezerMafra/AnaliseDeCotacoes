import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

#paramsReturn = pd.read_csv("../Code/paramsBestFutureReturnMean.csv")
paramsReturn = pd.read_csv("../Code/paramsBestStdDeviation.csv")

fig1 = plt.figure()
ax1 = fig1.add_axes([0.1,0.1,0.8,0.8])
ax1.set_xlabel('Semana')
ax1.set_ylabel("Retorno Cumulativo [%]")
ax1.set_title("Retorno Cumulativo dos 50 Melhores Parâmetros de Desvio Padrão ")

for i in paramsReturn.index:
    paramAux = paramsReturn.loc[i]

    fileName = ("PopSize="+"{:.0f}".format(paramAux.at['popSize'])+"_MutGene="
    +str(paramAux.at['mutGen'])+"_MutInd="
    +str(paramAux.at['mutInd'])+"_Crossover="
    +str(paramAux.at['cross'])+"_Elit="
    +str(paramAux.at['elit'])+"_Gen="
    +"{:.0f}".format(paramAux.at['nGenerations'])+".csv")

    #filePath = "../OutputExecucaoRetorno/"+fileName
    filePath = "../OutputExecucaoStD/"+fileName

    try:
        df = pd.read_csv(filePath)
        df = df.set_index('Day')

        dfReturns = df['FutureReturn']

        port_ret = dfReturns

        cumulative_ret = (port_ret + 1).cumprod()*100

        ax1.plot(cumulative_ret)

        print("{:.0f}".format(cumulative_ret.loc[195])+"   "+fileName)
    except FileNotFoundError:
        continue

plt.show()
