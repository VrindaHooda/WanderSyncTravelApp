public interface Task {
    String getTitle();
    String getDescription();
    LocalDate getDueDate();
    Status getStatus();
    Priority getPriority();
    void execute();
}