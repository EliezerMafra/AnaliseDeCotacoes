public class Param {
    private int popSize;
    private double mutGen;
    private double mutInd;
    private double cross;
    private double elit;
    private int gens;
    private double futureReturnMean;
    private double stdDeviation;
    private double maxDrawDown;

    public Param(int popSize, double mutGen, double mutInd, double cross, double elit, int gens) {
        this.popSize = popSize;
        this.mutGen = mutGen;
        this.mutInd = mutInd;
        this.cross = cross;
        this.elit = elit;
        this.gens = gens;
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public double getMutGen() {
        return mutGen;
    }

    public void setMutGen(double mutGen) {
        this.mutGen = mutGen;
    }

    public double getMutInd() {
        return mutInd;
    }

    public void setMutInd(double mutInd) {
        this.mutInd = mutInd;
    }

    public double getCross() {
        return cross;
    }

    public void setCross(double cross) {
        this.cross = cross;
    }

    public double getElit() {
        return elit;
    }

    public void setElit(double elit) {
        this.elit = elit;
    }

    public int getGens() {
        return gens;
    }

    public void setGens(int gens) {
        this.gens = gens;
    }

    public double getFutureReturnMean() {
        return futureReturnMean;
    }

    public void setFutureReturnMean(double futureReturnMean) {
        this.futureReturnMean = futureReturnMean;
    }

    public double getStdDeviation() {
        return stdDeviation;
    }

    public void setStdDeviation(double stdDeviation) {
        this.stdDeviation = stdDeviation;
    }

    public double getMaxDrawDown() {
        return maxDrawDown;
    }

    public void setMaxDrawDown(double maxDrawDown) {
        this.maxDrawDown = maxDrawDown;
    }
}
