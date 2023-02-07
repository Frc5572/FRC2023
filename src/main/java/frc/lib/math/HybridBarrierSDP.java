package frc.lib.math;

import org.ejml.data.DMatrix1Row;
import org.ejml.data.DMatrixRMaj;

/**
 * Hybrid Barrier SDP solver.
 * 
 * Solve problems of the type $$\begin{aligned} \min_{X} \quad &amp; \langle C, X \rangle \\
 * \textrm{s.t.} \quad &amp; \langle A_i, X \rangle = b_i \quad \forall \ i \in 1..m \\ &amp; X
 * \succeq 0 \end{aligned}$$
 */
public class HybridBarrierSDP {

    /** \( \in \mathbb{R}^{n \times n} \) */
    public DMatrixRMaj S, Stilde;
    /** \( \in \mathbb{R}^m \) */
    public DMatrix1Row y;
    /** \( \in \mathbb{R}^{m \times m} \) */
    public DMatrixRMaj H, Q, G;
    /** \( \in \mathbb{R} \) */
    public double eta;
    /** \( \in \mathbb{R}^{m \times n^2} \) */
    public DMatrix1Row A;

    /** \( \textrm{HybridBarrier}(m, n, C, \{ A_i \}_{i=1}^m, b \in \mathbb{R}^m) \) */
    public void hybridBarrier() {
        // TODO

    }

    /**
     * \( \textrm{Initialize}(m, n, C, \{ A_i \}_{i=1}^m, \textrm{A} \in \mathbb{R}^{m \times n^2},
     * b \in \mathbb{R}^m) \)
     */
    public void initialize() {
        // TODO
    }

    /**
     * \( \textrm{HybridGradient}(m, n, C, \{ A_i \}_{i=1}^m, b \in \mathbb{R}^m) \)
     */
    public void hybridGradient() {

    }

    /**
     * \( \textrm{HybridHessian}(V_3, V_4) \)
     */
    public void hybridHessian() {

    }
}
