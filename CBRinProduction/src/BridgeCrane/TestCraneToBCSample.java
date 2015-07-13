package BridgeCrane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.method.reuse.classification.KNNClassificationConfig;

public class TestCraneToBCSample implements StandardCBRApplication {

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
	


	//попытка сделать единый вывод результата для флекса ===================================================
	public ArrayList<CraneDescription> getInitCases(int numCases, String Corrosion, String Id,
			String Iskrivlenie, String Progib, String Skruchivanie, String Srok, String Viazkost) 
			throws ExecutionException {
		
	
				
// public void configure()
		_connector = new PlainTextConnector();
		_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("BridgeCrane/plaintextconfig.xml"));
		_caseBase  = new LinealCaseBase();
			
//public CBRCaseBase preCycle()		
		_caseBase.init(_connector);
		//эта строка использовалась ТОЛЬКО для вывода в консоль
		//java.util.Collection<CBRCase> cases = _caseBase.getCases(); 
		
		
//public static void main
			// Configure the query description.
			CraneDescription queryDesc = new CraneDescription();
			
			queryDesc.setIskrivlenie( Double.parseDouble(Iskrivlenie));//94.48);
			queryDesc.setProgib(	  Double.parseDouble(Progib));//30.); //Interval
			queryDesc.setCorrosion(   Double.parseDouble(Corrosion));//8.); //Interval
			queryDesc.setId(		  Id);//);"Case 11"  //только справочное поле - расчеты по нему не ведутся, т.к. я отключил подобие
			queryDesc.setSkruchivanie(Double.parseDouble(Skruchivanie));//35.74);
			//До 22:40 21/11/2010 поле Srok у меня передавалось целочисленным, исправил на double - помогло :)
			//И конечно же надо следить за передаваемыми в интервал значениями, нужно передавать максимальное для данного параметра.
			//Если переборщить с интервалом, могут пойти отрицательные значения подобия.
			queryDesc.setSrok(		  Double.parseDouble(Srok));//Integer.parseInt(Srok));//34);
			queryDesc.setViazkost(	  Double.parseDouble(Viazkost));//2.);
			
			
			
			// Configure the query solution - if needed.
			CraneSolution querySol = new CraneSolution();
			querySol.setDopustimo("Dopustimo"); //по-моему, только справочное поле, на отбор не влияет...
			
			CBRCase query = new CBRCase();
			query.setDescription(queryDesc);
			query.setSolution(querySol);
			
			
// public void cycle(CBRQuery query)
					
			// First configure the KNN
			NNConfig simConfig = new NNConfig();
			// Set the average() global similarity function for the description of the case
			simConfig.setDescriptionSimFunction(new Average());
			
					
			Attribute attribProgib       = new Attribute("Progib",       CraneDescription.class);
			Attribute attribIskrivlenie  = new Attribute("Iskrivlenie",  CraneDescription.class);
			Attribute attribSkruchivanie = new Attribute("Skruchivanie", CraneDescription.class);
			Attribute attribViazkost     = new Attribute("Viazkost",     CraneDescription.class);
			Attribute attribCorrosion    = new Attribute("Corrosion",    CraneDescription.class);
			Attribute attribSrok         = new Attribute("Srok",         CraneDescription.class);
			//Attribute attribId           = new Attribute("id",           CraneDescription.class);
			
			double smallWeight = 1; //пока здесь будет вес параметров
			double largeWeight = 1;
			//Threshold not applicable to type: class java.lang.Double !!!
			
			simConfig.addMapping(attribProgib,       new Interval(85.2));//max = 85.2
			//simConfig.setWeight(attribProgib, 		 smallWeight);
			
			simConfig.addMapping(attribIskrivlenie,  new Interval(111.55));
			//simConfig.setWeight(attribIskrivlenie, 	 smallWeight);
			
			simConfig.addMapping(attribSkruchivanie, new Interval(45.42));
			//simConfig.setWeight(attribSkruchivanie,  largeWeight);
			
