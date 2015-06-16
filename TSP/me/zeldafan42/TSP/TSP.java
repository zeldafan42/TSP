package me.zeldafan42.TSP;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class TSP
{
	double minLength = 0;

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
		
		for(double[] arr : adjMtx)
		{
			NumberFormat nf = new DecimalFormat("00.00");
			for(double d : arr)
			{
				System.out.print(nf.format(d) + " ");
			}
			System.out.print(System.lineSeparator());
		}
		
		
		if(args[0].equals("-e"))
		{
			int[] sequence = new int[count];
			int temp[] = new int[count];
			boolean b[] = new boolean[count];
			for(boolean bo :b )
			{
				bo = false;
			}
			enumerate(adjMtx, count, temp, sequence, b,0,0,0);
			for(int i : sequence)
			{
				System.out.println("<" + coords[i][0] + "," + coords[i][1] + ">");
			}
			System.out.println("Length: " + calcLength(sequence,adjMtx));
			System.out.println("-------");
		}
		else if(args[0].equals("-n"))
		{
			int[][] solution = new int[count][count];
			double bestRoute;
			
			for(int i=0; i<count; i++)
			{
				solution[i] = neighbourSearch(adjMtx, count, i);
			}
			
			for(int[] arr : solution)
			{
				for(int i : arr)
				{
					System.out.println("<" + coords[i][0] + "," + coords[i][1] + ">");
				}
				System.out.println("Length: " + calcLength(arr,adjMtx));
				System.out.println("-------");
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
	
	void enumerate(double adjMtx[][],int count,int curtour[], int mintour[], boolean visited[], double sum,int start, int step)
	{
		curtour[step] = start;
		
		for(int i = 1;i<count;i++)
		{
			if(!visited[i])
			{
				visited[i] = true;
				sum += adjMtx[start][i];
				step++;
				//System.out.println(i);
				enumerate(adjMtx,count,curtour,mintour,visited,sum,i,step);
				visited[i] = false;
				step--;
				sum -= adjMtx[start][i];
			}
			
		}
		if(step == count-1)
		{
			if(minLength == 0 || sum < minLength)
			{
				minLength = sum;
				System.arraycopy(curtour,0,mintour,0,count);
//				for(int i : curtour)
//				{
//					System.out.print(i);
//				}
//				System.out.println();
			}
		}
		
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
		int tmpElement = 0;
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
						tmpElement = nextElement;
						continue;
					}
					
					if(minCost > adjMtx[curElement][nextElement])
					{
						minCost = adjMtx[curElement][nextElement];
						tmpElement = nextElement;
					}
				}
			}
			curElement = tmpElement;
		}
		return res;
	}
}