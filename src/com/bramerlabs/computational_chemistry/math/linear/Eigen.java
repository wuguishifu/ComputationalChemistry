package com.bramerlabs.computational_chemistry.math.linear;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Eigen {

    public static RealMatrix[] eig(double[][] M) {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(MatrixUtils.createRealMatrix(M));
        return new RealMatrix[]{eigenDecomposition.getV(), eigenDecomposition.getD()};
    }

    public static RealMatrix[] eig(RealMatrix M) {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(M);
        return new RealMatrix[]{eigenDecomposition.getV(), eigenDecomposition.getD()};
    }

}
