package edu.ufpr.ge.operators.selection;

//  RankingAndCrowdingSelection.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.EuclideanDistance;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * This class implements a selection for selecting a number of solutions from a solution list. The solutions are taken by mean of its ranking and crowding distance values.
 */
public class RouletteWheelSelection<S extends Solution<?>>
        implements SelectionOperator<List<S>, List<S>> {

    private int solutionsToSelect = 0;
    private JMetalRandom randomGenerator;

    /**
     * Constructor
     */
    public RouletteWheelSelection(int solutionsToSelect) {
        this.solutionsToSelect = solutionsToSelect;
        this.randomGenerator = JMetalRandom.getInstance();
    }

    /* Getter */
    public int getSolutionsToSelect() {
        return solutionsToSelect;
    }

    /**
     * Execute() method
     */
    public List<S> execute(List<S> solutionList) throws JMetalException {
        if (null == solutionList) {
            throw new JMetalException("The solution list is null");
        } else if (solutionList.isEmpty()) {
            throw new JMetalException("The solution list is empty");
        } else if (solutionList.size() < solutionsToSelect) {
            throw new JMetalException("The population size (" + solutionList.size() + ") is smaller than"
                    + "the solutions to selected (" + solutionsToSelect + ")");
        }
        List<Double> euclideanDistances = getEuclideanDistances(solutionList);

        Double sum = euclideanDistances.stream().reduce(Double::sum).get();

        List<S> selectedSolutions = new ArrayList<>();
        do {
            double randomDouble = randomGenerator.nextDouble(0, sum);
            double cumulativeSum = 0.0;
            int index = -1;
            do {
                index++;
                cumulativeSum += euclideanDistances.get(index);
            } while (cumulativeSum < randomDouble);

            if (!selectedSolutions.contains(solutionList.get(index))) {
                selectedSolutions.add(solutionList.get(index));
            }
        } while (selectedSolutions.size() < solutionsToSelect);

        return selectedSolutions;
    }

    private List<Double> getEuclideanDistances(List<S> solutionList) {
        EuclideanDistance euclideanDistance = new EuclideanDistance();

        List<S> normalizedPopulation = (List<S>) SolutionListUtils.getNormalizedFront((List<Solution<?>>) solutionList, getMaximumValues(solutionList), getMinimumValues(solutionList));

        int numberOfObjectives = normalizedPopulation.get(0).getNumberOfObjectives();
        ArrayPoint worstSolution = new ArrayPoint(numberOfObjectives);
        for (int i = 0; i < numberOfObjectives; i++) {
            worstSolution.setDimensionValue(i, 1.01);
        }

        return normalizedPopulation
                .stream()
                .map((solution) -> euclideanDistance.compute(new ArrayPoint(solution), worstSolution))
                .collect(Collectors.toList());
    }

    private List<Double> getMaximumValues(List<S> solutionList) {
        int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();

        List<Double> maximumObjectiveValues = new ArrayList<>();
        for (int i = 0; i < numberOfObjectives; i++) {
            maximumObjectiveValues.add(Double.NEGATIVE_INFINITY);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (S solution : solutionList) {
                if (solution.getObjective(i) > maximumObjectiveValues.get(i)) {
                    maximumObjectiveValues.set(i, solution.getObjective(i));
                }
            }
        }

        return maximumObjectiveValues;
    }

    private List<Double> getMinimumValues(List<S> solutionList) {
        int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();

        List<Double> maximumObjectiveValues = new ArrayList<>();
        for (int i = 0; i < numberOfObjectives; i++) {
            maximumObjectiveValues.add(Double.NEGATIVE_INFINITY);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (S solution : solutionList) {
                if (solution.getObjective(i) > maximumObjectiveValues.get(i)) {
                    maximumObjectiveValues.set(i, solution.getObjective(i));
                }
            }
        }

        return maximumObjectiveValues;
    }

}