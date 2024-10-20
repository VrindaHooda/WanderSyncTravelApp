import java.time.LocalDate;

public abstract class BaseTask implements Task {
    protected String title;
    protected String description;
    protected LocalDate dueDate;
    protected Status status;
    protected Priority priority;

    public BaseTask(String title, String description, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.PENDING;
        this.priority = priority;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void markAsCompleted() {
        this.status = Status.COMPLETED;
        System.out.println("Task '" + title + "' is completed.");
    }

    public void markAsInProgress() {
        this.status = Status.IN_PROGRESS;
        System.out.println("Task '" + title + "' is now in progress.");
    }

    @Override
    public void execute() {
        if (this.status == Status.PENDING) {
            markAsInProgress();
        } else if (this.status == Status.IN_PROGRESS) {
            markAsCompleted();
        } else {
            System.out.println("Task '" + title + "' is already completed.");
        }
    }
}
