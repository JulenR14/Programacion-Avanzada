package es.uji.al435152_al449836.recomendaciones;

import es.uji.al435152_al449836.algoritmos.Algorithm;
import es.uji.al435152_al449836.algoritmos.excepciones.LikedItemNotFoundException;
import es.uji.al435152_al449836.datos.Row;
import es.uji.al435152_al449836.datos.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecSys{
    private Algorithm algorithm;
    private Map<String, Integer> estimaciones;

    public RecSys(Algorithm algorithm){
        this.algorithm = algorithm;
        estimaciones = new HashMap<>();
    }

    public void train(Table trainData){
        algorithm.train(trainData);
    }
    public void initialise(Table testData, List<String> testItemNames){

        for (int i = 0; i<testData.getRowCount(); i++){
            Row data = testData.getRowAt(i);
            Integer estimacion = (Integer) algorithm.estimate(data.getData());
            estimaciones.put(testItemNames.get(i),estimacion);
        }
    }
    public List<String> recommend(String nameLikedItem, int numRecommendations){
        List<String> lista = new ArrayList<>();
        if (!estimaciones.containsKey(nameLikedItem)) throw new LikedItemNotFoundException(nameLikedItem);
        Integer estimacionLiked = estimaciones.get(nameLikedItem);

        for (String k : estimaciones.keySet()){
            if (estimaciones.get(k) == estimacionLiked){
                lista.add(k);
                if (lista.size()==numRecommendations) return lista;
            }
        }
        return lista;
    }
}
