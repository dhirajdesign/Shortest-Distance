package com.bu.cs526.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class Project {
	
	//Direct Distance Class
	//Stores the node, and direct distance from node to Z
	protected static class DD {
		private String node;
		private int distance;
		
		//Constructor
		public DD() {
			node = "";
			distance = 0;
		}
		//Getters
		public String getNode() {return node;}
		public int getDistance() {return distance;}
		//Setters
		public void setNode(String n) {node = n;}
		public void setDistance(int d) {distance = d;}
	}
	
	//Matrix Class, Provides constructor that stores the
	//Start Node, End Node, and Weight
	protected static class Matrix {
		private String startNode;
		private String endNode;
		private int weight;
	
		//Constructor
		public Matrix(){
		  startNode = "";
		  endNode = "";
		  weight = 0;
		}
		//Getters
		public String getStartNode() {return startNode;}
		public String getEndNode() {return endNode;}
		public int getWeight() {return weight;}
		//Setters
		public void setStartNode(String s) {startNode = s;}
		public void setEndNode(String e) {endNode = e;}
		public void setWeight(int w) {weight = w;}
	}
	
	//Print the Matrix value in the parameter 
	//Made for debugging purposes
	public static void printEntry(Matrix m){
		System.out.println("Start Node = " + m.getStartNode());
		System.out.println("End Node = " + m.getEndNode());
		System.out.println("Weight = " + m.getWeight());
	}
	
	//Reads the input graph.txt file provided
	//Assigns the input to an ArrayList of Matrix class object
	//Initially uses a multidimensional ArrayList<String[]> to store the tokens[] 
	//from the file scanner object, then it separates it into two ArrayList
	//referred to as startNode and endNode, the two arrays are then used to 
	//create the final ArrayList of Matrix Class/object type that is provided
	//in the parameters
	public static String readGraph(ArrayList<Matrix> list) throws IOException{
	
		//Create a ArrayList of string arrays to store the []tokens
		ArrayList<String[]> t = new ArrayList<String[]>();
		String[] tokens;
		Scanner inputFile = new Scanner (new File("graph_input.txt"));
		String numProcesses = "test";
		//while there is data in the input file
		while (inputFile.hasNext()){
			//store the tokens in the tokens array
			tokens = inputFile.nextLine().trim().split("\\s+");
			//add the tokens[] to the t ArrayList
			t.add(tokens);
			
		}
		//create ArrayList to store start and end nodes
		ArrayList<String> endNodeList = new ArrayList<String>();
		ArrayList<String> startNodeList= new ArrayList<String>();

		//Loop through the multidimensional array list
		for (int x =0; x < t.size(); x++) {
			//store the current array in a temp array to copy to the endnode and startnode array list
			String [] testA = t.get(x);
			for(int z = 0; z < testA.length; z++) {
				if(x == 0) {
					endNodeList.add(testA[z]);//A B C D E ---> top of matrix
				}
				if(z == 0) {
					startNodeList.add(testA[z]);// A B C D E ----> left of matrix
				}	
			}
		}
		//Loop through only the weight values in the multidimensional array t
		for (int o =0; o < t.size(); o++) {
			//create a temp array to store the weight values
			String [] testA = t.get(o);
			for(int p = 0; p < testA.length; p++) {
				if (o != 0 && p != 0) {
					int weight = Integer.parseInt(testA[p]);
					String endNode = endNodeList.get(p);
					String startNode = startNodeList.get(o);
					
					//create a new matrix object and set the start, end, and weight values
					Matrix m = new Matrix();
					m.setEndNode(endNode);
					m.setStartNode(startNode);
					m.setWeight(weight);
					//add it to the list in the params
					list.add(m);
				}	
			}
		}
		//close the input file
		inputFile.close();
		return numProcesses;
	}
	
	//Reads the input from the direct distance text file
	//Assigns the input to an ArrayList of DD class object
	public static void readDistance(ArrayList<DD> list) throws IOException{
		//will store the individual input tokens in tokensD string array
		String[] tokensD;
		Scanner inputFile = new Scanner (new File("direct_distance.txt"));
		//while the inputFile has lines of data
		while (inputFile.hasNext()){
			//store the tokens from line
			tokensD = inputFile.nextLine().trim().split("\\s+");
			//create a DD object
			DD d = new DD();
			//assign the node token to a variable
			String node = tokensD[0];
			//parse to integer format and assign the distance token to a variable
			int distance = Integer.parseInt(tokensD[1]);
			
			//set the node and distance tokens to the DD class object
			d.setNode(node);
			d.setDistance(distance);
			list.add(d);
		}
		inputFile.close();
		
	}
	
	//finds the weight of the edge between two nodes
	public static int w(String n, String v) throws IOException {
		
		//format the input to upper case because the
		//graph nodes were all stored in upper case format
		n = n.toUpperCase();
		v = v.toUpperCase();
		
		ArrayList<Matrix> graph1 = new ArrayList<Matrix>();
		readGraph(graph1);
		int weight = 0;
		for(int i = 0; i < graph1.size(); i++) {
			if(graph1.get(i).startNode.contains(n) && 
			   graph1.get(i).endNode.contains(v))
			{
				weight = graph1.get(i).weight;
			}
		}
		return weight;
		 
	}
	
	//find the destination node that = to 0
	public static String destination() throws IOException{
		
		int destination = 0;
		String found = "";
		ArrayList<DD> ddList = new ArrayList<DD>();
		readDistance(ddList);
		
		for(int i = 0; i < ddList.size(); i++) {
			if(ddList.get(i).distance == destination) {
				 found = ddList.get(i).node;
			}
		}
		return found.toUpperCase();
	}

	//finds the direct distance from the node to the destination node
	// Z for this specific test case
	public static int dd(String node) throws IOException{
		node = node.toUpperCase();
		int found = 0;
		ArrayList<DD> ddList = new ArrayList<DD>();
		readDistance(ddList);
		
		for(int i = 0; i < ddList.size(); i++) {
			if(ddList.get(i).node.contains(node)) {
				 found = ddList.get(i).distance;
			}
		}
		return found;
	}
	
	//The first Algorithm 
	public static void findAdj1(String n) throws IOException {
		
		
		//will hold the previous node value for backtracking purposes
		String previousNode = "";
		
		//Matrix that stores all the start, end, and weight values for the nodes
		ArrayList<Matrix> graph1 = new ArrayList<Matrix>();
		readGraph(graph1);
		
		//Array list that stores the sequence of nodes 
		ArrayList<String> inPath= new ArrayList<String>();
		
		//Array list that stores the shortest path of nodes from user node
		ArrayList<String> shortPath= new ArrayList<String>();
		
		//Will Store shortest path length
		int sPathLength = 0;
		
		//used to break the loop if input is invalid
		boolean invalid = true;
		n = n.toUpperCase();
		String firstNode = n;
		if(dd(firstNode) != 0) {
			while (n != null) 
			{
				for(int k = 0; k < graph1.size(); k++) {
					if(graph1.get(k).startNode.contains(n) == true) {
						invalid = false;
						}
					}
				
				if(invalid) {
					System.out.println("Please enter a node that is included in the Graph");
					break;
				}
				
				//add to inPath Array List if it isn't in the path 
				if(inPath.contains(n) == false) {
					inPath.add(n);
					shortPath.add(n);
				}
		
				//Array List to store the adjacent nodes
				ArrayList<String> adjacentList= new ArrayList<String>();
				
				
				
				//for loop to go find the adjacent nodes in the array list
				for(int i = 0; i < graph1.size(); i++) {
					//if the list contains the node and has a adjacent value
					if(graph1.get(i).startNode.contains(n) &&
							graph1.get(i).weight > 0) {
						//store the node that is adjacent
						String adjNode = graph1.get(i).endNode;
						adjacentList.add(adjNode);
					}
					
				}
				
				//initialize backtrack boolean
				boolean backTrack = false;
				
				//if the adjacent list only contains one element and it is the previous node, you reached a dead end
				if(adjacentList.size() == 1 && adjacentList.get(0) == previousNode) {
					//Remove Dead end from Matrix array, and Shortest Path array and back track
					for(int e=0; e<shortPath.size(); e++) {
						if(shortPath.get(e) == n){
							shortPath.remove(e);
						}
					}
					backTrack = true;
					//remove the dead end node from the array
					for(int s=0; s < graph1.size(); s++) {
						if(graph1.get(s).startNode.contains(n)) {
							graph1.remove(s);
						}
						if(graph1.get(s).endNode.contains(n)) {
							graph1.remove(s);
						}
					}
					n = previousNode;
					//continue;
				}
				if(backTrack) {
					inPath.add(n);
					continue;
				}
			
				//To store the node with the shortest path to Z
				String selected = "";
				
				for(int f=0; f<adjacentList.size(); f++) {
					//Check if the node is already in the path
					if(inPath.contains(adjacentList.get(f))&& f !=0){
						//System.out.println(adjacentList.get(f) + " is already in Path.");
						adjacentList.remove(f);
					}
				}
				
		
				//to store the shortest Direct Distance value from the adjacent nodes
				int shortest = 0;
				//find the shortest distance and select it as the next node
				for(int ii=0; ii <adjacentList.size(); ii++) {
					if(shortest == 0) {
						shortest = dd(adjacentList.get(ii));
						selected = adjacentList.get(ii);
					}
					if(shortest > dd(adjacentList.get(ii))) {
						shortest = dd(adjacentList.get(ii));
						selected = adjacentList.get(ii);
					}
				}
				
				//if the final node Z is reached
				if(dd(selected) == 0) {
					//Add final node to shortest path array
					shortPath.add(selected);
					//Add final node to sequence of all nodes array
					inPath.add(selected);
					
					//Print Sequence of all nodes
					for(int r=0; r < inPath.size(); r++) {
						if(r == 0) {
							System.out.print("Sequence of all nodes: "+ inPath.get(r)+ " -> ");
						}
						if(r > 0 && r < inPath.size() - 1) {
							System.out.print(inPath.get(r)+ " -> ");
						}
						if(r == inPath.size() - 1) {
							System.out.print(inPath.get(r));
							System.out.println("");
						}
					}
					
					//Print Shortest Path 
					for(int r=0; r < shortPath.size(); r++) {
						if(r == 0) {
							System.out.print("Shortest Path: "+ shortPath.get(r)+ " -> ");
						}
						if(r > 0 && r < shortPath.size() - 1) {
							System.out.print(shortPath.get(r)+ " -> ");
						}
						if(r == shortPath.size() - 1) {
							System.out.print(shortPath.get(r));
							System.out.println("");
						}
					}
					
					//Print Shortest Path Length
					for(int r=0; r < shortPath.size(); r++) {
						if (r != shortPath.size() - 1) {
							sPathLength += w(shortPath.get(r), shortPath.get(r+1));
						}
					}
					System.out.println("Shortest Path Length: "+sPathLength);
					break;
					}
					else {
						
						shortPath.add(selected);
						inPath.add(selected);
						
						}
						previousNode = n;
						n = selected;
					}
			}
			else {
				System.out.println("Please enter another node value!");
			}
		}

	//The second Algorithm 
	public static void findAdj2(String n) throws IOException {
		
		
		//will hold the previous node value for backtracking purposes
		String previousNode = "";
		
		//Matrix that stores all the start, end, and weight values for the nodes
		ArrayList<Matrix> graph1 = new ArrayList<Matrix>();
		readGraph(graph1);
		
		//Array list that stores the sequence of nodes 
		ArrayList<String> inPath= new ArrayList<String>();
		
		//Array list that stores the shortest path of nodes from user node
		ArrayList<String> shortPath= new ArrayList<String>();
		
		//Will Store shortest path length
		int sPathLength = 0;
		
		//used to break the loop if input is invalid
		boolean invalid = true;
		n = n.toUpperCase();
		String firstNode = n;
		
		//if the user input is not the last node continue with algorithm
		if(dd(firstNode) != 0) {
			//While loop till the shortest path is found
			while (n != null) 
			{
				for(int k = 0; k < graph1.size(); k++) {
					if(graph1.get(k).startNode.contains(n) == true) {
						invalid = false;
						}
					}
				
				if(invalid) {
					System.out.println("Please enter a node that is included in the Graph");
					break;
				}
				
				//add to inPath Array List if it isn't in the path 
				if(inPath.contains(n) == false) {
					inPath.add(n);
					shortPath.add(n);
				}
		
				//Array List to store the adjacent nodes
				ArrayList<String> adjacentList= new ArrayList<String>();
				
				
				
				//for loop to go find the adjacent nodes in the array list
				for(int i = 0; i < graph1.size(); i++) {
					//if the list contains the node and has a adjacent value
					if(graph1.get(i).startNode.contains(n) &&
							graph1.get(i).weight > 0) {
						//store the node that is adjacent
						String adjNode = graph1.get(i).endNode;
						adjacentList.add(adjNode);
					}
					
				}
				
				//initialize backtrack boolean
				boolean backTrack = false;
				
				//if the adjacent list only contains one element and it is the previous node, you reached a dead end
				if(adjacentList.size() == 1 && adjacentList.get(0) == previousNode) {
					//Remove Dead end from Matrix array, and Shortest Path array and back track
					for(int e=0; e<shortPath.size(); e++) {
						if(shortPath.get(e) == n){
							shortPath.remove(e);
						}
					}
					backTrack = true;
					//remove the dead end node from the array
					for(int s=0; s < graph1.size(); s++) {
						if(graph1.get(s).startNode.contains(n)) {
							graph1.remove(s);
						}
						if(graph1.get(s).endNode.contains(n)) {
							graph1.remove(s);
						}
					}
					n = previousNode;
					//continue;
				}
				if(backTrack) {
					inPath.add(n);
					continue;
				}
			
				//To store the node with the shortest path to Z
				String selected = "";
				
				for(int f=0; f<adjacentList.size(); f++) {
					//Check if the node is already in the path
					if(inPath.contains(adjacentList.get(f))&& f !=0){
						//System.out.println(adjacentList.get(f) + " is already in Path.");
						adjacentList.remove(f);
					}
				}
				
		
				//to store the shortest Direct Distance value from the adjacent nodes
				int shortest = 0;
				//find the shortest distance and select it as the next node
				for(int ii=0; ii <adjacentList.size(); ii++) {
					if(shortest == 0) {
						shortest = w(n,adjacentList.get(ii)) + dd(adjacentList.get(ii));
						selected = adjacentList.get(ii);
					}
					if(shortest > w(n,adjacentList.get(ii)) + dd(adjacentList.get(ii))) {
						shortest = w(n,adjacentList.get(ii)) + dd(adjacentList.get(ii));
						selected = adjacentList.get(ii);
					}
				}
				
				//if the final node Z is reached
				if(dd(selected) == 0) {
					//Add final node to shortest path array
					shortPath.add(selected);
					//Add final node to sequence of all nodes array
					inPath.add(selected);
					
					//Print Sequence of all nodes
					for(int r=0; r < inPath.size(); r++) {
						if(r == 0) {
							System.out.print("Sequence of all nodes: "+ inPath.get(r)+ " -> ");
						}
						if(r > 0 && r < inPath.size() - 1) {
							System.out.print(inPath.get(r)+ " -> ");
						}
						if(r == inPath.size() - 1) {
							System.out.print(inPath.get(r));
							System.out.println("");
						}
					}
					
					//Print Shortest Path 
					for(int r=0; r < shortPath.size(); r++) {
						if(r == 0) {
							System.out.print("Shortest Path: "+ shortPath.get(r)+ " -> ");
						}
						if(r > 0 && r < shortPath.size() - 1) {
							System.out.print(shortPath.get(r)+ " -> ");
						}
						if(r == shortPath.size() - 1) {
							System.out.print(shortPath.get(r));
							System.out.println("");
						}
					}
					
					//Print Shortest Path Length
					for(int r=0; r < shortPath.size(); r++) {
						if (r != shortPath.size() - 1) {
							sPathLength += w(shortPath.get(r), shortPath.get(r+1));
						}
					}
					System.out.println("Shortest Path Length: "+sPathLength);
					break;
					}
					else {
						
						shortPath.add(selected);
						inPath.add(selected);
						
						}
						previousNode = n;
						n = selected;
					}
		}
		else {
			System.out.println("Please enter another node value!");
		}
	}
	
	//The test program
	public static void main(String[] args) throws IOException {
		
		//Test the two Algorithms
		boolean search = true;
		//Keep asking for input nodes to test
		while(search) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Please enter an input: ");
			String input = sc.nextLine();
			
			//Check if input is a single character/node 
			if(input.length() > 1 || input.isBlank() || input.matches("[a-zA-Z]") == false) {
			System.out.println("Please enter a valid node(one letter)!");
			continue;
			}
			System.out.println("First Algorithm");
			findAdj1(input);
			System.out.println("");
			System.out.println("Second Algorithm");
			findAdj2(input);
			System.out.println("--------------------------");
		}	
		
	}

}
