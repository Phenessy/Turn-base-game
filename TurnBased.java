/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.turn.based;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

// Player class
class Player {
    int health;
    int attack;
    boolean taunting = false;
    int tauntDuration = 0; 
    boolean defending = false;
    String name;
    Random random = new Random();
    String[] speech = {
        "Loser! Take this!",
        "I will win!",
        "It is over!"
    };

    public Player(String name, int health, int attack) {
        this.name = name;
        this.health = health;
        this.attack = attack;
    }

    public void taunt() {
        this.taunting = true;
        this.tauntDuration = 2;
        System.out.println(this.name + " is taunting!");
    }

    public void defend() {
        this.defending = true;
        System.out.println(this.name + " is defending!");
    }

    public void speech() {
        String quote = speech[random.nextInt(speech.length)];
        System.out.println(this.name + ": \"" + quote + "\"");
    }

    public void attack(Enemy target) {
        if (target.taunting && target.tauntDuration > 0 && random.nextInt(100) < 50) {
            System.out.println(this.name + " tries to attack but misses due to the enemy's taunt!");
            return;
        }

        int damage = this.attack + random.nextInt(5);
        boolean isCritical = random.nextInt(100) < 10;
        if (isCritical) {
            damage *= 2;
            System.out.println("Critical hit!");
        }
        if (target.defending) {
            System.out.println("Target is defending! Damage reduced.");
            damage /= 2;
            target.defending = false;
        }
        target.health -= damage;
        System.out.println(this.name + " attacks " + target.name + " for " + damage + " damage! " + target.name + " now has " + target.health + " HP.");
    }

    public void heal() {
        if (this.health < 100) {
            int healAmount = Math.min(10, 100 - this.health);
            this.health += healAmount;
            System.out.println(this.name + " heals for " + healAmount + " HP! Now has " + this.health + " HP.");
        } else {
            System.out.println("Healing failed! Health is already full.");
        }
    }
}

// Enemy class
class Enemy {
    int health;
    int attack;
    boolean taunting = false;
    int tauntDuration = 0;
    boolean defending = false;
    String name;
    Random random = new Random();
    String[] quotes = {
        "You cannot defeat me!",
        "Feel the power of darkness!",
        "Your attacks are futile!",
        "I will crush you!",
        "This is the end for you!"
    };

    public Enemy(String name, int health, int attack) {
        this.name = name;
        this.health = health;
        this.attack = attack;
    }

    public void taunt() {
        this.taunting = true;
        this.tauntDuration = 2;
        System.out.println(this.name + " is taunting!");
    }

    public void defend() {
        this.defending = true;
        System.out.println(this.name + " is defending!");
    }

    public void attack(Player target) {
        if (target.taunting && target.tauntDuration > 0 && random.nextInt(100) < 50) {
            System.out.println(this.name + " tries to attack but misses due to the player's taunt!");
            return;
        }

        int damage = this.attack + random.nextInt(5);
        boolean isCritical = random.nextInt(100) < 10;
        if (isCritical) {
            damage *= 2;
            System.out.println("Critical hit!");
        }
        if (target.defending) {
            System.out.println("Target is defending! Damage reduced.");
            damage /= 2;
            target.defending = false;
        }
        target.health -= damage;
        System.out.println(this.name + " attacks " + target.name + " for " + damage + " damage! " + target.name + " now has " + target.health + " HP.");
    }
}

// Main game class
public class TurnBased {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player player = new Player("Player", 100, 10);
        Enemy enemy = new Enemy("Dragon", 100, 10);

        System.out.println("Game start!");
        Queue<Object> turnQueue = new LinkedList<>();
        turnQueue.add(player);
        turnQueue.add(enemy);

        while (player.health > 0 && enemy.health > 0) {
            System.out.println("Player HP: " + player.health + " | Enemy HP: " + enemy.health);
            Object current = turnQueue.poll();

            if (current instanceof Player) {
                System.out.println("Player's turn! Choose an action: 1. Attack 2. Defend 3. Heal 4. Taunt 5. Speech");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: player.attack(enemy); break;
                    case 2: player.defend(); break;
                    case 3: player.heal(); break;
                    case 4: player.taunt(); break;
                    case 5: player.speech(); break;
                    default: System.out.println("Invalid choice! Skipping turn.");
                }
            } else if (current instanceof Enemy) {
                System.out.println("Enemy's turn!");
                int enemyChoice;
                if (player.health < 30){
                    enemyChoice = 1;
                } else if (!player.taunting && enemy.health > 50 && enemy.tauntDuration == 0){
                    enemyChoice =3;
                } else if (enemy.health < 30){
                    enemyChoice = 2;
                } else {
                    enemyChoice = new Random().nextInt(2) + 1;
                    
                }
                switch (enemyChoice) {
                    case 1: enemy.attack(player); break;
                    case 2: enemy.defend(); break;
                    case 3: enemy.taunt(); break;
                }
            }

            if (player.tauntDuration > 0) player.tauntDuration--;
            if (enemy.tauntDuration > 0) enemy.tauntDuration--;
            turnQueue.add(current);
        }

        System.out.println(player.health <= 0 ? "Game over! You lost." : "Congratulations! You defeated the " + enemy.name + "!");
        scanner.close();
    }
}
