package grow.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperateWithObjects {

    /*
     * Intermediate operations: map, filter, limit, sorted, distinct, skip, flat map
     * Terminate operations: count, forEach, collect, any match, all match, none match, find any, find first, reduce,
     * max, min
     */
    public static void main(String[] args) {
        List<PostalItem> list = new ArrayList<>();
        list.add(new PostalItem(10.23f, "address 1", "from 1", "to 1", 0));
        list.add(new PostalItem(0.56f, "address 2", "from 2", "to 2", 9));
        list.add(new PostalItem(5.23f, "address 3", "from 3", "to 3", 2));
        list.add(new PostalItem(5.41f, "address 4", "from 4", "to 4", 0));

        /*
         * Filter operation
         * Prints 1 and 4 items from list.
         */
        printStream("filter operation", list.stream().filter(item -> item.getWeight() > 5.3f));

        /*
         * Distinct operation
         */
        List<PostalItem> duplicates = new ArrayList<>(list);
        duplicates.add(list.get(0));
        duplicates.add(list.get(1));
        printStream("distinct operation", duplicates.stream().filter(item -> item.getWeight() > 5.3f)
                                                             .distinct());

        /*
         * Limit operation
         * Prints 1 item from list.
         */
        printStream("limit operation", list.stream().filter(item -> item.getWeight() > 5.3f)
                                                    .limit(1));

        /*
         * Skip operation
         * Prints 4 item from list.
         */
        printStream("skip operation", list.stream().filter(item -> item.getWeight() > 5.3f)
                                                  .skip(1));

        /*
         * Map and sort operation
         * Shows item addresses according their priority
         */
        printStream("map and sort operations", list.stream().sorted((o1, o2) -> o2.getPriority() - o1.getPriority())
                                                            .map(PostalItem::getAddress));

        /*
         * Flat map
         * Shows all addresses chars
         */
        printStream("Flat map", list.stream().map(item -> item.getAddress().split(""))
                                             .flatMap(Arrays::stream)
                                             .distinct());

        /*
         * Any matcher
         * Checks that collection contains element with 9 priority
         */
        printObject("Any matcher", list.stream().anyMatch(item -> item.getPriority() == 9));

        /*
         * All matcher
         * Checks that all collection elements have 9 priority
         */
        printObject("All matcher", list.stream().allMatch(item -> item.getPriority() == 9));

        /*
         * None matcher
         * Checks that there are no elements with 7 priority
         */
        printObject("All matcher", list.stream().noneMatch(item -> item.getPriority() == 7));

        /*
         * Find any
         * Finds any item that priority < 7
         */
        printObject("Find any", list.stream().filter(item -> item.getPriority() < 7).findAny().get());

        /*
         * Find first
         * Finds first item that priority > 4
         */
        printObject("Find first", list.stream().filter(item -> item.getPriority() > 4).findFirst().get());

        /*
         * Reduce operation
         * Find weight of all items
         */
        printObject("Reduce operation", list.stream().map(item -> item.getWeight())
                                                     .reduce((i1, i2) -> i1 + i2).get());

        /*
         * Max and min operations.
         * Shows the most heavy and light items.
         */
        printObject("Max operation", list.stream()
                                            .max((o1, o2) -> Float.floatToIntBits(o1.getWeight() - o2.getWeight())));
        printObject("Min operation", list.stream()
                                            .max((o1, o2) -> Float.floatToIntBits(o2.getWeight() - o1.getWeight())));

        /*
         * Collect operation.
         * Shows all addresses.
         */
        printObject("Collect operation", list.stream().map(item -> item.getAddress()).collect(Collectors.toList()));
    }

    private static void printStream(String operation, Stream<?> stream) {
        System.out.println(String.format("************ %s ****************", operation));
        stream.forEach(item -> System.out.println(item));
        System.out.println("**********************************************");
    }

    private static void printObject(String operation, Object object) {
        System.out.println(String.format("************ %s ****************", operation));
        System.out.println(object);
        System.out.println("**********************************************");
    }
}
