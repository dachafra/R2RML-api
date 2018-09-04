package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import es.upm.fi.dia.oeg.rmlc.api.model.JoinObject;
import org.apache.commons.rdf.api.Literal;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.api.Triple;

import java.util.ArrayList;
import java.util.Set;

public class JoinObjectImpl extends MappingComponentImpl implements JoinObject {

    private ArrayList<RDFTerm> columns;
    private String functions;

    public JoinObjectImpl(RDF rdf, ArrayList<RDFTerm> columns, String functions){
        super(rdf);
        this.columns=columns;
        setFunction(functions);
    }

    @Override
    public void setColumns(ArrayList<RDFTerm> columns) {
        this.columns = columns;
    }

    @Override
    public void setFunction(String functions) {
        for(int i=0 ; i< columns.size();i++){
            functions = functions.replace("columns["+i+"]",((Literal)columns.get(i)).getLexicalForm());
        }
        this.functions = functions;
    }

    @Override
    public ArrayList<RDFTerm> getColumns() {
        return columns;
    }

    @Override
    public String getFunctions() {
        return functions;
    }

    public Set<Triple> serialize(){return null;}
}
