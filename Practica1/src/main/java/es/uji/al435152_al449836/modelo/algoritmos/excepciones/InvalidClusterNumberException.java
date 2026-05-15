package es.uji.al435152_al449836.modelo.algoritmos.excepciones;

/**
 * Error lanzado cuando se piden mas clusters que filas disponibles.
 *
 * <p>KMeans necesita, como minimo, una fila distinta para inicializar cada
 * cluster. Si la peticion supera el numero de datos, el algoritmo quedaria en
 * un estado imposible desde el principio.
 */
public class InvalidClusterNumberException extends RuntimeException {
    private int numClusters;
    private int numeroDatos;

    /**
     * Guarda la peticion invalida y el tamano real del conjunto.
     */
    public InvalidClusterNumberException(int numeroClusters, int numeroDatos) {
        super(numeroClusters + ": clusters es superior a numero de datos: " + numeroDatos);
        this.numClusters = numeroClusters;
        this.numeroDatos = numeroDatos;
    }

    /**
     * Devuelve el numero de clusters solicitado.
     */
    public int getNumberOfCusters() {
        return numClusters;
    }

    /**
     * Devuelve el numero real de filas disponibles.
     */
    public int getNumeroDatos() {
        return numeroDatos;
    }
}
