import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * 
 * @author Steven Wojsnis
 * JuggleFest problem for internship application
 * 
 * Problem is solved by placing jugglers in their most preferred circuit, and then cutting each
 * circuit down to 6 jugglers by eliminating the least compatible jugglers(based on dot product of skills).
 * 
 * This process is done until all jugglers are assigned to a circuit. Each time a juggler is kicked out of
 * a circuit, their most preferred circuit becomes the one directly after their previously most preferred.
 * 
 * In the case that a juggler cannot get into any of their preferred circuits, they are placed based on
 * which of the remaining circuits with available spots has the highest dot product with their skills.
 *
 */
public class Main {
	
	//Declarations for various lists that will be used to hold the circuits (along with jugglers within each
	//circuit, the skills required for each circuit Jugglers, and Jugglers that don't fit 
	//into any of their preferences
	static LinkedHashMap<Integer, List<Juggler>> CircuitList = new LinkedHashMap<Integer, List<Juggler>>();
	static List<Circuits> CircuitTraits = new ArrayList<Circuits>();
	static List<Juggler> JugglerList = new ArrayList<Juggler>();
	static List<Juggler> misfitList = new ArrayList<Juggler>();
	
	//Counts of the circuits and jugglers used to find the number of jugglers that should be placed in each circuit
	static int circuitCount = 0, jugglerCount = 0;
	
	
	/**
	 * Reads in and sorts the information from the input file, and calls the methods: circuitAssign(),
	 * removeExcess(), misfitAssign(), and print() as needed to successfully place all Jugglers in the
	 * correct circuit.
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		
		int count=0; //Counter used to place Lists of circuits in the LinkedHashMap CircuitList

		//Reading in and organizing the text from the input file
		try (BufferedReader br = new BufferedReader(new FileReader("jugglefest.txt"))){
			String line = br.readLine();
			while(line != null){
				StringTokenizer tokenizer = new StringTokenizer(line, " ");

				if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equals("C")){
					circuitCount++;
					CircuitList.put(count++, new ArrayList<Juggler>());
					CircuitTraits.add(new Circuits(tokenizer.nextToken(), 
							tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken()));	
				}
				
				tokenizer= new StringTokenizer(line," ");
				if(tokenizer.hasMoreTokens()&&tokenizer.nextToken().equals("J")){
					jugglerCount++;
					JugglerList.add(new Juggler(tokenizer.nextToken(),tokenizer.nextToken(),
							tokenizer.nextToken(),tokenizer.nextToken(),tokenizer.nextToken()));
				}
				line = br.readLine();
			}//while
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		int max = jugglerCount/circuitCount; //maximum number of jugglers per circuit
		
		//Assigns each juggler to their favorite circuit, then removes the lowest members of each circuit
		//until each circuit has at most 6 jugglers
		circuitAssign();
		removeExcess(max);
		
		
		//Continues the assign/remove process until there aren't any more jugglers in JugglerList waiting 
		//to be assigned. Once this happens, before ending the while loop, jugglers who weren't eligible
		//to be in any of their preferred circuits are assigned to the most appropriate circuit.
		boolean continueAssign = true; 
		while(continueAssign){
			continueAssign = false;
			for(int j=0;j<jugglerCount;j++){
				if(!JugglerList.get(j).getName().equals("empty")){
					continueAssign = true;
				}
			}//for
			if(continueAssign){
				circuitAssign();
				removeExcess(max);
			}
			else
				misfitAssign();
		}//while
		
		print(); //Prints circuit contents in the proper format to output file
	
	}
	
	/**
	 * Assigns Jugglers that are in JugglerList to their currently most preferred circuit. 
	 * Also adjusts Juggler's most preferred circuit, so that in the case that the juggler is kicked
	 * out of the circuit, they do not attempt to rejoin it.
	 * 
	 * Once a Juggler is assigned to a circuit, they are added to that circuit's CircuitList, and
	 * their spot on the JugglerList is set to an "empty" Juggler. This is done, rather than simply
	 * removing the Juggler from JugglerList, to try to avoid the somewhat costly process of shifting
	 * each element in JugglerList to the left.
	 * 
	 * Similarly, if a Juggler reaches their last preferred circuit, and is still not allowed in, they
	 * are added to the misfitList, where they will later be assigned to an open circuit. Their spot on
	 * JugglerList is also set to an "empty" Juggler.
	 */
	public static void circuitAssign(){
		int jugglerPref, compatibility, jugglerH,jugglerE,jugglerP,circuitH,circuitE,circuitP;
		for(int i = 0;i<JugglerList.size();i++){
			if(!JugglerList.get(i).getName().equals("empty")){
				jugglerPref = Integer.parseInt(JugglerList.get(i).getPreference()[JugglerList.get(i).getPrefCount()].substring(1));
				if(JugglerList.get(i).getPrefCount() <9){
					JugglerList.get(i).setPrefCount(JugglerList.get(i).getPrefCount()+1); //updates the juggler's preferred circuit
					
					//stores value of juggler's skills, and their preferred circuit's skills for dot product.
					jugglerH=Integer.parseInt(JugglerList.get(i).getH().substring(2));
					jugglerE=Integer.parseInt(JugglerList.get(i).getE().substring(2));
					jugglerP=Integer.parseInt(JugglerList.get(i).getP().substring(2));
					circuitH=Integer.parseInt(CircuitTraits.get(jugglerPref).getH().substring(2));
					circuitE=Integer.parseInt(CircuitTraits.get(jugglerPref).getE().substring(2));
					circuitP=Integer.parseInt(CircuitTraits.get(jugglerPref).getP().substring(2));
					compatibility = (jugglerH*circuitH)+(jugglerE*circuitE)+(jugglerP*circuitP);
					JugglerList.get(i).setCompatibility(compatibility);
				
					// Adds the juggler to the appropriate circuit, replaces their spot on JugglerList with
					//an "empty" juggler object
					CircuitList.get(jugglerPref).add(JugglerList.get(i));
					JugglerList.set(i, new Juggler());
				}
				//If a juggler is not eligible for any of their preferences, they are added to the misfitList
				else{
					misfitList.add(JugglerList.get(i));
					JugglerList.set(i, new Juggler());
				}
			}
		}//for
	}
	
