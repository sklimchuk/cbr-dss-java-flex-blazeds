package BridgeCrane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EnumCyclicDistance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EnumDistance;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntCosine;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeep;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDeepBasic;
import jcolibri.method.retrieve.NNretrieval.similarity.local.ontology.OntDetail;
import jcolibri.method.retrieve.selection.SelectCases;

//Надо постепенно приходить к тому, чтобы каждая задача была описана в своем классе.
//Чтобы была осмысленная модульность.
//синим цветом помечаются глобальные переменные
//если очень надо будет парсить входные переменные в одной функции/процедуре, то можно делать через
//CraneDescription cdInpValues = new CraneDescription(); и дальше полям объекта присваивать значения

public class Util {
	
	Connector   _connector;
	CBRCaseBase _caseBase;
		
	public void configure() throws ExecutionException {
		try{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("BridgeCrane/plaintextconfig.xml"));
			_caseBase  = new LinealCaseBase();
			} catch (Exception e){
				throw new ExecutionException(e);
		}
	}
	
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);
		return _caseBase;
	}

	public void postCycle() throws ExecutionException {
		_connector.close();
	}
	
	//Задание параметров для query, состоящего из queryDesc и querySol
	public CBRCase setQuery(ArrayList<String> input) 
			throws ExecutionException, IOException {
		//Запрос по умолчанию: Искривление = 94.48, Коррозия = 8, Прогиб = 30, Скручивание = 35.74, Срок = 34, Вязкость = 2
		
		//!!!!!!нужны блоки try/catch а случай если нет данных вообще, иначе будет ошибка 
		if (input.get(3) == "") 	{input.set(3, "94.48") ;};
		if (input.get(1) == "") 	{input.set(1, "8") ;};
		if (input.get(4) == "") 	{input.set(4, "30") ;};
		if (input.get(5) == "") 	{input.set(5, "35.74") ;};
		if (input.get(6) == "") 	{input.set(6, "34") ;};
		if (input.get(7) == "")		{input.set(7, "2") ;};
		
		 FileWriter write = new FileWriter("defineQuery.txt");
		 PrintWriter text = new PrintWriter(write);
		 for(int i = 0; i < input.size() ; i++)
		      text.println(input.get( i ));
		 
		// Configure the query description.
		CraneDescription queryDesc = new CraneDescription();

		//Достаточно при false не с оздавать функцию подобия, здесь просто дополнительная проверка		
		if ((new Boolean(input.get(17))).booleanValue() == true)//(input.get(17) != "true") - НЕ РАБОТАЕТ!!!
			queryDesc.setIskrivlenie( Double.parseDouble(input.get(3)));
		if ((new Boolean(input.get(22))).booleanValue() == true)
			queryDesc.setProgib(	  Double.parseDouble(input.get(4)));
		if ((new Boolean(input.get(12))).booleanValue() == true)
			queryDesc.setCorrosion(   Double.parseDouble(input.get(1)));
		queryDesc.setId(		  input.get(2));//только справочное поле - расчеты по нему не ведутся, т.к. я отключил подобие
		if ((new Boolean(input.get(27))).booleanValue() == true)
			queryDesc.setSkruchivanie(Double.parseDouble(input.get(5)));
		if ((new Boolean(input.get(32))).booleanValue() == true)
			queryDesc.setSrok(		  Double.parseDouble(input.get(6)));
		if ((new Boolean(input.get(37))).booleanValue() == true)
			queryDesc.setViazkost(	  Double.parseDouble(input.get(7)));
		
		// Configure the query solution - if needed.
		CraneSolution querySol = new CraneSolution();
		//querySol.setDopustimo("Dopustimo"); //по-моему, только справочное поле, на отбор не влияет...

		CBRCase query = new CBRCase();
		query.setDescription(queryDesc);
		query.setSolution(querySol);
		
		text.flush();			
		 write.close();
		
		return query;
	}
	
	//исключительно для теста - после использования выкинуть
	public ArrayList<String> transArr(ArrayList<String> input) throws IOException{//ArrayList<String> input){
			
		//опробуем писать из BlazeDS в файл:
		 FileWriter write = new FileWriter("Data.txt");
		 PrintWriter text = new PrintWriter(write);
		 for(int i = 0; i < input.size() ; i++)
		      text.println(input.get( i ));
				 
		 text.flush();			
		 write.close();
		
		return input;//input.get(0);
	}
	
	//исключительно для теста - после использования выкинуть
	public double transArr2(String input, int input2, double input3 ) throws IOException{//ArrayList<String> input){
			
		//опробуем писать из BlazeDS в файл:
		 FileWriter write = new FileWriter("Data2.txt");
		 PrintWriter text = new PrintWriter(write);
		 //Из Flex Пишет в папку c:\apache-tomcat-6.0.26\bin\
		 //При проверке внутри Eclipse пишет в c:\_Eclipse-JavaEE-Projects\CBRinProduction\
		 text.println(input);
		 text.println(input2);
		 text.println(input3);
				 
		 text.flush();			
		 write.close();
		
		 input3 +=input2;
		
		return input3;//input+":) !!!";
	}
	
	
	//Задание метрик подобия и весов, которые будут передаваться во входных параметрах
	//public NNConfig defineSimilarityMethods(String simParam1, double weightParam1, boolean isUsingSimilParam1, ... )
	//на каждый диагн. параметр 4 вспомогательных параметра - надо передавать все одним массивом, а тут распарсить
	//его при надобности - иначе запарюсь с переносом по одному
	
	//paramCorrosion - это значение, передающееся в Interval и Threshold
	//работы пока приостановлены. нужно разобраться, как передавать Array из флекса в джаву и обратно
	//26.11.2010 разобрались :) Начинаем работать.
	public NNConfig setSimilarityMethodsAuto(ArrayList<String> input) throws IOException {
	//public NNConfig defineSimilarityMethodsTest(ArrayList<String> input) {
																
		//input.get(x);   // имя параметра, String
		//input.get(x+1); // имя функции подобия, String
		//input.get(x+2); // вес параметра, String -преобразовываем в-> double
		//input.get(x+3); // числовое значение для Interval и/или Threshold, String -преобразовываем в-> double
		//input.get(x+4); // будет ли использоваться в расчете, String -преобразовываем в-> boolean
				
		//Flex болезненно реагирует на разрывы в queryData:Array и выдает ошибку "Argument is not an Array!",
		//если пропускаем одно поле:
		//queryData[4] = progTI.text;
		//queryData[6] = skrTI.text;
		//поэтому надо делать однородный массив БЕЗ пропусков
				
		NNConfig simConfig = new NNConfig();
		Attribute attribute;
		LocalSimilarityFunction function;
		
		// String nameParam 	= input.get(i); 			//Пример: input.get(8)="Progib"
		// String similTypeParam 	= input.get(i+1); 			//Пример: input.get(9)="Interval"
		// double weightParam 	= Double.parseDouble(input.get(i+2)); 	//Пример: input.get(10)=0.8 
		// int parenthesesNumber 	= Integer.parseInt(input.get(i+3));	//Пример: input.get(11)=34 
		// boolean isUseParam 	= Boolean.parseBoolean(input.get(i+4));	//Пример: input.get(12)=true
		
		 FileWriter write = new FileWriter("defineSimilarityMethodsAuto.txt");
		 PrintWriter text = new PrintWriter(write);
	
		 int i = 8;
		 do//for(int i = 8; i < input.size() ; i++) //начинаем с 8-го индекса, до 7-го - сами параметры
		 {
			 
			//!!!!!!нужны блоки try/catch на случай если нет данных вообще, иначе будет ошибка
			 String nameParam 		= input.get(i); 			//Пример: input.get(8)="Progib"
			 String similTypeParam 	= input.get(i+1); 				//Пример: input.get(9)="Interval"
			 double weightParam 	= Double.parseDouble(input.get(i+2)); 		//Пример: input.get(10)=0.8 
			 double parenthesesNumber = Double.parseDouble(input.get(i+3));		//Пример: input.get(11)=34 
			 
			 //Если isUseParam = false, попробуем не учитывать его при поиске, 
			 //не создавая для него функцию подобия (в булевский тип переводить не обязательно!)
			 boolean isUseParam 	= (new Boolean(input.get(i+4))).booleanValue();	//Пример: input.get(12)=true
						
			 
			 if (isUseParam == true){	 
				 text.println("nameParam ="+nameParam+
				 			" / similTypeParam ="+similTypeParam+
				 			" / weightParam ="+ weightParam+
				 			" / parenthesesNumber ="+parenthesesNumber+
				 			" / isUseParam ="+isUseParam);
				 
				//Если isUseParam = true, тогда учитываем акой параметр. 
				 attribute = new Attribute(nameParam, CraneDescription.class); 
				 function = setLocalSimilarityAuto(similTypeParam, parenthesesNumber); 
				 simConfig.addMapping(attribute, function); 
				 simConfig.setWeight(attribute, weightParam);
			 }
			 		
			
			 i = i+5;
	      			 
		 }while(i < input.size());
		

		 text.flush();			
		 write.close();
		
		simConfig.setDescriptionSimFunction(new Average());

		return simConfig;
		
	}
	
	//Эта функция используется для setSimilarityMethodsAuto(ArrayList<String>)
	private LocalSimilarityFunction setLocalSimilarityAuto(String name, double param)	//было: int param)
	{
		if(name.equals("Equal"))
			return new Equal();
		else if(name.equals("Interval"))
			return new Interval(param);
		
		else if(name.equals("Threshold"))
			return new Threshold(param);
		else if(name.equals("EnumCyclicDistance"))
			return new EnumCyclicDistance();
		else if(name.equals("EnumDistance"))
			return new EnumDistance();
		else if(name.equals("OntCosine"))
			return new OntCosine();
		else if(name.equals("OntDeep"))
			return new OntDeep();
		else if(name.equals("OntDeepBasic"))
			return new OntDeepBasic();
		else if(name.equals("OntDetail"))
			return new OntDetail();
		else
		{
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error("Simil Function not found");
			return null;
		}
	}
	
	//Задание метрик подобия и весов, которые будут передаваться во входных параметрах
	//public NNConfig defineSimilarityMethods(String simParam1, double weightParam1, boolean isUsingSimilParam1, ... )
	
	public NNConfig setSimilarityMethodsManual() {
		
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

		//по умолчанию, без указания веса, вес = 1!
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

		return simConfig;
		
	}
		
	
	//выводим первоначальный список прецедентов ДО поиска
	public ArrayList<CraneAllFields> getInitialCasesLight() throws ExecutionException {
		
		configure(); //Инициализация _connector из xml-файла и определение типа базы прецедентов для _caseBase
		preCycle(); //Инициализация базы прецедентов //это равнозначно: _caseBase.init(_connector);
				
		ArrayList<CraneAllFields> arrcaf = new ArrayList<CraneAllFields>();
		
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
		{
			CraneDescription desc = (CraneDescription) c.getDescription();
			CraneSolution 	 sol  = (CraneSolution) 	c.getSolution();
			CraneAllFields   caf  = new CraneAllFields();
			caf.setId			(c.getID().toString());
			caf.setCorrosion	(desc.getCorrosion());
			caf.setIskrivlenie	(desc.getIskrivlenie());
			caf.setProgib		(desc.getProgib());
			caf.setSkruchivanie (desc.getSkruchivanie());
			caf.setSrok			(desc.getSrok());
			caf.setViazkost		(desc.getViazkost());
			caf.setDopustimo	(sol .getDopustimo());

			arrcaf.add(caf);
			
		}
		postCycle();
		return arrcaf;
	} 
	
	//прототип процедуры для добавления (адаптации) прецедента с измененными полями и новым Id
	//Для расширения и переаботки смотреть пример Test3, Test4
	public void setRetainedCase(ArrayList<String> input) throws ExecutionException{
		
		configure();
		preCycle();
	
		CraneDescription retainDesc = new CraneDescription();
		retainDesc.setIskrivlenie( Double.parseDouble(input.get(3)));
		retainDesc.setProgib(	  Double.parseDouble(input.get(4)));
		retainDesc.setCorrosion(   Double.parseDouble(input.get(1)));
		retainDesc.setId(		  input.get(2));//только справочное поле - расчеты по нему не ведутся, т.к. я отключил подобие
		retainDesc.setSkruchivanie(Double.parseDouble(input.get(5)));
		retainDesc.setSrok(		  Double.parseDouble(input.get(6)));
		retainDesc.setViazkost(	  Double.parseDouble(input.get(7)));
		CraneSolution retainSol = new CraneSolution();
		retainSol.setDopustimo("Dopustimo"); //по-моему, только справочное поле, на отбор не влияет...
		CBRCase retainedCase = new CBRCase();
		retainedCase.setDescription(retainDesc);
		retainedCase.setSolution(retainSol);

	
		// Define new ids for the compound attributes
		HashMap<Attribute, Object> componentsKeys = new HashMap<Attribute,Object>();
		componentsKeys.put(new Attribute("id", CraneDescription.class), input.get(2));//"testID"); 
		//кроме ID, бновляем еще и поля прецедента:
		componentsKeys.put(new Attribute("Corrosion", CraneDescription.class), input.get(1));
		componentsKeys.put(new Attribute("Iskrivlenie", CraneDescription.class), input.get(3));
		componentsKeys.put(new Attribute("Progib", CraneDescription.class), input.get(4));
		componentsKeys.put(new Attribute("Skruchivanie", CraneDescription.class), input.get(5));
		componentsKeys.put(new Attribute("Srok", CraneDescription.class), input.get(6));
		componentsKeys.put(new Attribute("Viazkost", CraneDescription.class), input.get(7));
		//попробуем обновить и Solution:
		componentsKeys.put(new Attribute("Dopustimo", CraneSolution.class), "Solution mogno meniat!");

		jcolibri.method.revise.DefineNewIdsMethod.defineNewIdsMethod(retainedCase, componentsKeys);
		//Uncomment next line to store cases into persistence
		jcolibri.method.retain.StoreCasesMethod.storeCase(_caseBase, retainedCase);

		postCycle();

	}
	
	
	//Вывод отобранных прецедентов по входным параметрам
	public ArrayList<CraneAllFields> getRetrievedCases(ArrayList<String> input)
