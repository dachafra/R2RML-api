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
package jenaTest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.optique.api.mapping.R2RMLMappingManager;
import eu.optique.api.mapping.R2RMLMappingManagerFactory;
import eu.optique.api.mapping.TriplesMap;

/**
 * JUnit Test Cases
 * 
 * @author Riccardo Mancini
 */
public class SerilizationMapping_Test
{
	
	@Test
	public void test1() throws Exception {

		try{

			FileInputStream fis = new FileInputStream(new File("test/mappingFiles/artist.ttl"));
			
			R2RMLMappingManager mm = R2RMLMappingManagerFactory.getJenaMappingManager();

			Model m = ModelFactory.createDefaultModel();
			m = m.read(fis,"testMapping", "TURTLE");
			Collection<TriplesMap> coll = mm.importMappings(m);
			
			FileOutputStream fos = new FileOutputStream(new File("test/mappingFiles/artistNew.ttl"));
			Model out = mm.exportMappings(coll, Model.class);
			out.write(fos, "TURTLE");
			
			FileInputStream fis1 = new FileInputStream(new File("test/mappingFiles/artistNew.ttl"));

			Model m1 = ModelFactory.createDefaultModel();
			m1 = m1.read(fis1,"testMapping1", "TURTLE");
			Collection<TriplesMap> coll1 = mm.importMappings(m);
			
			Assert.assertTrue(fis1!=null);
			Assert.assertTrue(coll1 != null && !coll1.isEmpty());
			
			Assert.assertTrue(true);
			
		}catch (NullPointerException e){
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	
}
