package es.uji.al435152_al449836.modelo.recomendaciones;

import es.uji.al435152_al449836.modelo.algoritmos.Algorithm;
import es.uji.al435152_al449836.modelo.algoritmos.excepciones.LikedItemNotFoundException;
import es.uji.al435152_al449836.modelo.datos.Row;
import es.uji.al435152_al449836.modelo.datos.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecSys{
    private Algorithm algorithm;                 // Algoritmo usado (KNN o KMeans)
    private Map<String, Integer> estimaciones;   // nombreItem -> clase estimada

    public RecSys(Algorithm algorithm){
        this.algorithm = algorithm;
        estimaciones = new HashMap<>();
    }

    public void train(Table trainData){
        // Delega el entrenamiento al algoritmo concreto
        algorithm.train(trainData);
    }

    public void initialise(Table testData, List<String> testItemNames){
        // Para cada dato de test, calcula su clase y la guarda asociada a su nombre
        for (int i = 0; i<testData.getRowCount(); i++){
            Row data = testData.getRowAt(i);
            Integer estimacion = (Integer) algorithm.estimate(data.getData()); // casteo necesario por generics
            estimaciones.put(testItemNames.get(i),estimacion);
        }
    }

    public List<String> recommend(String nameLikedItem, int numRecommendations){
        List<String> lista = new ArrayList<>();

        // Validación: el item debe existir
        if (!estimaciones.containsKey(nameLikedItem))
            throw new LikedItemNotFoundException(nameLikedItem);

        Integer estimacionLiked = estimaciones.get(nameLikedItem);

        // Busca items con la misma clase estimada
        for (String k : estimaciones.keySet()){
            if (estimaciones.get(k) == estimacionLiked){
                lista.add(k);

                // Se detiene cuando alcanza el número deseado
                if (lista.size()==numRecommendations) return lista;
            }
        }

        return lista; // puede devolver menos si no hay suficientes
    }
}