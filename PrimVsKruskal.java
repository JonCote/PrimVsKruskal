/* PrimVsKruskal.java
   CSC 226 - Summer 2021
   Assignment 3 - Prim MST versus Kruskal MST Template
   
   The file includes the "import edu.princeton.cs.algs4.*;" so that yo can use
   any of the code in the algs4.jar file. You should be able to compile your program
   with the command
   
	javac -cp .;algs4.jar PrimVsKruskal.java
	
   To conveniently test the algorithm with a large input, create a text file
   containing a test graphs (in the format described below) and run
   the program with
   
	java -cp .;algs4.jar PrimVsKruskal file.txt
	
   where file.txt is replaced by the name of the text file. Note: different operating systems have different commands.
   You should all know how to run the algs4.jar file from lab work.
   
   The input consists of a graph (as an adjacency matrix) in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry G[i][j] >= 0.0 of the adjacency matrix gives the weight (as type double) of the edge from 
   vertex i to vertex j (if G[i][j] is 0.0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that G[i][j]
   is always equal to G[j][i].
*/

 import edu.princeton.cs.algs4.*;
 import java.util.Scanner;
 import java.io.File;

//Do not change the name of the PrimVsKruskal class
public class PrimVsKruskal{
	

	/* PrimVsKruskal(G)
		Given an adjacency matrix for connected graph G, with no self-loops or parallel edges,
		determine if the minimum spanning tree of G found by Prim's algorithm is equal to 
		the minimum spanning tree of G found by Kruskal's algorithm.
		
		If G[i][j] == 0.0, there is no edge between vertex i and vertex j
		If G[i][j] > 0.0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static boolean PrimVsKruskal(double[][] G){
		int n = G.length;

		/* Build the MST by Prim's and the MST by Kruskal's */
		/* (You may add extra methods if necessary) */
		
		/* ... Your code here ... */
		int[] edgeTo = new int[n];
		double[][] primMST = new double[n][n];
		double[][] kruskMST = new double[n][n];
		double[] distTo = new double[n];
		boolean[] marked = new boolean[n];
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(n);
		MinPQ<Double> pqk = new MinPQ<Double>(n);
		UF uf = new UF(n);
		boolean pvk = true;
		

	

		for(int i = 0; i<n; i++){
			distTo[i] = Double.POSITIVE_INFINITY;
			for(int j = 0; j<n; j++){
				if(G[i][j] > 0.0){
					pqk.insert(G[i][j]);
				}
			}
		}
		
		distTo[0] = 0.0;
		pq.insert(0, 0.0);
		int primMST_size = 0;
		int kruskMST_size = 0;
		
		while((primMST_size < n-1 || kruskMST_size < n-1) && pvk == true){
			
			//prims
			if(!pq.isEmpty()){
				int v = pq.delMin();
				marked[v] = true;
				primMST[v][edgeTo[v]] = G[v][edgeTo[v]];
				primMST[edgeTo[v]][v] = G[v][edgeTo[v]];
				primMST_size++;

				for(int w = 0; w<n; w++){
					if(marked[w]){
						continue;
					}
					if(G[v][w] < distTo[w] && G[v][w] > 0.0){
						edgeTo[w] = v;
						distTo[w] = G[v][w];
						if(pq.contains(w)){
							pq.changeKey(w,distTo[w]);
						}else{
							pq.insert(w, distTo[w]);
						}
					}
				}
			}
			
			//kruskals
			if(!pqk.isEmpty()){
				Double ew = pqk.delMin();
				for(int i = 0; i<n; i++){
					for(int j = 0; j<n; j++){
						if(G[i][j] == ew){
							
							if(uf.find(i) != uf.find(j)){
								uf.union(i, j);
								kruskMST[i][j] = G[i][j];
								kruskMST[j][i] = G[i][j];
								kruskMST_size++;
							}
						}
					}
					
				}
			}

			//early detection system
			for(int i = 0; i<n; i++){
				if(primMST[i][edgeTo[i]] > 0.0){
					if(uf.find(i) == uf.find(edgeTo[i])){
						if(kruskMST[i][edgeTo[i]] == 0.0){
							pvk = false;
						}
					}
				}
			}
			
			
		}
		

		//Prints the MST edges and the adjacency matrix's for MST's
/*
		//testing output section for prim
		System.out.println("\nPrim MST:");
		double primSum = 0.0;
		for(int i = 0; i<n; i++){
			for(int j = i; j<n; j++){
				if(G[i][j] > 0.5){
					System.out.printf("%s-%s: %s\n", i, j, primMST[i][j]);
					primSum = primSum + primMST[i][j];

				}
				
			}
		}
		System.out.printf("%s", primSum);
		System.out.printf("\n\n");


		
		System.out.println("PrimMST Adjacency Matrix:");
		for(int i = 0; i<n; i++){
			for(int j = 0; j<n; j++){
				System.out.printf("%s ", primMST[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");
		


		//testing output section for krusk
		System.out.println("\nKrusk MST:");
		double kruskSum = 0.0;
		for(int i = 0; i<n; i++){
			for(int j = i; j<n; j++){
				if(G[i][j] > 0.5){
					System.out.printf("%s-%s: %s\n", i, j, kruskMST[i][j]);
					kruskSum = kruskSum + kruskMST[i][j];

				}
				
			}
		}
		System.out.printf("%s", kruskSum);
		System.out.printf("\n\n");


		
		System.out.println("kruskMST Adjacency Matrix:");
		for(int i = 0; i<n; i++){
			for(int j = 0; j<n; j++){
				System.out.printf("%s ", kruskMST[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");
		
*/

		/* Determine if the MST by Prim equals the MST by Kruskal */
		
		/* ... Your code here ... */

		return pvk;	
	}
		
	/* main()
	   Contains code to test the PrimVsKruskal function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below. 
	*/
   public static void main(String[] args) {
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int n = s.nextInt();
		double[][] G = new double[n][n];
		int valuesRead = 0;
		for (int i = 0; i < n && s.hasNextDouble(); i++){
			for (int j = 0; j < n && s.hasNextDouble(); j++){
				G[i][j] = s.nextDouble();
				if (i == j && G[i][j] != 0.0) {
					System.out.printf("Adjacency matrix contains self-loops.\n");
					return;
				}
				if (G[i][j] < 0.0) {
					System.out.printf("Adjacency matrix contains negative values.\n");
					return;
				}
				if (j < i && G[i][j] != G[j][i]) {
					System.out.printf("Adjacency matrix is not symmetric.\n");
					return;
				}
				valuesRead++;
			}
		}
		
		if (valuesRead < n*n){
			System.out.printf("Adjacency matrix for the graph contains too few values.\n");
			return;
		}	
		
        boolean pvk = PrimVsKruskal(G);
        System.out.printf("Does Prim MST = Kruskal MST? %b\n", pvk);
    }
}
