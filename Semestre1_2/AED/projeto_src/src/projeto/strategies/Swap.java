package projeto.strategies;

import projeto.draw.State;

public class Swap implements Strategy {

	@Override
	public void execute(State state) {
	  double a = state.pop();
	  double b = state.pop();
      state.push( a );
      state.push( b );	
    }

}
