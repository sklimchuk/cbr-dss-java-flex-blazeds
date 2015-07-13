package BridgeCrane;

public class Publisher {
	private String name;
	private double price; 

	//несмотря на то, что пишется: You must provide an empty constructor.
	//работает передача и без него
	
    public String getName() {
        return name;
	}
    public void setName(String name) {
       this.name = name;
    }
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Publisher [name=" + name + ", price=" + price + "]";
	}


}
