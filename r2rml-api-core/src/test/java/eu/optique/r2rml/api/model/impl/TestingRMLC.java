package eu.optique.r2rml.api.model.impl;

import eu.optique.r2rml.api.R2RMLMappingManager;

import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.jena.JenaRDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


public class TestingRMLC extends R2RMLMappingManagerImpl {

    public TestingRMLC (RDF rdf){super(rdf);}

    public void main (String args[]) throws Exception{
        JenaRDF jenaRDF = new JenaRDF();
        Model model = ModelFactory.createDefaultModel() ;
        model.read("/Users/dchaves/Downloads/rmlc.ttl") ;
        importMappings(((JenaRDF) getRDF()).asGraph(model));
    }


}
