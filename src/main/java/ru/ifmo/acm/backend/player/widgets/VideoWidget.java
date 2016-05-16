package ru.ifmo.acm.backend.player.widgets;

import ru.ifmo.acm.backend.player.PlayerInImage;
import ru.ifmo.acm.datapassing.CachedData;
import ru.ifmo.acm.datapassing.Data;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author: pashka
 */
public class VideoWidget extends Widget implements PlayerWidget {
    private PlayerInImage player;
    protected BufferedImage image;

    protected boolean inChange;
    protected boolean ready;
    protected boolean stopped;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int sleepTime;
    protected String currentUrl;

    public VideoWidget(int x, int y, int width, int height, int sleepTime, long updateWait) {
        super(updateWait);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        player = new PlayerInImage(width, height, null, null);
        image = player.getImage();
        this.sleepTime = sleepTime;
        ready = true;
    }

    private PlayerInImage manualTempPlayer;
    private String manualTempURL;

    public void changeManually(String url) {
        SwingUtilities.invokeLater(() -> {
            if (url == null) {
                manualTempURL = null;
                return;
            }
            manualTempPlayer = new PlayerInImage(width, height, null, url);
            manualTempURL = url;
        });
    }

    public void switchManually() {
        checkEDT();
        if (manualTempURL == null) {
            stop();
            return;
        }
        JComponent component = player.getComponent();
        player.setComponent(null);
        manualTempPlayer.setComponent(component);
        if (!stopped) {
            player.stop();
        }
        player = manualTempPlayer;
        image = player.getImage();
        currentUrl = manualTempURL;
        stopped = false;
    }

    public void change(String url) {
        log.info("Change to " + url);
        SwingUtilities.invokeLater(() -> {
            if (url == null) {
                if (!stopped) {
                    currentUrl = null;
                    stop();
                }
                return;
            }
            ready = false;
            PlayerInImage player2 = new PlayerInImage(width, height, null, url);
            Timer timer = new Timer(sleepTime, (a) -> {
                JComponent component = player.getComponent();
                player.setComponent(null);
                player2.setComponent(component);
                inChange = true;
                if (!stopped) {
                    player.stop();
                }
                player = player2;
                image = player2.getImage();
                ready = true;
                currentUrl = url;
                stopped = false;
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    public void setVolume(int volume) {
        checkEDT();
        player.setVolume(volume);
    }

    public void stop() {
        SwingUtilities.invokeLater(() -> {
            if (player != null && !stopped) {
                player.stop();
            }
            stopped = true;
            currentUrl = null;
        });
    }

    public boolean readyToShow() {
        checkEDT();
        return ready;
    }

    public MediaPlayer getPlayer() {
        checkEDT();
        return player.getPlayer();
    }

    public void paintImpl(Graphics2D g, int width, int height) {
        g.drawImage(player.getImage(), x, y, null);
    }

    @Override
    protected CachedData getCorrespondingData(Data data) {
        return null;
    }

    private void checkEDT() {
        if (!SwingUtilities.isEventDispatchThread()) {
            IllegalStateException e = new IllegalStateException();
            log.error("Not in EDT!", e);
            throw e;
        }
    }

}
