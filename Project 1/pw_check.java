//Sai Konduru 


import java.io.*; 
import java.util.*; 

public class pw_check
{
	static DLB dictDLB = new DLB(); 
	static int numLetters = 0; 
	static int numNumbers = 0; 
	static int numSymbols = 0;
	static char[] validChars = {'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 
                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
                            'u', 'v', 'w', 'x', 'y', 'z', '0', '2', '3', '5', '6', '7', '8', '9', '!', '@', '$', '^', '_', '*'};
	static char[] letters = {'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 
                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
							'u', 'v', 'w', 'x', 'y', 'z'};
	static char[] numbers = {'0', '2', '3', '5', '6', '7', '8', '9'};
	static char[] symbols = {'!', '@', '$', '^', '_', '*'};
	
	static double[][][][][] times = new double[validChars.length][validChars.length][validChars.length][validChars.length][validChars.length]; 
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		double index = 0.0; 
		if(args[0].equals("-find"))
		{
			//Generate dictionary 
			System.out.println("Generating Dictionary..."); 
			generateDictionaray(); 
			//Generate Passwords. 
			System.out.println("GENERATING PASSWORDS..."); 
			createPasswords(); 
			System.out.println("COMPLETE!"); 
		}
		else if(args[0].equals("-check"))
		{
			//Create an R-way trie with all valid passwords times. 
			System.out.println("Loading passwords..."); 
			File goodPasswordFile = new File("all_passwords.txt"); 
			Scanner goodPassScan = new Scanner(goodPasswordFile); 
			while(goodPassScan.hasNext())
			{
				String[] pass = goodPassScan.nextLine().split(",");  
				char[] password  = pass[0].toCharArray(); 
				times[indexOf(password[0])][indexOf(password[1])][indexOf(password[2])][indexOf(password[3])][indexOf(password[4])] = Double.parseDouble(pass[1]); 
			}
			System.out.println("Finished Loading!"); 
			
			//Keep asking for new password until user presses quit. 
			while(true)
			{
				System.out.println("Please Enter the Password You Want to Check or type quit to exit: "); 
				Scanner in = new Scanner(System.in); 
				String userPass = in.nextLine();
				if(userPass.equals("quit"))
				{
					System.out.println("Thank You For Using Password Cracker!"); 
					System.exit(0); 
				} 
				//If password isn't size of 5 fix it. 
				if (userPass.length() > 5)
				{
					userPass = userPass.substring(0, 5);
				}
				else if (userPass.length() < 5)
				{
					System.out.println(userPass); 
					int len = userPass.length();
					for (int i = 0; i < (5 - len); i++)
					{
						userPass += "b";
					}
					System.out.println(userPass); 
				} 
				char[] pass = userPass.toCharArray();
				//Check the r-way trie for the password and see if's time is valid. 
				if(indexOf(pass[0]) != -1 && indexOf(pass[1]) != -1 && indexOf(pass[2]) != -1 && indexOf(pass[3]) != -1 && indexOf(pass[4]) != -1)
				{
					//If password is found print the time. 
					if(times[indexOf(pass[0])][indexOf(pass[1])][indexOf(pass[2])][indexOf(pass[3])][indexOf(pass[4])] != 0.0)
					{
						System.out.println("Congratulations Your Password Was Found in " + times[indexOf(pass[0])][indexOf(pass[1])][indexOf(pass[2])][indexOf(pass[3])][indexOf(pass[4])] + " ms!"); 
					}
					//If not found give valid passwords. 
					else 
					{
						System.out.println("Sorry! Your Password was not found."); 
						findValid(pass); 
					}
				}
				else 
				{
					System.out.println("Sorry! Your Password was not found."); 
					findValid(pass); 
				}
				
			}
		}
	}
	public static void generateDictionaray() throws FileNotFoundException, IOException
	{
		double index = 0.0; 
		File dictionary = new File("dictionary.txt");
		Scanner dictionaryScan = new Scanner(dictionary); 
		//Look through file and makes a dictionary DLB. 
		while(dictionaryScan.hasNextLine())
		{
			String word = dictionaryScan.nextLine().toLowerCase(); 
			char[] wordArray = word.toCharArray();
			//Change ts to 7s, as to 4s, os to 0s, es to 3s, is to 1s, ls to 1s and ss to $s  
			for(int i = 0; i < wordArray.length; i++)
			{
				if(wordArray[i] == 't')
					wordArray[i] = '7'; 
				else if(wordArray[i] == 'a')
					wordArray[i] = '4';
				else if(wordArray[i] == 'o')
					wordArray[i] = '0'; 
				else if(wordArray[i] == 'e')
					wordArray[i] = '3'; 
				else if(wordArray[i] == 'i')
					wordArray[i] = '1'; 
				else if(wordArray[i] == 'l')
					wordArray[i] = '1'; 
				else if(wordArray[i] == 's')
					wordArray[i] = '$'; 
			}
			String newWord = new String(wordArray);
			index += 1.0; 
			if(!dictDLB.add(word, index))
			{
				System.out.println("FAILED TO ADD " + word); 
				System.exit(0); 
			}
			index += 1.0;
			if(!dictDLB.add(newWord, index))
			{
				System.out.println("FAILED TO ADD " + newWord); 
				System.exit(0);
			}
		}
	}
	public static void createPasswords() throws IOException
	{
		DLB passwords = new DLB(); 
		int index = 0; 
		
		StringBuilder word = new StringBuilder(); 
		File goodPasswordFile = new File("all_passwords.txt"); 
		FileWriter fWriter = new FileWriter(goodPasswordFile); 
		PrintWriter pWriter = new PrintWriter(fWriter); 
		double starttime = System.nanoTime(); 
		char[] wordArray = new char[5]; 
		StringBuilder temp = new StringBuilder(); 
		//Brute force search for all possible passwords while checking pruning rules. 
		for(int i = 0; i < validChars.length; i++)
		{
			word.append(validChars[i]);  
			addCount(validChars[i]); 
			if(dictDLB.find(word.toString()) == -1 && count(validChars[i]))
			{
				for(int j = 0; j < validChars.length; j++)
				{
					word.append(validChars[j]); 
					addCount(validChars[j]);
					if(dictDLB.find(word.toString()) == -1 && count(validChars[j]))
					{
						for(int k = 0; k < validChars.length; k++)
						{
							word.append(validChars[k]);  
							addCount(validChars[k]);
							if((dictDLB.find(word.toString()) == -1) && count(validChars[k]))
							{
								for(int l = 0; l < validChars.length; l++)
								{
									word.append(validChars[l]); 
									addCount(validChars[l]);
									if((dictDLB.find(word.toString()) == -1) && count(validChars[l]))
									{
										for(int m = 0; m < validChars.length; m++)
										{
											word.append(validChars[m]); 
											addCount(validChars[m]);
											if((dictDLB.find(word.toString()) == -1) && count(validChars[m]))
											{
												if(isLegal(word.toString()))
												{
													//Calculate runtime and write to file both word and string.  
													double time = (double)(System.nanoTime() - starttime); 
													pWriter.println(word.toString() + "," + time/(double)1000000);   
												}
											}
											word.deleteCharAt(4); 
											minusCount(validChars[m]); 
										}
									}
									word.deleteCharAt(3);
									minusCount(validChars[l]); 
								}
							}
							word.deleteCharAt(2);
							minusCount(validChars[k]); 
						}
					}
					word.deleteCharAt(1);
					minusCount(validChars[j]); 
				}
			}
			word.deleteCharAt(0);
			minusCount(validChars[i]); 
		}
		pWriter.close(); 
	}
	//Checks every charcter orientation and return if password is still legal. 
	public static boolean isLegal(String s)
	{
		char[] word = s.toCharArray(); 
		StringBuilder temp = new StringBuilder(); 
		temp.append(word[1]); 
		temp.append(word[2]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0); 
		
		temp.append(word[2]); 
		temp.append(word[3]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0); 
		
		temp.append(word[3]); 
		temp.append(word[4]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0); 
		
		temp.append(word[1]); 
		temp.append(word[2]); 
		temp.append(word[3]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(2); 
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0);
		
		temp.append(word[2]); 
		temp.append(word[3]); 
		temp.append(word[4]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(2); 
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0);
	
		temp.append(word[1]); 
		temp.append(word[2]); 
		temp.append(word[3]); 
		temp.append(word[4]); 
		if(dictDLB.find(temp.toString()) != -1)
		{
			return false; 
		}
		temp.deleteCharAt(3); 
		temp.deleteCharAt(2); 
		temp.deleteCharAt(1); 
		temp.deleteCharAt(0);
		

		return true; 
	}
	//Checks the character and increments the appropriate counter. 
	public static void addCount(char c)
	{
		if(c >= 97 && c <= 122)
		{
			numLetters++; 
		}
		else if(c >= 48 && c <= 57)
		{
			numNumbers++; 
		}
		else if(c == '!' || c == '@' || c == '$' || c == '^' || c == '_' || c == '*')
		{
			numSymbols++; 
		}
	}
	//Checks the 3-1 letters, 2-1 numbers, and 2-1 symbols rules. 
	public static boolean count(char c)
	{
		
		int numChars = numLetters + numNumbers + numSymbols; 
		
		switch(numChars)
		{
			case 1: 
			{
				return true; 
			}
			case 2: 
			{
				return true;
			}
			case 3: 
			{
				if(numNumbers > 2 || numSymbols > 2)
					return false; 
				break;
			}
			case 4:
			{
				if(numLetters > 3 || numNumbers > 2 || numSymbols > 2)
					return false; 
				break; 
			}
			case 5: 
			{
				if(!(numLetters >= 1 && numLetters <= 3) || !(numNumbers >= 1 && numNumbers <= 2) || !(numSymbols >= 1 && numSymbols <= 2))
					return false; 
				break; 
			}
		}
		return true; 
	}
	//Decrement the appropriate counters. 
	public static void minusCount(char c)
	{
		if(c >= 97 && c <= 122)
		{
			numLetters--; 
		}
		else if(c >= 48 && c <= 57)
		{
			numNumbers--; 
		}
		else if(c == '!' || c == '@' || c == '$' || c == '^' || c == '_' || c == '*')
		{
			numSymbols--; 
		}
	}
	//Hash the character to the appropriate index of the array. 
	public static int indexOf(char c)
	{
		if(c >=98 && c <= 104)
			return c - 90; 
		else if(c >= 106 && c <= 122)
			return c - 91; 
		else if(c == '0')
			return 0; 
		else if(c == '2' || c == '3')
			return c - 49; 
		else if(c >= 53 && c <= 57)
			return c - 50; 
		else if(c == '!')
			return 32; 
		else if(c == '@')
			return 33; 
		else if(c == '$')
			return 34; 
		else if(c == '^')
			return 35; 
		else if(c == '_')
			return 36; 
		else if(c == '*')
			return 37; 
		
		return -1; 
	}
	public static void findValid(char[] invalidPass)
	{

		int temp = 0; 
		numLetters = 0; 
		numNumbers = 0; 
		numSymbols = 0; 
		char[] newPass = new char[5]; 
		for(int i = 0; i < invalidPass.length; i++)
		{
			addCount(invalidPass[i]);
			if(invalidPass[i] == 'a' || invalidPass[i] == '4' || invalidPass[i] == '1' || invalidPass[i] == 'i' || !count(invalidPass[i]))
			{
				temp = i; 
				minusCount(invalidPass[i]); 
				break; 
			}
			else{
				newPass[i] = invalidPass[i]; 
			}
		}
		int index = temp; 
		String prefix = new String(newPass); 
		int numChars = numLetters + numSymbols + numNumbers;
		int count = 0; 
		
		//Checks each possible number of characters in prefix and creates 10 legal password. 
		//Check the created passwords to the R-way trie. 
		//If invalid ignore the password. 
		//If valid print out the password and time and increment counter. 
		if(numChars == 0)
		{
			outer:
			for(int m = 0; m < letters.length; m++)
			{
				
				for(int n = 0; n < numbers.length; n++)
				{
					
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{
							newPass[temp++] = letters[m]; 
							newPass[temp++] = letters[m];  
							newPass[temp++] = letters[m]; 
							newPass[temp++] = numbers[n]; 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
				}
			}
		}
		else if(numChars == 1)
		{
			outer:
			for(int m = 0; m < letters.length; m++)
			{
				
				for(int n = 0; n < numbers.length; n++)
				{
					
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{
							
							newPass[temp++] = letters[m];  
							newPass[temp++] = letters[m]; 
							newPass[temp++] = numbers[n]; 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
				}
			}
		
		}
		else if(numChars == 2)
		{
			if(numLetters == 1 || numLetters == 2)
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int n = 0; n < numbers.length; n++)
					{
						
						for(int l = 0; l < symbols.length; l++)
						{
							if(count != 10)
							{
								newPass[temp++] = letters[m]; 
								newPass[temp++] = numbers[n]; 
								newPass[temp] = symbols[l]; 
								temp = index; 
								double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
								if(time != 0.0)
								{
									System.out.println(new String(newPass) + ":        Time:" + time); 
									count++;
								}
							}
							else 
								break outer; 
						}
					}
				}
			}
			else if(numSymbols == 2)
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int n = 0; n < numbers.length; n++)
					{
						if(count != 10)
						{
							newPass[temp++] = letters[m];
							newPass[temp++] = letters[m];							
							newPass[temp++] = numbers[n];  
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer;
					}
				}
			}
			else if(numNumbers == 2)
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{
							newPass[temp++] = letters[m]; 
							newPass[temp++] = letters[m]; 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
					
				}
			}
			else if(numNumbers == 1 && numSymbols == 1)
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int n = 0; n < letters.length; n++)
					{
						
						for(int l = 0; l < letters.length; l++)
						{
							if(count != 10)
							{
								newPass[temp++] = letters[m]; 
								newPass[temp++] = letters[n]; 
								newPass[temp] = letters[l]; 
								temp = index; 
								double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
								if(time != 0.0)
								{
									System.out.println(new String(newPass) + ":        Time:" + time); 
									count++;
								} 
							}
							else 
								break outer; 
						}
					}
				}
			}
			
		}
		else if(numChars == 3)
		{
			if(numLetters == 3)
			{
				outer:
				for(int n = 0; n < numbers.length; n++)
				{
					
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{
							newPass[temp++] = numbers[n]; 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							}
						}
						else 
							break outer; 
					}
				}
			}
			else if(numNumbers == 2 || (numNumbers == 1 && numLetters == 2))
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{ 
							newPass[temp++] = letters[m]; 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
					
				}
			}
			else if(numSymbols == 2 || (numSymbols == 1 && numLetters == 2))
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					
					for(int n = 0; n < numbers.length; n++)
					{
						if(count != 10)
						{
							newPass[temp++] = letters[m];							
							newPass[temp++] = numbers[n];  
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer;
					}
				}
			}
			else if((numNumbers == 2 && numSymbols == 1) || (numSymbols == 2 && numNumbers == 1) || (numSymbols == 1 && numNumbers == 1 && numLetters == 1))
			{
				outer:
				for(int m = 0; m < letters.length; m++)
				{
					for(int n = 0; n < letters.length; n++)
					{
						if(count != 10)
						{
							newPass[temp++] = letters[m];
							newPass[temp] = letters[n]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer;	
					}
				}
			}
		}
		else if(numChars == 4)
		{
			if((numNumbers == 2 && numSymbols == 2) || (numSymbols == 2 && numNumbers == 1 && numLetters == 1) || (numNumbers == 2 && numSymbols == 1 && numLetters == 1))
			{
				for(int m = 0; m < letters.length; m++)
				{
					if(count != 10)
					{
						newPass[temp] = letters[m]; 
						temp = index; 
						double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
						if(time != 0.0)
						{
							System.out.println(new String(newPass) + ":        Time:" + time); 
							count++;
						} 
					}
					else 
						break;
				}
			}
			else if(numLetters == 3 && numNumbers == 1)
			{
				outer:
				for(int n = 0; n < numbers.length; n++)
				{
					for(int l = 0; l < symbols.length; l++)
					{
						if(count != 10)
						{ 
							newPass[temp] = symbols[l]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
					temp--; 
					if(newPass[temp] != numbers[n])
						newPass[temp] = numbers[n];
					else
						newPass[temp] = numbers[n+1]; 
					temp++; 
				}
			}
			else if(numLetters == 3 && numSymbols == 1)
			{
				outer:
				for(int l = 0; l < symbols.length; l++)
				{
					for(int n = 0; n < numbers.length; n++)
					{
						if(count != 10)
						{
							newPass[temp] = numbers[n]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break outer; 
					}
					temp--; 
					if(newPass[temp] == symbols[l])
						newPass[temp] = symbols[l]; 
					else 
						newPass[temp] = symbols[l+1]; 
					temp++; 
				}
			}
			else if(numLetters == 2)
			{
				if(numNumbers == 2)
				{
					
					outer:
					for(int n = 0; n < symbols.length; n++)
					{
						for(int l = 0; l < symbols.length; l++)
						{
							if(count != 10)
							{ 
								newPass[temp++] = symbols[l]; 
								temp = index; 
								double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
								if(time != 0.0)
								{
									System.out.println(new String(newPass) + ":        Time:" + time); 
									count++;
								} 
							}
							else 
								break outer; 
						}
						temp--; 
						if(newPass[temp] != symbols[n])
							newPass[temp] = symbols[n];
						else
							newPass[temp] = symbols[n + 1]; 
						temp++; 
					}
					
				}
				else if(numSymbols == 2)
				{
					outer:
					for(int n = 0; n < numbers.length; n++)
					{
						for(int l = 0; l < numbers.length; l++)
						{
							if(count != 10)
							{ 
								newPass[temp++] = numbers[l]; 
								temp = index; 
								double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
								if(time != 0.0)
								{
									System.out.println(new String(newPass) + ":        Time:" + time); 
									count++;
								} 
							}
							else 
								break outer; 
						}
						temp--; 
						if(newPass[temp] != numbers[n])
							newPass[temp] = numbers[n];
						else
							newPass[temp] = numbers[n+1]; 
						temp++; 
					}
				}
				else if(numNumbers == 1 && numSymbols == 1)
				{
					for(int m = 0; m < letters.length; m++)
					{
						if(count != 10)
						{
							newPass[temp] = letters[m]; 
							temp = index; 
							double time = times[indexOf(newPass[0])][indexOf(newPass[1])][indexOf(newPass[2])][indexOf(newPass[3])][indexOf(newPass[4])];
							if(time != 0.0)
							{
								System.out.println(new String(newPass) + ":        Time:" + time); 
								count++;
							} 
						}
						else 
							break;
					}
				}
			}
		}
	}
}






