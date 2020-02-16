package searchclient;

import java.util.ArrayDeque;
import java.util.HashSet;

public abstract class Strategy {
    private HashSet<State> explored;
    private final long startTime;

    public Strategy() {
        this.explored = new HashSet<>();
        this.startTime = System.currentTimeMillis();
    }

    public void addToExplored(State n) {
        this.explored.add(n);
    }

    public boolean isExplored(State n) {
        return this.explored.contains(n);
    }

    public int countExplored() {
        return this.explored.size();
    }

    public String searchStatus() {
        return String.format("#Explored: %,6d, #Frontier: %,6d, #Generated: %,6d, Time: %3.2f s \t%s", this.countExplored(), this.countFrontier(), this.countExplored()+this.countFrontier(), this.timeSpent(), Memory.stringRep());
    }

    public float timeSpent() {
        return (System.currentTimeMillis() - this.startTime) / 1000f;
    }

    public abstract State getAndRemoveLeaf();

    public abstract void addToFrontier(State n);

    public abstract boolean inFrontier(State n);

    public abstract int countFrontier();

    public abstract boolean frontierIsEmpty();

    @Override
    public abstract String toString();

    public static class StrategyBFS extends Strategy {
        private ArrayDeque<State> frontier;
        private HashSet<State> frontierSet;

        public StrategyBFS() {
            super();
            frontier = new ArrayDeque<>();
            frontierSet = new HashSet<>();
        }

        @Override
        public State getAndRemoveLeaf() {
            State n = frontier.pollFirst();
            frontierSet.remove(n);
            return n;
        }

        @Override
        public void addToFrontier(State n) {
            frontier.addLast(n);
            frontierSet.add(n);
        }

        @Override
        public int countFrontier() {
            return frontier.size();
        }

        @Override
        public boolean frontierIsEmpty() {
            return frontier.isEmpty();
        }

        @Override
        public boolean inFrontier(State n) {
            return frontierSet.contains(n);
        }

        @Override
        public String toString() {
            return "Breadth-first Search";
        }
    }

    public static class StrategyDFS extends Strategy {
        private ArrayDeque<State> frontier;
        private HashSet<State> frontierSet;

        public StrategyDFS() {
            super();
            frontier = new ArrayDeque<>();
            frontierSet = new HashSet<>();
            //throw new NotImplementedException();
        }

        @Override
        public State getAndRemoveLeaf() {
            State n = frontier.pollFirst();
            frontierSet.remove(n);
            return n;
            //throw new NotImplementedException();
        }

        @Override
        public void addToFrontier(State n) {
            frontier.addFirst(n);
            frontierSet.add(n);
            //throw new NotImplementedException();
        }

        @Override
        public int countFrontier() {
            return frontier.size();
            //throw new NotImplementedException();
        }

        @Override
        public boolean frontierIsEmpty() {
            return frontier.isEmpty();
            //throw new NotImplementedException();
        }

        @Override
        public boolean inFrontier(State n) {
            return frontierSet.contains(n);
            //throw new NotImplementedException();
        }

        @Override
        public String toString() {
            return "Depth-first Search";
        }
    }

    public static class StrategyBestFirst extends Strategy {
        private Heuristic heuristic;
        private ArrayDeque<State> frontier;
        private HashSet<State> frontierSet;

        public StrategyBestFirst(Heuristic h) {
            super();
            this.heuristic = h;
            frontier = new ArrayDeque<>();
            frontierSet = new HashSet<>();
        }

        @Override
        public State getAndRemoveLeaf() {
            State n = frontier.pollFirst();
            frontierSet.remove(n);
            return n;
        }

        @Override
        public void addToFrontier(State n) {
            
            ArrayDeque<State> new_frontier = new ArrayDeque<>();
            HashSet<State> new_frontierSet = new HashSet<>();
            boolean found_place = false;
            
            
            // if (!frontierIsEmpty()) {
            //     for (State n_old : frontier) {
            //         if (!found_place) {
            //             if (heuristic.compare(n, n_old) < 0) {
                            
            //                 // System.err.println("Comparing: " + n.g()+heuristic.f(n) + " to " + n_old.g()+heuristic.f(n_old) + " = " + heuristic.compare(n, n_old));
            //                 new_frontier.add(n);
            //                 new_frontierSet.add(n);
            //                 found_place = true;
            //             }
            //             new_frontier.add(n_old);
            //             new_frontierSet.add(n_old);
            //         }
            //         else {
            //             new_frontier.add(n_old);
            //             new_frontierSet.add(n_old);
            //         }
            //     }
            // }
            // else {
            //     new_frontier.addFirst(n);
            //     new_frontierSet.add(n);
            // }

            // frontier.clear();
            // frontierSet.clear();
            // frontier = new_frontier;
            // frontierSet = new_frontierSet;


            if (!frontierIsEmpty()) {
                if (heuristic.compare(n, frontier.getFirst()) < 0) {
                    frontier.addFirst(n);
                    frontierSet.add(n);
                } else {
                    frontier.addLast(n);
                    frontierSet.add(n);
                }
            } else {
                frontier.addFirst(n);
                frontierSet.add(n);
            }

            // for (State n_frontier : frontier) {
            //     if (heuristic.compare(n, n_frontier) < 0) {
            //         frontier.addFirst(n);
            //         frontierSet.add(n);
            //     }
            // }
            
        }

        @Override
        public int countFrontier() {
            return frontier.size();
        }

        @Override
        public boolean frontierIsEmpty() {
            return frontier.isEmpty();
        }

        @Override
        public boolean inFrontier(State n) {
            return frontierSet.contains(n);
        }

        @Override
        public String toString() {
            return "Best-first Search using " + this.heuristic.toString();
        }
    }
}
