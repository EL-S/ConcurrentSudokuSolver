import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver implements Runnable {
  
    private int start_x;
    private int start_y;
    private int[][] sudoku;
    
	public Solver(int x, int y, int[][] s){
    	this.start_x=x;
        this.start_y=y;
        // deep copy not required for first task as it's just the base sudoku
        this.sudoku = s;
        System.out.println("Created task at ("+this.start_x+", "+this.start_y+") with no choice");
    }
	
    public Solver(int x, int y, int c, int[][] s){
    	this.start_x=x;
        this.start_y=y;
        // create a deep copy of a nested array
        this.sudoku = Arrays.stream(s).map(int[]::clone).toArray(int[][]::new);
        
        // set the first cell to what was already chosen
        this.sudoku[this.start_y][this.start_x] = c;
        System.out.println("Created task at ("+this.start_x+", "+this.start_y+") choice = "+c);
    }

    // go through the unsolved section and create a new task for each fork but continue with the first task on this thread
    public void run() {
    	System.out.println(Thread.currentThread().getName()+" is running now, start_pos = ("+start_x+", "+start_y+")");
        for (int y = this.start_y; y < this.sudoku.length; y++) {
        	// start_x should be 0 after it starts at the correct spot the first time
			for (int x = this.start_y == y ? this.start_x : 0; x < this.sudoku[y].length; x++) {
				System.out.println(Thread.currentThread().getName()+" currently at ("+x+","+y+")");
            	if (this.sudoku[y][x] == 0)
            	{
            		// get the possible options for the current position if it's empty
            		List<Integer> choices = get_possible_choices(y,x);
        			if (!choices.isEmpty()) {
        				System.out.println(Thread.currentThread().getName()+" continue with "+choices.get(0)+" from "+choices+" for ("+x+","+y+")\n"
            							  +Thread.currentThread().getName()+" fork here with "+choices+" for ("+x+","+y+")");
        				
        				// create tasks for the other options
            			for (int i = 1; i < choices.size(); i++) {
            				Runnable solver = new Solver(x,y,choices.get(i),this.sudoku);
        		        	Pool.pool.execute(solver);
            			}
            			
        				// continue with the first choice here
            			this.sudoku[y][x] = choices.get(0);
            		}
        			else {
        				// no solution with this path, end task and the threadpool will start the LIFO task (back-tracking)
        				System.out.println(Thread.currentThread().getName()+" no solution with this path for ("+x+","+y+")");
            			Thread.currentThread().interrupt();
            			return;
        			}
        			
        			printArray();
                	// delay the next step by 1 second
                	try {
    					Thread.sleep(1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
            	}
            }
         }
        System.out.println("Found a solution at "+System.currentTimeMillis());
        printArray();
    }
    
    // output the sudoku
    public void printArray() {
    	String output = "";
    	output += Thread.currentThread().getName()+" Current output:\n";
        for (int i = 0; i < 9; i++) {  
            for (int j = 0; j < 9; j++) {  
                output += this.sudoku[i][j] + " ";  
            }  
            output += "\n"; 
        }
        output += "\n";
        System.out.print(output);  
    }
    
    // same as solver4
    private boolean check(int row, int line, int number) {  
        //check if there exists the same number at the row/line 
        for (int i = 0; i < 9; i++) {  
            if (this.sudoku[row][i] == number || this.sudoku[i][line] == number) {  
                return false;  
            }  
        }  
        //check if there exists the same number in the corresponding grid  
        int tempRow = row / 3;  
        int tempLine = line / 3;  
        for (int i = 0; i < 3; i++) {  
            for (int j = 0; j < 3; j++) {  
                if (this.sudoku[tempRow * 3 + i][tempLine * 3 + j] == number) {  
                    return false;  
                }  
            }  
        }  
        return true;  
    }
    
    //return possible options at a given co-ord
    private List<Integer> get_possible_choices(int y,int x) {
    	List<Integer> results = new ArrayList<Integer>();
    	for (int i = 1; i <= 9; i++)
    	{
    		if (check(y,x,i))
    		{
    			results.add(i);
    		}
    	}
    	return results;
    }
}