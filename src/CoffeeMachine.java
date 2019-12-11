//package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] result = {};

        CoffeeMachineBack coffeeMachine = new CoffeeMachineBack(400, 540, 120, 9, 550);

        System.out.println(17 >> 3);
        do {
            if (coffeeMachine.state == MachineStatus.READY) {
                System.out.println("Write action (buy, fill, take, remaining, exit):");
            }

            result = coffeeMachine.makeAction(scanner.nextLine());

            if (coffeeMachine.state == MachineStatus.WAITING_FOR_COFFEE_CHECK
                    || coffeeMachine.state == MachineStatus.WAITING_FOR_WATER_INPUT
                    || coffeeMachine.state == MachineStatus.READY
            ) {
                System.out.println();
            }

            for (String res : result) {
                System.out.println(res);
            }

            if (coffeeMachine.state == MachineStatus.READY) {
                System.out.println();
            }
        } while (coffeeMachine.state != MachineStatus.EXIT);
    }
}

enum MachineStatus {
    READY,
    WAITING_FOR_COFFEE_CHECK,
    WAITING_FOR_WATER_INPUT,
    WAITING_FOR_MILK_INPUT,
    WAITING_FOR_COFFEE_INPUT,
    WAITING_FOR_CUPS_INPUT,
    EXIT
}

class CoffeeMachineBack {
    private int remainWater;
    private int remainMilk;
    private int remainCoffee;
    private int remainCups;
    private int remainMoney;
    private int[][] coffeeParams = {
            {250, 0, 16, 4}, //espresso
            {350, 75, 20, 7}, //latte
            {200, 100, 12, 6} //cappuccino
    };

    public MachineStatus state;

    public CoffeeMachineBack(int water, int milk, int coffee, int cups, int money) {
        this.remainWater = water;
        this.remainMilk = milk;
        this.remainCoffee = coffee;
        this.remainCups = cups;
        this.remainMoney = money;
        this.state = MachineStatus.READY;
    }

    public String[] makeAction(String directive) {
        String[] result = {};

        switch (this.state) {
            case READY:
                switch (directive) {
                    case "buy":
                        this.state = MachineStatus.WAITING_FOR_COFFEE_CHECK;
                        result = getMachineAnswer();
                        break;
                    case "remaining":
                        this.state = MachineStatus.READY;
                        result = printCoffeeMachineState();
                        break;
                    case "take":
                        result = giveOutMoney();
                        this.remainMoney = 0;
                        this.state = MachineStatus.READY;
                        break;
                    case "fill":
                        this.state = MachineStatus.WAITING_FOR_WATER_INPUT;
                        result = waitingForWaterMessage();
                        break;
                    case "exit":
                        this.state = MachineStatus.EXIT;
                        break;
                }
                break;
            case WAITING_FOR_COFFEE_CHECK:
                this.state = MachineStatus.READY;
                switch (directive) {
                    case "1":
                        result = checkAmountOfResourcesAndMakeCoffee(0);
                        break;
                    case "2":
                        result = checkAmountOfResourcesAndMakeCoffee(1);
                        break;
                    case "3":
                        result = checkAmountOfResourcesAndMakeCoffee(2);
                        break;
                    case "back":
                        break;
                }
                break;
            case WAITING_FOR_WATER_INPUT:
                this.state = MachineStatus.WAITING_FOR_MILK_INPUT;
                remainWater += Integer.parseInt(directive);
                result = waitingForMilkMessage();
                break;
            case WAITING_FOR_MILK_INPUT:
                this.state = MachineStatus.WAITING_FOR_COFFEE_INPUT;
                remainMilk += Integer.parseInt(directive);
                result = waitingForCoffeeMessage();
                break;
            case WAITING_FOR_COFFEE_INPUT:
                this.state = MachineStatus.WAITING_FOR_CUPS_INPUT;
                remainCoffee += Integer.parseInt(directive);
                result = waitingForCupsMessage();
                break;
            case WAITING_FOR_CUPS_INPUT:
                remainCups += Integer.parseInt(directive);
                this.state = MachineStatus.READY;
                break;
        }

        return result;
    }

    private String[] getMachineAnswer() {
        return new String[]{"What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"};
    }

    private String[] waitingForWaterMessage() {
        return new String[]{"Write how many ml of water do you want to add:"};
    }

    private String[] waitingForMilkMessage() {
        return new String[]{"Write how many ml of milk do you want to add:"};
    }

    private String[] waitingForCoffeeMessage() {
        return new String[]{"Write how many grams of coffee beans do you want to add:"};
    }

    private String[] waitingForCupsMessage() {
        return new String[]{"Write how many disposable cups of coffee do you want to add:"};
    }

    private String[] printCoffeeMachineState() {
        return new String[]{"The coffee machine has:",
                this.remainWater + " of water",
                this.remainMilk + " of milk",
                this.remainCoffee + " of coffee beans",
                this.remainCups + " of disposable cups",
                this.remainMoney + " of money"
        };
    }

    private String[] giveOutMoney() {
        return new String[]{"I gave you $" + this.remainMoney};
    }

    private String[] checkAmountOfResourcesAndMakeCoffee(int coffeeType) {
        int[] coffeeResources = coffeeParams[coffeeType];

        if (this.remainWater < coffeeResources[0]) {
            return new String[]{"Sorry, not enough water!"};
        } else if (this.remainMilk < coffeeResources[1]) {
            return new String[]{"Sorry, not enough milk!"};
        } else if (this.remainCoffee < coffeeResources[2]) {
            return new String[]{"Sorry, not enough coffee beans!"};
        } else if (this.remainCups < 1) {
            return new String[]{"Sorry, not enough disposable cups!"};
        } else {
            this.remainWater -= coffeeResources[0];
            this.remainMilk -= coffeeResources[1];
            this.remainCoffee -= coffeeResources[2];
            this.remainCups--;
            this.remainMoney += coffeeResources[3];

            return new String[]{"I have enough resources, making you a coffee!"};
        }
    }
}