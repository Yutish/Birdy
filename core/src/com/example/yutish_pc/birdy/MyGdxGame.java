package com.example.yutish_pc.birdy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    //ShapeRenderer shapeRenderer;

    Texture playAgain;

    Texture[] life;
    int maxLife = 5;
    Texture gameOver;

    Texture[] birds;
    int flapState;
    float birdY;
    float velocity;
    float gravity = 2;
    Circle birdCircle;

    int score;
    int scoringTube;
    BitmapFont font;

    int gameState = 0;

    Texture topTube;
    Texture bottomTube;
    float gap = 500;
    float[] gapsInBetween = {300, 400, 445, 465, 500, 600};        // new feature......
    float maxTubeOffset;
    Random randomGenerator;
    Rectangle[] topTubeRectangle;
    Rectangle[] bottomTubeRectangle;

    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenubes;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        //shapeRenderer = new ShapeRenderer();

        life = new Texture[maxLife];
        for (int i = 0; i < maxLife; i++) {
            life[i] = new Texture("life.png");
        }
        gameOver = new Texture("gameover.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        birdCircle = new Circle();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenubes = Gdx.graphics.getWidth() * 0.75f;
        topTubeRectangle = new Rectangle[numberOfTubes];
        bottomTubeRectangle = new Rectangle[numberOfTubes];

        startGame();

        playAgain = new Texture("playagain.png");

    }

    public void startGame() {

        score = 0;
        scoringTube = 0;
        maxLife = 5;
        velocity = 0;
        birdY = 0;
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            gap = gapsInBetween[Math.abs(randomGenerator.nextInt()) % 6];
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenubes;

            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;

                Gdx.app.log("score", String.valueOf(score));

                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {

                if (birdY - 25 < Gdx.graphics.getHeight() - birds[0].getHeight())
                    velocity = -25;

            }
            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    gap = gapsInBetween[Math.abs(randomGenerator.nextInt()) % 6];
                } else {

                    tubeX[i] -= tubeVelocity;

                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(),
                        topTube.getHeight());
                bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
                        bottomTube.getWidth(), bottomTube.getHeight());

            }

            int space = 20;
            for (int i = 0; i < maxLife; i++) {

                batch.draw(life[i], space, Gdx.graphics.getHeight() - (life[i].getHeight() * 3 / 2), life[i].getWidth(), life[i].getHeight());
                space += 20 + life[i].getWidth();
            }


            if (birdY > 0) {

                velocity += gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }

        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {

                gameState = 1;

            }
        } else if (gameState == 2) {

            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2,
                    gameOver.getWidth(), gameOver.getHeight());

            if (Gdx.input.justTouched()) {

                gameState = 1;
                startGame();
            }
        }


        if (flapState == 0)
            flapState = 1;
        else
            flapState = 0;

        if (gameState == 0 || gameState == 2)
            batch.draw(playAgain, Gdx.graphics.getWidth() / 2 - playAgain.getWidth() / 4, Gdx.graphics.getHeight() / 4 - playAgain.getHeight() / 2
                    , playAgain.getWidth(), playAgain.getHeight());
        if (gameState == 1 || gameState == 0)
            batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        if (gameState == 1)
            font.draw(batch, String.valueOf(score), 100, 200);
        else if (gameState == 2)
            font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2 - font.getSpaceWidth(),
                    Gdx.graphics.getHeight() / 2 + gameOver.getHeight() + 100);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/

        boolean collision = false;
        for (int i = 0; i < numberOfTubes; i++) {
			/*shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()+tubeOffset[i],bottomTube.getWidth(),
																								bottomTube.getHeight());
			*/

            if (Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i])) {


                collision = true;
                if (maxLife == 0)
                    gameState = 2;

            }
        }
        if (collision == true) {
            maxLife -= 1;
            collision = false;
        }

        //shapeRenderer.end();
    }

	/*
	 @Override
	public void dispose () {
		batch.dispose();
		background.dispose();

	}*/
}
