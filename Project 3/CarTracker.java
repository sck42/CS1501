
import java.util.*; 
import java.io.*; 

public class CarTracker
{
	static final int SIZE = 45; 
	static IndexMinPQ<Car> pricePQ = new IndexMinPQ<Car>(SIZE);
	static IndexMinPQ<Car> mpgPQ = new IndexMinPQ<Car>(SIZE); 
	static int carIndex = 0; 
	static HashMap hm = new HashMap(); 
	public static void main(String[] args)
	{ 
	
		//Print Main Menu
		System.out.println("/////////////////////////////////////////////////");
		System.out.println("WELCOME TO CAR TRACKER!"); 
		System.out.println("Please Select An Option!"); 
		System.out.println("1) Add a Car"); 
		System.out.println("2) Update a Car"); 
		System.out.println("3) Remove a Car"); 
		System.out.println("4) Retrieve the Lowest Price"); 
		System.out.println("5) Retrieve the Lowest Mileage"); 
		System.out.println("6) Retrieve the Lowest Price by Make and Model"); 
		System.out.println("7) Retrieve the Lowest Mileage by Make and Model"); 
		System.out.println("8) Quit"); 
		System.out.println("/////////////////////////////////////////////////"); 
		Scanner sc = new Scanner(System.in); 
		int option = sc.nextInt(); 
		while(option != 8)
		{
			switch(option)
			{
				case 1:
					addCar(); 
					break; 
				case 2:
					if(!pricePQ.isEmpty())
						updateCar();
					else
						System.out.println("No Cars Entered"); 
					break;
				case 3:
					if(!pricePQ.isEmpty())
						removeCar();
					else
						System.out.println("No Cars Entered");
					break; 
				case 4: 
					if(!pricePQ.isEmpty())
					{
						Car c = (Car) pricePQ.minKey(); 
						System.out.println("4) Lowest Price Car");
						c.printCar(); 
					}
					else
						System.out.println("No Cars Entered");
					break; 
				case 5:
					if(!mpgPQ.isEmpty())
					{
						Car s = (Car) mpgPQ.minKey(); 
						System.out.println("5) Lowest Mileage Car");
						s.printCar(); 
					}
					else
						System.out.println("No Cars Entered");
					break; 
				case 6:
					if(!pricePQ.isEmpty())
					{
						System.out.println(); 
						System.out.println("6) Retrieve the Lowest Price by Make and Model "); 
						retrieve(true); 
					}
					else
						System.out.println("No Cars Entered");
					break; 
				case 7:
					if(!mpgPQ.isEmpty())
					{
						System.out.println(); 
						System.out.println("7) Retrieve the Lowest Mileage by Make and Model "); 
						retrieve(false); 
					}
					else
						System.out.println("No Cars Entered");
					break; 
				default: 
					System.out.println("INVALID ENTRY"); 
					break; 
			}
			System.out.println(); 
			System.out.println("/////////////////////////////////////////////////"); 
			System.out.println("Please Select An Option!"); 
			System.out.println("1) Add a Car"); 
			System.out.println("2) Update a Car"); 
			System.out.println("3) Remove a Car"); 
			System.out.println("4) Retrieve the Lowest Price"); 
			System.out.println("5) Retrieve the Lowest Mileage"); 
			System.out.println("6) Retrieve the Lowest Price by Make and Model"); 
			System.out.println("7) Retrieve the Lowest Mileage by Make and Model"); 
			System.out.println("8) Quit"); 
			System.out.println("/////////////////////////////////////////////////"); 
			option = sc.nextInt();
		}
		System.out.println("Thank You for Using Car Tracker!"); 
	}
	
