package hu.tomlincoln.emarsyshw;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

class DueDateCalculatorService {

    private static final LocalTime WORKDAY_START = LocalTime.of(9,0);
    private static final LocalTime WORKDAY_END = LocalTime.of(17,0);
    private static final int WEEKEND_DAYS = 2;
    private static final int NON_WORKING_HOURS_IN_ONE_DAY = 16;
    private static final int WORKING_HOURS_IN_ONE_DAY = 8;
    private static final int WORKING_HOURS_IN_ONE_WEEK = 40;

    LocalDateTime calculateDueDate(LocalDateTime submitTime, Duration turnaroundTime) {
        if (submitTime == null || turnaroundTime == null || isOutsideWorkingTime(submitTime)) {
            throw new IllegalArgumentException("Some parameter is null or submit time is outside working hours.");
        }
        return calculateAllTimeSegments(submitTime, (int)turnaroundTime.toHours());
    }

    private LocalDateTime calculateAllTimeSegments(LocalDateTime submitTime, int remainingDurationInHours) {
        LocalDateTime dueDateTime = calculateWeeks(submitTime, remainingDurationInHours);
        remainingDurationInHours = remainingDurationInHours % WORKING_HOURS_IN_ONE_WEEK;
        dueDateTime = calculateDays(dueDateTime, remainingDurationInHours);
        remainingDurationInHours = remainingDurationInHours % WORKING_HOURS_IN_ONE_DAY;
        dueDateTime = calculateHours(dueDateTime, remainingDurationInHours);
        return dueDateTime;
    }

    private LocalDateTime calculateWeeks(LocalDateTime dueDateTime, int remainingDurationInHours) {
        int plusWeeks = remainingDurationInHours / WORKING_HOURS_IN_ONE_WEEK;
        dueDateTime = dueDateTime.plusWeeks(plusWeeks);
        return dueDateTime;
    }

    private LocalDateTime calculateDays(LocalDateTime dueDateTime, int remainingDurationInHours) {
        int plusDays = remainingDurationInHours / WORKING_HOURS_IN_ONE_DAY;
        for (int i = 0; i < plusDays; i++) {
            dueDateTime = dueDateTime.plusDays(1);
            if (isOutsideWorkingTime(dueDateTime)) {
                dueDateTime = dueDateTime.plusDays(WEEKEND_DAYS);
            }
        }
        return dueDateTime;
    }

    private LocalDateTime calculateHours(LocalDateTime dueDateTime, int remainingDurationInHours) {
        for (int i = 0; i < remainingDurationInHours; i++) {
            dueDateTime = dueDateTime.plusHours(1);
            if (isOutsideWorkingTime(dueDateTime)) {
                if (!isOutsideWorkingTime(dueDateTime.plusHours(NON_WORKING_HOURS_IN_ONE_DAY))) {
                    dueDateTime = dueDateTime.plusHours(NON_WORKING_HOURS_IN_ONE_DAY);
                } else {
                    dueDateTime = dueDateTime.plusDays(WEEKEND_DAYS);
                }
            }
        }
        return dueDateTime;
    }

    private boolean isOutsideWorkingTime(LocalDateTime submitTime) {
        return submitTime.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                submitTime.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY ||
                submitTime.toLocalTime().isBefore(WORKDAY_START) ||
                submitTime.toLocalTime().isAfter(WORKDAY_END);
    }
}
