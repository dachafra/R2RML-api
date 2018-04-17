package es.upm.fi.dia.oeg.rmlc.api.binding.jena;



import es.upm.fi.dia.oeg.rmlc.api.model.TriplesMap;
import es.upm.fi.dia.oeg.rmlc.api.model.impl.InvalidRMLCMappingException;
import es.upm.fi.dia.oeg.rmlc.api.model.impl.RMLCMappingManagerImpl;
import org.apache.commons.rdf.jena.JenaGraph;
import org.apache.commons.rdf.jena.JenaRDF;
import org.apache.jena.rdf.model.Model;

import java.util.Collection;

public class JenaRMLCMappingManager extends RMLCMappingManagerImpl {

    private static JenaRMLCMappingManager INSTANCE = new JenaRMLCMappingManager(new JenaRDF());

    private JenaRMLCMappingManager(JenaRDF rdf) {
        super(rdf);
    }

    public Collection<TriplesMap> importMappings(Model model) throws InvalidRMLCMappingException {
        return importMappings(((JenaRDF) getRDF()).asGraph(model));
    }

    @Override
    public JenaGraph exportMappings(Collection<TriplesMap> maps) {
        return (JenaGraph) super.exportMappings(maps);
    }

    public static JenaRMLCMappingManager getInstance(){
        return INSTANCE;
    }
}
