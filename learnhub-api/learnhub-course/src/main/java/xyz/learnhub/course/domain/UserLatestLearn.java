package xyz.learnhub.course.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @create 2023/3/27 15:29
 */
@Data
public class UserLatestLearn {
    @JsonProperty("course")
    private Course course;

    @JsonProperty("record")
    private UserCourseRecord userCourseRecord;

    @JsonProperty("last_learn_hour")
    private CourseHour lastLearnHour;

    @JsonProperty("hour_record")
    private UserCourseHourRecord hourRecord;
}
