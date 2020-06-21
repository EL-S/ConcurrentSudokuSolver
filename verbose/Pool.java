import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Pool {
	static ExecutorService pool = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LifoBlockingDeque<Runnable>());
	
    public static void main(String[] args) {
    	int[][] sudoku = 
			{{0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 0}};
    	
    	//create the first thread at pos (0,0) with the overloaded constructor with no choice
	    System.out.println("Threadpool created.\nStarting.");
	    System.out.println("Started timing at "+System.currentTimeMillis());
	    Runnable solver = new Solver(0,0,sudoku);
	    pool.execute(solver);
    }
}