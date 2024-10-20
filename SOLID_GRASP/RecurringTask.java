import java.time.LocalDate;

public class RecurringTask extends BaseTask {
    private RecurrenceInterval interval;
    private LocalDate nextOccurrenceDate;

    public RecurringTask(String title, String description, LocalDate dueDate, Priority priority, RecurrenceInterval interval) {
        super(title, description, dueDate, priority);
        this.interval = interval;
        this.nextOccurrenceDate = calculateNextOccurrence(dueDate);
    }

    public RecurrenceInterval getInterval() {
        return interval;
    }

    private LocalDate calculateNextOccurrence(LocalDate currentDueDate) {
        switch (interval) {
            case DAILY:
                return currentDueDate.plusDays(1);
            case WEEKLY:
                return currentDueDate.plusWeeks(1);
            case MONTHLY:
                return currentDueDate.plusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid recurrence interval");
        }
    }

    @Override
    public void execute() {
        super.execute();

        if (getStatus() == Status.COMPLETED) {
            dueDate = nextOccurrenceDate;
            nextOccurrenceDate = calculateNextOccurrence(dueDate);
            status = Status.PENDING;
            System.out.println("Task '" + title + "' is recurring. Next occurrence on: " + dueDate);
        }
    }
}