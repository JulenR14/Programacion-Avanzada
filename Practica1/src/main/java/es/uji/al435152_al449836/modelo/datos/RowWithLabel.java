package es.uji.al435152_al449836.modelo.datos;

import java.util.List;

public class RowWithLabel extends Row{
    //Etiqueta referenciada por la fila
    private String label;

    //Constructor que inicializa los datos de la fila y su etiqueta
    public RowWithLabel(List<Double> numberlist, String label) {
        super(numberlist);
        this.label = label;
    }

    //Getter para obtener etiqueta de la fila
    public String getLabel(){
        return label;
    }

}
