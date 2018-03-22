package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import es.upm.fi.dia.oeg.rmlc.api.model.ColumnFunction;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.Triple;

import java.util.ArrayList;
import java.util.Set;


/**
 * Implementation of ColumnFunction
 *
 * @author David Chaves-Fraga
 */


public class ColumnFunctionImpl  extends MappingComponentImpl implements ColumnFunction {

    private ArrayList<String> functions;
    private String columnName;

    public ColumnFunctionImpl(RDF rdf, ArrayList<String> functions, String columnName){
        super(rdf);
        this.functions=functions;
        this.columnName=columnName;
    }

    @Override
    public Set<Triple> serialize() {
        return null;
    }

    public void setFunctions(ArrayList<String> functions){
        this.functions=functions;
    }

    public void setColumnName (String columnName){
        this.columnName=columnName;
    }

    public ArrayList<String> getFunction(){
        return functions;
    }

    public String getColumnName(){
        return columnName;
    }

    public void addFunction (String function){
        this.functions.add(function);
    }
}
