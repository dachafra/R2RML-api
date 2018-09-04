package es.upm.fi.dia.oeg.rmlc.api.model;

import org.apache.commons.rdf.api.RDFTerm;

import java.util.ArrayList;

public interface JoinObject {


    void setColumns (ArrayList<RDFTerm> columns);

    void setFunction (String functions);

    ArrayList<RDFTerm> getColumns ();

    String getFunctions ();

}
