package hu.tomlincoln.emarsyshw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class DueDateCalculatorServiceTest {

    private static DueDateCalculatorService underTest;

    @BeforeAll
    static void setUp() {
        underTest = new DueDateCalculatorService();
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenSubmitTimeIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(null, Duration.ofHours(4)));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenTurnaroundTimeIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(LocalDateTime.of(2019,10,3,0,0), null));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenAllParametersAreNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(null, null));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenSubmitTimeIsSaturday() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(LocalDateTime.of(2019,10,5,0,0), Duration.ofHours(4)));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenSubmitTimeIsSunday() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(LocalDateTime.of(2019,10,6,0,0), Duration.ofHours(4)));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenSubmitTimeIsBefore9AM() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(LocalDateTime.of(2019,10,3,8,59), Duration.ofHours(4)));
    }

    @Test
    void calculateDueDateThrowsIllegalArgumentExceptionWhenSubmitTimeIsAfter5PM() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.calculateDueDate(LocalDateTime.of(2019,10,3,17,1), Duration.ofHours(4)));
    }

    @Test
    void calculateDueDateReturnsPlusOneWeekWhenTurnaroundTimeIs40Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,3,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(40));
        Assertions.assertEquals(submitTime.plusWeeks(1), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlusTwoWeeksWhenTurnaroundTimeIs80Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,3,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(80));
        Assertions.assertEquals(submitTime.plusWeeks(2), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlusOneDayWhenSubmitTimeIsMondayAndTurnaroundTimeIs8Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,7,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(8));
        Assertions.assertEquals(submitTime.plusDays(1), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlusOneDayButSkipsWeekendWhenSubmitTimeIsFridayAndTurnaroundTimeIs8Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,4,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(8));
        Assertions.assertEquals(submitTime.plusDays(1).plusDays(2), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlusTwoDaysWhenSubmitTimeIsMondayAndTurnaroundTimeIs16Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,7,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(16));
        Assertions.assertEquals(submitTime.plusDays(2), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlus2HoursButSkipsWeekendWhenSubmitTimeIsFridayAndTurnaroundTimeIs16Hours() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,4,9,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(16));
        Assertions.assertEquals(submitTime.plusDays(2).plusDays(2), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlus2HoursWhenAndTurnaroundTimeIsOnSameDay() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,3,14,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(2));
        Assertions.assertEquals(submitTime.plusHours(2), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlus2HoursButSkipsNightWhenAndTurnaroundTimeIsOnNextDay() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,3,14,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(4));
        Assertions.assertEquals(submitTime.plusHours(4).plusHours(16), dueTime);
    }

    @Test
    void calculateDueDateReturnsPlus2HoursButSkipsNightAndWeekendWhenAndTurnaroundTimeIsOnNextNextWeek() {
        LocalDateTime submitTime = LocalDateTime.of(2019,10,3,14,0);
        LocalDateTime dueTime = underTest.calculateDueDate(submitTime, Duration.ofHours(12));
        Assertions.assertEquals(submitTime.plusHours(12).plusHours(16).plusDays(2), dueTime);
    }





}