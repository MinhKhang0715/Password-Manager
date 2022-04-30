package org.example.passwordmanager.GroupPasswords;

public class GroupDTO {

    private String groupName;
    private String groupPassword;

    public String getGroupName() {
        return groupName;
    }

    public GroupDTO setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public GroupDTO setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
        return this;
    }
}