			simConfig.addMapping(attribViazkost,     new Interval(3.9));
			//simConfig.setWeight(attribViazkost,      smallWeight);
			
			simConfig.addMapping(attribCorrosion,    new Interval(18.84));
			//simConfig.setWeight(attribCorrosion,     smallWeight);
			
			simConfig.addMapping(attribSrok,         new Interval(40));
			//simConfig.setWeight(attribSrok,          smallWeight);
			
			//может ИД оставить только информационным и не искать по нему вообще?
			//Поэтому я пока закоментировал, т.к. поиск точного соответствия (а не фильтрация - это в самом DataGrid)
			//по этому полю сейчас не имеет смысла
			//simConfig.addMapping(attribId,           new Equal());  
			//simConfig.setWeight(attribId,            smallWeight);
			
				
			
					
			// Execute NN
			//22/11/2010 пробую объявить глобально для экспорта CraneSolution
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
			//eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
			
			
			// Select k cases
			//eval = SelectCases.selectTopKRR(eval, 5); 	//попробуем ввести новую переменную
					
			// Select k cases
			
			//22/11/2010 пробую объявить глобально для экспорта CraneSolution
			Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, numCases);//50);
			//selectedcases = SelectCases.selectTopK(eval, numCases);//50);
			
			
			
			
			ArrayList<CraneDescription> casesR = new ArrayList<CraneDescription>();
			for(RetrievalResult rr: eval)
				if(selectedcases.contains(rr.get_case()))
				{
					casesR.add((CraneDescription) rr.get_case().getDescription());
				}
	

			//эта строка - проверка передаваемых значений
			//передается нормально, поэтому пока отключаю, чтобы с толку не сбивала.
			//casesR.add(queryDesc); 
						
			
//попробуем создать единый массив информации по прецеденту 
//(эту часть свободно можно убирать или переставлять без потери функционала):
//из-за использования eval идут отобранные прецеденты. нужно также сделать для начальных прецедентов 
//			ArrayList<RetrievalResult> casesAll = new ArrayList<RetrievalResult>();
//			for(RetrievalResult rr1: eval)
//				if(selectedcases.contains(rr1.get_case()))
//					casesAll.add(rr1);
//			int curCase = 0; //№ прецедента
//						
//			RetrievalResult rr1 = casesAll.get(curCase);
//			double sim = rr1.getEval();
//			
//			CBRCase _case = rr1.get_case();
//			
//			String Allinfo = null;
//			String delimiter = "\n";
//			Allinfo += _case.getID().toString()+" -> "+sim+" ("+(curCase+1)+"/"+casesAll.size()+")";
//			
//			CraneDescription desc1 = (CraneDescription) _case.getDescription();
//			Allinfo += delimiter + desc1.getCorrosion().toString();
//			Allinfo += delimiter + desc1.getIskrivlenie().toString();
//			Allinfo += delimiter + desc1.getProgib().toString();
//			Allinfo += delimiter + desc1.getSkruchivanie().toString();
//			Allinfo += delimiter + desc1.getSrok();
//			Allinfo += delimiter + desc1.getViazkost();
//			
//			CraneSolution sol1 = (CraneSolution) _case.getSolution();
//			Allinfo += delimiter + sol1.getDopustimo();
//			
//			CaseComponent just1 = _case.getJustificationOfSolution();
//			Allinfo += delimiter + just1.toString();
//			
//			CaseComponent res1 = _case.getResult();
//			Allinfo += delimiter + res1.toString();
			

			//!!!! создание начальной базы прецедентов:	 РАБОТАЕТ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!		
			CraneDescription queryDescInit = new CraneDescription();
			CraneSolution querySolInit = new CraneSolution();
			CBRCase queryInit = new CBRCase();
			queryInit.setDescription(queryDescInit);
			queryInit.setSolution(querySolInit);
			NNConfig simConfigInit = new NNConfig();
			simConfigInit.setDescriptionSimFunction(new Average());
			Collection<RetrievalResult> evalInit = 
				NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), queryInit, simConfigInit);
			Collection<CBRCase> selectedcasesInit = SelectCases.selectTopK(evalInit, 50);
			
			ArrayList<CraneDescription> casesRInit = new ArrayList<CraneDescription>();
			
			ArrayList<CraneSolution> casesRSolInit = new ArrayList<CraneSolution>();
			
			for(RetrievalResult rr: evalInit)
				if(selectedcasesInit.contains(rr.get_case())){
					casesRInit.add((CraneDescription) rr.get_case().getDescription());
					casesRSolInit.add((CraneSolution) rr.get_case().getSolution());
					
				}
			//!!!! вывод начальной базы прецедентов:	 РАБОТАЕТ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!  (КОНЕЦ)			
			
			_connector.close();
		
			return casesR;//;Allinfo
		}
			
		
