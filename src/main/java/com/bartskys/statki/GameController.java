package com.bartskys.statki;

import com.bartskys.statki.input.Input;
import com.bartskys.statki.input.MouseInputClick;
import com.bartskys.statki.input.MouseInputPos;
import com.bartskys.statki.math.Vector3f;
import com.bartskys.statki.model.*;

import java.util.ArrayList;
import java.util.Random;

import static com.bartskys.statki.utils.GameUtils.*;
import static org.lwjgl.glfw.GLFW.*;

class GameController {

    private static final String PLAYER1 = "Player01";
    private static final String PLAYER2 = "Player02";
    private static final float TILESIZE = 0.24f;
    private static final float TILESPACE = 0.5f;
    private float bulletSpeed = 0.04f;
    int particleCount = 100;
    int pLifespan = 50;
    private static Sign PLAYER1SETUP;
    private static Sign PLAYER2SETUP;
    private static RenderBox BACKGROUND;
    int p1Ships = 1;
    int p2Ships = 1;
    float scale = 0.489f;
    private static boolean running;
    private final Player player1, player2;
    private boolean p1setup, p2setup;
    float boardPosX = -1.1f;
    float boardPosY = 2.52f;
    ShipEnum shipType = ShipEnum.SINGLE;
    boolean direction = true;

    //Frame counting
    long lastTime = System.currentTimeMillis();
    long timer = System.currentTimeMillis();
    double delta = 0.0;
    double ns = 1000000000.0 / 60.0;
    int updates = 0;
    int frames = 0;
    //-------------
    private double mouseX, mouseY;
    int mClick; int mAction;
    boolean clickWait = false;
    boolean buttonD = false;
    boolean turn = true;
    boolean winner = false;
    int p1tries = 0;
    int p2tries = 0;
    Vector3f from, to; // Animation variables than need to be updated every frame
    private boolean animation = false;
    private boolean pAnimation = false;
    ArrayList<Bullet> bullets;
    ArrayList<Tile> p1Buffer, p2Buffer;
    ArrayList<Particle> particles;
    RenderBox testP;
    float time = 0.0f;

