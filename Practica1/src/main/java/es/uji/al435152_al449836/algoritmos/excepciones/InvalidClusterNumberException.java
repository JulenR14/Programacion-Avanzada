package es.uji.al435152_al449836.algoritmos.excepciones;

public class InvalidClusterNumberException extends RuntimeException {
    private int numClusters;
    private int numeroDatos;
    public InvalidClusterNumberException(int numeroClusters, int numeroDatos) {
        super(numeroClusters+ ": clusters es superior a numero de datos: " +numeroDatos);
        this.numClusters = numeroClusters;
        this.numeroDatos = numeroDatos;
    }
    public int getNumberOfCusters(){return numClusters;}
    public int getNumeroDatos(){return numeroDatos;}


}
