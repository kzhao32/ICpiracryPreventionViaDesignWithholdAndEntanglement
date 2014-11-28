import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GatesToRemove {

	public static void main(String[] args) {
		GatesToRemove instance = new GatesToRemove();
		instance.start(args);
	}

	public void start(String[] args) {
		boolean showDebug = false;
		boolean isOneOutput = false;
		int numberOfTrials = 100;
		PrintWriter outputStream = null; // create printwriter
		String[] inputFileNames = {"cQuiz2.bench", "cSoroushNotebook.bench", "c17.bench", "c432.bench", "c499.bench", "c880.bench", "c1355.bench", "c1908.bench", "c2670.bench", "c3540.bench", "c5315.bench", "c6288.bench", "c7552.bench"};
		//                               0                    1                    2           3             4             5              6             7               8             9              10             11            12    
		String[] inputFileNames2 = {"s15850.1.bench", "s382.bench", "s510.bench", "s838.1.bench", "s208.1.bench", "s38417.bench", "s526.bench", "s838.bench", "s1196.bench", "s208.bench", "s38584.1.bench", "s526n.bench", "s9234.1.bench", "s1238.bench", "s27.bench", "s386.bench", "s5378.bench", "s953.bench", "s13207.1.bench", "s298.bench", "s400.bench", "s641.bench", "s1423.bench", "s344.bench", "s420.1.bench", "s713.bench", "s1488.bench", "s349.bench", "s420.bench", "s820.bench", "s1494.bench", "s35932.bench", "s444.bench", "s832.bench"};

		String inputFileName = inputFileNames[12];//inputFileNames[0];
		String outputFileName = inputFileName + ".output.txt";
		String benchFileName = "";
		String faultFileName = "";
		String testPatternFileName = "";
		if (outputFileName.contains(".bench")) {
			outputFileName = outputFileName.substring(0, outputFileName.indexOf(".bench")) + ".txt";
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-i") && i + 1 < args.length) {
				inputFileName = args[i+1];
				outputFileName = inputFileName + ".output.txt";
				if (outputFileName.contains(".bench")) {
					outputFileName = outputFileName.substring(0, outputFileName.indexOf(".bench")) + ".txt";
				}
			}
		}
		String stringWalker = "";
		int numberOfInputs = 0;
		int numberOfOutputs = 0;
		int numberOfGates = 0;
		ArrayList<String> inputs = new ArrayList<String>();
		ArrayList<String> outputs = new ArrayList<String>();
		ArrayList<String> gates = new ArrayList<String>();
		try {	
			Scanner inputFile = new Scanner(new File(inputFileName));
			while (inputFile.hasNextLine()) {
				stringWalker = inputFile.nextLine();
				if (stringWalker.length() > 0)
				{
					if (stringWalker.startsWith("#")) {
					}
					else if (stringWalker.startsWith("INPUT")) {
						numberOfInputs++;
						inputs.add(stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")));
						//println(Integer.parseInt(stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")"))));
					}
					else if (stringWalker.startsWith("OUTPUT")) {
						numberOfOutputs++;
						outputs.add(stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")));
					}
					else if (stringWalker.contains("=")) {
						numberOfGates++;
						gates.add(stringWalker.split(" ")[0]);
					}
				}
			}
			inputFile.close();
			String unusedNode = "C";//Math.max(Math.max(Collections.max(inputs), Collections.max(outputs)), Collections.max(gates)) + 1;
			if (numberOfInputs != inputs.size()) {
				println("Error: the number of inputs do not match!");
			}
			if (numberOfOutputs != outputs.size()) {
				println("Error: the number of outputs do not match!");
			}
			if (numberOfGates != gates.size()) {
				println("Error: the number of gates do not match!");
			}
			listDataStructure[] gateInputs = new listDataStructure[numberOfGates];
			listDataStructure[] gateOutputs = new listDataStructure[numberOfGates];
			for (int i = 0; i < numberOfGates; i++) {
				gateInputs[i] = new listDataStructure();
				gateInputs[i].arrayList.add(gates.get(i));
				gateOutputs[i] = new listDataStructure();
				gateOutputs[i].arrayList.add(gates.get(i));
			}
			boolean isDone = true;
			int counter2 = 0;
			while (true) {
				isDone = true;
				inputFile = new Scanner(new File(inputFileName));
				while (inputFile.hasNextLine()) {
					stringWalker = inputFile.nextLine();
					if (stringWalker.length() > 0 && stringWalker.contains("="))
					{
						String[] gateInputsStringArray = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")).split(", ");
						for (int k = 0; k < gateInputsStringArray.length; k++) {
							String l = gateInputsStringArray[k];  	// l is the input(s)
							String m = stringWalker.split(" ")[0];	// m is the output
							for (int x = 0; x < numberOfGates; x++) {
								if (!gateInputs[x].arrayList.contains(l)) {
									for (String o : gateInputs[x].arrayList) {	// for ever current input
										if (m.equals(o)) {								// if m is an input
											gateInputs[x].arrayList.add(l); // then add l
											isDone = false;
											break;
										}
									}
								}
								if (!gateOutputs[x].arrayList.contains(m)) {
									for (String o : gateOutputs[x].arrayList) {	// for ever current output
										if (l.equals(o)) {								// if l is an output
											gateOutputs[x].arrayList.add(m); // then add m
											isDone = false;
											break;
										}
									}
								}
							}
						}
					}
				}
				inputFile.close();
				System.out.println("counter2 = " + counter2++);
				if (isDone) {
					break;
				}
			}
			int counter = 0;
			System.out.println("n = " + numberOfGates);
			outputStream = new PrintWriter(new FileOutputStream(outputFileName));
			outputStream.println("inputFileName=" + inputFileName + "; numberOfInputs=" + numberOfInputs + "; numberOfOutputs=" + numberOfOutputs + "; numberOfGates=" + numberOfGates + "; unusedNode=" + unusedNode);
			outputStream.println((isOneOutput ? "" : "trial" + "; ") + "number; gate1; gate2; numDuplicateGates; numRemovedGates; percentOfRemovedGates; numInputs; numOutputs; numBenchFiles; duplicateGates; inputNodes; outputNodes; afterOutputNodes; outputFocus; locationOfMux; testCubeTime (ms); testVector");
			int trialCounter = 0;
			double probabilityOfShowingAll = 1.0;
			for (int i = isOneOutput ? 0 : (int) (Math.random() * numberOfGates); isOneOutput ? i < numberOfGates : trialCounter < numberOfTrials; i = isOneOutput ? i + 1 : (int) (Math.random() * numberOfGates)) {
				// if 1 output, then sample everything,
				// if multiple output, then 
				//			for (int i = (int) (Math.random()*numberOfGates/10.0); i < numberOfGates; i += Math.ceil(Math.random()*2.0*numberOfGates/10.0)) {
				//				if (showDebug) {
				//					println("gate" + gates.get(i) + "'s inputGates: " + gateInputs[i].arrayList);
				//					println("gate" + gates.get(i) + "'s outputGates: " + gateOutputs[i].arrayList);
				//				}
				//				for (int j = (int) (Math.random()*numberOfGates/10.0); j < numberOfGates; j += Math.ceil(Math.random()*2.0*numberOfGates/10.0)) {
				for (int j = isOneOutput ? i : (int) (Math.random() * numberOfGates); j < numberOfGates; j = isOneOutput ? j + 1 : (int) (Math.random() * numberOfGates)) {
					ArrayList<String> forwardDuplicates = new ArrayList<String>();
					for (String beginingGateOutput : gateOutputs[i].arrayList) {
						if (gateInputs[j].arrayList.contains(beginingGateOutput)) {
							forwardDuplicates.add(beginingGateOutput);
						}
					}
					ArrayList<String> backwardDuplicates = new ArrayList<String>();
					for (String endingGateOutput : gateOutputs[j].arrayList) {
						if (gateInputs[i].arrayList.contains(endingGateOutput)) {
							backwardDuplicates.add(endingGateOutput);
						}
					}
					if (forwardDuplicates.size() > 0 && backwardDuplicates.size() > 0 && i != j) {
						println("Error: there may be sequential logic in the circuit");
					}
					// println("using " + (forwardDuplicates.size() > 0 ? "forwardDuplicates" : "backwardDuplicates"));
					ArrayList<String> duplicates = forwardDuplicates.size() > 0 ? forwardDuplicates : backwardDuplicates;
					// if (duplicates.size() <= 0) {
					//	 continue;
					// }
					int numberOfDuplicates = duplicates.size();
					if (duplicates.size() == 0) {
						duplicates.add(gates.get(i));
						duplicates.add(gates.get(j));
					}
					ArrayList<String> nodeInputs = new ArrayList<String>();
					ArrayList<String> nodeOutputs = new ArrayList<String>();
					ArrayList<String> nodeAfterOutputs = new ArrayList<String>();
					inputFile = new Scanner(new File(inputFileName));
					while (inputFile.hasNextLine()) {
						stringWalker = inputFile.nextLine();
						if (stringWalker.contains("OUTPUT(")) {
							String o = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")"));	// o is the output
							if (o.equals(gates.get(i))) {
								nodeOutputs.add(gates.get(i));
							} else if (o.equals(gates.get(j))) {
								nodeOutputs.add(gates.get(j));
							} 
						}
						if (stringWalker.contains("=")) {
							String[] gateParenthesisStringArray = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")).split(", ");
							String o = stringWalker.split(" ")[0];	// o is the output
							for (int k = 0; k < gateParenthesisStringArray.length; k++) {
								String l = gateParenthesisStringArray[k];  	// l is the input(s)
								if (duplicates.contains(o) && !duplicates.contains(l) && !nodeInputs.contains(l)) {
									nodeInputs.add(l);
								}
								if (duplicates.contains(l) && !duplicates.contains(o) && !nodeOutputs.contains(l)) {
									nodeOutputs.add(l);
								}
								if (duplicates.contains(l) && !duplicates.contains(o) && !nodeAfterOutputs.contains(o)) {
									nodeAfterOutputs.add(o);
								}
							}
						}
					}
					inputFile.close();
					if (isOneOutput && nodeOutputs.size() != 1) {
						continue;
					}
					if (nodeInputs.size() > 8) {
						continue;
					}
					double duration = 0;
					int beginingK = (int) (Math.random() * nodeOutputs.size());
					int incrementK = nodeOutputs.size();
					int beginingL = (int) (Math.random() * Math.pow(2, nodeInputs.size()));
					int incrementL = (int) Math.pow(2, nodeInputs.size());
					if (Math.random() < probabilityOfShowingAll) {
						beginingK = 0;
						incrementK = 1;
						beginingL = 0;
						incrementL = 1;
						probabilityOfShowingAll /= 2.0;
					}
//					int k = (int) (Math.random() * nodeOutputs.size());
					// String inputValues = "";
					// System.out.println("k = " + k);
					// for (int k = (int) (Math.random()*nodeOutputs.size()/5.0); k < nodeOutputs.size(); k += Math.ceil(Math.random()*2.0*nodeOutputs.size()/5.0)) {
					for (int k = beginingK; k < nodeOutputs.size(); k += incrementK)
					{
						//	    				for (long l = (long) (Math.random()*Math.pow(2, nodeInputs.size())/5.0); l < Math.pow(2, nodeInputs.size()); l += Math.ceil(Math.random()*2.0*Math.pow(2, nodeInputs.size())/5.0)) {
						for (int l = beginingL; l < Math.pow(2, nodeInputs.size()); l += incrementL) {
							String inputValues = Long.toBinaryString(l);
							while (inputValues.length() < nodeInputs.size()) {
								inputValues = "0" + inputValues;
							}
							//	    				while (inputValues.length() < nodeInputs.size()) {
							//    						inputValues += (Math.random() < 0.5 ? "0" : "1");
							//    					}
//							System.out.println("nodeInputs.size() = " + nodeInputs.size() + ", inputValues = " + inputValues);
							{
								String fileName = inputFileName;
								while (fileName.contains(".")) {
									fileName = fileName.substring(0, fileName.indexOf("."));
								}
								fileName += "." + gates.get(i) + "." + gates.get(j) + "." + nodeOutputs.get(k) + "." + inputValues;
								benchFileName = fileName + ".bench";
								faultFileName = fileName + ".flt";
								testPatternFileName = fileName + ".pat";
								//	    					System.out.println("a");
								PrintWriter faultStream = new PrintWriter(new FileOutputStream(faultFileName));
								//	    					System.out.println("b");
								faultStream.println((unusedNode + nodeOutputs.get(k)) + " /0");
								faultStream.close();
								if (showDebug) {
									System.out.println("\twrote file: " + faultFileName);
								}
								//	    					System.out.println("c");
								PrintWriter benchStream = new PrintWriter(new FileOutputStream(benchFileName));
								//	    					System.out.println("d");
								String append = "# subbench\n";
								for (int m = 0; m < nodeOutputs.size(); m++) {
									if (k != m) {
										append += "INPUT(" + (unusedNode + nodeOutputs.get(m)) + ")\n";
									}
								}
								String andGateInputs = "";
								for (String m : nodeInputs) {
									andGateInputs += (unusedNode + m) + ", ";
								}
								inputFile = new Scanner(new File(inputFileName));
								while (inputFile.hasNextLine()) {
									stringWalker = inputFile.nextLine();
									if (stringWalker.contains("INPUT(")) {
										String o = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")"));	// o is the input
										if (nodeInputs.contains(o)) {
											append += (unusedNode + o) + " = " + (inputValues.charAt(nodeInputs.indexOf(o)) == '1' ? "BUFF(" : "NOT(") + o + ")\n";
										}
										benchStream.println(stringWalker);
									} else if (stringWalker.contains("OUTPUT(")) {
										String o = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")"));	// o is the output
										if (nodeOutputs.contains(o)) {
											benchStream.println(stringWalker.substring(0, stringWalker.indexOf("(") + 1) + (unusedNode + o) + stringWalker.substring(stringWalker.indexOf(")")));
										} else {
											benchStream.println(stringWalker);
										}
									} else if (stringWalker.contains("=")) {
										String[] gateInputsStringArray = stringWalker.substring(stringWalker.indexOf("(") + 1, stringWalker.indexOf(")")).split(", ");
										String o = stringWalker.split(" ")[0];	// o is the output
										String gateInputsString = "";
										for (int m = 0; m < gateInputsStringArray.length; m++) {
											String n = gateInputsStringArray[m];  	// n is the input(s)
											gateInputsString += (nodeInputs.contains(n) || (nodeOutputs.contains(n)) ? (unusedNode + n) : n) + ", ";
										}
										gateInputsString = gateInputsString.substring(0, gateInputsString.length() - 2);
										String gateAfterOutputString = stringWalker.substring(stringWalker.indexOf(" = "), stringWalker.indexOf("(") + 1) + gateInputsString + ")";
										if (nodeOutputs.get(k).equals(o)) { // if this should be replaced with the and gate
											append += (unusedNode + o) + " = AND(" + andGateInputs.substring(0, andGateInputs.length() - 2) + ")\n";
										} else if (nodeOutputs.contains(o)) { // if this is 1 of the outputs, then it should be replaced with a primary input, so nothing here
											//benchStream.println((o + unusedNodeNumber) + gateAfterOutputString);
										} else if (nodeInputs.contains(o)) {
											append += (unusedNode + o) + " = " + (inputValues.charAt(nodeInputs.indexOf(o)) == '1' ? "BUFF(" : "NOT(") + o + ")\n";
											benchStream.println(stringWalker);
										} else if (nodeAfterOutputs.contains(o)) {
											benchStream.println(stringWalker.substring(0, stringWalker.indexOf("(") + 1) + gateInputsString + ")");	// doesnt work for c17 because it does 27->11 and 40->23
										} else if (duplicates.contains(o) && numberOfDuplicates > 0 && !nodeOutputs.contains(o)) { // true duplicate
											//    									benchStream.println(stringWalker);
										} else {
											benchStream.println(stringWalker);
										}
									} else {
										benchStream.println(stringWalker);
									}
								}
								benchStream.print(append.substring(0, append.length() - 1));
								inputFile.close();
								benchStream.close();
								System.out.println("\twrote file: " + benchFileName);
								// String Atalanta = System.getProperty("user.dir");
								// System.out.println(Atalanta);
								// atalanta-M -A -t cQuiz2.5.5.5.01.pat -f cQuiz2.5.5.5.01.flt -W 1 cQuiz2.5.5.5.01.bench
								String[] AtalantaArgsOneOutput = {"atalanta-M", "-D", "1", "-t", testPatternFileName, "-f", faultFileName, "-W", "1", benchFileName}; // TODO change -D to/from -A
								String[] AtalantaArgsRandOutput = {"atalanta-M", "-A", "-t", testPatternFileName, "-f", faultFileName, "-W", "1", benchFileName}; // TODO change -D to/from -A
								String[] AtalantaArgs = isOneOutput ? AtalantaArgsOneOutput : AtalantaArgsRandOutput;
								
								long startTime = 0;
								try {
									if (showDebug) {
										for (String s : AtalantaArgs) {
											System.out.print(s + " ");
										}
										System.out.println();
									}
									Runtime rt = Runtime.getRuntime();
									Process p = rt.exec(AtalantaArgs);
									startTime = System.currentTimeMillis();
//									duration = System.currentTimeMillis() - startTime;
									
//									BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//									String data = bufferedReader.readLine();
//									while (data != null) {
//										 System.out.println("bufferedReaderIndex = 1" + "; data = " + data);
//										if (data.contains("Computing time:")) { // && data.contains("s");
//											duration = Double.parseDouble(data.substring(data.indexOf("Computing time:") + "Computing time:".length(), data.indexOf("s")).trim());
//											break;
//										}
//										data = bufferedReader.readLine();
//									}
									
									
									p.waitFor();
									duration = System.currentTimeMillis() - startTime;
									if (showDebug) {
										System.out.println("\twrote file: " + testPatternFileName);
									}
								} catch (java.io.IOException ioe) {
									System.err.println("Error:    in I/O processing of Atalanta in dir\n");
									System.err.println("       or in calling external command");
									ioe.printStackTrace();
								} catch (java.lang.InterruptedException ie) {
									System.err.println("Error: the execution of the external program was interrupted");
									ie.printStackTrace();
								}

//								if (showDebug) {
//									BenchToDOTDigraph benchToDOTDigraph = new BenchToDOTDigraph(); // TODO comment in/out this and next 2 lines for visuals
//									String[] benchToDOTDigraphArgs = {benchFileName};
//									benchToDOTDigraph.start(benchToDOTDigraphArgs);
//								}

								Scanner testPatternFile;
								while (true) {
									try {
										testPatternFile = new Scanner(new File(testPatternFileName));
										break;
									} catch (IOException e) {		// if cannot open file
										//System.out.println("\terror using the file ");
									}
								}
								//	    					System.out.println("j");
								String testVector = "none";//(testPatternFile.hasNextLine() && nodeOutputs.size() == 1);
								while (testPatternFile.hasNextLine()) {
									stringWalker = testPatternFile.nextLine();
									if (nodeOutputs.size() == 1) {
										testVector = stringWalker;
										break;
									}
									int numXcounter = 0;
									for (int z = 0; z < nodeOutputs.size() - 1; z++) {
										char charAtInputPosition = stringWalker.charAt(stringWalker.length() - 1 - z);
										if (charAtInputPosition != 'x') {
											break;
										}
										numXcounter++;
									}
									if (numXcounter == nodeOutputs.size() - 1) {
										testVector = stringWalker;
										break;
									}
								}
								testPatternFile.close();
								
								if (!showDebug) {
									File[] files = new File[3];
									files[0] = new File(benchFileName);
									files[1] = new File(faultFileName);
									files[2] = new File(testPatternFileName);
									for (File f : files) {
										if (f.delete() && showDebug) {
											System.out.println("\tdeleted file: " + f.getName());
										}
									}
								}
//								if (!faultIsDetected && allOtherInputs.length > 0) {
//									faultIsDetected = true;
//									for (int z = 0; z < allOtherInputs.length; z++) {
//										if (allOtherInputs[z] != 4) {
//											faultIsDetected = false;
//											break;
//										}
//									}
//								}
//								canBeDetected += faultIsDetected ? "1" : "0";
								//println(inputValues);
								outputStream.println((isOneOutput ? "" : trialCounter + "; ") + counter++ + "; " + gates.get(i) + "; " + gates.get(j) + "; " + numberOfDuplicates + "; " + duplicates.size() + "; " + ((double) duplicates.size() / numberOfGates) + "; " + nodeInputs.size() + "; " + nodeOutputs.size() + "; " + (int) (nodeOutputs.size() * Math.pow(2, nodeInputs.size())) + "; " + duplicates + "; " + nodeInputs + "; " + nodeOutputs + "; " + nodeAfterOutputs + "; {" + nodeOutputs.get(k) + "}; {" + inputValues + "}; " + duration + "; " + testVector);
							}
							//	    				canBeDetected += duration + ", ";
						}
					}
