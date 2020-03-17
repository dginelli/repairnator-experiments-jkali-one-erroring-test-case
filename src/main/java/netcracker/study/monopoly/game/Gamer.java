package netcracker.study.monopoly.game;

import java.util.Random;

public class Gamer {

    String name;
    int money;
    boolean canGo;

    int first;
    int second;
    Random random = new Random();
    int order;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    int position;

    public boolean isCanGo() {
        return canGo;
    }

    public void setCanGo(boolean canGo) {
        this.canGo = canGo;
    }

    public void go(){
        first = random.nextInt(5) + 1;
        second = random.nextInt(5) + 1;
        position = position + first + second;
    }

    public void action(){

    }

    public void sell(int i){
        money+=i;
    }

    public void buy(int i){
        if(money > i){
            money-=i;
        } else {
            System.out.println("You can't buy this street");
        }
    }

    public void jailAction(boolean b){
        canGo = b;
    }

    public void pay(int i){
        if(money > i){
            money-=i;
        } else {
            System.out.println("You can't pay");
        }
    }
}
