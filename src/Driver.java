
import java.util.concurrent.*;

public class Driver {

	private static int NUM_THREADS = 1;
	
	
	private static int NUM_VEHICLES = 20;
	public static void main(String[] args) {
		final CityMap cityMap = new CityMap("./map.txt");
		for(int i=0;i<NUM_VEHICLES;i++){
			cityMap.addVehicle();
		}
		ControlLogic brain=new ControlLogic();
		//<threadpool>
		ExecutorService  e= Executors.newFixedThreadPool(NUM_THREADS);
		//</threadpool>
		Runnable start=new ControlerThread(cityMap, null, e, brain);
		e.execute(start);
	}

}
