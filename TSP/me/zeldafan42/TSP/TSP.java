package me.zeldafan42.TSP;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.file.Paths;
import java.util.Scanner;

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
		if (args.length != 2)
		{
			System.out.println("USAGE: tsp -e|-n <Path to textfile>");
			return;
		}
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		double adjMtx[][];
		double coords[][];
		int count = 0;
		// Read file
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

			if (count < 1)
			{
				sc.close();
				System.err.println("Count too small!!!!111");
				return;
			}

			coords = new double[count][2];

			for (int i = 0; i < count; i++)
			{
				for (int j = 0; j < 2; j++)
				{
					if (sc.hasNextDouble())
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

			// System.out.println(count);
			//
			// for(int i = 0; i< count; i++)
			// {
			// for(int j = 0;j<2;j++)
			// {
			// System.out.print(coords[i][j]+" ");
			// }
			// System.out.print(System.lineSeparator());
			// }
			sc.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		adjMtx = new double[count][count];

		double distance = 0;

		for (int j = 0; j < count; j++)
		{
			for (int i = j; i < count; i++)
		{
				if (i == j)
				{
					adjMtx[i][i] = 0; //a point has no distance to itself
				}
				else
				{
					distance = calcDistance(coords[i], coords[j]);
					adjMtx[j][i] = distance; //matrix is symmetric
					adjMtx[i][j] = distance;
				}
			}
		}

		// for(double[] arr : adjMtx)
		// {
		// NumberFormat nf = new DecimalFormat("00.00");
		// for(double d : arr)
		// {
		// System.out.print(nf.format(d) + " ");
		// }
		// System.out.print(System.lineSeparator());
		// }

		ThreadMXBean thx = ManagementFactory.getThreadMXBean();
		long start = thx.getCurrentThreadCpuTime();
		
		if (args[0].equals("-e"))
		{
			int[] sequence = new int[count];
			int temp[] = new int[count];
			
			enumerate(adjMtx, count, temp, sequence, new boolean[count], 0, 0,0);
			
			for (int i : sequence)
			{
				System.out.println("<" + coords[i][0] + "," + coords[i][1] + ">");
			}
			
			double length = calcLength(sequence, adjMtx);
			
			System.out.println("<" + coords[0][0] + "," + coords[0][1] + ">");
			System.out.println("Length: " + length);
			System.out.println("-------");
		} 
		else if (args[0].equals("-n"))
		{
			int[][] solution = new int[count][count];
			double bestRoute = -1;
			int bestSolution = 0;
			double length = 0;

			for (int i = 0; i < count; i++) //Nearest Neighbour for every different starting point
			{
				solution[i] = neighbourSearch(adjMtx, count, i); 
			}

			for (int i = 0; i < solution.length; i++)
			{
				for (int j : solution[i])
				{
					System.out.println("<" + coords[j][0] + "," + coords[j][1] + ">");
				}
				
				length = calcLength(solution[i], adjMtx);
				
				if (length < bestRoute || bestRoute == -1)
				{
					bestRoute = length;
					bestSolution = i;
				}

				System.out.println("<" + coords[solution[i][0]][0] + "," + coords[solution[i][0]][1] + ">");
				System.out.println("Length: " + length);
				System.out.println("-------");
			}

			System.out.println("Best Route found: " + System.lineSeparator());

			for (int i : solution[bestSolution])
			{
				System.out.println("<" + coords[i][0] + "," + coords[i][1] + ">");
			}
			System.out.println("<" + coords[solution[bestSolution][0]][0] + "," + coords[solution[bestSolution][0]][1] + ">");
			System.out.println("Length: " + bestRoute);
			System.out.println("-------");

		}
		else
		{
			System.err.println("Algorithm not supported");
			return;
		}
		
		double end = (thx.getCurrentThreadCpuTime() - start) / 1E9;
		
		System.out.println("Algorithm execution time: " + end + " seconds");

	}

	double calcDistance(double begin[], double end[])
	{
		double x = end[0] - begin[0];
		double y = end[1] - begin[1];
		return Math.sqrt(x * x + y * y);
	}

	void enumerate(double adjMtx[][], int count, int curtour[], int mintour[], boolean visited[], double sum, int start, int step) 
	{									//Attention: arrays are called by reference in java, so mintour is our return value
		curtour[step] = start;
		
		if (step == count - 1) //the last path is back to the starting point
		{
			sum += adjMtx[start][0];
//			System.out.println(sum);
//			for(int i : curtour)
//			{
//				System.out.print(i);
//			}
//			System.out.println();
			if (minLength == 0 || sum < minLength)
			{
				minLength = sum;
				System.arraycopy(curtour, 0, mintour, 0, count); //mintour is updated to the latest shortest path
				// for(int i : curtour)
				// {
				// System.out.print(i);
				// }
				// System.out.println();
			}
		}

		for (int i = 1; i < count; i++)
		{
			if (!visited[i])
			{
				visited[i] = true;
				sum += adjMtx[start][i];
				step++;
				// System.out.println(i);
				enumerate(adjMtx, count, curtour, mintour, visited, sum, i, step);
				
				step--;
				sum -= adjMtx[start][i];
				visited[i] = false;
			}
		}
	}

	double calcLength(int[] route, double[][] adjMtx)
	{
		double sum = 0;
		int i = 0;

		for (i = 0; i < route.length - 1; i++)
		{
			sum += adjMtx[route[i]][route[i + 1]];
		}

		sum += adjMtx[route[route.length - 1]][route[0]];

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

		for (i = 0; i < count; i++)
		{
			res[i] = -1337; //this is just an index that does not exist (not like 0 for instance). We could have picked -1 too...
			visited[i] = false;
		}

		for (i = 0; i < count; i++)
		{
			res[i] = curElement;
			visited[curElement] = true;
			minCost = -1;

			for (nextElement = 0; nextElement < count; nextElement++)
			{
				if (!visited[nextElement]) //only looks at unvisited points
				{
					if (minCost < 0) //and takes the one with the lowest cost (=distance)
					{
						minCost = adjMtx[curElement][nextElement];
						tmpElement = nextElement;
						continue;
					}

					if (minCost > adjMtx[curElement][nextElement])
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