/**
 * IrisSolution.java
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
 * Bean storing the solution for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class CraneSolution implements CaseComponent {

	String Dopustimo;
	
	public String toString()
	{
		return Dopustimo;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("Dopustimo", this.getClass());
	}

	/**
	 * @return Returns the Dopustimo.
	 */
	public String getDopustimo() {
		return Dopustimo;
	}

	/**
	 * @param type The type to set.
	 */
	public void setDopustimo(String Dopustimo) {
		this.Dopustimo = Dopustimo;
	}
	
}
