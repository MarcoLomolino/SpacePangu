package com.example.drawabletest.play.brick.bonus_brick;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.drawabletest.R;
import com.example.drawabletest.play.brick.Brick;
import com.example.drawabletest.play.game.Game;
import com.example.drawabletest.play.position.Position;

public class SlowDownBrick extends Brick {

    public SlowDownBrick(Context context, Position position) {
        super(context, position, 20, 1);
    }

    @Override
    public void setGraphic_brick() {
        super.setGraphic_brick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_green));
    }

    @Override
    public void setEffect(Game game) {
        /*
        Questo è il prototipo di quello che sarà il mattoncino per rallentare la velocità.
        L'effetto in se funziona. Il problema è che, per qualche motivo non risponde bene alle collisioni
        (suppongo si tratti del fatto che risettando la diezione/velocità questa non cambi dopo l'hit).
        Ho provato a risolvere inserendo alla fine dell'effetto un changeDirection()
        ma ha funzionato solo in parte: la palla risponde alle collisioni col mattoncino dello slow
        solo da alcune direzioni, se lo colpisce da sotto ad esempio la palla non rimbalza,
        invece se lo colpisce da sopra la palla si comporta come con tutti gli altri mattoncini.
        Per questi motivi non l'ho momentaneamente inserito nella partita, devo prima riuscire a comprendere
        come (e se) è possibile cambiare la direzione dopo la collisione in modo più realistico
         */
        float directionX = game.getBall().getDirection().getX() - 1;
        float directionY = game.getBall().getDirection().getY() + 1;

        if(directionX < game.getBall().getMIN_X()) {
            directionX = game.getBall().getMIN_X();
        }
        if(directionY > game.getBall().getMAX_Y()) {
            directionY = game.getBall().getMAX_Y();
        }

        Position direction = new Position(directionX, directionY);
        //Position position = game.getBall().getPosition();
        game.setBallDirection(/*position,*/ direction);
        game.getBall().changeDirection();
    }
}
