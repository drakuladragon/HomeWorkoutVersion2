package com.example.nhtha.homeworkoutversion2.model;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by nhtha on 15-Jan-18.
 */

public class Movement {
    private String exName;
    private String exTurn;
    private String exCode;
    private String exHelp;
    private AnimationDrawable movementAnimation;

    public Movement(String exName, String exTurn, String exCode) {
        this.exName = exName;
        this.exTurn = exTurn;
        this.exCode = exCode;
    }

    public Movement(String exName, String exTurn, AnimationDrawable movementAnimation) {
        this.exName = exName;
        this.exTurn = exTurn;
        this.movementAnimation = movementAnimation;
    }

    public Movement() {

    }

    public String getExCode() {
        return exCode;
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public String getExTurn() {
        return exTurn;
    }

    public void setExTurn(String exTurn) {
        this.exTurn = exTurn;
    }

    public AnimationDrawable getMovementAnimation() {
        return movementAnimation;
    }

    public void setMovementAnimation(AnimationDrawable movementAnimation) {
        this.movementAnimation = movementAnimation;
    }

    public String getExHelp() {
        return exHelp;
    }

    public void setExHelp(String exHelp) {
        this.exHelp = exHelp;
    }
}
