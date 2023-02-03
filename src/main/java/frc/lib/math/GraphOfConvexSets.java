package frc.lib.math;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GraphOfConvexSets {

    private List<Edge> m_edges;
    private List<int[]> m_sets;

    private List<Integer>[] m_neighbors;

    /**
     * c
     * 
     * @param edges
     */
    public void addConvexSet(Edge... edges) {
        int idxs[] = new int[edges.length];
        outer: for (int i = 0; i < edges.length; i++) {
            var edge = edges[i];
            for (int j = 0; j < m_edges.size(); j++) {
                if (m_edges.get(j).equals(edge)) {
                    idxs[i] = j;
                    continue outer;
                }
            }
            idxs[i] = m_edges.size();
            m_edges.add(edge);
        }

        m_sets.add(idxs);
    }

    public void calculateNeighbors() {
        m_neighbors = new ArrayList[m_sets.size()];

        for (int i = 0; i < m_sets.size(); i++) {
            for (int j = i + 1; j < m_sets.size(); j++) {
                outer: for (int k = 0; k < m_sets.get(i).length; k++) {
                    for (int h = 0; h < m_sets.get(j).length; h++) {
                        if (m_sets.get(i)[k] == m_sets.get(j)[h]) {
                            m_neighbors[i].add(j);
                            m_neighbors[j].add(i);
                            break outer;
                        }
                    }
                }
            }
        }
    }

    private static class Node {
        public int idx;
        public int cost;
        public int history[];

        public Node(int[] history, int idx, int cost) {
            this.history = history;
            this.idx = idx;
            this.cost = cost;
        }

        public int[] getHistory() {
            int newHistory[] = new int[history.length + 1];
            for (int i = 0; i < history.length; i++)
                newHistory[i] = history[i];
            newHistory[history.length] = idx;
            return newHistory;
        }
    }

    public int[] getShortestPath(int from, int to) {
        PriorityQueue<Node> q = new PriorityQueue<>();

        q.add(new Node(new int[] {}, from, 0));

        while (!q.isEmpty()) {
            var u = q.remove();

            if (u.idx == to) {
                return u.getHistory();
            }

            for (int i = 0; i < m_neighbors[u.idx].size(); i++) {
                q.add(new Node(u.getHistory(), m_neighbors[u.idx].get(i), u.cost + 1));
            }
        }
        return new int[] {};
    }

    /**
     * 
     * @param set1
     * @param set2
     * @return if sets dont share an edge, then it will return -1
     */
    public int getSharedEdge(int set1, int set2) {
        for (int k = 0; k < m_sets.get(set1).length; k++) {
            for (int h = 0; h < m_sets.get(set2).length; h++) {
                if (m_sets.get(set1)[k] == m_sets.get(set2)[h]) {
                    return m_sets.get(set1)[k];
                }
            }
        }
        return -1;
    }

    public static class ConvexCorridor {
        private Edge[] boundaries;
        private Edge entrance;
        private Edge exit;


        public ConvexCorridor(Edge[] boundaries, Edge entrance, Edge exit) {
            this.boundaries = boundaries;
            this.entrance = entrance;
            this.exit = exit;
        }

    }

    public Edge[] edgesExcept(int set, Edge[] ignore) {
        return (Edge[]) IntStream.of(m_sets.get(set))
            .filter((x) -> Stream.of(ignore).anyMatch(m_edges.get(x)::equals))
            .mapToObj((x) -> m_edges.get(x)).toArray();
    }

    public ConvexCorridor[] identifyingEntrancesandExists(int from, int to) {
        int[] sets = getShortestPath(from, to);
        ConvexCorridor[] toReturn = new ConvexCorridor[sets.length];
        Edge prev = null;
        for (int i = 0; i < sets.length - 1; i++) {
            Edge next = m_edges.get(getSharedEdge(sets[i], sets[i + 1]));
            toReturn[i] =
                new ConvexCorridor(edgesExcept(sets[i], new Edge[] {prev, next}), prev, next);
            prev = next;
        }
        toReturn[sets.length - 1] =
            new ConvexCorridor(edgesExcept(sets[sets.length - 1], new Edge[] {prev}), prev, null);

        return toReturn;
    }

}
