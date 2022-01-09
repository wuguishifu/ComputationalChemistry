package com.bramerlabs.computational_chemistry.utils;

import com.bramerlabs.computational_chemistry.graphing.Graph;
import com.bramerlabs.computational_chemistry.graphing.GraphSeries;
import com.bramerlabs.computational_chemistry.math.comparators.ArrayIndexComparator;
import com.bramerlabs.computational_chemistry.math.linear.Eigen;
import com.bramerlabs.computational_chemistry.math.vector.Vector2f;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;
import static org.apache.commons.math3.special.Erf.erf;

public class HHGroundStateHF {

    public static void main(String[] args) {

        int asize;
        double Z = 1, Elast, as, ap, rat, rp, F0, as1, ap1, as2, ap2, rq, r, NNr, nor, J, Qs, rmin = 0;
        double[] a, a1, ra, rplot, groundWaveFunction, waveFunctionMin = new double[500], Eg, Eg_sorted;
        double[][] T = new double[8][8], S = new double[8][8], A = new double[8][8], Ci, S_D, F = new double[8][8];
        double[][][][] G = new double[8][8][8][8];
        RealMatrix V_re, D_re, Vecp, Vec, Val, C, P, Fp;

        ArrayList<Double> Erec = new ArrayList<>();
        ArrayList<Double> Erecmin = new ArrayList<>();

        double pi = Math.PI, e = Math.E;

        rplot = new double[(int) ((3 - (-2)) / 0.01)]; // range in A.U. for plotting probability density
        for (int i = 0; i < rplot.length; i++) {
            rplot[i] = 0.01 * i - 2;
        }

        Eg = new double[42];
        Eg_sorted = new double[42];

        for (int y = 1; y <= 41; y++) {
            r = 0.45 + 0.05 * y; // distance between nuclei in A.U. (bohr radius)
            NNr = 1 / r;

            // alphas for the gaussian basis functions for each H atom
            a1 = new double[]{13.00773, 1.962079, 0.444529, 0.1219492}; // hydrogen
//            a1 = new double[]{0.297194, 1.236745, 5.749982, 38.216677}; // helium
            a = new double[8];
            for (int i = 0; i < 4; i++) {
                a[i] = a[i + 4] = a1[i];
            }
            asize = a.length;

            // one H nucleus origin, one at r
            ra = new double[]{0, 0, 0, 0, r, r, r, r};

            // run over all basis functions to calculate the overlap, kinetic energy, and coulomb matrices
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    as = a[i] + a[j];
                    ap = a[i] * a[j];
                    rat = ap / as;

                    // location of resulting gaussian
                    double a2 = -rat * (ra[i] - ra[j]) * (ra[i] - ra[j]);
                    rp = (a[i] * ra[i] + a[j] * ra[j]) / as;
                    S[i][j] = pow(pi / as, 1.5) * exp(a2);
                    S[j][i] = S[i][j]; // symmetry
                    T[i][j] = 0.5 * rat * (6 - 4 * rat * (ra[i] - ra[j]) * (ra[i] - ra[j])) * S[i][j];
                    T[j][i] = T[i][j]; // symmetry

                    double v = sqrt(as * (rp - r) * (rp - r));
                    if (rp == 0) {
                        F0 = 1 + sqrt(pi) / 2 * erf(v) / v;
                    } else if (rp - r < 0.00001f) { // use a error range because of rounding error
                        double u = sqrt(as * rp * rp);
                        F0 = sqrt(pi) / 2 * erf(u) / u + 1;
                    } else {
                        double u = sqrt(as * rp * rp);
                        F0 = sqrt(pi) / 2 * (erf(u) / u + erf(v) / v);
                    }

                    A[i][j] = -2 * pi * Z / as * exp(a2) * F0;
                }
            }

            RealMatrix[] eigS = Eigen.eig(S);
            V_re = eigS[0];
            D_re = eigS[1];

            // reconditioning the V and D matrices
            RealMatrix V_temp = MatrixUtils.createRealMatrix(new double[8][8]);
            RealMatrix D_temp = MatrixUtils.createRealMatrix(new double[8][8]);
            V_re = V_re.scalarMultiply(-1);
            for (int i = 0; i < 8; i++) {
                V_temp.setColumn(7 - i, V_re.getColumn(i));
            }
            for (int i = 0; i < 8; i++) {
                D_temp.setEntry(i, i, D_re.getEntry(7 - i, 7 - i));
            }
            V_re = V_temp.copy();
            D_re = D_temp.copy();

            RealMatrix V_array = MatrixUtils.createRealMatrix(new double[8][8]);
            for (int i = 0; i < asize; i++) {
                double[] V_col = V_re.getColumn(i);
                double Dii = sqrt(D_re.getEntry(i, i));
                for (int j = 0; j < V_col.length; j++) {
                    V_col[j] /= Dii;
                }
                V_array.setColumn(i, V_col);
            }
            V_re = V_array.copy();

            // iterate over each gaussian basis function
            // this is the naive approach, does not exploit symmetry of the system
            // for future optimization, symmetry of the system should be used, i.e. g(i, j, k, l) = g(l, k, j, i) etc.
            for (int i = 0; i < asize; i++) {
                for (int j = 0; j < asize; j++) {
                    as1 = a[i] + a[j];
                    ap1 = a[i] * a[j];
                    rp = (a[i] * ra[i] + a[j] * ra[j]) / as1;

                    for (int k = 0; k < asize; k++) {
                        for (int l = 0; l < asize; l++) {
                            as2 = a[k] + a[l];
                            ap2 = a[k] * a[l];
                            rq = (a[k] * ra[k] + a[l] * ra[l]) / as2;

                            if ((rp - rq) < 0.00001f) { // error bias
                                F0 = 1;
                            } else {
                                double v = as1 * as2 * (rp - rq) * (rp - rq) / (as1 + as2);
                                F0 = sqrt(pi) / 2 * erf(sqrt(v)) / sqrt(v);
                            }

                            G[i][j][k][l] = 2 * pow(pi, 2.5) / as1 / as2 / sqrt(as1 + as2)
                                    * exp(-ap1 * (ra[i] - ra[j]) * (ra[i] - ra[j]) / as1 - ap2
                                    * (ra[k] - ra[l]) * (ra[k] - ra[l]) / as2) * F0;
                        }
                    }
                }
            }

