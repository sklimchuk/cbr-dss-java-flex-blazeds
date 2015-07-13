package BridgeCrane;

public class CraneAllFields{

	//CraneDescription
	Double Srok;
	Double Progib;
	Double Iskrivlenie;
	Double Skruchivanie;
	Double Viazkost;
	Double Corrosion;
	String id;
	//CraneSolution
	String Dopustimo;
	//Additional fields
	Double Similarity;
	String JustificationSolution;
	String Result;
	
	//геттеры здесь не сильно и нужны...
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSrok() {
		return Srok;
	}
	
	public void setSrok(double srok) {
		Srok = srok;
	}
	
	public Double getProgib() {
		return Progib;
	}

	public void setProgib(Double progib) {
		Progib = progib;
	}

	public Double getIskrivlenie() {
		return Iskrivlenie;
	}

	public void setIskrivlenie(Double iskrivlenie) {
		Iskrivlenie = iskrivlenie;
	}

	public Double getSkruchivanie() {
		return Skruchivanie;
	}

	public void setSkruchivanie(Double skruchivanie) {
		Skruchivanie = skruchivanie;
	}

	public Double getViazkost() {
		return Viazkost;
	}

	public void setViazkost(Double viazkost) {
		Viazkost = viazkost;
	}

	public Double getCorrosion() {
		return Corrosion;
	}

	public void setCorrosion(Double corrosion) {
		Corrosion = corrosion;
	}
	//CraneSolution
	public String getDopustimo() {
		return Dopustimo;
	}

	public void setDopustimo(String Dopustimo) {
		this.Dopustimo = Dopustimo;
	}
	//Additional fields	
	public Double getSimilarity() {
		return Similarity;
	}
	
	public void setSimilarity(Double similarity) {
		Similarity = similarity;
	}

	/**
	 * @return the justificationSolution
	 */
	public String getJustificationSolution() {
		return JustificationSolution;
	}

	/**
	 * @param justificationSolution the justificationSolution to set
	 */
	public void setJustificationSolution(String justificationSolution) {
		JustificationSolution = justificationSolution;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return Result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		Result = result;
	}
	
}


