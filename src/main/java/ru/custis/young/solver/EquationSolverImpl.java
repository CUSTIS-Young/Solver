package ru.custis.young.solver;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class EquationSolverImpl implements EquationSolver {
    /**
     * Ищет целочисленные решения уравнения
     * Σα<sub>i</sub>x<sub>i</sub> = B
     * <p/>
     * Возвращает первое попавшееся решение.
     *
     * @param number  Число B
     * @param factors Коэффициенты линейного уравнения (α<sub>i</sub>)
     * @return Одно из целочисленных решений (массив со значениями x<sub>i</sub>)
     * @throws NoSolutionException если решений не существует.
     */
    @NotNull
    @Override
    public int[] solve(int number, @NotNull int[] factors) throws NoSolutionException {
        assert number >= 0 && number <= 99;
        for (int factor : factors) {
            assert factor >= 1;
        }

        // массив с перебираемыми значениями x<sub>i</sub>.
        int[] row = new int[factors.length];

        // вначале инициализируем его нулями
        Arrays.fill(row, 0);

        // далее в цикле перебираем возможные значения и проверяем, сошлось ли равенство
        do {
            final int sum = calculateSum(row, factors);
            if (sum < number) {
                // ещё можно увеличить перебираемую сейчас переменнную
                row[0]++;
            } else if (sum == number) {
                // Бинго!
                return row;
            } else if (sum > number) {
                // уже больше, чем надо. Сбрасываем до нуля перебираемую сейчас переменную
                // и увеличиваем на 1 следующую. Если и она уже слишком большая, то сбрасываем и её
                // и увеличиваем на 1 следующую. И так далее.
                int i = 0;
                do {
                    row[i] = 0;
                    i++;
                    if (i < row.length) {
                        row[i]++;
                    } else {
                        // уже всё перебрали :(
                        throw new NoSolutionException();
                    }
                } while (row[i] * factors[i] > number);
            }
        } while (true);
    }

    /**
     * Вычисляет сумму &Sigma;&alpha;<sub>i</sub>x<sub>i</sub>.
     *
     * @param summands Массив со значениями переменных x<sub>i</sub>
     * @param factors  Массив со значениями коэффициентов &alpha;<sub>i</sub>
     * @return Сумму &Sigma;&alpha;<sub>i</sub>x<sub>i</sub>.
     */
    private int calculateSum(int[] summands, int[] factors) {
        assert summands.length == factors.length;

        int sum = 0;
        for (int i = 0; i < summands.length; i++) {
            sum += summands[i] * factors[i];
        }
        return sum;
    }
}