import java.io.*; 

public class DLBTest
{
	public static void main(String[] args)
	{
		/* DLB dlb = new DLB(); 
		if(dlb.add("Hello", 1))
		{
			if(dlb.add("Hi", 2))
			{
				System.out.println("YES!!!!!!!!!!!!!!!"); 
				System.out.println("DLB SEARCH:" + dlb.find("Hi")); 
			}
			else
				System.out.println("SECOND FAILED!!!!!!!!!!!!"); 
		}
		else
		{
			System.out.println("BOOOOOOOOOOOOOO"); 
		} */
		DLB dlb = new DLB(); 
	/* 	dlb.add("stop", 0);
		dlb.add("start", 1);
		dlb.add("hi", 2); 
		dlb.add("sup", 998); 
		dlb.add("and", 200); 
		dlb.add("a", 340); 
		dlb.add("amount", 944); 
		dlb.add("Congratulations", 4); 
		dlb.add("all", 30); 
		System.out.println("DLB CONGRATULATIONS: " + dlb.find("Congratulations"));
		System.out.println("DLB STOP: " + dlb.find("stop")); 
		System.out.println("DLB START: " + dlb.find("start")); 
		System.out.println("DLB AMOUNT: " + dlb.find("amount")); 
		System.out.println("DLB AND: " + dlb.find("and")); 
		System.out.println("DLB A: " + dlb.find("a")); 
		System.out.println("DLB HI: " + dlb.find("hi")); 
		System.out.println("DLB SUP: " + dlb.find("sup")); 
		System.out.println("DLB ALL: " + dlb.find("all"));  */
		dlb.add("the", 0); 
		dlb.add("be", 1); 
		dlb.add("of", 2); 
		dlb.add("and", 3); 
		dlb.add("a", 4); 
		dlb.add("to", 5); 
		dlb.add("in", 6); 
		dlb.add("he", 7); 
		dlb.add("all", 8); 
		
		System.out.println("DLB THE: " + dlb.find("the"));
		System.out.println("DLB BE: " + dlb.find("be")); 
		System.out.println("DLB OF: " + dlb.find("of")); 
		System.out.println("DLB AND: " + dlb.find("and")); 
		System.out.println("DLB A: " + dlb.find("a")); 
		System.out.println("DLB TO: " + dlb.find("to"));
		System.out.println("DLB IN: " + dlb.find("in")); 
		System.out.println("DLB HE: " + dlb.find("he")); 
		System.out.println("DLB ALL: " + dlb.find("all")); 
		
		 
	}
}