	/**
	 * Checks to see if there are more than the allowed amount of Jugglers in any of circuits, and if so,
	 * removes the Juggler with the lowest dot product with the circuit (compatibility) until the circuit
	 * contains at most the max amount of Jugglers.
	 * 
	 * Removed Jugglers are returned to JugglerList for reassignment.
	 * 
	 * @param max = the maximum amount of Jugglers allowed in any given circuit
	 */
	static void removeExcess(int max){
		for(int i=0;i<circuitCount;i++){
			while(CircuitList.get(i).size() > max){
				Juggler lowest = CircuitList.get(i).get(0); //Initially sets lowest Juggler to the first Juggler in the circuit
				int lowestPosition = 0; 
				
				//Cycles through all the Jugglers in the circuit and compares their compatibilities with the circuit
				for(int j=0;j<CircuitList.get(i).size();j++){
					if (CircuitList.get(i).get(j).getCompatibility() <= lowest.getCompatibility()){
						lowest = CircuitList.get(i).get(j); //If a lower compatibility is found, adjust accordingly
						lowestPosition = j;
					}
				}//for
				lowest = CircuitList.get(i).get(lowestPosition);
				
				//Takes the lowest in the circuit, and moves it back to the JugglerList to be reassigned
				if(JugglerList.get(i).getName().equals("empty")) 
					JugglerList.set(i, lowest);
				else
					JugglerList.add(i, lowest);
				
				CircuitList.get(i).remove(lowestPosition);
				
			}//while
		}//for
	}
	
