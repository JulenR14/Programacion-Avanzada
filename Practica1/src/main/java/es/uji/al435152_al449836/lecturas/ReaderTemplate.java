package es.uji.al435152_al449836.lecturas;


import es.uji.al435152_al449836.datos.Table;

public abstract class ReaderTemplate <T extends Table>{
    private String source;
    private T table;

    public ReaderTemplate(String source, String headers, String data){
        this.source = source;
    }

    public abstract void openSource(String source);
    public abstract void processHeaders(String headers);
    public abstract void processData(String data);
    public abstract void closeSource();
    public abstract boolean hasMoreData();
    public abstract String getNextData();

    public final T ReadTableFromSource(){
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


}
