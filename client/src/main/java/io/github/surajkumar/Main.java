package io.github.surajkumar;

public class Main {

    public static void main(String[] args) {
        Bot bot = new MyCustomBot();
        bot.connectToServer("localhost", 43594);
    }
}
