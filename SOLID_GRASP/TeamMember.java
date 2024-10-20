public class TeamMember {
    private String name;
    private String email;
    private Role role;

    public TeamMember(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void joinProject(Project project) {
        project.addTeamMember(this);
    }

    public void leaveProject(Project project) {
        project.removeTeamMember(this);
    }
}