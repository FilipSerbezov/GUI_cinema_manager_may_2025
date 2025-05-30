package main_1;
public class useful_static_functions {
    public static void fill_int_arr_manually (int[] container_arr, java.util.Scanner scan, int init_index) {
        container_arr[init_index] = scan.nextInt();
        if (init_index < container_arr.length - 1) {
            fill_int_arr_manually(container_arr, scan, init_index + 1);
        }
    }
    public static void fill_random_1d_arr(int[] int_arr, int lower_bound, int upper_bound, int init, java.util.Random randomizer) {
        int_arr[init] = randomizer.nextInt(upper_bound - lower_bound + 1) + lower_bound;
        if (init < int_arr.length - 1) {
            fill_random_1d_arr(int_arr, lower_bound, upper_bound, init + 1, randomizer);
        }
    }
    public static void print_int_arr(int[] int_arr, int init) {
        System.out.print(int_arr[init] + "; ");
        if (init < int_arr.length - 1) {
            print_int_arr(int_arr, init + 1);
        }
    }
    public static void print_2d_int_arr(int[][] array_2d, int recur_dim2_index) {
        if (recur_dim2_index < array_2d[0].length) {
            System.out.print("\n[");
            print_2d_arr_dim2_row(array_2d, recur_dim2_index, 0);
            System.out.print("]; ");
            print_2d_int_arr(array_2d, recur_dim2_index + 1);
        }
    }
    public static void print_2d_arr_dim2_row(int[][] array_2d, int dim2_layer, int recur_dim1_index) {
        if (recur_dim1_index < array_2d.length) {
            System.out.print(array_2d[recur_dim1_index][dim2_layer] + "; ");
            print_2d_arr_dim2_row(array_2d, dim2_layer, recur_dim1_index + 1);
        }
    }
    public static void get_coord_2_layer(int[][] array_2d, int coord_2_index, int[] container_arr, int recur_coord_1_index) {
        if (recur_coord_1_index < array_2d.length - 1) {
            container_arr[recur_coord_1_index] = array_2d[recur_coord_1_index][coord_2_index];
            get_coord_2_layer(array_2d, coord_2_index, container_arr, recur_coord_1_index + 1);
        }
    }
    public static void print_2d_byte_arr(byte[][] array_2d, int recur_dim2_index) {
        if (recur_dim2_index < array_2d[0].length) {
            System.out.print("\n[");
            print_2d_byte_arr_dim2_row(array_2d, recur_dim2_index, 0);
            System.out.print("]; ");
            print_2d_byte_arr(array_2d, recur_dim2_index + 1);
        }
    }
    public static void print_2d_byte_arr_dim2_row(byte[][] array_2d, int dim2_layer, int recur_dim1_index) {
        if (recur_dim1_index < array_2d.length) {
            System.out.print(array_2d[recur_dim1_index][dim2_layer] + "; ");
            print_2d_byte_arr_dim2_row(array_2d, dim2_layer, recur_dim1_index + 1);
        }
    }

    public static Integer[] convert_to_integer_arr(int[] int_arr) {
        Integer[] integer_arr = new Integer[int_arr.length];
        int index = 0;
        while (index < int_arr.length) {
            integer_arr[index] = Integer.valueOf(int_arr[index]);
            index += 1;
        }
        return integer_arr;
    }

    public static java.util.HashSet<Integer> convert_int_arr_to_hashset(int[] integer_arr) {
        java.util.HashSet<Integer> hashset_from_arr = new java.util.HashSet<>();
        int index = 0;
        while (index < integer_arr.length) {
            hashset_from_arr.add(integer_arr[index]);
            index += 1;
        }
        return hashset_from_arr;
    }

    public static boolean char_arrays_are_equal(char[] input_arr_1, char[] input_arr_2) {
        int length_1 = input_arr_1.length;
        int length_2 = input_arr_2.length;

        if (length_1 != length_2) {
            return false;
        }

        int iter_index = 0;
        while (iter_index < length_1) {
            if (!(input_arr_1[iter_index] == input_arr_2[iter_index])) {
                return false;
            }
            iter_index += 1;
        }
        return true;
    }
    // public static void found_in_arr(int search_int, int[] main_arr, int init_index, boolean init_bool /*false by default*/) {
    //     if (main_arr[init_index] == search_int) {
    //         init_bool = true;
    //         return;
    //     }
    //     if (init_index < main_arr.length - 1) {
    //         found_in_arr(search_int, main_arr, init_index + 1, init_bool);
    //     }
    // }
    // public static Comparable[] asc_selection_sort_comp_array(Comparable[] input_array) {
    //     Comparable[] sorted_array = new Comparable[input_array.length];
    //     int sorted_index = 0;
    //     while (sorted_index < input_array.length) {
    //         Comparable min_element = input_array[sorted_index + 1];
    //         int min_search_index = 0;
    //         while (min_search_index < input_array.length) {
    //             if () {

    //             }
    //         }
    //     }
    // }
}