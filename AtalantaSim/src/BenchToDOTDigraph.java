import java.io.*;
import java.util.*;

public class BenchToDOTDigraph {

	public static void main(String[] args) {
		BenchToDOTDigraph instance = new BenchToDOTDigraph();
		instance.start(args);
	}
	
	boolean showDebug = false;
	PrintWriter outputStream = null; // create printwriter
	String userDirectory = System.getProperty("user.dir") + "\\";
	String dot = userDirectory+"Graphviz/bin/dot.exe";
	String imagePath = userDirectory+"Logic Gates/";
	
	public void start(String[] args) {
		String[] inputFileNames = {"cQuiz2.bench", "cSoroushNotebook.bench", "c17.bench", "c432.bench", "c499.bench", "c880.bench", "c1355.bench", "c1908.bench", "c2670.bench", "c3540.bench", "c5315.bench", "c6288.bench", "c7552.bench"};
                                 //      0                    1                    2           3             4             5              6             7               8             9              10             11            12    
		String[] inputFileNames2 = {"s15850.1.bench", "s382.bench", "s510.bench", "s838.1.bench", "s208.1.bench", "s38417.bench", "s526.bench", "s838.bench", "s1196.bench", "s208.bench", "s38584.1.bench", "s526n.bench", "s9234.1.bench", "s1238.bench", "s27.bench", "s386.bench", "s5378.bench", "s953.bench", "s13207.1.bench", "s298.bench", "s400.bench", "s641.bench", "s1423.bench", "s344.bench", "s420.1.bench", "s713.bench", "s1488.bench", "s349.bench", "s420.bench", "s820.bench", "s1494.bench", "s35932.bench", "s444.bench", "s832.bench"};

		
//		for (int q = 0; q < inputFileNames.length; q++) 
		{
		String inputFileName = inputFileNames[3];//"cQuiz2.f.f.f.0.bench";//inputFileNames[0]; //inputFileNames2[1];
		String outputFileName = inputFileName;
		if (outputFileName.contains(".bench")) {
			outputFileName = outputFileName.substring(0, outputFileName.indexOf(".bench"));
		}
		String graphName = outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
			graphName = args[0];
			if (graphName.contains(".bench")) {
				graphName = graphName.substring(0, graphName.indexOf(".bench"));
			}
			outputFileName = graphName;
		}
		String gvOutputFileName = outputFileName+".gv";
		String title = "a";
		boolean titleIsSet = false;
		boolean splines = false;
		boolean digraph = true;		// non digraph isnt working at the moment
		String edge = digraph ? " -> " : " -- ";
		ArrayList<String> darkColors = new ArrayList<String>();
		darkColors.add("blue");
		darkColors.add("purple");
//		purple
//		{"blue", "purple"};
//		darkColors.
    	try 
    	{	
    		Scanner inputFile = new Scanner(new File(inputFileName));
    		int numberOfInputs = 0;
    		int numberOfOutputs = 0;
    		int numberOfGates = 0;
    		String stringWalker = "";
    		ArrayList<String> inputs = new ArrayList<String>();
    		while (inputFile.hasNextLine())
    		{							// keep reading lines to compute the number of agents
    			stringWalker = inputFile.nextLine().replace(".", "");
    			if (stringWalker.length()>0)
    			{
    				if (stringWalker.startsWith("#"))
    				{
    				}
    				else if (stringWalker.startsWith("INPUT")) {
						numberOfInputs++;
						String intValue = stringWalker.substring(6, stringWalker.length()-1);
						inputs.add(intValue);
    				}
    				else if (stringWalker.startsWith("OUTPUT")) {
						numberOfOutputs++;
    				}
    				else
    					numberOfGates++;
    			}
    		}
    		inputFile.close();			// close file to prevent corruption
    		if (numberOfInputs==0||numberOfOutputs==0)
    		{							// check if the number of rows is valid
    			println("\terror: invalid bench file due to number of inputs/outputs. Exiting...");
    			println("\t\tthere are "+numberOfInputs+" inputs and "+numberOfOutputs+" outputs");
    			System.exit(0);			// return since the number of columns not yet modified
    		}							//	 --> errors later down the road
    		try								// initialize postfix
    		{								// attempt to open data file
    			outputStream = new PrintWriter(new FileOutputStream(gvOutputFileName));
    		}
    		catch(FileNotFoundException e)
    		{
    			println("\terror opening the file "+outputFileName);
    			System.exit(0);
    		}
    		println((digraph ? "di" : "") + "graph " + title);
    		println("{");
    		println("\t//"+numberOfInputs+" Inputs");
    		println("\t//"+numberOfOutputs+" Outputs");
    		println("\t//"+numberOfGates+" Gates");
    		println("\toverlap = false;");
    		if (!digraph) {
    			println("\tlayout = neato;");
    		} else {
    			println("\trankdir = LR;");
    		}
    		if (!splines) {
    			println("\tsplines = false;");
//    			println("\tsplines = ortho;");
    			//println("\toutputorder = edgesfirst;");//
    		}
    		println("\t\"" + graphName + "\" [color = invis]");
    		ArrayList<String> outputs = new ArrayList<String>();
    		inputFile = new Scanner(new File(inputFileName));
    		int containsCluster = 0;
    		String append = "";
    		while (inputFile.hasNextLine())
    		{							// keep reading lines to compute the number of agents
    			stringWalker = inputFile.nextLine().replace(".", "");
    			if (stringWalker.length()>0)
    			{
    				if (stringWalker.startsWith("#")||stringWalker.length()==0) {
    					if (stringWalker.contains("# subbench")) {
    						containsCluster = 1;
    						println("\tsubgraph cluster {\n\trankdir = LR;"); 
    					}
    				}
    				else if (stringWalker.startsWith("INPUT("))
    				{
    					String intValue = stringWalker.substring(6, stringWalker.length()-1);
    					String inputExtraNode = "\t"+intValue+" [label = \"INPUT("+intValue+")\" shape = cds style = filled]\n";
    					inputExtraNode += "\tO"+intValue+" [label = \"\", fixedsize=\"false\", width=0, height=0, shape=none]\n\t"+intValue+edge+"O"+intValue+" [arrowhead=none]\n";
    					if (containsCluster > 0) {
    						append += inputExtraNode;
    					} else 
    					{
    						print(inputExtraNode);
    					}
    				}
    				else if (stringWalker.startsWith("OUTPUT("))
    				{
    					String intValue = stringWalker.substring(7, stringWalker.length()-1);
    					outputs.add(intValue);
    					String outputExtraNode = "";
    					if (inputs.contains(intValue)) {
    						outputExtraNode += "\tOO"+intValue+" [label = \"OUTPUT("+intValue+")\" shape = cds style = filled fillcolor = yellow]\n"; // input->output
    						outputExtraNode += ("\tO"+intValue+edge+"OO"+intValue+";\n"); // connection
    					} else {
    						outputExtraNode += ("\tO"+intValue+" [label = \"OUTPUT("+intValue+")\" shape = cds style = filled fillcolor = yellow]\n"); // output node
    						//outputExtraNode += ("\t"+intValue+edge+"O"+intValue+";\n"); // connection
    					}
    					if (containsCluster > 0) {
    						append += outputExtraNode;
    					} else {
    						print(outputExtraNode);
    					}
    				}
    				else //gate
    				{
    					stringWalker = stringWalker.replaceAll(" ", "");
    					String color, fontColor, gate;
    					if (stringWalker.contains("NAND"))
    					{
    						color = "blue"; fontColor = "white"; gate = "NAND";
    					}
    					else if (stringWalker.contains("AND"))
    					{
    						color = "orange";  fontColor = "black"; gate = "AND";
    					}
    					else if (stringWalker.contains("XNOR"))
    					{
    						color = "magenta"; fontColor = "black"; gate = "XNOR";
    					}
    					else if (stringWalker.contains("NOR"))
    					{
    						color = "darkgreen"; fontColor = "white"; gate = "NOR";
    					}
    					else if (stringWalker.contains("XOR"))
    					{
    						color = "cyan"; fontColor = "black"; gate = "XOR";
    					}
    					else if (stringWalker.contains("OR"))
    					{
    						color = "purple"; fontColor = "white"; gate = "OR";
    					}
    					else if (stringWalker.contains("BUF"))
    					{
    						color = "green"; fontColor = "black"; gate = "BUF";//normal
    					}
    					else// if (stringWalker.contains("NOT"))
    					{
    						color = "red"; fontColor = "white"; gate = "NOT";
    					}
    					stringWalker = stringWalker.replace("NAND", "").replace("AND", "").replace("XNOR", "").replace("NOR", "").replace("XOR", "").replace("OR", "").replace("BUFF", "").replace("BUF", "").replace("NOT", "");
						String gateLabel = stringWalker.substring(0, stringWalker.indexOf('='));
						stringWalker = stringWalker.replace(gateLabel+"=", "");
						stringWalker = stringWalker.substring(1, stringWalker.length()-1);
						stringWalker = stringWalker+","; // string walker are the inputs
						print("\t"+gateLabel+" [label = \""+gateLabel+"\" shape = none fillcolor = "+color+" fontcolor = "+fontColor+" image=\""+gate+".png\"];\n");
						String gateExtraNode = "";
						if (!outputs.contains(gateLabel)) {
							gateExtraNode += ("\tO"+gateLabel+" [label = \"\", fixedsize=\"false\", width=0, height=0, shape=none]\n");
							print("\t"+gateLabel+edge+"O"+gateLabel+" [arrowhead=none color = "+(outputs.contains(gateLabel)?"black":color+" tailclip=false")+"]\n"); // connection
						} 
						else
						{
							append += ("\t"+gateLabel+edge+"O"+gateLabel+" [arrowhead=none color = "+(outputs.contains(gateLabel)?"black":color+" tailclip=false")+"]\n"); // connection
						}
						while (stringWalker.contains(","))
						{
							gateExtraNode += "\tO"+stringWalker.substring(0,stringWalker.indexOf(','))+edge+gateLabel+" [color = "+color+"];\n";
							stringWalker = stringWalker.substring(stringWalker.indexOf(',')+1);
						}
						if (containsCluster > 0) {
    						append += gateExtraNode;
    					} else {
    						print(gateExtraNode);
    					}
    				}
    			}
    		}
    		inputFile.close();
    		if (containsCluster > 0) {
    			println("\t}");
    			//print(append);
    		}
    		print(append);
        	println("}");
        	System.out.println("\twrote file: " + gvOutputFileName + " to " + userDirectory + gvOutputFileName);
        	outputStream.close();
    	}
    	catch (IOException e)
    	{								// if cannot open file
    		println("\terror using the file "+inputFileName);
    		System.exit(0);
    	}
		
		String type = "pdf";
		String outputLocation = userDirectory + outputFileName + "." + type;
		String[] args2 = { dot, "-T" + type, "-o", outputLocation, gvOutputFileName};//, "-Gimagepath="+imagePath };
		//dot -Tpdf -o a.pdf a.gv
		for (String s:args2) { // TODO
			System.out.print(s+" ");
		}
		System.out.println();
		try {
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(args2);
			p.waitFor();
		} catch (java.io.IOException ioe) {
			System.err.println("Error:    in I/O processing of tempfile in dir\n");
			System.err.println("       or in calling external command");
			ioe.printStackTrace();
		} catch (java.lang.InterruptedException ie) {
			System.err.println("Error: the execution of the external program was interrupted");
			ie.printStackTrace();
		}
		
		System.out.println("\twrote file: " + outputFileName + "." + type + " to " + outputLocation);
		}
	}
	
	public void print(String s) {
		if (showDebug) {
			System.out.print(s);
		}
		outputStream.print(s);
	}

	public <T> void print(T s) {
		if (showDebug) {
			System.out.print("" + s);
		}
		outputStream.print(s);
	}

	public void println() {
		if (showDebug) {
			System.out.println();
		}
		outputStream.println();
	}

	public void println(int i) {
		if (showDebug) {
			System.out.println(i);
		}
		outputStream.println(i);
	}

	public void println(String s) {
		if (showDebug) {
			System.out.println(s);
		}
		outputStream.println(s);
	}
}
