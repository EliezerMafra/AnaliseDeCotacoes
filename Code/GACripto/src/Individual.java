import java.util.Random;

public class Individual {
    private float[] chromossome;
    private float fitness;
    public float sum; //TEMPORARIO EXCLUIR POSTERIORMENTE
    private static final float FIRST_FITNESS = -1;

    public Individual(float[] chromossome) {
        this.chromossome = chromossome;
        this.fitness = FIRST_FITNESS;
        this.normalize();
        this.sum = 0;

        for (int i = 0; i < chromossome.length; i++) {
            this.sum += chromossome[i];
        }
    }

    public Individual(int chromossomeLenght){
        Random rand = new Random();
        float auxRand[] = new float[chromossomeLenght];

        for (int gene = 0; gene < chromossomeLenght; gene++) {
            auxRand[gene] = rand.nextFloat();
        }

        this.fitness = FIRST_FITNESS;
        this.chromossome = auxRand;
        this.normalize();

        this.sum = 0;
        for (int i = 0; i < chromossome.length; i++) {
            this.sum += chromossome[i];
        }


    }

    public float[] getChromossome() {
        return chromossome;
    }

    public int getChromossomeLength() {
        return chromossome.length;
    }

    public void setChromossome(float[] chromossome) {
        this.chromossome = chromossome;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getGene(int offset){
        return this.chromossome[offset];
    }

    /*
    gene must be: 0<=gene<=1
     */
    public void setGene(int offset, float gene){
        this.chromossome[offset] = gene;
    }

    public void normalize(){
        float sum = 0;

        for (int i = 0; i < this.getChromossomeLength(); i++) {
            sum += this.chromossome[i];
        }

        for (int i = 0; i < this.getChromossomeLength(); i++) {
            this.chromossome[i] = this.chromossome[i]/sum;
        }

    }

    public String toString(){
        String str = "";

        for (int gene = 0; gene < this.getChromossomeLength(); gene++) {
            str +=this.chromossome[gene] +"|";
        }

        return str;
    }
}
