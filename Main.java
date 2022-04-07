import java.lang.Thread;
import java.util.Random; //used to generate a stream of pseudorandom numbers.

class CustomThread extends Thread {

    private int[] array;
    private int low, high, split;

    public CustomThread(int[] array, int low, int high)

    {
        this.array = array;
        this.low = low;
        this.high = Math.min(high, array.length);
    }

    public int getSplitSum()
    {
        return split;
    }
    public void run()

    {
        split = SingleThreadSum(array, low, high);
    }

    public static int SingleThreadSum(int[] array)
    {
        return SingleThreadSum(array, 0, array.length);
    }
    public static int SingleThreadSum(int[] arr, int low, int high)
    {
        int total = 0;
        for (int i = low; i < high; i++) {
            total += arr[i];
        }
        return total;
    }
    public static int computeSumParallel(int[] array)
    {
        return computeSumParallel(array, Runtime.getRuntime().availableProcessors());
    }
    public static int computeSumParallel(int[] array, int threads)
    {
        int size = (int) Math.ceil(array.length * 1.0 / threads); //ceil returns the smallest (closest to negative infinity) floating-point value that is greater than or equal to the argument and is equal to a mathematical integer.

        CustomThread[] sums = new CustomThread[threads];
        for (int i = 0; i < threads; i++) {
            sums[i] = new CustomThread(array, i * size, (i + 1) * size); //class summation extends thread
            sums[i].start(); //Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.

        }

        try {
            for (CustomThread sum : sums) {
                sum.join(); //Waits for this thread to die.An invocation of this method behaves in exactly the same way as the invocation
            }

        } catch (InterruptedException e) { } //Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity

        int total = 0;
        for (CustomThread sum : sums) {

            total += sum.getSplitSum();
        }
        return total;
    }
}

//Main
public class Main {
    public static void main(String[] args)
    {
        Random rand = new Random();
        int[] arr = new int[200000000]; //200 million (8 zeros) check
        for (int i = 0; i < arr.length; i++) {

            arr[i] = rand.nextInt(10) + 1; //random numbers between 1 through 10
        }
        long startTime = System.currentTimeMillis();
        System.out.println("\nComputing the sum of 200 million random numbers between 1 and 10: " + CustomThread.SingleThreadSum(arr));
        System.out.println(" Parallel Execution - Speed: " + (System.currentTimeMillis() - startTime + " milliseconds" ));

        System.out.println("--------------------------------");

        startTime = System.currentTimeMillis();
        System.out.println("Computing the sum of 200 million random numbers between 1 and 10: " + CustomThread.computeSumParallel(arr));
        System.out.println(" Multithreaded Execution - Speed: " + (System.currentTimeMillis() - startTime + " milliseconds" ));
    }
}

//Jon Campbell Mod 8 Concurrency
//Reference: Intro to Java Programming by Y. Daniel Liang, 10th Edition