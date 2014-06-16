package ru.custis.young.solver;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface EquationSolver {
    @NotNull
    int[] solve(int number, @NotNull int[] factors) throws NoSolutionException;
}