	//Ask user for vin, make, model, price, mpg and color to make/add a car. 
	public static void addCar()
	{
		System.out.println(); 
		System.out.println("1) Add a Car"); 
		System.out.println("Please enter the VIN number"); 
		Scanner sc = new Scanner(System.in); 
		String vin = sc.next(); 
		System.out.println("Please enter the make"); 
		String make = sc.next(); 
		System.out.println("Please enter the model"); 
		String model = sc.next(); 
		System.out.println("Please enter the Price"); 
		double price = sc.nextDouble(); 
		System.out.println("Please enter the Mileage"); 
		double mpg = sc.nextDouble(); 
		System.out.println("Please enter the Color"); 
		String color = sc.next(); 
		//Check if car has already been added and add if not found. 
		if(!hm.containsKey(vin))
		{
			mpgPQ.insert(carIndex, new Car(vin, make, model, price, mpg, color, false)); 
			pricePQ.insert(carIndex, new Car(vin, make, model, price, mpg, color, true));
			hm.put(vin, carIndex);
			carIndex++; 
		}
		else 
			System.out.println("Car Previously Entered"); 
		System.out.println("DONE"); 
	}
	
	//Ask user for the vin of the car they want to update. 
	public static void updateCar()
	{
		System.out.println(); 
		System.out.println("2) Update a Car"); 
		System.out.println("Please enter the VIN number"); 
		Scanner sc = new Scanner(System.in); 
		String vin = sc.next(); 
		double price = 0.0;
		double mpg = 0.0; 
		String color = ""; 
		//Check if vin exists. If found ask user what they want to change and change that. 
		if(hm.containsKey(vin))
		{
			int index = (int)hm.get(vin); 
			Car c = (Car) pricePQ.keyOf(index); 
			System.out.println("Car Found!");
			c.printCar();
			System.out.println("/////////////////////////////////////////////////"); 
			System.out.println("Please Select An Option!"); 
			System.out.println("1) Change the Price"); 
			System.out.println("2) Change the Mileage"); 
			System.out.println("3) Change the Color"); 
			System.out.println("/////////////////////////////////////////////////"); 
			int option = sc.nextInt(); 
			switch(option)
			{
				case 1:
					System.out.println("Please enter the Price"); 
					price = sc.nextDouble(); 
					c.setPrice(price); 
					break; 
				case 2:
					System.out.println("Please enter the Mileage"); 
					mpg = sc.nextDouble();
					c.setMPG(mpg); 
					break; 
				case 3: 
					System.out.println("Please enter the Color"); 
					color = sc.next();
					c.setColor(color); 
					break; 
				default:
					System.out.println("INVALID INPUT"); 
					break; 
			}
			pricePQ.changeKey(index, c);
			mpgPQ.changeKey(index, c);			
		}
		else 
			System.out.println("Car Not Found"); 
		System.out.println("DONE"); 
	}
	
	//Ask user for the vin of the car they want to remove. 
	public static void removeCar()
	{
		System.out.println(); 
		System.out.println("3) Remove a Car"); 
		System.out.println("Please enter the VIN number"); 
		Scanner sc = new Scanner(System.in); 
		String vin = sc.next(); 
		
		//Check to see if car exists. If found remove it. 
		if(hm.containsKey(vin))
		{
			int index = (int) hm.get(vin); 
			Car c = (Car) pricePQ.keyOf(index);
			System.out.println("Car Found!");
			c.printCar(); 
			System.out.println("Removing Car..."); 
			pricePQ.delete(index); 
			mpgPQ.delete(index);
			hm.remove(vin); 
		}
		else
			System.out.println("Car Not Found");
		System.out.println("DONE"); 
	}
	
	//Ask user for the make and model of the car. 
	public static void retrieve(boolean isPrice)
	{
		Scanner sc = new Scanner(System.in); 
		System.out.println("Please enter the make"); 
		String make = sc.next(); 
		System.out.println("Please enter the model"); 
		String model = sc.next(); 
		//If they wanted the min price look in the price priority queue. 
		if(isPrice)
		{
			for(int i = 1; i <= SIZE; i++)
			{
				Car c = (Car) pricePQ.retrieve(i); 
				if(c.getMake().equals(make) && c.getModel().equals(model))
				{
					System.out.println("Car Found!");
					c.printCar(); 
					return; 
				}
			}
		}
		//if they wanted the min mgp look in the mpg priority queue. 
		else
		{
			for(int i = 1; i <= SIZE; i++)
			{
				Car c = (Car) mpgPQ.retrieve(i); 
				if(c.getMake().equals(make) && c.getModel().equals(model))
				{
					System.out.println("Car Found!");
					c.printCar(); 
					return; 
				}
			}
		}
		System.out.println("Car Not Found"); 
	}
}