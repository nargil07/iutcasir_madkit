/*
* Prey.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package turtlekit.simulations.hunt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import turtlekit.kernel.Turtle;

/**
 * A Prey
 *
 * @author Fabien MICHEL
 * @version 1.1 17/10/2000
 */
public class Prey extends Turtle {

    private int radius;

    List<Turtle> friends;
    List<Turtle> enemies;

    public Prey(int radius) {
        super("live");
        this.radius = radius;
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    public Prey() {
        //first behavior to do (here it is the only behavior of this turtle)
        super("live");
    }

    public void setup() {
        playRole("prey");
        randomHeading();
        setColor(Color.white);
        if (countTurtlesHere() > 0) {
            fd(1);
        }
    }

//a behavior
    public String live() {
        if (catched()) {
            return null;
        }
        turnRight(Math.random() * 60);
        turnLeft(Math.random() * 60);
        this.getEnemiesAndFriends();
        println(Integer.toString(enemies.size()));
        println(Integer.toString(friends.size()));
        move();
        return "live";
    }

    void move() {
        for (int i = 0; i < 6; i++) {
            if (countTurtlesAt(dx() + i, dy() + i) > 0) {
                for (Turtle enemy : enemies) {
                    if ((dx() + i == enemy.xcor()) && (dy() + i == enemy.dy())) {
                        i = 0;
                        turnRight(160);
                    }
                }
            }

        }

        // avoid being two on the same patch  
        if (countTurtlesAt(dx(), dy()) == 0) {
            fd(1);
        }
        friends.clear();
        enemies.clear();
    }

// test if I'm dead
    boolean catched() {
        int cpt = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    Turtle[] tur = turtlesAt(i, j);
                    if (tur != null && tur.length > 0 && tur[0].isPlayingRole("predator")) // instead of "instanceof". So predator can be another java class
                    {
                        cpt++;
                    }
                }
            }
        }
        if (cpt > 3) {
            return true;
        }
        return false;
    }

    void getEnemiesAndFriends() {
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (!(i == 0 && j == 0)) {
                    Turtle[] tur = turtlesAt(i, j);
                    if (tur != null && tur.length > 0 && tur[0].isPlayingRole("predator")) // instead of "instanceof". So predator can be another java class
                    {
                        enemies.add(tur[0]);
                    } else if (tur != null && tur.length > 0 && tur[0].isPlayingRole("prey")) {
                        friends.add(tur[0]);
                    }
                }
            }
        }
    }
}
