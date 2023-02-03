package frc.lib.math;

import edu.wpi.first.math.geometry.Translation2d;

public class Edge {

    private Translation2d a, b;

    public Edge(Translation2d a, Translation2d b) {
        this.a = a;
        this.b = b;
    }

    public Translation2d getA() {
        return a;
    }

    public Translation2d getB() {
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge e = (Edge) obj;
            return (e.a.minus(this.a).getNorm() < 0.001 && e.b.minus(this.b).getNorm() < 0.001)
                || (e.b.minus(this.a).getNorm() < 0.001 && e.a.minus(this.b).getNorm() < 0.001);
        }
        return false;
    }

}
