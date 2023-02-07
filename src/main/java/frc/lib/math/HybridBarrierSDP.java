package frc.lib.math;

import org.ejml.data.DMatrix1Row;
import org.ejml.data.DMatrixD1;
import org.ejml.data.DMatrixRMaj;

import static org.ejml.dense.row.CommonOps_DDRM.*;
import static org.ejml.dense.row.SpecializedOps_DDRM.*;
import static org.ejml.dense.row.mult.MatrixVectorMult_DDRM.*;
import static org.ejml.dense.row.mult.SubmatrixOps_DDRM.*;
import static org.ejml.UtilEjml.*;

/**
 * Hybrid Barrier SDP solver.
 * 
 * Solve problems of the type $$\begin{aligned} \min_{X} \quad &amp; \langle C,
 * X \rangle \\
 * \textrm{s.t.} \quad &amp; \langle A_i, X \rangle = b_i \quad \forall \ i \in
 * 1..m \\ &amp; X
 * \succeq 0 \end{aligned}$$
 * 
 * Algorithm is found on page 46 of <a href="https://arxiv.org/abs/2101.08208">Solving SDP Faster: A Robust IPM Framework and Efficient Implementation</a>
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
    public DMatrixRMaj A_;

    /** \( T \): Maximum number of iterations */
    public int T = 100;

    /** \( \epsilon_N \): TODO */
    public double epsilonN = 0.2;

    /** \( \epsilon \): TODO */
    public double epsilon = 0.2;

    /**
     * \( \textrm{HybridBarrier}(m, n, C, \{ A_i \}_{i=1}^m, b \in \mathbb{R}^m) \)
     */
    public void hybridBarrier(DMatrixRMaj C, DMatrixRMaj A[], DMatrixD1 b) { // 10
        int m = A.length;
        int n = C.numCols;
        initialize(m, n, C, A, b); // 11
        DMatrixD1 g = new DMatrixRMaj(m, 1),
                delta_y = new DMatrixRMaj(m, 1);
        DMatrixRMaj tmp1 = new DMatrixRMaj(n, n), tmp2 = new DMatrixRMaj(n, n), tmp3 = new DMatrixRMaj(n, n),
                tmp4 = new DMatrixRMaj(n, n),
                tmp5 = new DMatrixRMaj(n, n),
                tmp6 = new DMatrixRMaj(n, n),
                tmp7 = new DMatrixRMaj(n, n), V1 = new DMatrixRMaj(n, n), V2 = new DMatrixRMaj(n, n),
                V3 = new DMatrixRMaj(n, n), V4 = new DMatrixRMaj(n, n);
        double etaMul = (1.0 + epsilonN / (20.0 * Math.sqrt(Math.sqrt(m * n))));
        for (int i = 0; i < T; i++) { // 12
            eta = eta * etaMul; // 13
            hybridGradient(m, n, g, C, A, b); // 14
            mult(G, g, delta_y);
            changeSign(delta_y); // 15
            addEquals(y, delta_y); // 16
            S.zero();
            for (int j = 0; j < m; j++) {
                scale(y.unsafe_get(j, 0), A[j], tmp1);
                subtractEquals(tmp1, C);
                addEquals(S, tmp1);
            } // 17
            lowRankSlackUpdate(V1, V2); // 18
            multTransB(V1, V2, tmp1);
            addEquals(Stilde, tmp1); // 19
            invert(Stilde, tmp1);
            mult(tmp1, V1, tmp2);
            multTransA(V2, tmp2, tmp3);
            addIdentity(tmp3, tmp2, 1.0);
            invert(tmp2, tmp3);
            mult(tmp1, V1, tmp2);
            mult(tmp2, tmp3, V3);
            changeSign(V3); // 20
            mult(tmp1, V2, V4); // 21
            hybridHessian(m, n, A, V3, V4); // 23
        }
    }

    /**
     * \( \textrm{LowRankSlackUpdate}(Snew, S) \)
     * 
     * Unlike the rest of this solver, it is found in Algorithm 4 on page 21
     */
    public void lowRankSlackUpdate(DMatrixRMaj V1, DMatrixRMaj V2) {

    }

    /**
     * \( \textrm{Initialize}(m, n, C, \{ A_i \}_{i=1}^m, \textrm{A} \in
     * \mathbb{R}^{m \times n^2},
     * b \in \mathbb{R}^m) \)
     */
    public void initialize(int m, int n, DMatrixRMaj C, DMatrixRMaj A[], DMatrixD1 b) {
        DMatrixRMaj yiAi = new DMatrixRMaj(n, n),
                    yiAiC = new DMatrixRMaj(n, n),
                    Sinv = new DMatrixRMaj(n, n),
                    SinvxSinv = new DMatrixRMaj(n, n),
                    SinvxSinvA = new DMatrixRMaj(n, n),
                    R = new DMatrixRMaj(n, n),
                    O = new DMatrixRMaj(n, n),
                    AjSinv = new DMatrixRMaj(n, n),
                    SinvAi = new DMatrixRMaj(n, n),
                    SinvAiSinv = new DMatrixRMaj(n, n),
                    SinvAjSinv = new DMatrixRMaj(n, n),
                    SinvAiSinvAjSinv = new DMatrixRMaj(n, n),
                    SinvAiSinvAjSinvxSinv = new DMatrixRMaj(n, n),
                    Hinv = new DMatrixRMaj(n, n),
                    HinvA = new DMatrixRMaj(n, n),
                    HinvASinvAiSinvAjSinvxSinv = new DMatrixRMaj(n, n),
                    HinvASinvAiSinvAjSinvxSinvA = new DMatrixRMaj(n, n),
                    SinvAiSinvxSinvAjSinv = new DMatrixRMaj(n, n),
                    SinvAiSinvxSinvAjSinvA = new DMatrixRMaj(n, n),
                    HinvASinvAiSinvxSinvAjSinvA = new DMatrixRMaj(n, n),
                    SinvAiSinvxSinv = new DMatrixRMaj(n, n),
                    SinvAiSinvxSinvA = new DMatrixRMaj(n, n),
                    HinvASinvAiSinvxSinvA = new DMatrixRMaj(n, n),
                    SinvAjSinvxSinv = new DMatrixRMaj(n, n),
                    SinvAjSinvxSinvA = new DMatrixRMaj(n, n),
                    HinvASinvAjSinvxSinvA = new DMatrixRMaj(n, n),
                    HinvASinvAiSinvxSinvAHinvASinvAjSinvxSinvA = new DMatrixRMaj(n, n),
                    m1n1H = new DMatrixRMaj(n, n),
                    O2 = new DMatrixRMaj(n, n),
                    Q2 = new DMatrixRMaj(n, n),
                    O2m1n1H = new DMatrixRMaj(n, n),
                    RO2m1n1H = new DMatrixRMaj(n, n),
                    Q2RO2m1n1H = new DMatrixRMaj(n, n)
                    ; // TODO cut this down and make it inputs
        vectorizeStack(A, A_); // 31
        eta = 1.0 / (Math.sqrt(m * n) + 2.0);
        T = (int) Math.ceil(40.0 / epsilonN * Math.sqrt(Math.sqrt(m * n)) * Math.log(m * n / epsilon)); // 32
        findInitialFeasibleDualVector(); // 33
        S.zero();
        for(int i = 0; i < m; i++) {
            scale(y.unsafe_get(i, 0), A[i], yiAi);
            subtract(yiAi, C, yiAiC);
            addEquals(S, yiAiC);
        }
        Stilde.setTo(S); // 34
        invert(S, Sinv);
        kron(Sinv, Sinv, SinvxSinv);
        multTransB(SinvxSinv, A_, SinvxSinvA);
        mult(A_, SinvxSinvA, H); // 35
        invert(H, Hinv);
        mult(Hinv, A_, HinvA);
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < m; j++) {
                mult(A[j], Sinv, AjSinv);
                mult(Sinv, A[i], SinvAi);
                mult(SinvAi, Sinv, SinvAiSinv);
                mult(SinvAiSinv, AjSinv, SinvAiSinvAjSinv);
                kron(SinvAiSinvAjSinv, Sinv, SinvAiSinvAjSinvxSinv);
                mult(HinvA, SinvAiSinvAjSinvxSinv, HinvASinvAiSinvAjSinvxSinv);
                multTransB(HinvASinvAiSinvAjSinvxSinv, A_, HinvASinvAiSinvAjSinvxSinvA);
                Q.unsafe_set(i, j, trace(HinvASinvAiSinvAjSinvxSinvA)); // 37
                mult(Sinv, AjSinv, SinvAjSinv);
                kron(SinvAiSinv, SinvAjSinv, SinvAiSinvxSinvAjSinv);
                multTransB(SinvAiSinvxSinvAjSinv, A_, SinvAiSinvxSinvAjSinvA);
                mult(HinvA, SinvAiSinvxSinvAjSinvA, HinvASinvAiSinvxSinvAjSinvA);
                R.unsafe_set(i, j, trace(SinvAiSinvxSinvAjSinvA)); // 38
                mult(SinvAiSinv, Sinv, SinvAiSinvxSinv);
                mult(SinvAjSinv, Sinv, SinvAjSinvxSinv);
                multTransB(SinvAiSinvxSinv, A_, SinvAiSinvxSinvA);
                multTransB(SinvAjSinvxSinv, A_, SinvAjSinvxSinvA);
                mult(HinvA, SinvAiSinvxSinvA, HinvASinvAiSinvxSinvA);
                mult(HinvA, SinvAjSinvxSinvA, HinvASinvAjSinvxSinvA);
                mult(HinvASinvAiSinvxSinvA, HinvASinvAjSinvxSinvA, HinvASinvAiSinvxSinvAHinvASinvAjSinvxSinvA);
                O.unsafe_set(i, j, trace(HinvASinvAiSinvxSinvAHinvASinvAjSinvxSinvA)); // 39
            }
        }
        scale((m - 1.0) / (n - 1.0), H, m1n1H);
        scale(-2.0, O, O2);
        scale(2.0, Q, Q2);
        add(O2, m1n1H, O2m1n1H);
        add(R, O2m1n1H, RO2m1n1H);
        add(Q2, RO2m1n1H, Q2RO2m1n1H);
        scale(255.0 * Math.sqrt((double) n / (double) m), Q2RO2m1n1H);
        invert(Q2RO2m1n1H, G); // 41
    }

    public void findInitialFeasibleDualVector() {

    }

    private void vectorizeStack(DMatrixRMaj A[], DMatrixRMaj A_) {
        A_ = reshapeOrDeclare(A_, A.length, A[0].getNumElements());
        for(int n = 0; n < A.length; n++) {
            int N = A[n].numRows;
            for(int i = 0; i < A[n].numRows; i++) {
                for(int j = 0; j < A[n].numCols; j++) {
                    A_.unsafe_set(n, j * N + i, A[n].unsafe_get(i, j));
                }
            }
        }
    }

    /**
     * \( \textrm{HybridGradient}(m, n, C, \{ A_i \}_{i=1}^m, b \in \mathbb{R}^m) \)
     */
    public void hybridGradient(int m, int n, DMatrixD1 g, DMatrixRMaj C, DMatrixRMaj A[], DMatrixD1 b) {
        DMatrixRMaj Sinv = new DMatrixRMaj(n, n),
                    HSinv = new DMatrixRMaj(n, n),
                    SinvAi = new DMatrixRMaj(n, n),
                    SinvAiSinv = new DMatrixRMaj(n, n),
                    SinvAiSinvxSinv = new DMatrixRMaj(n, n),
                    ASinvAiSinvxSinv = new DMatrixRMaj(n, n),
                    ASinvAiSinvxSinvA = new DMatrixRMaj(n, n),
                    HSinvASinvAiSinvxSinvA = new DMatrixRMaj(n, n),
                    nablaPhiLogy = new DMatrixRMaj(m, 1),
                    nablaPhiVoly = new DMatrixRMaj(m, 1),
                    m1n1nablaPhiLogy = new DMatrixRMaj(m, 1),
                    nablaPhiVolym1n1nablaPhiLogy = new DMatrixRMaj(m, 1),
                    etaB = new DMatrixRMaj(m, 1); // TODO cut this down and make it inputs
        invert(S, Sinv);
        mult(H, Sinv, HSinv);
        for(int i = 0; i < m; i++) {
            mult(Sinv, A[i], SinvAi);
            nablaPhiLogy.set(i, 0, -trace(SinvAi)); // 3
            mult(SinvAi, Sinv, SinvAiSinv);
            kron(SinvAiSinv, Sinv, SinvAiSinvxSinv);
            mult(A_, SinvAiSinvxSinv, ASinvAiSinvxSinv);
            multTransB(ASinvAiSinvxSinv, A_, ASinvAiSinvxSinvA);
            mult(HSinv, ASinvAiSinvxSinvA, HSinvASinvAiSinvxSinvA);
            nablaPhiVoly.unsafe_set(i, 0, -trace(HSinvASinvAiSinvxSinvA)); // 4
        }
        scale((m - 1.0) / (n - 1.0), nablaPhiLogy, m1n1nablaPhiLogy);
        add(nablaPhiVoly, m1n1nablaPhiLogy, nablaPhiVolym1n1nablaPhiLogy);
        scale(255.0 * Math.sqrt((double) n / (double) m), nablaPhiVolym1n1nablaPhiLogy);
        scale(eta, b, etaB);
        subtract(etaB, nablaPhiVolym1n1nablaPhiLogy, g); // 6
    }

    /**
     * \( \textrm{HybridHessian}(V_3, V_4) \)
     */
    public void hybridHessian(int m, int n, DMatrixRMaj A[], DMatrixRMaj V3, DMatrixRMaj V4) {
        DMatrixRMaj Sinv = new DMatrixRMaj(n, n),
                V3V4 = new DMatrixRMaj(n, n),
                AiV3V4 = new DMatrixRMaj(n, n),
                V3V4Ai = new DMatrixRMaj(n, n),
                AiV3V4Aj = new DMatrixRMaj(n, n),
                AjV3V4Ai = new DMatrixRMaj(n, n),
                SinvAiV3V4Aj = new DMatrixRMaj(n, n),
                SinvAjV3V4Ai = new DMatrixRMaj(n, n),
                V3V4AiV3V4Aj = new DMatrixRMaj(n, n),
                Hinv = new DMatrixRMaj(n, n),
                SinvAiV3V4AjSinv = new DMatrixRMaj(n, n),
                SinvAiV3V4AjSinvxSinv = new DMatrixRMaj(n, n),
                ASinvAiV3V4AjSinvxSinv = new DMatrixRMaj(n, n),
                ASinvAiV3V4AjSinvxSinvA = new DMatrixRMaj(n, n),
                HinvASinvAiV3V4AjSinvxSinvA = new DMatrixRMaj(n, n),
                m1n1H = new DMatrixRMaj(m, m),
                Q3 = new DMatrixRMaj(m, m),
                Q3m1n1H = new DMatrixRMaj(m, m); // TODO cut this down and make it inputs
        invert(S, Sinv);
        invert(H, Hinv);
        multTransB(V3, V4, V3V4);
        double tmp1 = 0;
        for (int i = 0; i < m; i++) {
            mult(A[i], V3V4, AiV3V4);
            mult(V3V4, A[i], V3V4Ai);
            for (int j = 0; j < m; j++) {
                mult(AiV3V4, A[j], AiV3V4Aj);
                mult(Sinv, AiV3V4Aj, SinvAiV3V4Aj);
                tmp1 = trace(SinvAiV3V4Aj);
                mult(V3V4, AiV3V4Aj, V3V4AiV3V4Aj);
                tmp1 += trace(V3V4AiV3V4Aj);
                mult(A[j], V3V4Ai, AjV3V4Ai);
                mult(Sinv, AjV3V4Ai, SinvAjV3V4Ai);
                tmp1 += trace(SinvAjV3V4Ai);
                H.unsafe_set(i, j, H.unsafe_get(i, j) + tmp1); // 12
                mult(SinvAiV3V4Aj, Sinv, SinvAiV3V4AjSinv);
                kron(SinvAiV3V4AjSinv, Sinv, SinvAiV3V4AjSinvxSinv);
                mult(A_, SinvAiV3V4AjSinvxSinv, ASinvAiV3V4AjSinvxSinv);
                multTransB(ASinvAiV3V4AjSinvxSinv, A_, ASinvAiV3V4AjSinvxSinvA);
                mult(Hinv, ASinvAiV3V4AjSinvxSinvA, HinvASinvAiV3V4AjSinvxSinvA);
                Q.unsafe_set(i, j, Q.unsafe_get(i, j) + trace(HinvASinvAiV3V4AjSinvxSinvA)); // 13
            }
        }
        scale(3.0, Q, Q3);
        scale((m - 1.0) / (n - 1.0), H, m1n1H);
        add(Q3, m1n1H, Q3m1n1H);
        scale(225.0 * 1.001 * Math.sqrt((double) n/(double) m), Q3m1n1H);
        invert(Q3m1n1H, G); // 15
    }
}
