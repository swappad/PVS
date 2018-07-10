public class Deadlock extends Thread{
    public static void main(String[] args) throws  Exception{
        Object l1 = new Object();
        Object l2 = new Object();
        Thread t1 = new Deadlock(l1,l2);
        Thread t2 = new Deadlock(l2,l1);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }

    private Object lock1;
    private Object lock2;

    public Deadlock(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        synchronized (lock1){
            System.out.println("First lock acquired");
            synchronized (lock2){
                System.out.println("Second lock acquired");
            }
        }
        System.out.println("Done! All locks released!");
    }
}
