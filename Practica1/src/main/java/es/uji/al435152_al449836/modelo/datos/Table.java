package es.uji.al435152_al449836.modelo.datos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table {
    //Atributos para tabla sin etiquetas
    private List<String> headers;
    private HashMap<Integer, Row> rows;

    //Contructor
    public Table(){
        headers = new ArrayList<String>();
        rows = new HashMap<Integer,Row>();
    }

    //Getter que devuelve la fila que se pasa como parametro
    public Row getRowAt(int row){
        return rows.get(row);
    }

    //Getter que crea una lista en la cual va agregando los datos de las columas de cada fila
    public List<Double> getColumnAt(int colum){
        List<Double> columnas = new ArrayList<>();
        for(Row r: rows.values()){
            columnas.add(r.getData().get(colum));
        }
        return columnas;
    }

    //Getter que devuelve cantidad de filas
    public int getRowCount() {
        return rows.size();
    }

    //Getter que devuelve las cabeceras de la tabla
    public List<String> getHeaders() {
        return headers;
    }

    //Setter para agregar las cabeceras
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    //Función encargada de agregar fila a la tabla
    public void addRow(Row row){
        rows.put(rows.size(),row);
    }
}
