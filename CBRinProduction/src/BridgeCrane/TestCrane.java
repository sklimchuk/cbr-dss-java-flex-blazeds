/**
 * Test6.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garcнa.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package BridgeCrane;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.HoldOutEvaluator;
import jcolibri.exception.AttributeAccessException;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.selection.SelectCases;


import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.evaluators.*;





/**
 * This example shows how to use the Plain Text connector.
 * Here we only read the cases and store a new one in the persistence file.
 * <p>
 * The case base (iris_data_jCOLIBRI.txt) contains information about iris:
 * <pre>
 * #Columns are: Sepal Length, Sepal Width, Petal Length, Petal Width, Type of Iris,
 * 
 * Case 1,5.1,3.5,1.4,0.2,Iris-setosa
 * Case 2,4.9,3,1.4,0.2,Iris-setosa
 * Case 3,4.7,3.2,1.3,0.2,Iris-setosa
 * ...
 * </pre>
 * 
 * These cases are mapped into the following structure:
 *  * <pre>
 * Case
 *  |
 *  +- Description
 *  |       |
 *  |       +- id *          (1)
 *  |       +- sepalLength   (2)
 *  |       +- sepalWidth    (3)
 *  |       +- petalLength   (4)
 *  |       +- petalWidth    (5)
 *  |
 *  +- Solution
 *          |
 *          +- type *        (6)
 * </pre>
 * The attributes with * are the ids of the compound objects and the numbers between parenthesis are the corresponding columns in the text file.
 * <p>
 * The mapping is configured by the <b>plaintextconfig.xml</b> file following the schema defined in PlainTextConnector:
 * <pre>
 * &lt;TextFileConfiguration&gt;
 *	&lt;FilePath&gt;jcolibri/test/test6/iris_data_jCOLIBRI.txt&lt;/FilePath&gt;
 *	&lt;Delimiters&gt;,&lt;/Delimiters&gt;
 *	&lt;DescriptionClassName&gt;jcolibri.test.test6.IrisDescription&lt;/DescriptionClassName&gt;
 *	&lt;DescriptionMappings&gt;
 *		&lt;Map&gt;sepalLength&lt;/Map&gt;
 *		&lt;Map&gt;sepalWidth&lt;/Map&gt;
 *		&lt;Map&gt;petalLength&lt;/Map&gt;
 *		&lt;Map&gt;petalWidth&lt;/Map&gt;		
 *	&lt;/DescriptionMappings&gt;
 *	&lt;SolutionClassName&gt;jcolibri.test.test6.IrisSolution&lt;/SolutionClassName&gt;
 *	&lt;SolutionMappings&gt;
 *      &lt;Map&gt;type&lt;/Map&gt;
 *	&lt;/SolutionMappings&gt;
 * &lt;/TextFileConfiguration&gt;
 * </pre>
 * First, we define the path containing the data and the characters used as delimiters (comma in this example).
 * <br>
 * Then we map each part of the case. Following the order of the columns in the text file we have to indicate to which attributes are mapped.
 * This connector only uses the id of the description. It must be the first column of each row and is not included in the mapping file
 * <br>
 * 
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * 
 * @see jcolibri.connector.PlainTextConnector
 */
public class TestCrane implements StandardCBRApplication {

	Connector _connector;
	CBRCaseBase _caseBase;
	
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	public void configure() throws ExecutionException {
		try{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("BridgeCrane/plaintextconfig.xml"));
			_caseBase  = new LinealCaseBase();
			} catch (Exception e){
				throw new ExecutionException(e);
		}

	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		return _caseBase;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException {
		
		// First configure the KNN
		NNConfig simConfig = new NNConfig();
		// Set the average() global similarity function for the description of the case
		simConfig.setDescriptionSimFunction(new Average());
		// The Progib attribute uses the equal() local similarity function
		Attribute Progib = new Attribute("Progib", CraneDescription.class);
		simConfig.addMapping(Progib, new Interval(31));
		simConfig.setWeight(Progib, 0.5);
		
		//simConfig.addMapping(new Attribute("Progib", CraneDescription.class), new Equal());
		// Iskrivlenie --> equal()
		simConfig.addMapping(new Attribute("Iskrivlenie", CraneDescription.class), new Equal());
		// Skruchivanie --> equal()
		simConfig.addMapping(new Attribute("Skruchivanie", CraneDescription.class), new Equal());
		// Viazkost --> equal()
		simConfig.addMapping(new Attribute("Viazkost", CraneDescription.class), new Equal());
		// Corrosion --> equal()
		simConfig.addMapping(new Attribute("Corrosion", CraneDescription.class), new Interval(10));
		// id --> equal()
		simConfig.addMapping(new Attribute("id", CraneDescription.class), new Equal());
		
		
		// A bit of verbose
		System.out.println("Query Description:");
		System.out.println(query.getDescription());
		System.out.println();
		
		// Execute NN
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		// Select k cases
		eval = SelectCases.selectTopKRR(eval, 5);
		
		// Print the retrieval
		System.out.println("Retrieved cases:");
		for(RetrievalResult nse: eval)
			System.out.println(nse);
				

		
		/*
		 * этот блок был в примере по умолчанию
		 * Я закомментировал специально для придания функционала кНН
		//Obtain only the first case
		CBRCase newcase = _caseBase.getCases().iterator().next();
		//Modify its id attribute and store it back
		Attribute id = newcase.getDescription().getIdAttribute();
		try {
			Date d = new Date();
			id.setValue(newcase.getDescription(), ("case "+d.toString()).replaceAll(" ", "_"));
		} catch (AttributeAccessException e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(e);
		}
		
		ArrayList<CBRCase> casestoLearnt = new ArrayList<CBRCase>();
		casestoLearnt.add(newcase);
		_caseBase.learnCases(casestoLearnt);
		*/

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		_connector.close();

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the application
		TestCrane test = new TestCrane();
		
		
		try {
				
			
			// Configure it
			test.configure();
			// Run the precycle --> load the cases
			test.preCycle();
			//формируем запрос
			
			// Configure the query description.
			CraneDescription queryDesc = new CraneDescription();
			//queryDesc.setSrok(34.0);
			queryDesc.setProgib(50.); //Interval
			//queryDesc.setIskrivlenie(94.48);
			//queryDesc.setSkruchivanie(35.74);
			//queryDesc.setViazkost(2.);
			queryDesc.setCorrosion(8.); //Interval
			//queryDesc.setId("Case 11");
			
			// Configure the query solution - if needed.
			CraneSolution querySol = new CraneSolution();
			querySol.setDopustimo("Dopustimo");
			
			CBRCase query = new CBRCase();
			query.setDescription(queryDesc);
			query.setSolution(querySol);
			
			// Run a cycle with the query
			test.cycle(query);
			
			System.out.println("Cycle finished.");
			
			// Run the postcycle
			test.postCycle();
			
			
			/*
			 *Этот код работает, к-во циклов = 69
			 
			HoldOutEvaluator eval = new HoldOutEvaluator();
			eval.init(new TestCrane());
			eval.HoldOut(5, 1);
			
			System.out.println(Evaluator.getEvaluationReport());
			jcolibri.evaluation.tools.EvaluationResultGUI.show(Evaluator.getEvaluationReport(), "TestCrane - Evaluation", false);
			*/
			
		
			
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(TestCrane.class).error(e);
		}

	}

}
