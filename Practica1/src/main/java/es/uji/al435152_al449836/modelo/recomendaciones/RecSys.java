package es.uji.al435152_al449836.modelo.recomendaciones;

import es.uji.al435152_al449836.modelo.algoritmos.Algorithm;
import es.uji.al435152_al449836.modelo.algoritmos.excepciones.LikedItemNotFoundException;
import es.uji.al435152_al449836.modelo.datos.Row;
import es.uji.al435152_al449836.modelo.datos.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Nucleo generico del sistema de recomendacion.
 *
 * <p>Esta clase envuelve un algoritmo concreto y lo transforma en un servicio
 * de recomendaciones util para la aplicacion. Su flujo se divide en tres pasos:
 * - entrenamiento del algoritmo con el conjunto adecuado;
 * - estimacion de la clase de cada item del conjunto de test;
 * - busqueda de items que compartan clase con la cancion elegida.
 *
 * <p>La idea de recomendacion es facil de explicar: si dos canciones acaban con
 * la misma clase o cluster estimado, se consideran candidatas a recomendarse.
 */
public class RecSys {
    /**
     * Algoritmo concreto que decide la clase de cada item.
     */
    private Algorithm algorithm;

    /**
     * Mapa nombre de cancion -> clase estimada por el algoritmo.
     */
    private Map<String, Integer> estimaciones;

    /**
     * Construye el recomendador alrededor del algoritmo indicado.
     */
    public RecSys(Algorithm algorithm) {
        this.algorithm = algorithm;
        estimaciones = new HashMap<>();
    }

    /**
     * Delega el entrenamiento al algoritmo elegido.
     */
    public void train(Table trainData) {
        algorithm.train(trainData);
    }

    /**
     * Calcula la clase estimada de cada item del conjunto de test.
     *
     * <p>El resultado se almacena en un mapa porque la interfaz trabaja con
     * nombres de canciones y no con indices numericos.
     */
    public void initialise(Table testData, List<String> testItemNames) {
        for (int i = 0; i < testData.getRowCount(); i++) {
            Row data = testData.getRowAt(i);
            Integer estimacion = (Integer) algorithm.estimate(data.getData());
            estimaciones.put(testItemNames.get(i), estimacion);
        }
    }

    /**
     * Devuelve canciones con la misma clase estimada que la cancion elegida.
     *
     * <p>Primero se valida que la cancion exista. Despues se toma su clase como
     * referencia y se recorre el mapa completo buscando coincidencias.
     */
    public List<String> recommend(String nameLikedItem, int numRecommendations) {
        List<String> lista = new ArrayList<>();

        if (!estimaciones.containsKey(nameLikedItem)) {
            throw new LikedItemNotFoundException(nameLikedItem);
        }

        Integer estimacionLiked = estimaciones.get(nameLikedItem);

        for (String k : estimaciones.keySet()) {
            if (k.equals(nameLikedItem)) {
                continue;
            }

            if (estimaciones.get(k).equals(estimacionLiked)) {
                lista.add(k);

                if (lista.size() == numRecommendations) {
                    return lista;
                }
            }
        }

        return lista;
    }
}
