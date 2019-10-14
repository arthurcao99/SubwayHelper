package Model;

import java.util.ArrayList;

public class Line {
	private String lineName;
	private ArrayList<Station> stations;
	public Line(String lineName){
		this.lineName=lineName;
		this.stations=new ArrayList<Station>();
	}
	public Line() {
		// TODO Auto-generated constructor stub
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public ArrayList<Station> getStations() {
		return stations;
	}
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}
	public String toString(){
		String showStations="";
		for (int i=0;i<this.stations.size();i++){
			showStations=showStations+this.stations.get(i).getStationName()+" ";
		}
		return showStations;
	}
}
