package ru.custis.young.solver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquationSolver1 implements EquationSolver {
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
        if (number == 0) {
            return row;
        }
        /// Здесь ошибка. Сначала сравнение number с factors[0], а потом сортировка массива, чтобы на месте
        /// factors[0] оказался наименьший коэффициент. Очевидно, нужно сначала сортировать, а потом использовать
        /// factors[0].
        /// См. тест EquationSolverTest.wrongOrderTest(), который иллюстрирует эту ошибку.
        ///
        /// Кроме того, кажется, что здесь уместнее использовать не сортировку, а просто искать максимальный элемент.
        /// Во-первых, это быстрее (а мы решаем задачу оптимизации). Сложность поиска максимального элемента имеет
        /// сложность O(n), а сложность использованного метода сортировки (см. javadoc) — O(n*log(n)).
        /// Во-вторых, это сделало бы код более читабельным.
        if (number < factors[0]) {
            throw new NoSolutionException();
        }
        Arrays.sort(factors);
        int GCD = gcd(getList(factors));
        if (number % GCD != 0) {
            throw new NoSolutionException();
        } else {
            for (int i = 0; i < factors.length; i++) {
                factors[i] /= GCD;
            }
            number /= GCD;
        }
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
    private int calculateSum(@NotNull int[] summands, @NotNull int[] factors) {
        assert summands.length == factors.length;

        int sum = 0;
        for (int i = 0; i < summands.length; i++) {
            sum += summands[i] * factors[i];
        }
        return sum;
    }

    /**
     * Конвертирует массив в список
     *
     * @param numbers массив
     * @return <code>List<Integer></code>
     */
    @NotNull
    private List<Integer> getList(@NotNull int[] numbers) {
        List<Integer> result = new ArrayList<Integer>(numbers.length);
        for (int number : numbers) {
            result.add(number);
        }
        return result;
    }

    /**
     * НОД 2 чисел
     *
     * @param number1 Число 1
     * @param number2 Число 2
     * @return НОД
     */
    private int gcd(int number1, int number2) {
        if (number2 == 0) {
            return number1;
        }
        return gcd(number2, number1 % number2);
    }

    /**
     * НОД всех чисел списка
     *
     * @param numbers Список чисел
     * @return НОД
     */
    private int gcd(@NotNull List<Integer> numbers) {
        if (numbers.size() == 1) {
            return numbers.get(0);
        }
        if (numbers.size() == 2) {
            return gcd(numbers.get(0), numbers.get(1));
        }
        int removed = numbers.get(0);
        numbers.remove(0);
        return gcd(gcd(numbers), removed);
    }

    /// конвертировать массив в список (метод getList) кажется излишним. Почему бы не реализовать вычисление НОД сразу
    /// для массивов?
    /// (например, так, как сделано в двух методах ниже)

    /**
     * Вычисляет наибольший общий делитель массива чисел
     *
     * @param numbers массив чисел
     * @return наибольший общий делитель
     */
    private int getGreatestCommonDivisor(int[] numbers) {
        int greatestCommonDivisor = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            greatestCommonDivisor = getGreatestCommonDivisor(greatestCommonDivisor, numbers[i]);
        }
        return greatestCommonDivisor;
    }

    /**
     * Вычисляет наибольший общий делитель двух чисел (алгоритм Евклида)
     *
     * @param a первое число
     * @param b второе число
     * @return наибольший общий делитель
     */
    private int getGreatestCommonDivisor(int a, int b) {
        if (b == 0) {
            return a;
        }
        return getGreatestCommonDivisor(b, a % b);
    }
}
