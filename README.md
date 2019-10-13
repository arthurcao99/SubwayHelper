# SubwayHelper
# 北京地铁线路规划
仓库代码：
地铁出行路线规划设计：https://www.cnblogs.com/caolx/p/11558996.html

## 1.结构概述
该项目分为Control、Main、Model这三个模块。Control模块用于存放文件读取和计算路径代码，Main模块用于存放主函数，Model用于存放各个实体类，如Station类、Line类等。
## 2.算法使用
由于开始规划这个项目时，未仔细阅读任务要求，错误认为该项目需要利用Dijkstra最短路径算法来求出两个站点之间的最短距离，后发现该程序的要求仅仅是最少站点，对换乘次数并未作具体要求，因此采用了BFS广度优先搜索算法来计算两个站点之间最短路径。
## 3.具体代码分析
### 3.1 Model模块
#### 3.1.1 Station
```
private String stationName; //用于存放站点名称
private LinkedList<Station> connectedStation; //用于存放该站点相邻站点，类似于邻接表结构
private Boolean isChangingStation=false; //用于判断该站点是否为换乘站点
```
#### 3.1.2 Line
```
private String lineName; //用于存放线路名称
private ArrayList<Station> stations; //用于存放该线路中的所有站点信息
```
#### 3.1.3 Graph
通过自定义图结构，方便后续BFS的计算。
```
private Set<Station> allStations = new HashSet<Station>(); //用于存放所有站点信息
private Map<Station, LinkedList<Station>> adj = new HashMap<Station, LinkedList<Station>>(); //邻接表，用于存放每个站点相邻站点的信息
```
### 3.2 Control模块
#### 3.2.1 InformationProcessing
该类主要实现了从文件中读取地铁信息，并以此生成邻接表结构的图。由于存在换乘站点，因此多条线路中可能存在同一站点，为了便于后续邻接表的生成，只能允许存在一个相同站点信息，需要判断读入的站点信息是否已经存在。因此通过isExisted方法来判断是否已存在相同站点信息。同时还需读入每个站点相邻站点的信息，因此需要第二次读入操作。
```
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
```
```
public Station Search(String stationName, ArrayList<Station> allStations) //用于搜索站点信息
public void addConnection(String stationName,Station station) //用于添加站点邻接信息
public Map<String, Line> generateMap(ArrayList<Line> lines) //用于创建map映射，方便读取线路信息
public void showLine(String lineName,Map<String, Line> lineMap,String filepath) throws IOException //用于生成某条线路的所有站点，并进行输出
public Graph generateGraph(ArrayList<Station> allStations) //用于将现有数据生成邻接表
```
#### 3.2.2 ShortestPath
该类主要用来进行最短路径的计算，所用的算法为BFS广度优先搜索，所用的数据结构为邻接表。
为了实现BFS算法，我用Map来初始化了visited，用Map来存放pre。
```
//核心算法 BFS
public void bfs(Station s){
		Queue<Station> queue=new LinkedList<>(); 
		queue.add(s);
		pre.put(s, s);
		visited.put(s, true);
		while(!queue.isEmpty()){
			Station v=queue.remove();
			for (Station w: g.getAdj().get(v)) {
				if (!visited.get(w)) {
					queue.add(w);
					visited.put(w, true);
					pre.put(w, v);
				}
			}
		}
		
	}
```
由于存在不同的站点类型，有普通站点，也有换乘站点，换乘站点存在于多条线路中。为了确定当前处于哪条线路中，对不同的情况需要进行不同的分析。当当前站点为普通站点时，直接获得当前线路。但当当前站点为换乘站点时，又需要分为两种情况进行讨论。第一种，当前为换乘站点，下一站为普通站点时，直接获取下一站站点的线路作为当前线路。第二种，连续两个都为换乘站点时，取两个站点同时都有的线路作为当前线路。
```
//最麻烦的输出代码

public void showShortestPath(ArrayList<Station> path, String filepath) throws IOException {
		File f=new File(filepath);
		FileOutputStream fos1=new FileOutputStream(f);
		OutputStreamWriter dos1=new OutputStreamWriter(fos1);
		
		System.out.println("总站点数："+path.size()+"站");
		dos1.write("总站点数："+path.size()+"站"+"\n");
		System.out.println();
		System.out.println("具体线路：");
		dos1.write("具体线路："+"\n");
		Station start=path.get(0);
		Station second=path.get(1);
		String lineName="";
		if (start.getChangingStation()==false&&second.getChangingStation()==false){
			lineName=this.inLine(start).get(0).getLineName();
		}
		else if (start.getChangingStation()==true&&second.getChangingStation()==false){
			lineName=this.inLine(start).get(0).getLineName();
		}
		else if(start.getChangingStation()==true&&second.getChangingStation()==true){
			lineName=this.isInTheSameLine(start,second);
		}
		else{
			lineName=this.inLine(start).get(0).getLineName();
		}
		System.out.println("--->乘坐："+lineName);
		dos1.write("--->乘坐："+lineName+"\n");
		System.out.println(start.getStationName());
		dos1.write(start.getStationName()+"\n");
		for (int i=1;i<path.size();i++){
			Station nowStation=path.get(i);
			String curLineName="";
			if (i<path.size()-1){
				Station nextStation=path.get(i+1);
				if (nowStation.getChangingStation()==false&&nextStation.getChangingStation()==false){
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				else if (nowStation.getChangingStation()==true&&nextStation.getChangingStation()==false){
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				else if(nowStation.getChangingStation()==true&&nextStation.getChangingStation()==true){
					curLineName=this.isInTheSameLine(nowStation,nextStation);
				}
				else{
					curLineName=this.inLine(nowStation).get(0).getLineName();
				}
				System.out.println(nowStation.getStationName());
				dos1.write(nowStation.getStationName()+"\n");
				if (!lineName.equals(curLineName)){
					lineName=curLineName;
					System.out.println("--->换乘："+lineName);
					dos1.write("--->换乘："+lineName+"\n");
				}
			}
			else{
					else{
				Station preStation=path.get(i-1);
				if (preStation.getChangingStation()==true&&nowStation.getChangingStation()==false){
					if (!this.inLine(nowStation).get(0).getLineName().equals(lineName)){
						System.out.println("--->换乘："+this.inLine(nowStation).get(0).getLineName());
						dos1.write("--->换乘："+this.inLine(nowStation).get(0).getLineName()+"\n");
					}
					
				}

				System.out.println(nowStation.getStationName());
				dos1.write(nowStation.getStationName()+"\n");
			}
			}
		}
		dos1.close();
	}
```
```
public String isInTheSameLine(Station s1,Station s2) //返回两个换乘站点的相同线路名
public ArrayList<Line> inLine(Station station) //判断当前站点所在的线路，换乘站点存在多个线路
public Iterable<Station> path(Station endStation) //用于得到路径信息
```
### 3.1 Main模块
#### 3.1.1 Subway
该主函数用于实现不同参数输入，实现不同的功能。利用switch case结构来对不同的参数进行不同功能的实现。
```
public static void main(String[] args) throws Exception {
		switch (args[0]){
            case "-map":
                //-map subway.txt
                if(args.length==2){
                    InformationProcessing ii=new InformationProcessing();
                    ii.inputInformation(args[1]);
                    System.out.println("成功读取subway.txt文件");
        			ArrayList<Line> lines=new ArrayList<Line>();
        			lines=ii.inputInformation(args[1]);
        			for (int i=0;i<lines.size();i++){
        				System.out.println(lines.get(i).getLineName());
        				for (int j=0;j<lines.get(i).getStations().size();j++){
        					System.out.print(lines.get(i).getStations().get(j).getStationName()+" ");
        				}
        				System.out.println();
        			}
                }else{
                    System.out.println("验证参数格式！");
                }
                break;

            case "-a":
                //-a 1号线 -map subway.txt -o station.txt
                if(args.length==6){
                    InformationProcessing ii=new InformationProcessing();
                    ArrayList<Line> lines=new ArrayList<Line>();
                    lines=ii.inputInformation(args[3]);
                    if (ii.isLineExisted(args[1], ii.generateMap(ii.getLines()))==true){
                    	ii.showLine(args[1],ii.generateMap(ii.getLines()),args[5]);
                    }
                    else {
                    	throw new Exception("线路不存在");
                    }
                    System.out.println("已将结果写入station.txt文件");
                }else{

                    System.out.println("验证参数格式！");
                }
                break;
            case "-b":
                //-b 洪湖里 复兴路 -map subway.txt -o routine.txt

                if(args.length==7){
                    ArrayList<Line> lines=new ArrayList<Line>();
                    InformationProcessing ii=new InformationProcessing();
                    lines=ii.inputInformation(args[4]);
                    Graph g=ii.generateGraph(ii.getAllStations());
                    if (args[1].equals(args[2])){
                    	throw new Exception("请输入两个不同站点");
                    }
                   
                    if (ii.Search(args[1], ii.getAllStations())==null){
                    	throw new Exception("站点1不存在，请重新输入");
                    }
                    if (ii.Search(args[2], ii.getAllStations())==null){
                    	throw new Exception("站点2不存在，请重新输入");
                    }
                    ShortestPath sp=new ShortestPath(ii.Search(args[1], ii.getAllStations()),g,lines);
                    ArrayList<Station> path=new ArrayList<>();
                    path=(ArrayList<Station>)sp.path(ii.Search(args[2],ii.getAllStations()));
                    sp.showShortestPath(path,args[6]);

                    System.out.println("已将结果写入routine. txt文件");
                }else{
                    System.out.println("验证参数格式！");
                }
                break;
            default:
            	throw new Exception("参数输入错误");
        }

	}
```
## 4.程序演示
1..利用 ```-map```参数来获得对应的自定义地铁文件（命名为 subway.txt）
```
java subway -map subway.txt
```
运行结果：

