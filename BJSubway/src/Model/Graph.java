package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Graph {
	private ArrayList<Station> all=new ArrayList<Station>();
	private Set<Station> allStations = new HashSet<Station>();
	private Map<Station, LinkedList<Station>> adj = new HashMap<Station, LinkedList<Station>>();
	
	public ArrayList<Station> getAll() {
		return all;
	}

	public void setAll(ArrayList<Station> all) {
		this.all = all;
	}

	public Graph() {
		
	}
	
	public Graph(Set<Station> allStations,Map<Station, LinkedList<Station>> adj,ArrayList<Station> all) {
		this.allStations = allStations;
		this.adj = adj;
		this.all=all;
	}

	public Set<Station> getAllStations() {
			return allStations;
	}
	
	public void setAllStations(Set<Station> allStations) {
		this.allStations = allStations;
	}

	public Map<Station, LinkedList<Station>> getAdj() {
		return adj;
	}

	public void setAdj(Map<Station, LinkedList<Station>> adj) {
		this.adj = adj;
	}
}
