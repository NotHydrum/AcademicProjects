import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class TrafficAnalysis {
	
	public static void main(String[] args) throws IOException {
		File traceA = new File("MP3_Grupo27_A.csv");
		File traceB = new File("MP3_Grupo27_B.csv");
		analyse(traceA, "A");
		analyse(traceB, "B");
    }
	
	private static void analyse(File trace, String identifier) throws IOException {
		System.out.println("Analysing trace " + identifier + "..." + "\n");
		//########################################## SYN ###########################################
		Scanner traceScanner = new Scanner(trace);
		HashMap<String, ArrayList<Double>> synRequests = new HashMap<>();
		while (traceScanner.hasNextLine()){
			String packageLine = traceScanner.nextLine();
		    String[] parameters = packageLine.substring(1, packageLine.length() - 1).split("\",\"");
		    if (parameters[6].equals("TCP") && parameters[9].equals("0x002")) {
		    	ArrayList<Double> timings = synRequests.get(parameters[2] + " " + parameters[3]);
		       	if (timings != null) {
		        	timings.add(Double.parseDouble(parameters[1]));
		        		synRequests.put(parameters[2] + " " + parameters[3], timings);
		        	}
		        else {
		        	timings = new ArrayList<>();
		        	timings.add(Double.parseDouble(parameters[1]));
		        	synRequests.put(parameters[2] + " " + parameters[3], timings);
		        }
		    }
		}
		HashMap<String, ArrayList<Double>> attacks = new HashMap<>();
		for (Entry<String, ArrayList<Double>> entry : synRequests.entrySet()) {
			String key = entry.getKey();
			ArrayList<Double> value = entry.getValue();
			if (value.size() >= 50 && value.size() / (value.get(value.size() - 1) - value.get(0)) >= 0.5) {
				attacks.put(key, value);
			}
		}
		if (!attacks.isEmpty()) {
			int count = 1;
			for (Entry<String, ArrayList<Double>> entry : attacks.entrySet()) {
				String key = entry.getKey();
				ArrayList<Double> value = entry.getValue();
				int rhythms = rhythms(value);
				System.out.println("SYN Flood attack #" + count + "\n" + 
					report(key.split(" ")[0], key.split(" ")[1], value.size(), rhythms,
							(value.get(value.size() - 1) - value.get(0)) / 60.0));
				FileWriter dataWriter = new FileWriter(new File(identifier + "_SYNFlood_#" + count + ".txt"));
				for (int i = 1; i < value.size(); i++) {
					dataWriter.append(value.get(i) + " " + (value.get(i) - value.get(i - 1)) + "\n");
				}
				dataWriter.close();
				count++;
			}
		}
		else {
			System.out.println("No SYN Flood attacks detected." + "\n");
		}
		traceScanner.close();
		//########################################## UDP ###########################################
		traceScanner = new Scanner(trace);
		HashMap<String, ArrayList<Double>> dnsRequests = new HashMap<>();
		while (traceScanner.hasNextLine()){
			String packageLine = traceScanner.nextLine();
		    String[] parameters = packageLine.substring(1, packageLine.length() - 1).split("\",\"");
		    if (parameters[6].equals("DNS")) {
		    	ArrayList<Double> timings = dnsRequests.get(parameters[2] + " " + parameters[3]);
		        if (timings != null) {
		        	timings.add(Double.parseDouble(parameters[1]));
		        	dnsRequests.put(parameters[2] + " " + parameters[3], timings);
		        }
		        else {
		        	timings = new ArrayList<>();
		        	timings.add(Double.parseDouble(parameters[1]));
		        	dnsRequests.put(parameters[2] + " " + parameters[3], timings);
		        }
		    }
		}
		attacks = new HashMap<>();
		for (Entry<String, ArrayList<Double>> entry : dnsRequests.entrySet()) {
			String key = entry.getKey();
			ArrayList<Double> value = entry.getValue();
			if (value.size() >= 50 && value.size() / (value.get(value.size() - 1) - value.get(0)) >= 0.5) {
				attacks.put(key, value);
			}
		}
		if (!attacks.isEmpty()) {
			int count = 1;
			for (Entry<String, ArrayList<Double>> entry : attacks.entrySet()) {
				String key = entry.getKey();
				ArrayList<Double> value = entry.getValue();
				int rhythms = rhythms(value);
				System.out.println("UDP Flood attack #" + count + "\n" + 
					report(key.split(" ")[0], key.split(" ")[1], value.size(), rhythms,
							(value.get(value.size() - 1) - value.get(0)) / 60.0));
				FileWriter dataWriter = new FileWriter(new File(identifier + "_SYNFlood_#" + count + ".txt"));
				for (int i = 1; i < value.size(); i++) {
					dataWriter.append(value.get(i) + " " + (value.get(i) - value.get(i - 1)) + "\n");
				}
				dataWriter.close();
				count++;
			}
		}
		else {
			System.out.println("No UDP Flood attacks detected." + "\n");
		}
		traceScanner.close();
		//########################################## PoD ###########################################
		traceScanner = new Scanner(trace);
		HashMap<String, ArrayList<Double>> pings = new HashMap<>();
		while (traceScanner.hasNextLine()){
			String packageLine = traceScanner.nextLine();
	    	String[] parameters = packageLine.substring(1, packageLine.length() - 1).split("\",\"");
	    	if (parameters[6].equals("ICMP") && parameters[7].equals("8")) {
        		ArrayList<Double> timings = pings.get(parameters[2] + " " + parameters[3]);
        		if (timings != null) {
        			timings.add(Double.parseDouble(parameters[1]));
        			pings.put(parameters[2] + " " + parameters[3], timings);
        		}
        		else {
        			timings = new ArrayList<>();
        			timings.add(Double.parseDouble(parameters[1]));
        			pings.put(parameters[2] + " " + parameters[3], timings);
        		}
        	}
        }
		attacks = new HashMap<>();
		for (Entry<String, ArrayList<Double>> entry : pings.entrySet()) {
			String key = entry.getKey();
			ArrayList<Double> value = entry.getValue();
			if (value.size() >= 50 && value.size() / (value.get(value.size() - 1) - value.get(0)) >= 0.5) {
				attacks.put(key, value);
			}
		}
		if (!attacks.isEmpty()) {
			int count = 1;
			for (Entry<String, ArrayList<Double>> entry : attacks.entrySet()) {
				String key = entry.getKey();
				ArrayList<Double> value = entry.getValue();
				int rhythms = rhythms(value);
				System.out.println("Ping of Death attack #" + count + "\n" + 
					report(key.split(" ")[0], key.split(" ")[1], value.size(), rhythms,
							(value.get(value.size() - 1) - value.get(0)) / 60.0));
				FileWriter dataWriter = new FileWriter(new File(identifier + "_SYNFlood_#" + count + ".txt"));
				for (int i = 1; i < value.size(); i++) {
					dataWriter.append(value.get(i) + " " + (value.get(i) - value.get(i - 1)) + "\n");
				}
				dataWriter.close();
				count++;
			}
		}
		else {
			System.out.println("No Ping of Death attacks detected." + "\n");
		}
        traceScanner.close();
	}
	
	private static int rhythms(ArrayList<Double> timings) {
		int rhythms = 1;
		double currentRhythm = timings.get(1) - timings.get(0);
		double lastRhythm = timings.get(1) - timings.get(0);
		boolean justChanged = false;
		for (int i = 2; i < timings.size(); i++) {
			if (((currentRhythm > 0.1 && (timings.get(i) - timings.get(i - 1) < currentRhythm * 0.8 ||
					timings.get(i) - timings.get(i - 1) > currentRhythm * 1.2)) ||
					(currentRhythm <= 0.1 && (timings.get(i) - timings.get(i - 1) < currentRhythm * 0.65 ||
							timings.get(i) - timings.get(i - 1) > currentRhythm * 1.35))) && !justChanged) {
				rhythms++;
				lastRhythm = currentRhythm;
				currentRhythm = timings.get(i) - timings.get(i - 1);
				justChanged = true;
			}
			else if ((timings.get(i) - timings.get(i - 1) < currentRhythm * 0.8 ||
					timings.get(i) - timings.get(i - 1) > currentRhythm * 1.2) && justChanged) {
				rhythms--;
				currentRhythm = lastRhythm;
				justChanged = false;
			}
			else if (justChanged) {
				justChanged = false;
			}
		}
		return rhythms;
	}
	
	private static String report(String attacker, String victim, int packets, int rhythms, double duration) {
		return "Attacker: " + attacker + "\n" +
			"Victim: " + victim + "\n" +
			"Packets sent: " + packets + "\n" +
			"Number of different rhythms: " + rhythms + "\n" +
			"Attack duration: " + duration + " minutes." + "\n";
	}

}
