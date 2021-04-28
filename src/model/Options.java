package model;

public class Options {

    boolean stop;
    boolean pause;
    boolean rewind;
    boolean forward;
    boolean plus15;
    boolean minus15;
    boolean scroll;
    double playSpeed;

    public boolean isPlus15() {
        return plus15;
    }

    public void setPlus15(boolean plus15) {
        this.plus15 = plus15;
    }

    public boolean isMinus15() {
        return minus15;
    }

    public void setMinus15(boolean minus15) {
        this.minus15 = minus15;
    }

    public Options() {
        this.stop = false;
        this.pause = false;
        this.rewind = false;
        this.forward = false;
        this.plus15 = false;
        this.minus15 = false;
        this.scroll = false;
        this.playSpeed = 100;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setRewind(boolean rewind) {
        this.rewind = rewind;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setPlaySpeed(double playSpeed) {
        this.playSpeed = playSpeed;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isRewind() {
        return rewind;
    }

    public boolean isForward() {
        return forward;
    }

    public double getPlaySpeed() {
        return playSpeed;
    }
}
