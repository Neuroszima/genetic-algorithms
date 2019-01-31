package program;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import simulation.*;


public class Main {
	public static double zakres_min;
	public static double zakres_max;
	
	public static void main(String[] args) throws NumberFormatException, IOException, AWTException {
		InputStreamReader input_stream = new InputStreamReader(System.in);
		BufferedReader buf_read = new BufferedReader(input_stream);
		
		double a, b, c, d, e;
		
		
		int resolution;
		int[] seeds;
		int population;
		
		System.out.println("podaj funkcję której max. chcesz wyznaczyć: (a+bx+cx^2+dx^3+ex^4)");
		a = program.Procedures.inputParam(buf_read, 1);
		b= program.Procedures.inputParam(buf_read, 2);
		c= program.Procedures.inputParam(buf_read, 3);
		d= program.Procedures.inputParam(buf_read, 4);
		e= program.Procedures.inputParam(buf_read, 5);
		System.out.println("podano równanie : "+a+" + "+b+"x + "+c+"x^2 + "+d+"x^3 + "+e+"x^4 ");
		
		Procedures.setZakres(buf_read);
		System.out.println("podany zakres: A="+Main.zakres_min+" , B="+Main.zakres_max);
		System.out.println("podaj l. genów w chromosomie: ");
		
		do
		{
			try
			{
				resolution = Integer.parseInt(buf_read.readLine());
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
			population = Integer.parseInt(buf_read.readLine());
			break;
			}catch (Exception ex)
			{
				System.out.println("podałeś złe dane, spróbuj jeszcze raz");
			}
		}while(true);
		seeds = program.Procedures.makeSeeds(resolution, population);
		SingularSimulation try_nr_0 = new SingularSimulation(seeds, resolution, a, b, c, d, e, Main.zakres_min, Main.zakres_max, population);
		do
		{
			System.out.println("Czy chcesz jeszcze raz skorzystać z programu?");
			String n  = buf_read.readLine();
			if (n.equals("T") || n.equals("t"))	
			{
				System.out.println("Czy chcesz zmienić dane?");
				n  = buf_read.readLine();
				if (n.equals("T") || n.equals("t"))	
				{
					try_nr_0 = new SingularSimulation();
				}else
				{
					try_nr_0.runSimulation();
				}
			}else
			{
				break;
			}
		}while (true);
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
