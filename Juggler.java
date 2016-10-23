import java.util.StringTokenizer;

/**
 * This class is used to create objects used to represent individual jugglers.
 * Along with the juggler's name, hand-eye coordination, endurance, and pizzazz, each Juggler object
 * will contain their current most preferred circuit, an ordered array containing all of their preferences,
 * and their compatibility, which is there dot product with a given circuit.
 * 
 * @author Steven Wojsnis
 *
 */
public class Juggler {
	String Name, H, E, P;
	String[] preference = new String[10];
	int prefCount = 0;
	int compatibility;
	
	public Juggler(){
		Name="empty";
	}
	
	public Juggler(String n,String h, String e, String p, String prefs){
		Name=n;
		H=h;
		E=e;
		P=p;
	
		//Because a single string containing all of the juggler's preferences is passed to
		//the constructor, tokenizer is used to separate and store each individual preference.
		StringTokenizer tokenizer = new StringTokenizer(prefs, ",");
		for(int i=0;i<10;i++){
			preference[i]=tokenizer.nextToken();
		}
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getH() {
		return H;
	}

	public void setH(String h) {
		H = h;
	}

	public String getE() {
		return E;
	}

	public void setE(String e) {
		E = e;
	}

	public String getP() {
		return P;
	}

	public void setP(String p) {
		P = p;
	}

	public String[] getPreference() {
		return preference;
	}

	public void setPreference(String[] preference) {
		this.preference = preference;
	}

	public int getPrefCount() {
		return prefCount;
	}

	public void setPrefCount(int prefCount) {
		this.prefCount = prefCount;
	}

	public int getCompatibility() {
		return compatibility;
	}

	public void setCompatibility(int compatibility) {
		this.compatibility = compatibility;
	}
	
	
}