//	//Код ниже работает !!!!!!!!!!для getInitCases(!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	если нужно вывести однин конкретный прецедент по номеру:
//	ArrayList<RetrievalResult> casesRR = new ArrayList<RetrievalResult>();
//	for(RetrievalResult rr: eval)
//		if(selectedcases.contains(rr.get_case()))
//			casesRR.add(rr);	
//	//Номер выводимого прецедента
//	int currentCase = 0;
//	RetrievalResult rr = casesRR.get(currentCase);
//	double sim = rr.getEval();
//	CBRCase _case = rr.get_case();
//	CraneDescription desc = (CraneDescription) _case.getDescription();
//	CraneSolution sol = (CraneSolution) _case.getSolution();
//return desc (или sol);
	
//=======================================================================================================	
//	this.accommodation.setText(desc.getAccommodation().toString()); //если нужно обратиться к конкретному полю

		
//=======================================================================================================		
////		// Код ниже работает !!!!!!!!!!для getInitCases(!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		CraneDescription queryDesc = new CraneDescription();
//		queryDesc.setSrok(34.0);
//		queryDesc.setProgib(30.); //Interval
//		queryDesc.setIskrivlenie(94.48);
//		queryDesc.setSkruchivanie(35.74);
//		queryDesc.setViazkost(2.);
//		queryDesc.setCorrosion(9.); //Interval
//		queryDesc.setId("Case 11");
//		
//		return queryDesc;
//		
//	}
//=======================================================================================================
			
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException {
		
		// First configure the KNN
		NNConfig simConfig = new NNConfig();
		// Set the average() global similarity function for the description of the case
		simConfig.setDescriptionSimFunction(new Average());
		
				
		Attribute attribProgib       = new Attribute("Progib",       CraneDescription.class);
		Attribute attribIskrivlenie  = new Attribute("Iskrivlenie",  CraneDescription.class);
		Attribute attribSkruchivanie = new Attribute("Skruchivanie", CraneDescription.class);
		Attribute attribViazkost     = new Attribute("Viazkost",     CraneDescription.class);
		Attribute attribCorrosion    = new Attribute("Corrosion",    CraneDescription.class);
		Attribute attribSrok         = new Attribute("Srok",         CraneDescription.class);
		Attribute attribId           = new Attribute("id",           CraneDescription.class);
		
		double smallWeight = 1;
		double largeWeight = 1;
		//Threshold not applicable to type: class java.lang.Double !!!
		
		
		
		simConfig.addMapping(attribProgib,       new Interval(85.2));//max = 86
		//simConfig.setWeight(attribProgib, 		 smallWeight);
		
		simConfig.addMapping(attribIskrivlenie,  new Interval(111.55));
		//simConfig.setWeight(attribIskrivlenie, 	 smallWeight);
		
		simConfig.addMapping(attribSkruchivanie, new Interval(45.42));
		//simConfig.setWeight(attribSkruchivanie,  largeWeight);
		
		simConfig.addMapping(attribViazkost,     new Interval(3.9));
		//simConfig.setWeight(attribViazkost,      smallWeight);
		
		simConfig.addMapping(attribCorrosion,    new Interval(18.84));
		//simConfig.setWeight(attribCorrosion,     smallWeight);
		
		simConfig.addMapping(attribSrok,         new Interval(40));
		//simConfig.setWeight(attribSrok,          smallWeight);
		
		//может ИД оставить только информационным и не искать по нему вообще?
		//Поэтому я пока закоментировал, т.к. поиск точного соответствия (а не фильтрация - это в самом DataGrid)
		//по этому полю сейчас не имеет смысла
		//simConfig.addMapping(attribId,           new Equal());  
		//simConfig.setWeight(attribId,            smallWeight);
				
		
		System.out.println("++++Вес " + "Progib       = " + simConfig.getWeight(attribProgib));
		System.out.println("++++Вес " + "Iskrivlenie  = " + simConfig.getWeight(attribIskrivlenie));
		System.out.println("++++Вес " + "Skruchivanie = " + simConfig.getWeight(attribSkruchivanie));
		System.out.println("++++Вес " + "Viazkost     = " + simConfig.getWeight(attribViazkost));
		System.out.println("++++Вес " + "ID           = " + simConfig.getWeight(attribId));
		System.out.println("++++Вес " + "simConfig.getGlobalSimilFunction(attribProgib) = "
				+ simConfig.getGlobalSimilFunction(attribProgib));
		System.out.println("++++Вес " + "simConfig.getLocalSimilFunction(attribProgib) = "
				+ simConfig.getLocalSimilFunction(attribProgib));
		System.out.println("++++Вес " + "simConfig.getGlobalSimilFunction(attribIskrivlenie) = "
				+ simConfig.getGlobalSimilFunction(attribIskrivlenie));
		System.out.println("++++Вес " + "simConfig.getLocalSimilFunction(attribIskrivlenie) = "
				+ simConfig.getLocalSimilFunction(attribIskrivlenie));
		System.out.println();
		
		// A bit of verbose
		System.out.println("Query Description:");
		System.out.println(query.getDescription());
		System.out.println();
		
		// Execute NN
		//полезная для понимания работы функция
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
		
		// Select k cases
		eval = SelectCases.selectTopKRR(eval, 5); 	//попробуем ввести новую переменную
		
		// Print the retrieval
		System.out.println("Retrieved cases:");
		for(RetrievalResult nse: eval)
			System.out.println(nse);
		
		//Вот так я распарсил все элементы прецедента: ------------------------------------------
			
		Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, 50); 
		ArrayList<RetrievalResult> casesAll = new ArrayList<RetrievalResult>();
		for(RetrievalResult rr1: eval)
			if(selectedcases.contains(rr1.get_case()))
				casesAll.add(rr1);
		int curCase = 0; //№ прецедента
					
		RetrievalResult rr1 = casesAll.get(curCase);
		double sim = rr1.getEval();
		
		CBRCase _case = rr1.get_case();
		
		String Allinfo = null;
		String delimiter = "\n";
		Allinfo = _case.getID().toString()+" -> "+sim+" ("+(curCase+1)+"/"+casesAll.size()+")";
		
		CraneDescription desc1 = (CraneDescription) _case.getDescription();
		Allinfo += delimiter + "Corrosion: " + '\t' + desc1.getCorrosion().toString();
		Allinfo += delimiter + "Iskrivlenie: " + '\t' + desc1.getIskrivlenie().toString();
		Allinfo += delimiter + "Progib: " + '\t' + desc1.getProgib().toString();
		Allinfo += delimiter + "Skruchivanie: " + '\t' + desc1.getSkruchivanie().toString();
		Allinfo += delimiter + "Srok: " + '\t'+'\t' + desc1.getSrok();
		Allinfo += delimiter + "Viazkost: " + '\t' + desc1.getViazkost();
		
		CraneSolution sol1 = (CraneSolution) _case.getSolution();
		Allinfo += delimiter + "Dopustimo? " + '\t' + sol1.getDopustimo();
		
		CaseComponent just1 = _case.getJustificationOfSolution();
		Allinfo += delimiter + "Justification: " + '\t' + just1;
		
		CaseComponent res1 = _case.getResult();
		Allinfo += delimiter + "Result: " + '\t' + res1;
		
		System.out.println(Allinfo);
		//Вот так я распарсил все элементы прецедента: ------------------------------------------  (КОНЕЦ)		
		
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		_connector.close();

	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// Create the application
		TestCraneToBCSample testBC = new TestCraneToBCSample();
		
		
		
		try {
				
			
			// Configure it
			testBC.configure();
			// Run the precycle --> load the cases
			testBC.preCycle();
			//формируем запрос
			
			// Configure the query description.
			CraneDescription queryDesc = new CraneDescription();
			queryDesc.setSrok(26.);
			queryDesc.setProgib(46.31); 
			queryDesc.setIskrivlenie(69.86);
			queryDesc.setSkruchivanie(25.2);
			queryDesc.setViazkost(3.33);
			queryDesc.setCorrosion(9.74); 
			//queryDesc.setId("Case 6"); //только справочное поле - расчеты по нему не ведутся, т.к. я отключил подобие
			//queryDesc.setSrok(12.);
			//queryDesc.setProgib(9.03); 
			//queryDesc.setIskrivlenie(27.86);
			//queryDesc.setSkruchivanie(7.8);
			//queryDesc.setViazkost(3.9);
			//queryDesc.setCorrosion(2.77); 
			//queryDesc.setId("Case 6");
			
			// Configure the query solution - if needed.
			CraneSolution querySol = new CraneSolution();
			querySol.setDopustimo("Dopustimo"); //по-моему, только справочное поле, на отбор не влияет...
			
			CBRCase query = new CBRCase();
			query.setDescription(queryDesc);
			query.setSolution(querySol);
			
			// Run a cycle with the query
			testBC.cycle(query);
			
			System.out.println("Cycle finished.===============================================================");
			
			// Run the postcycle
			testBC.postCycle();
			
			//попробую проверить результаты выдачи функции локально:
			ArrayList<CraneDescription> arrCD = testBC.getInitCases(5, "10", "*", "80", "60", "35", "30", "3");
			System.out.println("Проверка функции getInitCases");
		    for(int i = 0; i < arrCD.size() ; i++)
		      System.out.println( arrCD.get( i ) );
		 
//
//		   Util ut = new Util();
//		   ArrayList<CraneAllFields> caf = new ArrayList<CraneAllFields>();
//		   ArrayList<CraneAllFields> caf2 = new ArrayList<CraneAllFields>();
//		   //caf = ut.getInitialCasesLight();
//		   caf = ut.getRetrievedCases(100, "", "*", "", "", "", "", "");//(100, "10", "*", "80", "60", "35", "30", "3");
//		   caf2 = ut.getInitialCasesLight();
//  	       for(int i = 0; i < caf.size() ; i++)
//		      System.out.println( caf.get( i ).getCorrosion()+"   /   "+caf2.get( i ).getCorrosion() );
//  	     System.out.println(caf2.size());
//  	   System.out.println(caf2.get(13).getCorrosion()+caf2.get(14).getCorrosion());   
		
		    
		    System.out.println("Проверка обновленной getRetrievedCases");
		    Util ut = new Util();
		    ArrayList<CraneAllFields> caf = new ArrayList<CraneAllFields>();
		    ArrayList<String> als = new ArrayList<String>();
		
		    caf = ut.getInitialCasesLight();
		    for(int i = 0; i < caf.size() ; i++)
			      System.out.println( caf.get( i ).getId());

			
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(TestCraneToBCSample.class).error(e);
		} 
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			
//			//эта ветка catch нужна была только для ut.transArr(15.2)
//			e.printStackTrace();
//		}

	}
	
	

}