//	public ArrayList<CraneAllFields> getRetrievedCases(int numCases, String Corrosion, String Id,
//			String Iskrivlenie, String Progib, String Skruchivanie, String Srok, String Viazkost)
			throws ExecutionException, IOException {
								
		 FileWriter write = new FileWriter("getRetrievedCases.txt");
		 PrintWriter text = new PrintWriter(write);
		 for(int i = 0; i < input.size() ; i++)
		      text.println(input.get( i ));
		 text.flush();			
		 write.close();
		
		//Инициализация _connector из xml-файла и определение типа базы прецедентов для _caseBase
		configure();
		//Инициализация базы прецедентов		
		preCycle(); //это равнозначно: _caseBase.init(_connector);
		
		//Этот блок для первоначально заполнения параметров TextInput, когда при загрузке программы 
		//выполняется searchCases(), а поля незаполнены
		int numCases = 0;
//		try{
			 numCases = Integer.parseInt(input.get(0));
//		}
//		catch(ExecutionException e){
//			System.out.println(e.getMessage());
//		}finally{
//			
//		}
		
		 
		//Эта строка фактически не работает, т.к. из флексового NumericStepper нормально передается число отбора
		if (numCases == 0) 		{numCases = 3;};
		
		// Execute NN
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(
				_caseBase.getCases(), 
				//defineQuery(Corrosion, Id, Iskrivlenie, Progib, Skruchivanie, Srok, Viazkost),
				setQuery(input),
				//defineSimilarityMethods()); //это основная была
				setSimilarityMethodsAuto(input));
				
		//отбор только нужного количества прецедентов = numCases
		eval = SelectCases.selectTopKRR(eval, numCases); 	 
					
		ArrayList<CraneAllFields> arrcaf = new ArrayList<CraneAllFields>();
		
		//Полностью вся информация о прецеденте (включая подобие) содержится в переменной eval, имеющей тип RetrievalResult.
		for(RetrievalResult rr: eval)
		{
			CraneDescription desc = (CraneDescription) rr.get_case().getDescription();
			CraneSolution    sol  = (CraneSolution) rr.get_case().getSolution();
		
			CraneAllFields caf = new CraneAllFields();
			caf.setId(rr.get_case().getID().toString());
			caf.setSimilarity(setRounding(rr.getEval(), 3));//степень подобия
			caf.setCorrosion(desc.getCorrosion());
			caf.setIskrivlenie(desc.getIskrivlenie());
			caf.setProgib(desc.getProgib());
			caf.setSkruchivanie(desc.getSkruchivanie());
			caf.setSrok(desc.getSrok());
			caf.setViazkost(desc.getViazkost());
			caf.setDopustimo(sol.getDopustimo());
			//caf.setJustificationSolution(rr.get_case().getJustificationOfSolution().toString());
			//caf.setResult(rr.get_case().getResult().toString()); 
			
			arrcaf.add(caf);
			
		}
		postCycle(); //это равнозначно: _connector.close();
		
		return arrcaf;//casesAll;
	} 
	
	//Функция для округления. Может использоваться для округления Similarity
	//Scale = количество знаков после запятой
	public double setRounding(double aValue, int Scale)
	  {
	    BigDecimal decimal = new BigDecimal(aValue);
	    decimal = decimal.setScale(Scale,BigDecimal.ROUND_DOWN); //decimal = decimal.setScale(10,BigDecimal.ROUND_UP);
	    return decimal.doubleValue();
	  }
	
	//тестирум связку valueObjects во Flex
	public CraneDescription voTest(CraneDescription cd) throws IOException {
	//public Publisher voTest(Publisher pub) throws IOException {
		
		 FileWriter write = new FileWriter("voTest.txt");
		 PrintWriter text = new PrintWriter(write);
		 //for(int i = 0; i < cd.size() ; i++)
		 
		 text.println(cd.toString());
		 	  
		 //	  if (cd.getProgib()==null) cd.setProgib(45.);
		 	  
//		      text.println(cd.getSrok());
//		      text.println(cd.getCorrosion());
//		      text.println(cd.getProgib());
//		      text.println(cd.getSkruchivanie());
		 text.flush();			
		 write.close();
		 
		 cd.setId(cd.getId()+"1985");
		 //cd.setCorrosion(77.);
		 cd.setSrok(111);
		 return cd;  //cd.toString();
		
	}
	
	public Publisher voTest(Publisher pub) throws IOException {
		
		 FileWriter write = new FileWriter("voTest.txt");
		 PrintWriter text = new PrintWriter(write);
		
		 text.println(pub.getName());
		 text.println(pub.getPrice());
		
		 text.flush();			
		 write.close();
		 
		 pub.setPrice(pub.getPrice()+1);
		 pub.setName(pub.getName()+"0511");
		 
		 return pub;  //cd.toString();
		
	}

}
