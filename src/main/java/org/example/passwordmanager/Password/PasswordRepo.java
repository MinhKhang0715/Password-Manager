package org.example.passwordmanager.Password;

import org.example.passwordmanager.Database.DBConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class PasswordRepo implements PasswordDAO {
    private final DBConfig dbConfig;

    public PasswordRepo() throws IOException {
        dbConfig = DBConfig.getInstance();
    }

    @Override
    public void createMasterPassword(PasswordDTO passwordDTO) {
        try {
            String json = "{\"passwords\":{}}";
            JSONObject original = new JSONObject(json);
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject masterPassword = new JSONObject()
                    .put("id", passwordDTO.getId())
                    .put("service-name", passwordDTO.getName())
                    .put("description", passwordDTO.getDescription())
                    .put("value", passwordDTO.getValue())
                    .put("date-created", passwordDTO.getDateCreated().toString());
            passwordsObject.put("Master", masterPassword);
            FileOutputStream fileOutputStream = dbConfig.getOutputStream();
            fileOutputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Password repo at CREATE MASTER PASSWORD method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public PasswordDTO getMasterPassword() {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject masterPassword = passwordsObject.getJSONObject("Master");
            return new PasswordDTO()
                    .setValue(masterPassword.getString("value"));
        } catch (IOException e) {
            System.out.println("Password repo at CREATE MASTER PASSWORD method: ERROR opening file");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(PasswordDTO passwordDTO, String groupName) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject passwordGroup = passwordsObject.getJSONObject(groupName);
            JSONArray arrayOfPasswords = passwordGroup.getJSONArray("array-passwords");
            JSONObject passwordObject = new JSONObject()
                    .put("name", passwordDTO.getName())
                    .put("value", passwordDTO.getValue())
                    .put("date-created", passwordDTO.getDateCreated().toString())
                    .put("description", passwordDTO.getDescription());
            if (arrayOfPasswords.length() == 0) {
                passwordDTO.setId(0);
                passwordObject.put("id", passwordDTO.getId());
                arrayOfPasswords.put(passwordObject);
                FileOutputStream fileOutputStream = dbConfig.getOutputStream();
                fileOutputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
            } else {
                JSONObject latestPassword = arrayOfPasswords.getJSONObject(arrayOfPasswords.length() - 1);
                int latestId = latestPassword.getInt("id");
                passwordObject.put("id", ++latestId);
                arrayOfPasswords.put(passwordObject);
                FileOutputStream fileOutputStream = dbConfig.getOutputStream();
                fileOutputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.out.println("Password repo at CREATE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<PasswordDTO> readAllFromGroup(String groupName) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject passwordGroup = passwordsObject.getJSONObject(groupName);
            JSONArray arrayOfPasswords = passwordGroup.getJSONArray("array-passwords");
            ArrayList<PasswordDTO> passwordDTOSList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);
            int arraySize = arrayOfPasswords.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject passwordObject = arrayOfPasswords.getJSONObject(i);
                PasswordDTO passwordDTO = new PasswordDTO()
                        .setId(passwordObject.getInt("id"))
                        .setName(passwordObject.getString("name"))
                        .setValue(passwordObject.getString("value"))
                        .setDescription(passwordObject.getString("description"))
                        .setDateCreated(LocalDate.parse(passwordObject.getString("date-created"), formatter));
                passwordDTOSList.add(passwordDTO);
            }
            return passwordDTOSList;
        } catch (IOException e) {
            System.out.println("Password repo at READ ALL method: ERROR opening file");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(PasswordDTO passwordDTO, String groupName) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject passwordGroup = passwordsObject.getJSONObject(groupName);
            JSONArray arrayOfPasswords = passwordGroup.getJSONArray("array-passwords");
            int arraySize = arrayOfPasswords.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject passwordObject = arrayOfPasswords.getJSONObject(i);
                if (passwordObject.getInt("id") == passwordDTO.getId()) {
                    passwordObject.put("value", passwordDTO.getValue())
                            .put("description", passwordDTO.getDescription())
                            .put("name", passwordDTO.getName())
                            .put("date-created", passwordDTO.getDateCreated().toString());
                    break;
                }
            }
            FileOutputStream fileOutputStream = dbConfig.getOutputStream();
            fileOutputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Password repo at UPDATE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PasswordDTO passwordDTO, String groupName) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject passwordGroup = passwordsObject.getJSONObject(groupName);
            JSONArray arrayOfPasswords = passwordGroup.getJSONArray("array-passwords");
            int arraySize = arrayOfPasswords.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject passwordObject = arrayOfPasswords.getJSONObject(i);
                if (passwordObject.getInt("id") == passwordDTO.getId()) {
                    arrayOfPasswords.remove(i);
                    break;
                }
            }
            FileOutputStream fileOutputStream = dbConfig.getOutputStream();
            fileOutputStream.write(original.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Password repo at DELETE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public PasswordDTO searchPasswordByIdFromGroup(int id, String groupName) {
        try {
//            System.out.println(groupName + "\n" + id);
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONObject original = new JSONObject(new String(fileInputStream.readAllBytes()));
            JSONObject passwordsObject = original.getJSONObject("passwords");
            JSONObject passwordGroup = passwordsObject.getJSONObject(groupName);
            JSONArray arrayOfPasswords = passwordGroup.getJSONArray("array-passwords");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
            int size = arrayOfPasswords.length();
            for (int i = 0; i < size; i++) {
                JSONObject passwordObject = arrayOfPasswords.getJSONObject(i);
                if (passwordObject.getInt("id") == id) {
                    return new PasswordDTO()
                            .setDateCreated(LocalDate.parse(passwordObject.getString("date-created"), formatter))
                            .setId(id)
                            .setName(passwordObject.getString("name"))
                            .setDescription(passwordObject.getString("description"))
                            .setValue(passwordObject.getString("value"));
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Password repo at searchPasswordById(int id) method: ERROR: Cannot open or write to DB file");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<PasswordDTO> searchPasswordByNameFromGroup(String name, String groupName) {
        try {
            ArrayList<PasswordDTO> result = new ArrayList<>();
            FileInputStream fileInputStream = dbConfig.getInputStream();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
            JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("name").equals(name)) {
                    PasswordDTO passwordDTO = new PasswordDTO()
                            .setDateCreated(LocalDate.parse(jsonObject.getString("date-created"), formatter))
                            .setId(jsonObject.getInt("id"))
                            .setName(jsonObject.getString("name"))
                            .setDescription(jsonObject.getString("description"));
                    result.add(passwordDTO);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
