package com.bramerlabs.computational_chemistry.graphing;

import java.awt.event.*;

public class GraphListener implements KeyListener, MouseListener, MouseMotionListener, WindowListener, MouseWheelListener {

    private final boolean[] keysDown = new boolean[KeyEvent.KEY_LAST];
    private final boolean[] keysDownLast = new boolean[KeyEvent.KEY_LAST];

    private final boolean[] mouseButtonsDown = new boolean[MouseEvent.MOUSE_LAST];
    private final boolean[] mouseButtonsDownLast = new boolean[MouseEvent.MOUSE_LAST];

    private int mouseX, mouseY;
    private int clickX, clickY;
    private int scrollAmount;
    private double preciseWheelRotation;
    private double totalScroll;
    private double scrollMult = 1;

    private boolean windowClosed = false;

    public void update() {
        System.arraycopy(keysDown, 0, keysDownLast, 0, keysDown.length);
        System.arraycopy(mouseButtonsDown, 0, mouseButtonsDownLast, 0, mouseButtonsDown.length);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysDown[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysDown[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButtonsDown[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButtonsDown[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public boolean isKeyDown(int keyCode) {
        return keysDown[keyCode];
    }

    public boolean isMouseButtonDown(int buttonCode) {
        return mouseButtonsDown[buttonCode];
    }

    public boolean isMouseButtonPressed(int buttonCode) {
        return !mouseButtonsDownLast[buttonCode] && mouseButtonsDown[buttonCode];
    }

    public boolean isMouseButtonReleased(int buttonCode) {
        return mouseButtonsDownLast[buttonCode] && !mouseButtonsDown[buttonCode];
    }

    public boolean isKeyPressed(int keyCode) {
        return !keysDownLast[keyCode] && keysDown[keyCode];
    }

    public boolean isKeyReleased(int keyCode) {
        return keysDownLast[keyCode] && !keysDown[keyCode];
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getClickX() {
        return clickX;
    }

    public int getClickY() {
        return clickY;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        windowClosed = true;
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public boolean isWindowClosed() {
        return windowClosed;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        preciseWheelRotation = e.getPreciseWheelRotation();
        scrollAmount = e.getScrollAmount();
        totalScroll += scrollAmount * preciseWheelRotation;
        if (preciseWheelRotation < 0) {
            scrollMult = scrollMult + (scrollAmount / 30.);
        }
        else {
            double n = scrollMult - (scrollAmount / 30.);
            if (n > 0) {
                scrollMult = n;
            }
        }
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public double getPreciseWheelRotation() {
        return preciseWheelRotation;
    }

    public double getTotalScroll() {
        return this.totalScroll;
    }

    public double getScrollMult() {
        return scrollMult;
    }
}
