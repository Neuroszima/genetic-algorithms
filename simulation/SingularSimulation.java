package simulation;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Random;

import program.Main;

public class SingularSimulation  {
	
	private double a, b, c, d, e;
	private double min, max;
	private int resolution, population;
	private int[] seeds, survivours, best_s, best_t;
	private double[] propabilities;
	private double[] results;
	private final static double crossingOverPropability = 0.8;
	private final static double mutationPropability = 0.003;
	private double eps;
	private static int step_count;
	BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
	Random rand = new Random();
	private int res;
	private int gene_count;
	
	double solution;
	
	Robot bot = new Robot();
	
	public SingularSimulation(int[] seeds, int resolution, double a, double b, double c, double d, double e, double min, double max, int population) throws AWTException, NumberFormatException, IOException
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.min = min;
		this.max = max;
		this.resolution = resolution;
		int temp = resolution;
		temp = 1<<temp;
		this.res = temp;
		this.seeds = seeds;
		this.population = population;
		this.eps = (this.max-this.min)/this.res;
		results = new double[resolution];
		
		this.runSimulation();
	}
	
	public SingularSimulation() throws IOException, AWTException, NumberFormatException 
	{
		
		double a, b, c, d, e;
		
		int resolution;
		int[] seeds;
		int population;
		
		System.out.println("podaj funkcję której max. chcesz wyznaczyć: (a+bx+cx^2+dx^3+ex^4)");
		a = simulation.Procedures.inputParam(buf, 1);
		b= simulation.Procedures.inputParam(buf, 2);
		c= simulation.Procedures.inputParam(buf, 3);
		d= simulation.Procedures.inputParam(buf, 4);
		e= simulation.Procedures.inputParam(buf, 5);
		System.out.println("podano równanie : "+a+" + "+b+"x + "+c+"x^2 + "+d+"x^3 + "+e+"x^4 ");
		
		Procedures.setZakres(buf);
		System.out.println("podany zakres: A="+Main.zakres_min+" , B="+Main.zakres_max);
		System.out.println("podaj l. genów w chromosomie: ");
		do
		{
			try
			{
				resolution = Integer.parseInt(buf.readLine());
			break;
			}catch (Exception ex)
			{
				System.out.println("podałeś złe dane, spróbuj jeszcze raz");
			}
		}while(true);
		
		System.out.println("podaj populację (il. startową osobników): ");
		do
		{
			try
			{
			population = Integer.parseInt(buf.readLine());
			break;
			}catch (Exception ex)
			{
				System.out.println("podałeś złe dane, spróbuj jeszcze raz");
			}
		}while(true);
		
		
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.min = program.Main.zakres_min;
		this.max = program.Main.zakres_max;
		this.resolution = resolution;
		int temp = resolution;
		temp = 1<<temp;
		this.res = temp;
		this.seeds = Procedures.makeSeeds(resolution, population);
		this.population = population;
		this.eps = (this.max-this.min)/this.res;
		results = new double[resolution];
		
		this.runSimulation();
		
	}

	
	
	public void runSimulation() throws NumberFormatException, IOException
	{
		
		System.out.println("podaj liczbę kroków symulacji które mają być wykonane:");
		step_count = Integer.parseInt(buf.readLine());
		this.printSeeds();
		this.printVariables();
		System.out.println("Czy rozpocząć proces?");
		String n = buf.readLine();
		for (int j = 0; j<step_count; j++)
		{
			System.out.println("petla :"+j);
			propabilities = this.calculatePropabilities();
			seeds = this.generateOffspring();
			this.crossingOver();
			this.mutationGenerator();
			survivours = this.countSurviours();
		}
		this.calculateSolution();
		
	}
	
	

	private double[] calculatePropabilities()
	{
		double[] aim_function = new double[population];
		double a_prim = 0;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal fraction = new BigDecimal(0);
		double loc_e = this.e;
		double loc_d = this.d;
		this.results = new double[population];
		
		if ( loc_e == 0 && loc_d == 0 ) //gdy największy współczynnik jest przy x^2
		{
			for (int i= 0; i<population; i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				results[i] = a + a_prim + b*x + c*x*x;
				if (results[i] < 0)
				{
					a_prim = a_prim - (results[i]);
				}
				
			}
			System.out.println(" wartości funkcji będą zawyżone o: "+a_prim+"\n");
			for (int i = 0; i<population;i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				
				results[i] = a + a_prim + b*x + c*x*x;
				fraction = new BigDecimal(results[i]);
				sum = sum.add(fraction);
				
			}
			System.out.println("końcowa suma: " + sum.toEngineeringString() + "\n");
			for (int i = 0; i<population;i++)
			{
				fraction = new BigDecimal(results[i]);
				fraction = fraction.divide(sum, BigDecimal.ROUND_HALF_DOWN);
				aim_function[i] = fraction.doubleValue();
			}
		}else if ( loc_e == 0) //gdy współczynnik jest przy x^3
		{
			for (int i= 0; i<population; i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				results[i] = a + a_prim + b*x + c*x*x + d*x*x*x;
				if (results[i] < 0)
				{
					a_prim = a_prim - (results[i]);
				}
			}
			for (int i = 0; i<population;i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				results[i] = a + a_prim + b*x + c*x*x + d*x*x*x;
				fraction = new BigDecimal(results[i]);
				sum = sum.add(fraction);
			}
			for (int i = 0; i<population;i++)
			{
				fraction = new BigDecimal(results[i]);
				fraction = fraction.divide(sum, BigDecimal.ROUND_HALF_DOWN);
				aim_function[i] = fraction.doubleValue();
			}
		}else //przy danych wszystkich współczynnikach
		{
			for (int i= 0; i<population; i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				results[i] = a + a_prim + b*x + c*x*x + d*x*x*x + e*x*x*x*x;
				if (results[i] < 0)
				{
					a_prim = a_prim - (results[i]);
				}
			}
			for (int i = 0; i<population;i++)
			{
				double x = this.min + (seeds[i]*(this.eps));
				results[i] = a + a_prim + b*x + c*x*x + d*x*x*x + e*x*x*x*x;
				fraction = new BigDecimal(results[i]);
				sum = sum.add(fraction);
			}
			for (int i = 0; i<population;i++)
			{
				fraction = new BigDecimal(results[i]);
				fraction = fraction.divide(sum, BigDecimal.ROUND_HALF_DOWN);
				aim_function[i] = fraction.doubleValue();
			}
		}
		return aim_function;
	}
	
	private int[] generateOffspring()
	{
		int[] offspring = new int[population];
		double[] wheel = new double[population];
		wheel[0] = propabilities[0];
		
		double ball;
		for (int i = 1; i<population; i++) //tworzenie ruletki
		{
			wheel[i] = wheel[i-1] + propabilities[i];
				
		}
		System.out.println("Rozpoczynam losowanie");
		for (int i = 0; i<population; i++)
		{
			ball = rand.nextDouble();
			for(int j = 0; j<population; j++)
			{
				
				if (ball < wheel[j])
				{
					offspring[i] = seeds[j];
					
					break;
				}
			}
		}
		return offspring;
	}
	
	public void printSeeds()
	{
		int mask = 0x0001;
		int temp;
		String[] seeds_print= new String[population];
		for (int i = 0; i<population; i++)
		{
			temp = seeds[i];
			String seed = "";
			for (int j = 0; j<resolution; j++)
			{
				if ((temp & mask) == 0)
				{
					seed = seed + "0";
					temp = temp>>>1;
				}else
				{
					seed = seed + "1";
					temp = temp>>>1;
				}
			}
			seeds_print[i] = seed + "  ";
		}
		String spacer = "";
		int digits = (int) Math.log10(seeds[population/2]);
		for (int i = 0; i<digits; i++)
		{
			spacer = spacer + " ";
		}
		System.out.println("\npopulacja: (" +population+" osobników) \n");
		String getformat = "%" + Integer.toString((resolution)-2) + "d" + spacer + "  ";
		System.out.format(seeds_print[0] +" %n", 0);
		System.out.format(getformat + "%n", seeds[0]);
		int end = 0;
		for (int i = 1; i<population;i++)
		{
			//seeds_print[i] +" "+ seeds_print[i+1] +" "+ seeds_print[i+2] +" "+ seeds_print[i+3] + "\n"
			System.out.format(seeds_print[i] +" ", 0);
			if (i%4 == 0)
			{
				System.out.format("%n", 0);
				for (int j = 0; j<4; j++)
				{
					System.out.format(getformat, seeds[i-(3-j)]);
					
				}
				System.out.format("%n%n", 0);
				end = i;
				
			}
			if ((end + 4) >= population)
			{
				
				end = population - end;
				for (int j = 1; j<end;j++)
				{
					System.out.format(seeds_print[i+j] +" ", 0);
				}
				System.out.format("%n", 0);
				for (int j = 1; j<end; j++)
				{
					System.out.format(getformat, seeds[i+j]);
				}
				System.out.println();
				break;
			}
			
		}
	}
	
	private void crossingOver()
	{
		double event = rand.nextDouble();
		double[] wheel = new double[population];
		wheel[0] = propabilities[0];
		for (int i = 1; i<population; i++) //tworzenie ruletki
		{
			wheel[i] = wheel[i-1] + propabilities[i];
		}
		
		/*
		 * System.out.println("\nCS dało rezultat:");
			double couple1 = rand.nextDouble();
			double couple2 = 0;
		 *
		 *kod na pozniej
		 */
		
		int c1 = 0, c2 = 0;
		int location_c1 =0, location_c2 =0;
		int cs_num = 0;
			
			for (int i = 0; i<population; i++) // mnożymy sąsiadów
			{
				if ((i+1) > population)
					break;
				location_c1 = i;
				location_c2 = i+1;
				c1 = seeds[i];
				c2 = seeds[i+1];
				event = rand.nextDouble();
				if (event < crossingOverPropability)
				{
					int temp1 = c1, temp2 = c2;
					int mask = rand.nextInt(this.resolution);
					mask = (1<<mask)-1;
					int temp_0_1, temp_0_2;
					temp_0_1 = (c1 & ~mask);
					temp_0_2 = (c2 & ~mask);
					temp1 = (temp1 & (mask));
					temp2 = (temp2 & (mask));
				
					c1 = ( temp_0_1 | temp2);
					c2 = ( temp_0_2 | temp1);
					cs_num=cs_num+1;
				}
				i++;
				
				seeds[location_c1] = c1;
				seeds[location_c2] = c2;
				
			}
			System.out.println("liczba cs-ov :"+cs_num);
		
		/*int z=0;
		do 
		{
			for(int j = z; j<population; j++)
			{
				if (couple1 < wheel[j])
				{
					c1 = seeds[j];
					location_c1 = j;
					break;
				}
			}
			do
			{
				couple2 = rand.nextDouble();
				for(int j = z; j<population; j++)
				{
					if (couple2 < wheel[j])
					{
						c2 = seeds[j];
						location_c2 = j;
						break;
					}
				}
			}while (location_c1 == location_c2);
			z = z +2;
		}while (z<population);
		
		*
		*kod na pozniej
		*
		*/
		
	}

	private void mutationGenerator()
	{
		int mask;
		double mut = 0;
		int x=0;
		for (int i = 1; i<population;i++)
		{
			mask = 0x0001;
			int temp = seeds[i];
			for (int j = 0; j<resolution; j++)
			{
				mut = rand.nextDouble();
				if (mut <= mutationPropability)
				{
					temp = (temp ^ mask);
					mask = mask<<1;
					x++;
				}else
				{
					mask = mask<<1;
				}
			}
			seeds[i] = temp;
		}
		System.out.println("Dokonano " + x + " mutacji.");
	}
	
	public void printVariables()
	{
		System.out.println("równanie: ");
		System.out.println("podano  : "+a+" + "+b+"x + "+c+"x^2 + "+d+"x^3 + "+e+"x^4 "+"\n");
		System.out.println("długość genu i rozdzielczość:");
		System.out.println("r = "+this.res+"\nresolution = "+this.resolution+"\n");
		System.out.println("zakres, min = "+ this.min+ ", max = "+this.max);
		System.out.println("Liczba kroków i \"eps\":");
		System.out.println("eps = "+this.eps+" , steps = "+this.step_count);
	}
	
	public void getResult()
	{
		int[] best = new int[3];
		System.out.println("Rezultat próby:");
		System.out.println("Najlepsze genotypy :");
		best = showBest();
		for (int i = 0; i < 3; i++)
		{
			System.out.println("Genotyp : "+ best_t[best_s[i]] + " liczący : " + best[best_s[i]]);
		}
		
	}
	
	private int[] showBest() 
	{
		int[] max = new int[3];
		best_s = new int[3];
		
		int memo1=0, memo2=0;
		for (int i = 0; i < 3;i++)
		{
			for (int j = 0; j< gene_count;j++)
			{
				if (memo1 !=0)
				{
					if (memo2 != 0)
					{
						if (max[i] < survivours[j])
						{
							max[i] = survivours[j];
							best_s[i] = j;
						}
					}else
					{
						if (max[i] < survivours[j])
						{
							max[i] = survivours[j];
							memo2 = j;
							best_s[i] = j;
						}
					}
				}else
				{
					if (max[i] < survivours[j])
					{
						max[i] = survivours[j];
						memo1 = j;
						best_s[i] = j;
					}
				}
			}
		}
		
		return max;
		
	}

	private int[] countSurviours()
	{
		int[] survivours_local = new int[population];
		int[] genotypes = new int[population];
		int geno_count = 1;
		int mask = seeds[0];
		genotypes[0] = mask;
		
		for (int i = 0; i<population; i++) //policz liczbę różnych genotypów i zapisz oryginalne w tablicy
		{
			
			for (int j = 0; j<geno_count; j++)
			{
				mask = genotypes[j];
				if ( seeds[i] == mask)
				{
					survivours_local[j]++;
					mask = genotypes[0];
					break;
				}else
				{
					if ( (j) == geno_count-1)
					{
						geno_count++;
						genotypes[j+1] = seeds[i];
						survivours_local[j+1]++;
						mask = genotypes[0];
						break;
					}
				}
			}
		}
		System.out.println();
		System.out.println("Liczba oryginalnych genotypów : " + geno_count);
		System.out.println("podsumowanie: ");
		for (int i = 0; i<geno_count; i++)
		{
			System.out.println("genotyp: "+ genotypes[i]+" jest reprezentowany przez: " + survivours_local[i]+" rozbitków");
		}
		gene_count = geno_count;
		best_t = genotypes;
		
		return survivours_local;
	}
	
	private void calculateSolution() {
		
		double avg_x = 0;
		for (int i = 0; i<population;i++)
		{
			double x = this.min + (seeds[i]*(this.eps));
			avg_x = avg_x + x;		
		}
		
		avg_x = avg_x/population;
		System.out.println("końcowa wartość średnia x = "+ avg_x);
	}
}

