/*******************************************************************************
 * Copyright 2013, the Optique Consortium
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This first version of the R2RML API was developed jointly at the University of Oslo, 
 * the University of Bolzano, La Sapienza University of Rome, and fluid Operations AG, 
 * as part of the Optique project, www.optique-project.eu
 ******************************************************************************/
package rdf4jTest;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import eu.optique.r2rml.api.binding.rdf4j.RDF4JR2RMLMappingManager;
import eu.optique.r2rml.api.model.impl.SQLBaseTableOrViewImpl;
import org.junit.Assert;
import org.junit.Test;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

import eu.optique.r2rml.api.model.LogicalTable;
import eu.optique.r2rml.api.model.ObjectMap;
import eu.optique.r2rml.api.model.PredicateMap;
import eu.optique.r2rml.api.model.PredicateObjectMap;
import eu.optique.r2rml.api.model.SubjectMap;
import eu.optique.r2rml.api.model.Template;
import eu.optique.r2rml.api.model.TriplesMap;

/**
 * JUnit Test Cases
 * 
 * @author Riccardo Mancini
 */
public class PredicateObjectMap_Test
{
	
	@Test
	public void test1() throws Exception{

		InputStream fis = getClass().getResourceAsStream("../mappingFiles/test2.ttl");
		
		RDF4JR2RMLMappingManager mm = RDF4JR2RMLMappingManager.getInstance();
		
		// Read the file into a model.
		RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
		Model m = new LinkedHashModel();
		rdfParser.setRDFHandler(new StatementCollector(m));
		rdfParser.parse(fis, "testMapping");
		
		Collection<TriplesMap> coll = mm.importMappings(m);
		
		Assert.assertTrue(coll.size()==1);
		
		Iterator<TriplesMap> it=coll.iterator();
		while(it.hasNext()){
			TriplesMap current=it.next();

			SubjectMap s=current.getSubjectMap();
			Template t=s.getTemplate();
			Assert.assertTrue(t.getColumnName(0).contains("Name"));
			
			LogicalTable table=current.getLogicalTable();
			
			SQLBaseTableOrViewImpl ta= (SQLBaseTableOrViewImpl) table;
			Assert.assertTrue(ta.getTableName().contains("Student"));
			
		}			
		
	}
	
	

	
	
	@Test
	public void test2() throws Exception {
		
		InputStream fis = getClass().getResourceAsStream("../mappingFiles/test2.ttl");
		
		RDF4JR2RMLMappingManager mm = RDF4JR2RMLMappingManager.getInstance();
		
		// Read the file into a model.
		RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
		Model m = new LinkedHashModel();
		rdfParser.setRDFHandler(new StatementCollector(m));
		rdfParser.parse(fis, "testMapping");
		
		Collection<TriplesMap> coll = mm.importMappings(m);
		
		Assert.assertTrue(coll.size()==1);
		
		Iterator<TriplesMap> it=coll.iterator();
		while(it.hasNext()){
			TriplesMap current=it.next();

			Iterator<PredicateObjectMap> pomit=current.getPredicateObjectMaps().iterator();
			while(pomit.hasNext()){
				PredicateObjectMap pom=pomit.next();
				
				Iterator<PredicateMap> pmit=pom.getPredicateMaps().iterator();
				PredicateMap p=pmit.next();
				Assert.assertTrue(p.getConstant().toString().contains("name"));
				Assert.assertFalse(pmit.hasNext());
				
				Iterator<ObjectMap> omit=pom.getObjectMaps().iterator();
				ObjectMap o=omit.next();
				Assert.assertTrue(o.getColumn().contains("Name"));
				Assert.assertFalse(omit.hasNext());
			}
		}
			
		
	}

	
}