//					outputStream.println(counter++ + "; " + gates.get(i) + "; " + gates.get(j) + "; " + numberOfDuplicates + "; " + duplicates.size() + "; " + ((double) duplicates.size() / numberOfGates) + "; " + nodeInputs.size() + "; " + nodeOutputs.size() + "; " + (int) (nodeOutputs.size() * Math.pow(2, nodeInputs.size())) + "; " + duplicates + "; " + nodeInputs + "; " + nodeOutputs + "; " + nodeAfterOutputs + "; " + nodeOutputs.get(k) + "; " + inputValues + "; " + duration);
					if (!isOneOutput) {
						break;
					}
				}
				if (!isOneOutput) {
					trialCounter++;
				}
			}
			outputStream.close();
			System.out.println("\twrote file: " + outputFileName);
			} catch (IOException e) {		// if cannot open file
				System.out.println("\terror using the file " + inputFileName + ", " + benchFileName + ", " + faultFileName + ", or " + testPatternFileName);
				System.exit(0);
			} finally {
				outputStream.close();
			}
			println("done");
		}

		public class listDataStructure {
			public ArrayList<String> arrayList;	

			public listDataStructure() {
				arrayList = new ArrayList<String>();
			}
		}

		public void print(String s) {
			System.out.print(s);
		}

		public <T> void print(T s) {
			System.out.print("" + s);
		}

		public void println() {
			System.out.println();
		}

		public void println(int i) {
			System.out.println(i);
		}

		public void println(String s) {
			System.out.println(s);
		}
	}