            // self-consistent loop
            double[][] C_val = new double[1][asize];
            for (int i = 0; i < asize; i++) {
                C_val[0][i] = 1;
            }
            C = MatrixUtils.createRealMatrix(C_val);
            nor = C.multiply(MatrixUtils.createRealMatrix(S)).multiply(C.transpose()).getEntry(0, 0);
            C = C.scalarMultiply(1 / nor); // initial guess
            Elast = 1;
            P = C.transpose().multiply(C);

            // generating Fock matrix
            double Ecur = 100;
            Erec.clear();
            while ((abs(Ecur - Elast)) > 0.000001f) {
                Elast = Ecur;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        J = 0;
                        for (int k = 1; k < 8; k++) {
                            for (int l = 1; l < 8; l++) {
                                J = J + P.getEntry(k, l) * G[i][j][k][l];
                            }
                        }
                        F[i][j] = T[i][j] + A[i][j] + J;
                    }
                }

                Fp = V_re.transpose().multiply(MatrixUtils.createRealMatrix(F)).multiply(V_re);
                RealMatrix[] eigF = Eigen.eig(Fp);
                Vecp = eigF[0];
                Val = eigF[1];
                Vec = V_re.multiply(Vecp);

                double[] EigVal = new double[8];
                Double[] EigValComp = new Double[8];
                for (int i = 0; i < 8; i++) {
                    EigVal[i] = Val.getEntry(i, i);
                    EigValComp[i] = Val.getEntry(i, i);
                }

                ArrayIndexComparator comparator = new ArrayIndexComparator(EigValComp);
                Integer[] indices = comparator.createIndexArray();
                Arrays.sort(indices, comparator);
                Arrays.sort(EigVal);
                Ecur = EigVal[0];
                Erec.add(EigVal[0]);

                double[] GrCoeff = Vec.getColumn(indices[0]);

                // new C matrix, normalized w.r.t. S
                C = MatrixUtils.createRealMatrix(new double[1][8]);
                C.setRow(0, GrCoeff);

                // new input density matrix
                P = P.scalarMultiply(0.6).add(C.transpose().scalarMultiply(0.2).multiply(C));
            }

            // only want to populate the ground level wave function once for the optimized C
            groundWaveFunction = new double[rplot.length];
            for (int x = 0; x < 500; x++) {
                double sum = 0;
                for (int i = 0; i < 4; i++) {
                    sum += C.getEntry(0, i) * exp(-a[i] * rplot[x] * rplot[x]);
                    sum += C.getEntry(0, i + 4) * exp(-a[i + 4] * (rplot[x] - r) * (rplot[x] - r));
                }
                groundWaveFunction[x] = sum;
            }

            Qs = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            Qs += G[i][j][k][l] * C.getEntry(0, i) * C.getEntry(0, j)
                                    * C.getEntry(0, k) * C.getEntry(0, l);
                        }
                    }
                }
            }
            Eg[y] = 2 * C.multiply(MatrixUtils.createRealMatrix(T).add(MatrixUtils.createRealMatrix(A)))
                    .multiply(C.transpose()).getEntry(0, 0) + Qs + NNr;
            System.arraycopy(Eg, 0, Eg_sorted, 0, Eg.length);
            Arrays.sort(Eg_sorted);
            if (Eg[y] == Eg_sorted[0]) {
                rmin = r;
                Erecmin.clear();
                Erecmin.addAll(Erec);
                System.arraycopy(groundWaveFunction, 0, waveFunctionMin, 0, groundWaveFunction.length);
            }

        }

        ArrayList<Vector2f> fockData = new ArrayList<>();
        for (int i = 0; i < Erecmin.size(); i++) {
            fockData.add(new Vector2f(i, Erecmin.get(i).floatValue()));
        }

        ArrayList<Vector2f> energyData = new ArrayList<>();
        for (int i = 0; i < Eg.length; i++) {
            float x = 0.45f + 0.05f * i;
            energyData.add(new Vector2f(x, (float) Eg[i]));
        }

        ArrayList<Vector2f> densityData = new ArrayList<>();
        for (int i = 0; i < rplot.length; i++) {
            densityData.add(new Vector2f((float) rplot[i], (float) waveFunctionMin[i]));
        }

        // fock energy level convergence
        Graph fock = new Graph();
        fock.title("Fock Energy Level Convergence vs. Step");
        fock.xLabel("Step of Convergence");
        fock.yLabel("Fock Energy Level (a.u.)");
        fock.addSeries(new GraphSeries(fockData, "-b"));
        fock.repaint();

        // total energy
        Graph energy = new Graph();
        energy.title("Total Energy vs. H-H Bond Length");
        energy.xLabel("H-H Bond Length");
        energy.yLabel("Total Energy (a.u.)");
        energy.addSeries(new GraphSeries(energyData, "-m"));
        energy.repaint();

        // electron probability density
        Graph density = new Graph();
        density.title("Probability Density Around H-H Bond");
        density.xLabel("r Position");
        density.yLabel("Probability Density");
        density.addSeries(new GraphSeries(densityData, "-k"));
        density.repaint();

        System.out.print("Minimum H-H Bond Radius: " + rmin);
    }


}

