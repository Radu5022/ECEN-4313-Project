import java.io.*;
import java.util.*;


public class CityMap {
	private class Intersection{
		private Road roadlist[];
		private int r=0;
		private String name="";// "42,-13"
		
		private Intersection(String a){
			roadlist= new Road[4];
			name=a;
			intersections[internum]=this;
			internum=internum+1;
			//Do something?
			System.out.print("made "+name+"\n");
		}
		private void addRoad(Intersection end){
			int speedlimit=25;
			int[] coords=new int[4];
			String n= new String(name);
			String[] split= n.split(",");
			coords[0]=Integer.parseInt(split[0]);
			coords[1]=Integer.parseInt(split[1]);
			n=new String(end.name);
			split=n.split(",");
			coords[2]=Integer.parseInt(split[0]);
			coords[3]=Integer.parseInt(split[1]);
			int length=(int) Math.sqrt((coords[0]-coords[2])^2+(coords[1]-coords[3]^2));System.out.print("making road from "+name+" to "+end.name+"\n");
			roadlist[r]=new Road(speedlimit,length,end);
			roads[roadnum]=roadlist[r];
			r=r+1;
			roadnum=roadnum+1;
		}
	}
	public class Road{
		private int speedLimit;
		private int length; 
		private Intersection end;
		
		
		public Road(int speedLimit, int length, Intersection end){
			this.speedLimit = speedLimit;
			this.length = length;
			//this.end = new Intersection();
			this.end = end;
			//System.out.print("made road from "+name+" to "+end.name+"\n");
		}
	}
	public int MAX_INTERSECTIONS=100;
	private Intersection intersections[]=new Intersection[MAX_INTERSECTIONS];
	private int internum=0;
	private Road roads[]=new Road[MAX_INTERSECTIONS*4];
	private int roadnum=0;
	//private Map<(int,int),Intersection> lookup;
	
