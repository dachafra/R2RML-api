package eu.optique.r2rml.api.model.impl;

import eu.optique.r2rml.api.model.ColumnFunction;
import eu.optique.r2rml.api.model.TransFunct;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.api.Triple;

import java.util.ArrayList;
import java.util.Set;


/**
 * Implementation of Function
 *
 * @author David Chaves-Fraga
 */

public class TransFunctImpl extends MappingComponentImpl implements TransFunct {

    private ArrayList<String> functions;
    private ArrayList<ColumnFunction> columnFunctions;

    public TransFunctImpl(RDF rdf, ArrayList<String> functions, ArrayList<ColumnFunction> columnFunctions){
        super(rdf);
        this.functions=functions;
        this.columnFunctions=columnFunctions;
    }

    @Override
    public Set<Triple> serialize() {
        return null;
    }

    @Override
    public void setFunctions(ArrayList<String> functions){
        this.functions = functions;
    }

    @Override
    public void setColumnFunctions (ArrayList<ColumnFunction> columnFunctions){
        this.columnFunctions = columnFunctions;
    }

    @Override
    public ArrayList<String> getFunction(){
        return  functions;
    }

    @Override
    public ArrayList<ColumnFunction> getColumnFunctions(){
        return columnFunctions;
    }
}
