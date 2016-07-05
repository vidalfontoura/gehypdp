/*
 */
package edu.ufpr.math.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.ufpr.cluster.algorithm.Point;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;

/**
 *
 *
 * @author Vidal
 */
public class MathUtilsTest {

    @Test
    public void testNormalization() throws FileNotFoundException, IOException {

        List<Point> points = DataInstanceReader.readPoints("/points.data", "Double", true);

        List<Point> normalizeData = MathUtils.normalizeData(points);

        for (Point p : normalizeData) {
            System.out.println(p);
        }
    }

}
