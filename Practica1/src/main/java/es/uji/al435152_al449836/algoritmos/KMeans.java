package es.uji.al435152_al449836.algoritmos;

import es.uji.al435152_al449836.datos.Table;

import java.util.List;

public class KMeans {
        //Atributos 
        private int numClusters;
        private int numIterations;
        private long seed;

        //Contructor
        public KMeans(int numClusters, int numIterations, long seed) {
            this.numClusters = numClusters;
            this.numIterations = numIterations;
            this.seed = seed;
        }


        public void train(Table table){

        }

        public Integer estimate(List<Double> sample){
            return 0;
        }

}