class Procedures 
{
	public static double inputParam(BufferedReader b_read, int param) throws NumberFormatException, IOException
	{
		double value;
		System.out.println("podaj parametr nr."+ param);
		do
		{
			try
			{
				value = Double.parseDouble(b_read.readLine());
				break;
			}catch (Exception exc)
			{
				System.out.println("podałeś nieprawidłowe dane, powtórz wprowadzanie danych");
			}
		}while (true);
		
		return value;
	}
	
	public static void setZakres(BufferedReader b_read)
	{
		System.out.println("Podaj zakres wyliczeń: (wartość startowa, potem końcowa):");
		do
		{
			try 
			{
				program.Main.zakres_min = Double.parseDouble(b_read.readLine());
				break;
			}catch (Exception e)
			{
				System.out.println("podałeś zły zakres, spróbuj ponownie.");
			}
		}while (true);
		do
		{
			try 
			{
				program.Main.zakres_max = Double.parseDouble(b_read.readLine());
				break;
			}catch (Exception e)
			{
				System.out.println("podałeś zły zakres, spróbuj ponownie.");
			}
		}while (true);
	}
	
	public static int[] makeSeeds(int resolution, int population)
	{
		int seeds[] = new int[population];
		Random rand = new Random();
		int temp = 1<<resolution;
		for (int i = 0; i < population ; i++)
		{
			seeds[i] = rand.nextInt(temp);
		}
		
		return seeds;
	}
}