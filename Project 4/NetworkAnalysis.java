import java.io.*; 
import java.util.*; 

public class NetworkAnalysis 
{
	private static EdgeWeightedGraph ewg; 
	public static void main(String[] args) 
	{
		File f;
		//Grab from from command line and create graph using file. 
		try
		{
			f = new File(args[0]);
			Scanner sc = new Scanner(f);
			ewg = new EdgeWeightedGraph(sc); 
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Exception: " + e); 
			return; 
		}
		
		System.out.println("///////////////////////////////////////////////"); 
		System.out.println("WELCOME!"); 
		showMenu(); 
		Scanner sc = new Scanner(System.in); 
		int option = sc.nextInt();  
		while(option != 6)
		{
			switch(option)
			{
				case 1: 
					getLowestPath(); 
					break; 
				case 2: 
					System.out.println("2) Copper-Only Connectioned"); 
					//Run Kruskals and check if there exists an edge within the path that isn't copper. 
					KruskalMST kl = new KruskalMST(ewg); 
					boolean isCopper = true; 
					for(Edge e : kl.edges())
					{
						if(!e.isCopper())
							isCopper = false; 
					} 
					if(isCopper)
					{
						System.out.println("The Entered Graph is Copper-Only Connected!"); 
						System.out.println("///////////////////////////////////////////////"); 
					}
					else 
					{
						System.out.println("The Entered Graph is not Copper-Only Connected!"); 
						System.out.println("///////////////////////////////////////////////"); 
					}
					break; 
				case 3: 
					maxData();
					try
					{
						Scanner s = new Scanner(f);
						ewg = new EdgeWeightedGraph(s); 
					}
					catch(FileNotFoundException e)
					{
						System.out.println("Exception: " + e); 
						return; 
					} 
					break; 
				case 4: 
					//Run Kruskals and print the edges of lowest latency tree. 
					System.out.println("4) Lowest Average Latency Spanning Tree"); 
					KruskalMST k = new KruskalMST(ewg); 
					int counter = 0; 
					System.out.print("Edges: ");
					for(Edge e : k.edges())
					{
						System.out.print(e.toString() + " "); 
						counter++; 
					}
					System.out.println(); 
					System.out.println("The Average Latency of the Tree is " + (k.weight()/counter)); 
					break; 
				case 5:  
					//Run dfs and check if any vertex with in the graph has less than or equal to two connections to the reset of the graph. 
					System.out.println("5) Failure Vertexes"); 
					DepthFirstSearch dfs = new DepthFirstSearch(ewg, 0); 
					if(dfs.fail())
						System.out.println("There is a failure point in the graph."); 
					else 
						System.out.println("There is no failure point in this graph."); 
					System.out.println("///////////////////////////////////////////////"); 
					break; 
				default: 
					System.out.println("Invalid Entry!"); 
					break; 
			}
			showMenu();
			option = sc.nextInt(); 
		}
		
	}
	//Print menu
	public static void showMenu()
	{
		System.out.println("Please Select an Option"); 
		System.out.println("1) Lowest Latency Path"); 
		System.out.println("2) Copper-Only Connectioned"); 
		System.out.println("3) Max Amount of Data"); 
		System.out.println("4) Lowest Average Latency Spanning Tree"); 
		System.out.println("5) Failure Vertexes"); 
		System.out.println("6) Quit"); 
		System.out.println("///////////////////////////////////////////////"); 
	}
	public static void getLowestPath()
	{
		//Ask user for source and end vertexes. 
		System.out.println("1) Lowest Latency Path"); 
		System.out.println("Please enter the source vertex"); 
		Scanner sc = new Scanner(System.in); 
		int s = sc.nextInt(); 
		System.out.println("Please enter the end vertex"); 
		int t = sc.nextInt(); 
		double min = 10000000000000000000.0; 
		//Run Dijkstras, Find smallest bandwidth within the path. 
		DijkstraSP dsp = new DijkstraSP(ewg, s); 
		System.out.print("Edges: ");
		for(Edge e : dsp.pathTo(t))
		{
			System.out.print(e.toString() + " ");
			if(min > e.weight())
				min = e.weight(); 
			
		} 
		//Print the lowest bandwidth and Latency. 
		System.out.printf("\nLowest Bandwidth is %.0f\n", min); 
		System.out.println("Lowest Latency from " + s + " to " + t + " = " + dsp.distTo(t) + " seconds"); 
		System.out.println("///////////////////////////////////////////////"); 
	}
	//Ask user for source and end vertexes. 
	//Run FordFulkerson and get the value of maxflow. 
	public static void maxData()
	{
		System.out.println("3) Max Amount of Data"); 
		System.out.println("Please enter the source vertex"); 
		Scanner sc = new Scanner(System.in); 
		int s = sc.nextInt(); 
		System.out.println("Please enter the end vertex"); 
		int t = sc.nextInt(); 
		FordFulkerson maxFlow = new FordFulkerson(ewg, s, t); 
		System.out.printf("The Max Amount of Data to Pass is %.0f\n", maxFlow.value()); 
		System.out.println("///////////////////////////////////////////////"); 
	}
}