    GameController() {
        ViewRenderer.init();
        running = true;
        player1 = new Player(boardPosX, boardPosY - 10.0f,scale, PLAYER1);
        player2 = new Player(boardPosX, boardPosY +0.265f,scale, PLAYER2);
        p1setup = p2setup = true;
        from = to = new Vector3f();

        PLAYER1SETUP = new Sign(
                "Player1 Setup",
                -8f,
                 4.25f
        );
        PLAYER2SETUP = new Sign(
                "Player2 Setup",
                2.25f,
                4.25f
        );
        BACKGROUND = new RenderBox(
                "background",
                "res/statkitlo.png",
                new Vector3f(0.0f, 0.0f, -0.1f),
                12.80f * 0.8f, 7.20f * 0.8f
        );
        bullets = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            bullets.add(new Bullet(
                    from, to, animation, i,
                    new RenderBox("bullet"+i,"res/dot.png",from, TILESIZE, TILESIZE)
            ));
        }
    }

    void mainLoop() {

        //Main Loop
        while (running) {
            // Computer Setup
            if(p1setup) {
                randClick(player1, mouseX, mouseY, mClick, mAction, direction);
                p1tries++;
            }
            if(!p1setup && p2setup) {
                randClick(player2, mouseX, mouseY, mClick, mAction, direction);
                p2tries++;
            }

            // Player Setup
            if((p1setup || p2setup) && Input.isKeyDown(GLFW_KEY_D) && !buttonD) {
                direction = !direction;
                buttonD = true;
            }

            if(!animation && !pAnimation && mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                Tile tempT;
                if(isMouseOnTile(player1.getBoard(), mouseX, mouseY))
                    tempT = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                else if (isMouseOnTile(player2.getBoard(), mouseX,mouseY))
                    tempT = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                else tempT = new Tile();
                if(p1setup)
                    playerSetup(tempT, player1);
                if(p2setup && !p1setup)
                    playerSetup(tempT, player2);
            }

            // Main Game
            if(!pAnimation && !animation && !p1setup && !p2setup && !winner) {
                if(turn) {
                    p2Buffer = copyBoard(player2.getBoard());
                    randClick(player2, mouseX, mouseY, mClick, mAction, direction);
                    if(mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait)
                        if(isMouseOnTile(player2.getBoard(), mouseX, mouseY)) {
                            clickWait = true;
                            Tile t = tileFromMouse(player2.getBoard(), mouseX, mouseY);
                            p1tries++;
                            if(!t.isShotAt()) {
                                if(shoot(t, player2)) {
                                    if(!animation && !pAnimation) {
                                        to = t.getPosition();
                                        from = randTilePosition(player1.getBoard(), direction);
                                        if(t.isOwned()) {
                                            particles = null;
                                            particleCount = 1000;
                                            pLifespan = 200;
                                        }
                                        else {
                                            particles = null;
                                            particleCount = 55;
                                            pLifespan = 50;
                                        }

                                        generateBullets(player1);
                                        generateParticles(t, particleCount);
                                        animation = true;
                                    }
                                    turn = !turn;
                                }
                            }
                    }
                }
                else {
                    p1Buffer = copyBoard(player1.getBoard());
                    randClick(player1, mouseX, mouseY, mClick, mAction, direction);
                    if(mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait)
                        if(isMouseOnTile(player1.getBoard(), mouseX, mouseY)) {
                            clickWait = true;
                            Tile t = tileFromMouse(player1.getBoard(), mouseX, mouseY);
                            p2tries++;
                            if(!t.isShotAt()) {
                                if(shoot(t, player1)) {
                                    if(!animation && !pAnimation) {
                                        to = t.getPosition();
                                        from = randTilePosition(player2.getBoard(), direction);
                                        if(t.isOwned()) {
                                            particles = null;
                                            particleCount = 1000;
                                            pLifespan = 100;
                                        }
                                        else {
                                            particles = null;
                                            particleCount = 55;
                                            pLifespan = 50;
                                        }

                                        generateBullets(player2);
                                        generateParticles(t, particleCount);
                                        animation = true;
                                    }
                                    turn = !turn;
                                }
                            }
                    }
                }
                if(!animation && !pAnimation && winner(player1) || winner(player2)) {
                    winner = true;
                    System.out.println("P1 Tries: " + p1tries + " | P2 Tries: " + p2tries);
                }
            }
            // Update game events and Counts frames and updates
            frameUpdate();



            if (glfwWindowShouldClose(ViewRenderer.getWindow()))
                running = false;
        }
        ViewRenderer.terminate();
    }

    private void generateBullets(Player player) {
        Random r = new Random();
        for(Bullet b : bullets) {
            b.setTo(new Vector3f(
                    (to.x - TILESIZE/2) + (r.nextFloat()*0.3f),
                    (to.y - TILESIZE/2) + (r.nextFloat()*0.3f),
                    0.0f
            ));
            b.setFrom(randTilePosition(player.getBoard(), direction));
            b.setAnimation(true);
            b.setSpeed(bulletSpeed + (new Random().nextFloat() * bulletSpeed));
            b.setSize((r.nextFloat()+r.nextFloat())/2);
        }
    }

    private void generateParticles(Tile t, int number) {
        Random r = new Random();
        if(particles == null) {
            particles = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                Particle p = new Particle(
                        r.nextInt(pLifespan) + pLifespan/5,
                        new Vector3f(
                                (t.getPosition().x - TILESIZE) + (r.nextFloat()/2),
                                (t.getPosition().y - TILESIZE) + (r.nextFloat()/2),
                                0.0f
                        ),
                        (r.nextFloat() + 0.9f) * 0.1f,
                        r.nextFloat() * 0.1f
                );
                p.setAlive(true);
                particles.add(p);
            }
        }
        for (Particle p : particles) {
           p.setLifespan(r.nextInt(pLifespan) + pLifespan/5);
           p.setPosition(new Vector3f(
                   (t.getPosition().x - TILESIZE) + (r.nextFloat()/2),
                   (t.getPosition().y - TILESIZE) + (r.nextFloat()/2),
                   0.0f
           ));
           p.setSpeed((r.nextFloat() + 0.9f) * 0.1f);
           p.setSize(r.nextFloat() * 0.1f);
           p.setAlive(true);
        }
    }

    private void animateParticles() {
        for(Particle p : particles) {
           p.setSize(p.getSize() + (p.getSpeed()*p.getSize())*0.5f);
           p.setLifespan(p.getLifespan()-1);
        }
    }

    private void animateShots(ArrayList<Bullet> bullets) {
        float speed;
        for(Bullet b : bullets) {
            speed = b.getSpeed();
            b.updatePos(speed);
            float absX = Math.abs(b.getTo().x - b.getFrom().x);
            float absY = Math.abs(b.getTo().y - b.getFrom().y);

            if(
                    absX > (TILESIZE*60f)-absX
            ) {
                b.setSize(b.getSize()+(b.getSize()*0.04f));
            }
            if(
                    absX <= TILESIZE*40f-absX
            ) b.setSize(b.getSize() - (b.getSize() * 0.01f));
            if(
                absX <= TILESIZE*0.2f &&
                absY <= TILESIZE*0.2f
            )
                b.setAnimation(false);

        }
    }

    private boolean shoot(Tile shot, Player player) {
        for (Tile t : player.getBoard()) {
            if(t.equals(shot)) {
                t.setShotAt(true);
                return true;
            }
        }
        return false;
    }

    private void render() {
        if(!animation && !pAnimation && !p1setup && !p2setup && !clickWait) {
            p1Buffer = copyBoard(player1.getBoard());
            p2Buffer = copyBoard(player2.getBoard());
        }

        ViewRenderer.renderStart();
        ViewRenderer.renderBox(BACKGROUND);
        //ViewRenderer.renderBox(testP);
        if(animation)
            renderShots();
        if(!animation && pAnimation)
            renderParticles();

        if (p1setup) {
            renderSign(PLAYER1SETUP);
            renderSetup(player1);
        }
        else if (p2setup) {
            renderSign(PLAYER2SETUP);
            renderSetup(player2);
        }
        if(!p1setup && !p2setup) {
            renderShotBoard(p1Buffer);
            renderShotBoard(p2Buffer);
        }

        if(!animation && !pAnimation && !p1setup && winner(player1)) renderSign(
                new Sign("player1 won",
                        -7.5f,
                        4.25f)
        );
        if(!animation && !pAnimation && !p2setup && winner(player2)) renderSign(
                new Sign("player2 won",
                        2.75f,
                        4.25f)
        );
        if(winner) {
            renderWinnerBoard(player1);
            renderWinnerBoard(player2);
        }

        ViewRenderer.renderFinish();
    }

    private void update() {
        // Aligning mouse position to the board so that the mouse coords correspond to the board coords
        if(animation) {
            if(checkAnim(bullets)) {
                clickWait = true;
                animateShots(bullets);
            } else {
                pAnimation = true;
                time = 0.0f;
                animation = false;
            }
        } else {
            if(!animation && !pAnimation && !p1setup && !p2setup){
                p1Buffer = copyBoard(player1.getBoard());
                p2Buffer = copyBoard(player2.getBoard());
            }
        }

        if(!animation && pAnimation) {
            if(time < pLifespan) time++;
            if(checkParticles()) {
                clickWait = true;
                animateParticles();
            } else pAnimation = false;


        }

        mouseX = ((float) MouseInputPos.xPos / 1280f - 1.0f) * 10.0f;
        mouseY = ((float) MouseInputPos.yPos / 720f - 1.0f) * (-10.0f * 9.0f / 16.0f);
        mClick = MouseInputClick.mbutton;
        mAction = MouseInputClick.maction;
        // Click reset

        int wait = 30;
        // Done for testing so bullet speed could be super fast. TODO: Remove this for "prod"
        if(p1setup || p2setup)
            wait = 30;
        else
            wait =  1;
        //---------------------------------------------------------

        if (frames >= wait && mAction != GLFW_PRESS && clickWait)
            clickWait = false;
        if (frames >= 30 && Input.isKeyDown(GLFW_KEY_D) && buttonD)
            buttonD = false;

        glfwPollEvents();
    }

    private boolean checkParticles() {
        for (Particle p : particles) {
            if (p.isAlive()) return true;
        }
        return false;
    }

    private ArrayList<Tile> copyBoard(ArrayList<Tile> board) {
        ArrayList<Tile> copy = new ArrayList<>();
        for(int i = 0; i < board.size(); i++) {
            Tile t = board.get(i);
            Tile copyT = new Tile(t.getName(), t.getPosition(), t.getPlayer());
            copyT.setOwned(t.isOwned());
            copyT.setShotAt(t.isShotAt());
            copyT.setOwnedByShip(t.getOwnedByShip());
            copy.add(copyT);
        }
        return copy;
    }

    private boolean checkAnim(ArrayList<Bullet> bullets) {
        for(Bullet b : bullets)
            if(b.isAnimation()) return true;

        return false;
    }

    private void renderSign(Sign sign) {
        for(RenderBox r : sign.getSign()) {
            ViewRenderer.renderBox(r);
        }
    }

    private void renderSetup(Player player) {
        int shipNum = 0;
        if(player.getName() == PLAYER1)
            shipNum = p1Ships;
        else shipNum = p2Ships;


        if (isMouseOnTile(player.getBoard(), mouseX, mouseY)) {
            ArrayList<Tile> tiles;
            tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 1, direction);

            if(shipNum > 4 && shipNum <= 7)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 2, direction);
            if(shipNum > 7 && shipNum < 10)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 3, direction);
            if(shipNum == 10)
                tiles = tilesFromTile(player.getBoard(), tileFromMouse(player.getBoard(), mouseX, mouseY), 4, direction);
            for (Tile t: tiles) {
                ViewRenderer.renderShip(t);
            }
        }
        renderBoard(player);
    }

    private void renderBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isOwned())
                ViewRenderer.renderShip(t);
            else
                ViewRenderer.renderEmptyTile(t);
        }
    }

    private void renderShotBoard(ArrayList<Tile> board) {
        for (Tile t : board) {
            if (t.isShotAt()) {
                if(t.isOwned())
                    ViewRenderer.renderHit(t);
                else ViewRenderer.renderShot(t);
            }
            else
                ViewRenderer.renderEmptyTile(t);
        }
    }

    private void renderShots() {
        for(Bullet b : bullets) {
            ViewRenderer.renderBoxScale(b.getBullet(), b.getSize());
        }
    }

    private void renderParticles() {
        for(Particle p : particles) {
            if(p.isAlive())
                if(p.getLifespan() < 50) {
                    ViewRenderer.renderBoxFade(
                            p.getRenderBox(),
                            time *(1f/p.getLifespan()*0.5f),
                            p.getSize()
                    );
                }
        }
    }

    private void renderWinnerBoard(Player player) {
        for (Tile t : player.getBoard()) {
            if (t.isShotAt() && t.isOwned()) {
                ViewRenderer.renderHit(t);
            }
            if(t.isShotAt() && !t.isOwned()) ViewRenderer.renderShot(t);
            else ViewRenderer.renderEmptyTile(t);
        }
    }

    private boolean winner(Player player) {
        int owned, shot;
        owned = shot = 0;
        for (Tile t : player.getBoard()) {
            if(t.isShotAt() && t.isOwned()) shot++;
            if(t.isOwned()) owned++;
        }
        if(shot == owned) return true;
        else return false;
    }

    void playerSetup(Tile t, Player player) {
        if (p1setup || p2setup) {
            int pShips = -1;
            if (p1setup)
                pShips = p1Ships;
            if(p2setup && !p1setup)
                pShips = p2Ships;
            switch (pShips) {
                case 0: case 1: case 2: case 3: case 4: {
                    shipType = ShipEnum.SINGLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (player.assembleShip(t, 1, pShips, shipType, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else if(player.getName().equals(PLAYER2)) p2Ships++;
                    }
                }
                break;
                case 5: case 6: case 7: {
                    shipType = ShipEnum.DOUBLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (player.assembleShip(t, 2, pShips,shipType, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else p2Ships++;
                    }
                }
                break;
                case 8: case 9: {
                    shipType = ShipEnum.TRIPLE;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (player.assembleShip(t, 3, pShips, shipType, direction))
                                    if(player.getName().equals(PLAYER1))
                                        p1Ships++;
                                    else p2Ships++;
                    }
                } break;
                case 10: {
                    shipType = ShipEnum.QUAD;
                    if (mClick == GLFW_MOUSE_BUTTON_1 && mAction == GLFW_PRESS && !clickWait) {
                        clickWait = true;
                        if (!t.isOwned())
                            if (checkAdjacent(t, player.getBoard()))
                                if (player.assembleShip(t, 4, pShips, shipType, direction))
                                    if(player.getName().equals(PLAYER1)) {
                                        //showBoard(player1);
                                        p1setup = false;
                                        System.out.println("P1 Tries:  " + p1tries);
                                        p1tries = 0;
                                    }
                                    else {
                                        //showBoard(player2);
                                        p2setup = false;
                                        System.out.println("P2 Tries:  " + p2tries);
                                        p2tries = 0;
                                    }
                    }
                } break;
            }
        }
    }

    private void frameUpdate() {
        //Timed updates
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;
        if (delta >= 1.0) {
            update();
            // Render after update
            render();
            updates++;
            delta--;
        }
        frames++;
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            System.out.println(updates + " ups, " + frames + " fps");
            updates = 0;
            frames = 0;
        }
    }
}