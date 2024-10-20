import java.time.LocalDate;
import java.util.List;

public class HighPriorityTask extends BaseTask {
    private List<TeamMember> teamMembersToNotify;

    public HighPriorityTask(String title, String description, LocalDate dueDate, List<TeamMember> teamMembersToNotify) {
        super(title, description, dueDate, Priority.HIGH);
        this.teamMembersToNotify = teamMembersToNotify;
    }

    @Override
    public void execute() {
        super.execute();

        if (getStatus() == Status.IN_PROGRESS) {
            notifyTeamMembers();
        }
    }

    private void notifyTeamMembers() {
        for (TeamMember teamMember : teamMembersToNotify) {
            System.out.println("Notifying " + teamMember.getName() + " (" + teamMember.getEmail() + ") about high-priority task: '" + title + "'");
        }
    }
}