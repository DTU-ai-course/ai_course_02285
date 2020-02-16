package searchclient;

import java.util.Comparator;

public abstract class Heuristic implements Comparator<State> {

    int counter = 0;
    int[] goal_x = new int[100];
    int[] goal_y = new int[100];

    public Heuristic(State initialState) {
        // Here's a chance to pre-process the static parts of the level.
        char[][] goals = initialState.client.goals;

        for (int row = 1; row < goals.length - 1; row++) {
            for (int col = 1; col < goals.length - 1; col++) {
                char g = goals[row][col];
                if (g > 0) {
                    goal_x[counter] = row;
                    goal_y[counter] = col;
                    counter++;
                } 
            }
        }
    }

    // h = max { abs(current_cell.x – goal.x), abs(current_cell.y – goal.y) }

    public int h(State n) {

        int agent_row = n.agentRow;
        int agent_col = n.agentCol;
        
        int minDist = 9999999;

        for (int i = 0; i < 9; i++) {
            int x = Math.abs(agent_row - goal_x[i]);
            int y = Math.abs(agent_col - goal_y[i]);
    
            double value = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    
            int sld = (int) value;

            if (sld < minDist) {
                minDist = sld;
            }

        }

        return minDist;
        //throw new NotImplementedException();
    }

    public abstract int f(State n);

    @Override
    public int compare(State n1, State n2) {
        return this.f(n1) - this.f(n2);
    }

    public static class AStar extends Heuristic {
        public AStar(State initialState) {
            super(initialState);
        }

        @Override
        public int f(State n) {
            // System.err.println("f = " + n.g() + " and " + this.h(n));
            return n.g() + this.h(n);
        }

        @Override
        public String toString() {
            return "A* evaluation";
        }
    }

    public static class WeightedAStar extends Heuristic {
        private int W;

        public WeightedAStar(State initialState, int W) {
            super(initialState);
            this.W = W;
        }

        @Override
        public int f(State n) {
            return n.g() + this.W * this.h(n);
        }

        @Override
        public String toString() {
            return String.format("WA*(%d) evaluation", this.W);
        }
    }

    public static class Greedy extends Heuristic {
        public Greedy(State initialState) {
            super(initialState);
        }

        @Override
        public int f(State n) {
            return this.h(n);
        }

        @Override
        public String toString() {
            return "Greedy evaluation";
        }
    }
}
