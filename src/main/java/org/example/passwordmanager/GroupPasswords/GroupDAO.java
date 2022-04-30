package org.example.passwordmanager.GroupPasswords;

import java.util.ArrayList;

public interface GroupDAO {
    void createGroup(GroupDTO groupDTO);
    ArrayList<GroupDTO> readAllGroup();
    void updateGroup(GroupDTO oldGroup, GroupDTO newGroup);
    void deleteGroup(GroupDTO groupDTO);
    GroupDTO searchGroupByName(String name);
}
