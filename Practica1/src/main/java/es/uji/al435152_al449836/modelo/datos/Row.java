package es.uji.al435152_al449836.modelo.datos;

import java.util.List;

public class Row {
    //Lista de datos de la fila
    private List<Double> data;

    //Contructor que obtiene los datos de la fila
    public Row(List<Double> numberlist){
        data = numberlist;
    }

    //Getter para obtener datos
    public List<Double> getData(){
        return data;
    }
}
