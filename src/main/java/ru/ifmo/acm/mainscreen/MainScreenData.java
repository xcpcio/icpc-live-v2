package ru.ifmo.acm.mainscreen;


import ru.ifmo.acm.datapassing.ClockData;
import ru.ifmo.acm.mainscreen.statuses.*;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Aksenov239 on 15.11.2015.
 */
public class MainScreenData {
    public static MainScreenData getMainScreenData() {
        if (mainScreenData == null) {
            mainScreenData = new MainScreenData();
            //new DataLoader().frontendInitialize();
        }
        return mainScreenData;
    }

    private MainScreenData() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/mainscreen.properties"));

            String backupAdvertisements = properties.getProperty("backup.advertisements");

            long latency = Long.parseLong(properties.getProperty("latency.time"));

            long timeAdvertisement = Long.parseLong(properties.getProperty("advertisement.time")) + latency;
            advertisementStatus = new AdvertisementStatus(backupAdvertisements, timeAdvertisement);

            String backupPersons = properties.getProperty("backup.persons");
            long timePerson = Long.parseLong(properties.getProperty("person.time")) + latency;
            personStatus = new PersonStatus(backupPersons, timePerson);

            standingsStatus = new StandingsStatus(latency);

            int sleepTime = Integer.parseInt(properties.getProperty("sleep.time"));

            teamStatus = new TeamStatus(sleepTime);

            cameraStatus = new CameraStatus(sleepTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clockData = new ClockData();
    }

    public void update() {
      advertisementStatus.update();
      personStatus.update();
      standingsStatus.update();
    }

    private static MainScreenData mainScreenData;

    public AdvertisementStatus advertisementStatus;
    public ClockData clockData;
    public PersonStatus personStatus;
    public StandingsStatus standingsStatus;
    public TeamStatus teamStatus;
    public CameraStatus cameraStatus;
}
