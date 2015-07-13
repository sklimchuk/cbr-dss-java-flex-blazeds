/**
 * IrisDescription.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garcнa.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package BridgeCrane;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean storing the description for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */

//The CraneDescription POJO is shown below.
//конструктор с параметрами пока не делал - это поламает схему работы, т.к. присоздании объекта CraneDescription
//нужно будет вводить сразу параметры, а сейчас этого нет.

public class CraneDescription implements CaseComponent {

	Double Srok;
	Double Progib;
	Double Iskrivlenie;
	Double Skruchivanie;
	Double Viazkost;
	Double Corrosion;
	String id;
	
	public CraneDescription(){
		
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[id=" + id + ", Srok=" + Srok + ", Progib=" + Progib
				+ ", Iskrivlenie=" + Iskrivlenie + ", Skruchivanie="
				+ Skruchivanie + ", Viazkost=" + Viazkost + ", Corrosion="
				+ Corrosion + "]";
	}

	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the srok
	 */
	
	//первоначально было: double getSrok() {
	//для double при вводе чисел не обязательно ставить десятичную точку, достаточно: 26 вместо 26.
	
	public double getSrok() {    
		return Srok;
	}

	/**
	 * @param srok the srok to set
	 */
	//первоначально было: void setSrok(double srok)
	public void setSrok(double srok) {  
		Srok = srok;
	}

	/**
	 * @return the progib
	 */
	public Double getProgib() {
		return Progib;
	}

	/**
	 * @param progib the progib to set
	 */
	public void setProgib(Double progib) {
		Progib = progib;
	}

	/**
	 * @return the iskrivlenie
	 */
	public Double getIskrivlenie() {
		return Iskrivlenie;
	}

	/**
	 * @param iskrivlenie the iskrivlenie to set
	 */
	public void setIskrivlenie(Double iskrivlenie) {
		Iskrivlenie = iskrivlenie;
	}

	/**
	 * @return the skruchivanie
	 */
	public Double getSkruchivanie() {
		return Skruchivanie;
	}

	/**
	 * @param skruchivanie the skruchivanie to set
	 */
	public void setSkruchivanie(Double skruchivanie) {
		Skruchivanie = skruchivanie;
	}

	/**
	 * @return the viazkost
	 */
	public Double getViazkost() {
		return Viazkost;
	}

	/**
	 * @param viazkost the viazkost to set
	 */
	public void setViazkost(Double viazkost) {
		Viazkost = viazkost;
	}

	/**
	 * @return the corrosion
	 */
	public Double getCorrosion() {
		return Corrosion;
	}

	/**
	 * @param corrosion the corrosion to set
	 */
	public void setCorrosion(Double corrosion) {
		Corrosion = corrosion;
	}

		
	
}
