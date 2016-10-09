package com.scorpio.test.util;

/**
 * When a class is locally declared, Java does not allow
 * you to reference variables outside the context unless
 * they are final. This abuses the weakness of 'final' so
 * that we can detect that certain portions of code have run
 */
public class Trigger{
    private boolean tripped = false;
    public void trip(){this.tripped = true;}
    public void reset(){this.tripped = false;}
    public boolean isTripped(){return this.tripped;}
}
