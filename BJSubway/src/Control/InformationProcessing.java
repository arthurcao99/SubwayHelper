package Control;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import Model.Graph;
import Model.Line;
import Model.Station;

public class InformationProcessing {
	private ArrayList<Line> lines=new ArrayList<Line>();
	private ArrayList<Station> allStations=new ArrayList<Station>();
	private Map<String, Line> lineMap=new HashMap<>();
	private Graph graph;
	
	public ArrayList<Line> getLines() {
		return lines;
	}
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
	public ArrayList<Station> getAllStations() {
		return allStations;
	}
	public void setAllStations(ArrayList<Station> allStations) {
		this.allStations = allStations;
	}
	public Graph getGraph() {
		return graph;
	}
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public ArrayList<Line> inputInformation(String filePath) throws IOException{
		File file =new File(filePath);
		ArrayList<Line> lines=this.lines;
		ArrayList<Station> allStations=this.allStations;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
			        "UTF-8"));
			String lineTxt=null;
			while ((lineTxt=br.readLine())!=null){
				String[] txt = lineTxt.trim().split(" ");
				
				Line line=new Line();
				line.setLineName(txt[0]);
				ArrayList<Station> stations=new ArrayList<Station>();
				for (int i=1;i<txt.length;i++){
					Station station=isExisted(txt[i],allStations);
					if (station==null){
						station=new Station();
						station.setStationName(txt[i]);
						allStations.add(station);
					}else{
						station.setChangingStation(true);
					}
					stations.add(station);
				}
				line.setStations(stations);
				lines.add(line);
			}		
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
			        "UTF-8"));
			lineTxt=null;
			while ((lineTxt=br.readLine())!=null){
				String[] txt = lineTxt.trim().split(" ");
				for (int i=1;i<txt.length;i++){
					Station station=new Station();
					if (i>1&&i<txt.length-1){
						station=this.Search(txt[i], allStations);
						if (station!=null){
							Station newStation=new Station();
							newStation=this.Search(txt[i-1], allStations);
							if (newStation!=null){
								station.addConnection(newStation);
							}
							newStation=this.Search(txt[i+1], allStations);
							if (newStation!=null){
								station.addConnection(newStation);
							}							
						}
					}else if (i==1){
						station=this.Search(txt[i], allStations);
						if (station!=null){
							Station newStation=new Station();
							
							newStation=this.Search(txt[i+1], allStations);
							if (newStation!=null){
								station.addConnection(newStation);
							}							
						}
					}else if (i==txt.length-1){
						station=this.Search(txt[i], allStations);
						if (station!=null){
							Station newStation=new Station();
							
							newStation=this.Search(txt[i-1], allStations);
							if (newStation!=null){
								station.addConnection(newStation);
							}							
						}
					}				
				}
			}			
			this.allStations=allStations;		
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	public Station isExisted(String stationName, ArrayList<Station> allStations){
		for (int i=0;i<allStations.size();i++){
			if (stationName.equals(allStations.get(i).getStationName())){
				return allStations.get(i);
			}
		}
		return null;
	}



	
	public Station Search(String stationName, ArrayList<Station> allStations){
		for (int i=0;i<allStations.size();i++){
			if (stationName.equals(allStations.get(i).getStationName())){
				return allStations.get(i);
			}
		}
		return null;
	}
	public void addConnection(String stationName,Station station){
		Station nowStation=this.Search(stationName, allStations);
		station.addConnection(nowStation);
	}
	
	public Map<String, Line> generateMap(ArrayList<Line> lines){
		Map<String, Line> lineMap=new HashMap<String,Line>();
		for (int i=0;i<lines.size();i++){
			lineMap.put(lines.get(i).getLineName(), lines.get(i));
		}
		return lineMap;
		
	}
	public void showLine(String lineName,Map<String, Line> lineMap,String filepath) throws IOException {
		File f=new File(filepath);
		FileOutputStream fos1=new FileOutputStream(f);
		OutputStreamWriter dos1=new OutputStreamWriter(fos1);
		dos1.write(lineName+": ");
		dos1.write(lineMap.get(lineName).toString());
		dos1.close();
		System.out.print(lineName+": "+lineMap.get(lineName).toString());

	}
	public Boolean isLineExisted(String lineName,Map<String, Line> lineMap){
		return lineMap.containsKey(lineName);
	}
	public Graph generateGraph(ArrayList<Station> allStations){
		Set<Station> all = new HashSet<Station>();
		for (int i=0;i<allStations.size();i++){
			all.add(allStations.get(i));
		}
		
		Map<Station, LinkedList<Station>> adj = new HashMap<Station, LinkedList<Station>>();
		for (int i=0;i<allStations.size();i++){
			adj.put(allStations.get(i), allStations.get(i).getConnectedStation());
		}
		Graph g=new Graph(all,adj,allStations);
		this.graph=g;
		return g;
	}



}
