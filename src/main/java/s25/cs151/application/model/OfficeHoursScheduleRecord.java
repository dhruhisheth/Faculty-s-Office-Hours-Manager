package s25.cs151.application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OfficeHoursScheduleRecord {
    private final StringProperty studentName;
    private final StringProperty scheduleDate;
    private final StringProperty timeSlot;
    private final StringProperty course;
    private final StringProperty reason;
    private final StringProperty comment;

    public OfficeHoursScheduleRecord(String studentName, String scheduleDate, String timeSlot,
                                     String course, String reason, String comment) {
        this.studentName = new SimpleStringProperty(studentName);
        this.scheduleDate = new SimpleStringProperty(scheduleDate);
        this.timeSlot = new SimpleStringProperty(timeSlot);
        this.course = new SimpleStringProperty(course);
        this.reason = new SimpleStringProperty(reason);
        this.comment = new SimpleStringProperty(comment);
    }

    public String getStudentName() { return studentName.get(); }
    public String getScheduleDate() { return scheduleDate.get(); }
    public String getTimeSlot() { return timeSlot.get(); }
    public String getCourse() { return course.get(); }
    public String getReason() { return reason.get(); }
    public String getComment() { return comment.get(); }

}
