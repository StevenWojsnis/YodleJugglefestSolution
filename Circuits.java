
/**
 * This class is used to act as individual circuits. Each circuit receives it's own object of this
 * class. These objects will contain the name of the circuit, along with the hand-eye coordination, endurance
 * and pizzazz of the circuit.
 * 
 * @author Steven Wojsnis
 *
 */
public class Circuits {
	String Name, H, E, P;
	
	public Circuits(String n, String h, String e, String p){
		Name=n;
		H=h;
		E=e;
		P=p;
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
}
