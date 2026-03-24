package es.uji.al435152_al449836.lecturas;

import es.uji.al435152_al449836.datos.Row;
import es.uji.al435152_al449836.datos.RowWithLabel;
import es.uji.al435152_al449836.datos.Table;
import es.uji.al435152_al449836.datos.TableWithLabels;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CSV {

    /**
     * Función que se encarga de leer los datos de un fichero csv y convertirlo en objeto Table
     * @param ref (referencia del fichero)
     * @return objeto de tipo Table con los datos
     */
    public Table readTable(String ref) {
        Table table = new Table();
        // Obtenemos la lista de líneas del archivo usando tu función auxiliar
        List<String> datoscsv = leerCSV(ref);

        String[] elementos = datoscsv.get(0).split(",");

        List<String> headers = new ArrayList<>();
        for (String h : elementos) {
            headers.add(h);
        }
        table.setHeaders(headers);

        for (int i = 1; i < datoscsv.size(); i++) {
            elementos = datoscsv.get(i).split(",");
            //A partir de la segunda iteración ya son los datos de la tabla
            List<Double> r = new ArrayList<>();
            for (String valor : elementos) {
                r.add(Double.parseDouble(valor));
            }
            // Creamos el objeto Row y lo agregamos a la tabla
            Row row = new Row(r);
            table.addRow(row);
        }

        return table;
    }

    public TableWithLabels readTableWithLabels(String irisFile) {
        TableWithLabels tableWithLabels = new TableWithLabels();

        List<String> datoscsv = leerCSV(irisFile); //obtenemos la lista de lineas del archivo

        List<String> headers = new ArrayList<String>(); //lista para meter los headers
        //Se van agregando a la lista de headers el header de cada columna
        String[] listaHeaders = datoscsv.get(0).split(",");
        for (int j=0; j < listaHeaders.length-1; j++){
            headers.add(listaHeaders[j]);
        }
        tableWithLabels.setHeaders(headers);//se agregan a la tabla los headers

        for (int i=1; i < datoscsv.size(); i++){
            //A partir de la segunda iteración ya son los datos de la tabla
            List<Double> r = new ArrayList<Double>();
            //Se van agregando a la lista los datos de cada columna menos la ultima (la ultima columna es la label)
            String[] listaDatos = datoscsv.get(i).split(",");
            for (int j=0; j < listaDatos.length-1; j++){
                r.add(Double.parseDouble(listaDatos[j]));
            }
            //El ultimo dato de la linea es la label
            String label = listaDatos[listaDatos.length-1];

            //Creamos el objeto de la fila con etiquetas
            RowWithLabel row = new RowWithLabel(r, label);
            tableWithLabels.addRow(row);
        }

        return tableWithLabels;
    }

    /**
     * Función que se encarga de leer cualquier fichero csv y pasar una lista de las lineas
     * @param refFichero
     * @return Lista de las lineas del fichero
     */
    private List<String> leerCSV(String refFichero) {
        //Obtener ruta
        String ref = "";
        try{
            ref = getClass().getClassLoader().getResource(refFichero).toURI().getPath();
        }catch (URISyntaxException e){
            e.printStackTrace();
            return null;
        }

        //Inicializa el buffer para leer los datos
        try(BufferedReader buffer = new BufferedReader(new FileReader(ref))){
            List<String> lista = new ArrayList<>(); //Lista de lineas
            String linea; //Cada una de las lineas
            //Va leyendo linea a linea el fichero y lo agrega a la lista
            while ((linea = buffer.readLine()) != null){
                lista.add(linea);
            }
            return lista; //Devuelve lista
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
