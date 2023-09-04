package projeto.strategies;

import projeto.draw.*;

//class StdDraw @ introcs.cs.princeton.edu/java/stdlib/StdDraw.java

public class Window implements Strategy {

	@Override
	public void execute(State state) {
		int ysize = (int) Math.floor(state.pop());
		int xsize = (int) Math.floor(state.pop());

        StdDraw.clear(StdDraw.WHITE);  
        StdDraw.setPenColor(StdDraw.BLACK);
        
        StdDraw.setCanvasSize(xsize, ysize);
        StdDraw.setXscale(-xsize/2, xsize/2);
        StdDraw.setYscale(-ysize/2, ysize/2);
	}

}
