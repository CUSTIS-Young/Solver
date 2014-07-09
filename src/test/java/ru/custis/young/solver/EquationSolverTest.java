package ru.custis.young.solver;

import atunit.AtUnit;
import atunit.Unit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Тесты на {@link EquationSolver}.
 */
@RunWith(AtUnit.class)
public class EquationSolverTest {
    @Unit
    private final EquationSolver equationSolver = new EquationSolver1();

    @Test
    public void solveTest() {
        List<int[]> factorsList = new ArrayList<int[]>();
        factorsList.add(new int[]{3});
        factorsList.add(new int[]{2, 3});
        factorsList.add(new int[]{2, 3, 2});
        factorsList.add(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 12, 17});
        factorsList.add(new int[]{13, 11, 17, 19});
        factorsList.add(new int[]{5, 7});
        factorsList.add(new int[]{3, 6, 9, 12, 15});
        factorsList.add(new int[]{21, 22, 23, 24, 25});
        factorsList.add(new int[]{2, 4, 6});
        factorsList.add(new int[]{2, 4, 6, 8, 10, 12, 14, 16, 18});

        long maxDelta = 0;

        for (int i = 0; i < 100; i++) {
            for (int[] factors : factorsList) {
                long startTime = System.currentTimeMillis();
                try {
                    try {
                        equationSolver.solve(i, factors);
                    } catch (IllegalArgumentException e) {
                        System.out.println(i);
                        for (int f : factors) {
                            System.out.print(f + " ");
                        }
                        System.out.println();
                    }
                } catch (NoSolutionException ignore) {

                }
                long endTime = System.currentTimeMillis();

                maxDelta = Math.max(maxDelta, endTime - startTime);
            }
        }

        System.out.println("MaxDelta is " + maxDelta + " ms.");
    }

    @Test
    public void wrongOrderTest() throws NoSolutionException {
        // уравнение 91x + y = 1, очевидно, имеет решение x = 0, y = 1.
        equationSolver.solve(1, new int [] {91, 1});
    }
}
