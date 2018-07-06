public class Programm {

    public static void main(String[] args){

        BulletinBoard bulletinBoard = new BulletinBoard();
        new Thread(new Person(bulletinBoard,"UserA")).start();
        new Thread(new Person(bulletinBoard,"UserB")).start();

    }

}
