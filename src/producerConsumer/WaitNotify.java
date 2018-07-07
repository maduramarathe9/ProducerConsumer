package producerConsumer;

public class WaitNotify {


    private static int count ;
    private static int[] buffer ;
    private static Object lock = new Object();

    static class Producer{

        public void produce()  {
            synchronized (lock) {
                while (isFull(buffer)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer[count++] = 1;
                lock.notifyAll();

            }
        }
    }


    static class Consumer{

        public void consume()  {
            synchronized (lock) {
                while (isEmpty(buffer)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer[--count] = 0;
                lock.notifyAll();
            }
        }
    }

    public static boolean isFull(int[] buffer){
        if (buffer.length==count){
            return true;
        }
        return false;

    }

    public static boolean isEmpty(int[] buffer){
        if (count==0){
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {


        buffer = new int[10];
        count = 0;

        Producer p = new Producer();
        Consumer c = new Consumer();


        Runnable producerTask = ()->{

            for(int i = 0;i<50;i++){
                p.produce();
            }
            System.out.println("Done Producing");
        };

        Runnable consumerTask = ()->{
          for(int i = 0;i<40;i++){
              c.consume();
          }
            System.out.println("Done consuming");

        };

        Thread producerThread = new Thread(producerTask);
        Thread consumerThread = new Thread(consumerTask);


        consumerThread.start();
        producerThread.start();


        consumerThread.join();
        producerThread.join();


        System.out.println("Count value is "+count);


    }




}