	public CityMap(String mapFile){
		//Create City map, initialize any variables
		Reader filereader = null;
		try {
			filereader = new FileReader(mapFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader in = new BufferedReader( filereader);
		String line;
		String[] splitLine;
		int[] digits = new int[4];
		try {
			while((line = in.readLine()) != null){
				splitLine = line.split("(\\]\\^\\[)|(, )|(\\[|\\])");	//Line is split so that the four digits are indices 1 - 4
				//System.out.println(splitLine[0]);
				for(int i = 1; i < 5; i++){
					digits[i-1] = Integer.parseInt(splitLine[i]);
				}//Lines split into digits
				
				String name1=Integer.toString(digits[0]).concat(",").concat(Integer.toString(digits[1]));//name of intersections
				Intersection a=findInter(name1);
				String name2=Integer.toString(digits[2]).concat(",").concat(Integer.toString(digits[3]));
				Intersection b=findInter(name2);
				
				if(a==null)a=new Intersection(name1);//allocate new intersections
				if(b==null)b=new Intersection(name2);
				
				a.addRoad(b);//make roads
				b.addRoad(a);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Intersection findInter(String name){
		System.out.print("finding "+name+"\n");
		if(internum==0)return null;
		for(Intersection i:intersections){
			if(i==null)return null;
			if(i.name.equals(name)) return i;
		}
		//shouldnt get here
		return null;
	}

	public void addIntersection(String edge){
		//Use formatting of string to build connections
	}
	
	
	public Intersection getrandom(){
		Random r=new Random();
		return intersections[r.nextInt(intersections.length-1)];
	}
	
	//-----Dijksta's algorithm stuff-----
	class Interwrap{
		ArrayList<Intersection> path;
		final Intersection inter;
		int dist=Integer.MAX_VALUE;
		Interwrap(Intersection i){
			inter=i;
		}
	}
	void initDjk(ArrayList<Interwrap> unchecked, Interwrap current,Intersection src){//initializes dijkstra's algorithm
		for(Intersection inter: this.intersections){
			unchecked.add(new Interwrap(inter));
			if( inter==src){
				int index=unchecked.size()-1;
				unchecked.get(index).dist=0;
				current=unchecked.get(index);
			}
		}
	}
	Interwrap setCurrent(ArrayList<Interwrap> unchecked,Interwrap current){//adds neighbors of the smallest unchecked node
		
		for(Road road:current.inter.roads){//check neighbors of current
			Intersection i=road.end;
			for(Interwrap n:unchecked){
				if (n.inter==i){
					
					neighbor(current,n,road.length);
				}
			}
		}
		
		unchecked.remove(current);
		
		//set current to next closest unchecked
		int index=-1;
		int close=Integer.MAX_VALUE;
		for(Interwrap inter:unchecked){
			if (inter.dist<close){
				close=inter.dist;
				index=unchecked.indexOf(inter);
			}
		}
		current=unchecked.get(index);
		return current;
	}
	void neighbor(Interwrap current,Interwrap n,int length){
		//takes total distance to neighboring node and neighboring node
		if (n.dist>current.dist+length){
			n.dist=current.dist+length;
			n.path= new ArrayList<Intersection>(current.path);
			n.path.add(current.inter);
		}
	}
	
	
	public Intersection[] dijkstra(Intersection src, Intersection dst){
		//one more time
		Interwrap current=null;
		ArrayList<Interwrap> unchecked=new ArrayList<Interwrap>();
		
		//init
		for(Intersection inter: this.intersections){
			unchecked.add(new Interwrap(inter));
			if( inter==src){
				int index=unchecked.size()-1;
				unchecked.get(index).dist=0;
				current=unchecked.get(index);
			}
		}
		//end init
		
		while(current.inter!=dst){
			current=setCurrent(unchecked, current);
		}
		current.path.add(current.inter);
		return current.path.toArray(new Intersection[current.path.size()]);
	}
	
	
	
//	public List<Intersection> dijkstra(Intersection src,Intersection dst){
//		//lets try again
//		class Interwrap{
//			List<Intersection> path;
//			static Intersection inter;
//			int dist=Integer.MAX_VALUE;
//			Interwrap(Intersection i){
//				inter=i;
//			}
//			
//		}
//		Interwrap current;
//		//ArrayList<Intersection> output=new ArrayList<Intersection>;
//		//List<Interwrap> checked=new List<Interwrap>;
//		List<Interwrap> unchecked=new ArrayList<Interwrap>();
//		
//		void init(){//initializes dijkstra's algorithm
//			for(Intersection inter: this.intersections){
//				unchecked.add(new Interwrap(inter));
//				if( inter==src){
//					int index=unchecked.size()-1;
//					unchecked.get(index).dist=0;
//					current=inter;
//				}
//			}
//		}
//		void neighbor(Interwrap current,Interwrap n){
//			//takes total distance to neighboring node and neighboring node
//			if (n.dist>current.dist){
//				n.dist=dist;
//				n.path= new List(current.path);
//				n.path.add(current.inter);
//			}
//		}
//		
//		void setCurrent(){//adds neighbors of the smallest unchecked node
//			
//			for(Road road:current.inter.roads){//check neighbors of current
//				Intersection i;
//				if (road.end==current.inter){
//					i= road.start;
//				}
//				else{
//					i= road.end;
//				}
//				for(Interwrap n:unchecked){
//					if (n.inter==i){
//						neighbor(current,inter);
//					}
//				}
//			}
//			
//			unchecked.remove(current);
//			
//			//set current to next closest unchecked
//			int index=-1;
//			int close=Integer.MAX_VALUE;
//			for(Interwrap inter:unchecked){
//				if (inter.dist<close){
//					close=inter.dist;
//					index=unchecked.indexOf(inter);
//				}
//			}
//			current=unchecked.get(index);
//		}
//		
//		init();
//		while(current.inter!=dst){
//			setCurrent();
//		}
//		current.path.add(current.inter);
//		return current.path;
//		
//	}
		/*public class dnode{
			Intersection i=new Intersection;
			int dist=Integer.MAX_VALUE;
			dnode(Intersection inter){
				this.i=inter;
			}
		}
		int getNearestIndex(Arraylist list,Intersection src){
			int i=0;
			int a=0;
			int dist=Integer.MAX_VALUE;
			for(dnode node:list){
				int b=((src.coords[0]-node.i.coords[0])^2+(src.coords[1]-node.i.coords[1])^2)^(1/2);
				if b<dist{
					dist=b;
					i=a;
				}
				a=a+1;
			}
			return i;
		}
		void neighbors(dnode node){
			for(Road r:node.i.road){
				boolean b=true;
				for(dnode node: checked){
					if(r.end==node.i){
						b=false;
						break;
					}
				}
				if(b){
					if (node.dist+r.length<r.end.dist){
						
					}
				}
			}
		}
		List unchecked=new Arraylist();
		List checked=new Arraylist();
		for (Intersection i:intersections){
			dnode node=node(i);
			if i==src{
				node.dist=0;
			}
			unchecked.add(node);
		}
		while(!unchecked.isEmpty()){
			int index=getNearestIndex(unchecked,src);
			dnode node=unchecked.remove(index);
			checked.add(0,node);
		}
		
	}*/
	
	
	
}
