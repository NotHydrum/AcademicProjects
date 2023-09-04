import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * On a laptop with an Intel Core i7-3630QM @ 2.4GHz, 8GB of RAM and an HDD it took the program 20 minutes to read the
 * data and figure out the answers to Q1-Q6, and a further 2 minutes to create the data file used by gnuplot to
 * generate the graph for Q7.
 * @author Group number: 27
 * @author Miguel Nunes fc56338
 * @author Henrique Catarino fc56278
 * @author Francisco Oliveira fc56318
 */
public class TrafficAnalysis {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Processing data. This may take a while...");
		File trace = new File("MP2_Grupo27.csv");
		Scanner traceScanner = new Scanner(trace);
		traceScanner.nextLine();
		int ipv4Counter = 0;
		int ipv6Counter = 0;
		List<String> ipv4UniqueDestinations = new ArrayList<>();
		List<String> tcpSourcePorts = new ArrayList<>();
		int minimumLength = Integer.MAX_VALUE;
		int maximumLength = 0;
		double totalLength = 0;
		List<Integer> lengths = new ArrayList<>();
		int tcpRSTCounter = 0;
		while (traceScanner.hasNextLine()){
			String packageLine = traceScanner.nextLine();
			String[] parameters = packageLine.substring(1, packageLine.length() - 1).split("\",\"");
			if (parameters[3].split("\\.").length > 1) {
				ipv4Counter++;
				if (!ipv4UniqueDestinations.contains(parameters[3])) {
					ipv4UniqueDestinations.add(parameters[3]);
				}
			}
			else {
				ipv6Counter++;
			}
			if (parameters[6].equals("TCP") && !tcpSourcePorts.contains(parameters[4])) {
				tcpSourcePorts.add(parameters[4]);
			}
			int length = Integer.parseInt(parameters[7]);
			if (length < minimumLength) {
				minimumLength = length;
			}
			else if (length > maximumLength) {
				maximumLength = length;
			}
			totalLength += length;
			lengths.add(length);
			if (parameters[6].equals("TCP") && parameters[8].equals("0x004")) {
				tcpRSTCounter++;
			}
		}
		traceScanner.close();
		System.out.println("Nº IPV4 Connections: " + ipv4Counter + "\n" +
							"Nº IPV6 Connections: " + ipv6Counter + "\n" +
							"Nº Unique IPV4 Destinations: " + ipv4UniqueDestinations.size() + "\n" +
							"Nº Unique TCP Source Ports: " + tcpSourcePorts.size() + "\n" +
							"Minimum Data Length: " + minimumLength + "\n" +
							"Average Data Length: " + totalLength / (ipv4Counter + ipv6Counter) + "\n" +
							"Maximum Data Length: " + maximumLength + "\n" +
							"Nº of TCP connection failures: " + tcpRSTCounter + "\n" + "\n" +
							"Creating gnuplot graphic data...");
		File graphData = new File("GraphData.txt");
		graphData.createNewFile();
		FileWriter dataWriter = new FileWriter(graphData, true);
		for (int i = 0; i < minimumLength; i++) {
			dataWriter.append(i + " " + 100 + "\n");
		}
		for (int i = minimumLength; i < maximumLength; i++) {
			double counter = 0;
			for (int f = 0; f < lengths.size(); f++) {
				if (lengths.get(f) > i) {
					counter++;
				}
			}
			dataWriter.append(i + " " + (counter / lengths.size() * 100) + "\n");
		}
		dataWriter.append(maximumLength + " " + 0);
		dataWriter.close();
		System.out.println("...Done");
	}

}
