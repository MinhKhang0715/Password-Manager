package org.example.passwordmanager.GroupPasswords;

import org.example.passwordmanager.Database.DBConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class GroupRepo implements GroupDAO {
    private final DBConfig dbConfig;

    public GroupRepo() throws IOException {
        this.dbConfig = DBConfig.getInstance();
    }

    @Override
    public void createGroup(GroupDTO groupDTO) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject groupPasswords = new JSONObject()
                    .put("array-passwords", new JSONArray())
                    .put("group-pass", groupDTO.getGroupPassword());
            passwordsObject.put(groupDTO.getGroupName(), groupPasswords);
            FileOutputStream outputStream = dbConfig.getOutputStream();
            outputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Group repo at CREATE GROUP method ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<GroupDTO> readAllGroup() {
        ArrayList<GroupDTO> groupDTOS = new ArrayList<>();
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            Iterator<String> keys = passwordsObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals("Master")) {
                    GroupDTO groupDTO = new GroupDTO()
                            .setGroupName(key);
                    JSONObject jsonObject = passwordsObject.getJSONObject(key);
                    groupDTO = groupDTO.setGroupPassword(jsonObject.getString("group-pass"));
                    groupDTOS.add(groupDTO);
                }
            }
            return groupDTOS;
        } catch (IOException e) {
            System.out.println("Group repo at READ ALL GROUP method ERROR");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateGroup(GroupDTO oldGroup, GroupDTO newGroup) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            if (oldGroup.getGroupName().equals(newGroup.getGroupName())) {
                JSONObject password = passwordsObject.getJSONObject(oldGroup.getGroupName());
                password.put("group-pass", newGroup.getGroupPassword());
                FileOutputStream outputStream = dbConfig.getOutputStream();
                outputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
            }
            else {
                Iterator<String> keys = passwordsObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals(oldGroup.getGroupName())) {
                        passwordsObject.put(newGroup.getGroupName(), passwordsObject.getJSONObject(key));
                        JSONObject jsonObject = passwordsObject.getJSONObject(newGroup.getGroupName());
                        jsonObject.put("group-pass", newGroup.getGroupPassword());
                        passwordsObject.remove(key);
                        break;
                    }
                }
                FileOutputStream outputStream = dbConfig.getOutputStream();
                outputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.out.println("Group repo at UPDATE GROUP method ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGroup(GroupDTO groupDTO) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            passwordsObject.remove(groupDTO.getGroupName());
            FileOutputStream outputStream = dbConfig.getOutputStream();
            outputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Group repo at DELETE GROUP method ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public GroupDTO searchGroupByName(String name) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            Iterator<String> keys = passwordsObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.equals(name)) {
                    GroupDTO groupDTO = new GroupDTO().setGroupName(key);
                    JSONObject jsonObject = passwordsObject.getJSONObject(key);
                    groupDTO = groupDTO.setGroupPassword(jsonObject.getString("group-pass"));
                    return groupDTO;
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Group repo at SEARCH GROUP BY NAME method ERROR");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isGroupExist(String groupName) {
        return searchGroupByName(groupName) != null;
    }

    @Override
    public void createNoGroup() {
        GroupDTO groupDTO = new GroupDTO().setGroupName("No group").setGroupPassword("");
        this.createGroup(groupDTO);
    }
}
