package es.upm.fi.dia.oeg.rmlc.api.model.impl;

import org.apache.commons.rdf.api.RDF;
import org.apache.commons.rdf.jena.JenaRDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


public class TestingRMLC extends RMLCMappingManagerImpl {

    public TestingRMLC (RDF rdf){super(rdf);}

    public void main (String args[]) throws Exception{
        JenaRDF jenaRDF = new JenaRDF();
        Model model = ModelFactory.createDefaultModel() ;
        model.read("../example-rmlc.ttl") ;
        importMappings(((JenaRDF) getRDF()).asGraph(model));
    }


}