	/**
	 * Assigns Jugglers who were not allowed in any of their preferred circuits to any remaining
	 * available circuits.
	 * 
	 * Essentially does the same as circuitAssign(), except it assigns from misfitList, not JugglerList.
	 * Assigns misfit Jugglers by finding Circuits with available spots, and then finding Jugglers with
	 * the highest dot product of skills (compatibility), until their are no more available spots.
	 */
	static void misfitAssign(){
		int compatibility, jugglerH,jugglerE,jugglerP,circuitH,circuitE,circuitP;
		for(int i=0;i<CircuitList.size();i++){
			Juggler chosen = new Juggler(); //temporarily sets the chosen Juggler to be an "empty" Juggler
			while(CircuitList.get(i).size() <6){
				int highest=0, position=0;
				
				//Cycles through all of the misfit Jugglers to see which circuit they are most proficient in
				for(int j=0;j<misfitList.size();j++){
					
					//stores value of juggler's skills, and their preferred circuit's skills for dot product.
					jugglerH=Integer.parseInt(misfitList.get(j).getH().substring(2));
					jugglerE=Integer.parseInt(misfitList.get(j).getE().substring(2));
					jugglerP=Integer.parseInt(misfitList.get(j).getP().substring(2));
					circuitH=Integer.parseInt(CircuitTraits.get(i).getH().substring(2));
					circuitE=Integer.parseInt(CircuitTraits.get(i).getE().substring(2));
					circuitP=Integer.parseInt(CircuitTraits.get(i).getP().substring(2));
					compatibility = (jugglerH*circuitH)+(jugglerE*circuitE)+(jugglerP*circuitP);
					
					if (compatibility >= highest){
						highest = compatibility;
						position = j;
						chosen = misfitList.get(j); //If a new highest compatibility is found, that Juggler is set as chosen
					}
				}//for
				
				//Chosen is added to the Circuit, removed from misfitList
				CircuitList.get(i).add(chosen);
				misfitList.remove(position);
				
			}//while
		}//for
	}
	
	/**
	 * Prints the circuits (starting from C1999, ending in C0), with all the Jugglers in each circuit
	 * along with their preferences, and their dot product(compatibility) with each of their preferences.
	 * 
	 * Because of the fact that not all of the Juggler-preferred Circuit dot products were performed
	 * previously, this method also calculates each Juggler's dot product with each of their preferred
	 * circuits.
	 */
	public static void print(){
		int compatibility, jugglerH,jugglerE,jugglerP,circuitH,circuitE,circuitP;
		try(PrintStream out = new PrintStream(new FileOutputStream("JugglefestAnswer.txt"))){
			System.setOut(out);
			
			//Cycles through all the circuits
			for(int i=CircuitList.size()-1;i>=0;i--){
				out.print("C"+i+" ");
				
				//Cycles through all the Jugglers in each circuit
				for(int j=0;j<CircuitList.get(i).size();j++){
					out.print(CircuitList.get(i).get(j).getName()+" ");
					
					//Cycles through each Juggler's preference array
					for(int k=0;k<CircuitList.get(i).get(j).getPreference().length;k++){
						int position = Integer.parseInt(CircuitList.get(i).get(j).getPreference()[k].substring(1));
						
						jugglerH=Integer.parseInt(CircuitList.get(i).get(j).getH().substring(2));
						jugglerE=Integer.parseInt(CircuitList.get(i).get(j).getE().substring(2));
						jugglerP=Integer.parseInt(CircuitList.get(i).get(j).getP().substring(2));
						circuitH=Integer.parseInt(CircuitTraits.get(position).getH().substring(2));
						circuitE=Integer.parseInt(CircuitTraits.get(position).getE().substring(2));
						circuitP=Integer.parseInt(CircuitTraits.get(position).getP().substring(2));
						compatibility = (jugglerH*circuitH)+(jugglerE*circuitE)+(jugglerP*circuitP);
						

						if(k==CircuitList.get(i).get(j).getPreference().length-1 && j<CircuitList.get(i).size()-1)
							out.print(CircuitList.get(i).get(j).getPreference()[k]+":"+compatibility+", ");
						else
							out.print(CircuitList.get(i).get(j).getPreference()[k]+":"+compatibility+" ");
					}//for
				}//for
				out.println();
			}//for
			
			
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
}
