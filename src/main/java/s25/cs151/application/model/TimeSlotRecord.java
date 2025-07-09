package s25.cs151.application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeSlotRecord {
    private final StringProperty fromHour;
    private final StringProperty toHour;

    public TimeSlotRecord(String fromHour, String toHour) {
        this.fromHour = new SimpleStringProperty(fromHour);
        this.toHour = new SimpleStringProperty(toHour);
    }

    public String getFromHour() {
        return fromHour.get();
    }

    public StringProperty fromHourProperty() {
        return fromHour;
    }

    public String getToHour() {
        return toHour.get();
    }

    public StringProperty toHourProperty() {
        return toHour;
    }
}