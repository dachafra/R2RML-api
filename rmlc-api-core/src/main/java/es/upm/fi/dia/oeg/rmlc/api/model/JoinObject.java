package es.upm.fi.dia.oeg.rmlc.api.model;

import java.util.ArrayList;

public interface JoinObject {


    public void setColumns (ArrayList<String> columns);

    public void setFunction (String functions);

    public ArrayList<String> getColumns ();

    public String getFunctions ();

}
