

public class Car implements Comparable<Car>{
	private String vin; 
	private String make; 
	private String model; 
	private double price; 
	private double mpg; 
	private String color; 
	private boolean comPrice; 
	public Car(String v, String mk, String md, double p, double mp, String c, boolean b)
	{
		vin = v; 
		make = mk; 
		model = md; 
		price = p; 
		mpg = mp; 
		color = c; 
		comPrice = b; 
	}
	public Car(String mk, String md)
	{
		make = mk; 
		model = md; 
	}
	public void setPrice(double p)
	{
		price = p; 
	}
	public void setMPG(double m)
	{
		mpg = m; 
	}
	public void setColor(String c)
	{
		color = c; 
	}
	public String getVin()
	{
		return vin; 
	}
	public String getMake()
	{
		return make; 
	}
	public String getModel()
	{
		return model; 
	}
	public double getPrice()
	{
		return price; 
	}
	public double getMPG()
	{
		return mpg; 
	}
	public String getColor()
	{
		return color; 
	}
	//Check which to compare then compare them. 
	public int compareTo(Car c)
	{
		if(comPrice)
			return Double.compare(this.price, c.getPrice()); 
		else
			return Double.compare(this.mpg, c.getMPG()); 
	}
	//Print car information. 
	public void printCar()
	{
		System.out.println("VIN Number: " + vin); 
		System.out.println("Make: " + make);
		System.out.println("Model: " + model);
		System.out.printf("Price: $%.2f\n", price);
		System.out.println("MPG: " + mpg);
		System.out.println("Color: " + color); 
	}
}