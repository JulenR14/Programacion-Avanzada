package es.uji.al435152_al449836.algoritmos.excepciones;

public class InvalidClusterNumberException extends RuntimeException {
    public InvalidClusterNumberException(int numeroClusters, int numeroDatos) {
        super(numeroClusters+ ": clusters es superior a numero de datos: " +numeroDatos);
    }
}
