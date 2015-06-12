package me.zeldafan42.TSP;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class TSP
{

	public static void main(String[] args)
	{
		TSP tsp = new TSP();
		tsp.run(args);
	}
	
	void run(String[] args)
	{
		if (args.length !=2)
		{
			System.out.println("USAGE: tsp -e|-n <Path to textfile>");
			return;
		}
			java.util.Locale.setDefault(java.util.Locale.ENGLISH);
			double adjMtx[][];
			double coords[][];
			int count = 0;
		//Read file
		try
		{
			Scanner sc = new Scanner(Paths.get(args[1], new String[0]));

			if (sc.hasNextInt())
			{
				count = sc.nextInt();
			}
			else
			{
				System.err.println("File is empty");
				sc.close();
				return;
			}

			if(count < 1)
			{
				sc.close();
				System.err.println("Count too small!!!!111");
				return;
			}

			coords = new double[count][2];

			for(int i = 0; i< count; i++)
			{
				for(int j = 0;j<2;j++)
				{
					if(sc.hasNextDouble())
					{
						coords[i][j] = sc.nextDouble(); 
					}
					else
					{
						System.err.println("File is incorrect, please check file!");
						sc.close();
						return;
					}
				}
			}
			
//			System.out.println(count);
//			
//			for(int i = 0; i< count; i++)
//			{
//				for(int j = 0;j<2;j++)
//				{
//					System.out.print(coords[i][j]+" ");
//				}
//				System.out.print(System.lineSeparator());
//			}
			sc.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		adjMtx = new double[count][count];


		int j = 0;
		double distance = 0;
		
		for(j=0; j<count;j++)
		{
			for(int i=j;i<count;i++)
			{
				if(i==j)
				{
					adjMtx[i][i] = 0;
				}
				else
				{
					distance = calcDistance(coords[i],coords[j]);
					adjMtx[j][i] = distance;
					adjMtx[i][j] = distance;
				}
			}
		}
		
		
		
		if(args[0].equals("-e"))
		{
			int[] sequence;
			sequence = enumerate(adjMtx, count);
		}
		else if(args[0].equals("-n"))
		{
			int[][] solution = new int[count][count];
			double bestRoute;
			
			for(int i=0; i<count; i++)
			{
				solution[i] = neighbourSearch(adjMtx, count, i);
			}
			
			
		}
		else
		{
			System.err.println("Algorithm not supported");
			return;
		}
		
	}



	double calcDistance(double begin[],double end[])
	{
		double x = end[0] - begin[0];
		double y = end[1] - begin[1];
		return (x*x+y*y);
	}
	
	int[] enumerate(double adjMtx[][],int count)
	{
		int[] res = new int[count];
		
		
		
		
		return res;
	}
	
	double calcLength(int[] route, double[][] adjMtx)
	{
		double sum = 0;
		int i = 0;
		
		for(i=0; i<route.length-1; i++)
		{
			sum += adjMtx[route[i]][route[i+1]];
		}

		sum += adjMtx[route[route.length-1]][route[0]];
		
		
		
		return sum;
	}
	
	int[] neighbourSearch(double adjMtx[][], int count, int startIndex)
	{
		int[] res = new int[count];
		int curElement = startIndex;
		int nextElement = 0;
		double minCost = 0;
		int i = 0;
		boolean visited[] = new boolean[count];
		
		for(i=0; i<count; i++)
		{
			res[i] = -1337;
			visited[i] = false;
		}
		
		for(i = 0; i<count;i++)
		{
			res[i] = curElement;
			visited[curElement] = true;
			minCost = -1;
			
			for(nextElement = 0; nextElement<count; nextElement++)
			{
				if(!visited[nextElement])
				{
					if(minCost<0)
					{
						minCost = adjMtx[curElement][nextElement];
						curElement = nextElement;
						continue;
					}
					
					if(minCost > adjMtx[curElement][nextElement])
					{
						minCost = adjMtx[curElement][nextElement];
						curElement = nextElement;
					}
				}
			}
		}
		
		return res;
	}

}