import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<BaseTask> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public void addTask(BaseTask task) {
        tasks.add(task);
    }

    public void removeTask(BaseTask task) {
        tasks.remove(task);
    }

    public void addTeamMember(TeamMember member) {
        if (!teamMembers.contains(member)) {
            teamMembers.add(member);
        }
    }

    public void removeTeamMember(TeamMember member) {
        teamMembers.remove(member);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<BaseTask> getTasks() {
        return tasks;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void executeAllTasks() {
        for (BaseTask task : tasks) {
            task.execute();
        }
    }
}