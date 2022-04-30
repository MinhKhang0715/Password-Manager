package org.example.passwordmanager.GroupPasswords;

import java.io.IOException;
import java.util.ArrayList;

public class GroupService implements GroupDAO{
    GroupRepo groupRepo;

    public GroupService() throws IOException {
        groupRepo = new GroupRepo();
    }

    @Override
    public void createGroup(GroupDTO groupDTO) {
        groupRepo.createGroup(groupDTO);
    }

    @Override
    public ArrayList<GroupDTO> readAllGroup() {
        return groupRepo.readAllGroup();
    }

    @Override
    public void updateGroup(GroupDTO oldGroup, GroupDTO newGroup) {
        groupRepo.updateGroup(oldGroup, newGroup);
    }

    @Override
    public void deleteGroup(GroupDTO groupDTO) {
        groupRepo.deleteGroup(groupDTO);
    }

    @Override
    public GroupDTO searchGroupByName(String name) {
        return groupRepo.searchGroupByName(name);
    }
}
