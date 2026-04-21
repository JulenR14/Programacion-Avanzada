package es.uji.al435152_al449836.lecturas;


import es.uji.al435152_al449836.datos.Table;

import java.net.URISyntaxException;

public abstract class ReaderTemplate <T extends Table>{
    private String source;
    protected T table;

    public ReaderTemplate(String source){
        this.source = source;
    }

    public abstract void openSource(String source);
    public abstract void processHeaders(String headers);
    public abstract void processData(String data);
    public abstract void closeSource();
    public abstract boolean hasMoreData();
    public abstract String getNextData();


    public final T readTableFromSource() {
        openSource(source);
        if (hasMoreData()){
            processHeaders(getNextData());
            while (hasMoreData()){
                 processData(getNextData());
            }
        }
        closeSource();
        return table;
    }
    public Table getTable(){return table;}

    public void setTable(T table){
        this.table = table;
    }


}