2.支持一个新的命令行参数 ```-a```进行地铁线路查询，并通过命令参数```-o```进行输出
```
java subway -a 2号线 -map subway.txt -o station.txt
```
运行结果：
3.利用参数```-b```查询两个站点之间最短（经过的站点数最少）路线，并输出
```
subway.exe -b 动物园 和平里北街 -map subway.txt -o routine.txt
```
3.1 两个站点均为普通站点
运行结果：

3.2 起点为转站站点，终点为普通站点
运行结果：

3.3 起点为普通站点，终点为转站站点
运行结果：

4.健壮性演示
参数错误：
站点不存在：
线路不存在：
输入相同站点：

## 5.总结
通过这次大作业，我明白了一个项目不仅仅是只有编写程序这么简单，重要的是开始的规划，所需知识的学习，实践代码，完善代码，查漏补缺，对项目进行各种各样的测试，这整个过程都是十分重要的。不仅如此，通过这次大作业，我也得以复习了大二数据结构课程所学习的图算法，对邻接表这一数据结构也有了更深的认识，对广度优先搜索算法也变得更加熟练，同时，由于本次大作业是使用Java作为主要语言，因此也锻炼了Java编程能力，总之受益匪浅。但这次实验中依然存在不足，一些代码过分冗余、复杂，一些算法也不太熟练。希望通过这次实验能弥补自身的不足，争取在期末大作业中更进一步。
