package grow.streams;

import java.util.Arrays;
import java.util.stream.Stream;

public class OperateWithIntegers {

    /*
     * Some additional operations with numbers.
     */
    public static void main(String[] args) {
        int[] numbers = new int[] { 3, 11, 90, 76, 9, 123, 67, 56 };

        System.out.println("Sum of all: " + Arrays.stream(numbers).sum());
        System.out.println("Average value: " + Arrays.stream(numbers).average().getAsDouble());
        System.out.print("Infitity stream: ");
        Stream.iterate(1, i -> i*2).limit(12).forEach(i -> System.out.print(i + " "));
        System.out.println();
        System.out.print("Fibonacci: ");
        Stream.iterate(new int[] { 0, 1 }, i -> new int[] { i[1], i[0] + i[1] }).limit(10)
              .forEach(i -> System.out.print("(" + i[0] + "," + i[1] +")"));
        System.out.println();
        System.out.print("Generate values: ");
        Stream.generate(Math::random).limit(5).forEach(i -> System.out.print(i + " "));
        System.out.println();
    }

}
