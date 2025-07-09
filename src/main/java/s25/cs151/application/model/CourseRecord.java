package s25.cs151.application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseRecord {
    private final StringProperty courseCode;
    private final StringProperty courseName;
    private final StringProperty sectionNumber;

    public CourseRecord(String courseCode, String courseName, String sectionNumber) {
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
        this.sectionNumber = new SimpleStringProperty(sectionNumber);
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getSectionNumber() {
        return sectionNumber.get();
    }

    public StringProperty sectionNumberProperty() {
        return sectionNumber;
    }
}