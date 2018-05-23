package jenaTest;

import es.upm.fi.dia.oeg.rmlc.api.binding.jena.JenaRMLCMappingManager;
import es.upm.fi.dia.oeg.rmlc.api.model.TriplesMap;
import es.upm.fi.dia.oeg.rmlc.api.model.impl.InvalidRMLCMappingException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import java.util.Collection;

public class JenaRMLCMappingManagerTest {

    @Test
    public void test (){
        JenaRMLCMappingManager mm = JenaRMLCMappingManager.getInstance();
        Model m = ModelFactory.createDefaultModel();
        m = m.read("path/to/rmlc/mapping");
        try {
            Collection<TriplesMap> triplesMaps = mm.importMappings(m);
        } catch (InvalidRMLCMappingException e) {
            System.out.println("Exception in read mapping: " + e.getMessage());
        }
    }
}
