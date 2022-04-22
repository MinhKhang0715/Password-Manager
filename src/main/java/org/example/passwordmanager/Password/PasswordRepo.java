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
    public void create(PasswordDTO passwordDTO) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(DBConfig.pathInLinux));
            JSONObject jsonObject = new JSONObject()
                    .put("name", passwordDTO.getName())
                    .put("value", passwordDTO.getValue())
                    .put("date-created", passwordDTO.getDateCreated().toString())
                    .put("description", passwordDTO.getDescription());
            if (bufferedReader.readLine() == null) {
                passwordDTO.setId(0);
                jsonObject.put("id", passwordDTO.getId());
                FileOutputStream fileOutputStream = dbConfig.getOutputStream();
                System.out.println("PasswordRepo: DB File empty, creating new master password");
                JSONArray jsonArray = new JSONArray().put(jsonObject);
                fileOutputStream.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
            } else {
                FileInputStream fileInputStream = dbConfig.getInputStream();
                JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
                JSONObject latestJson = jsonArray.getJSONObject(jsonArray.length() - 1);
                int latestId = latestJson.getInt("id");
                jsonObject.put("id", ++latestId);
                jsonArray.put(jsonObject);
                FileOutputStream fileOutputStream = dbConfig.getOutputStream();
                fileOutputStream.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.out.println("Password repo at CREATE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<PasswordDTO> readAll() {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
            ArrayList<PasswordDTO> passwordDTOList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);
            int arrSize = jsonArray.length();
            for (int i = 0; i < arrSize; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PasswordDTO passwordDTO = new PasswordDTO()
                        .setName(jsonObject.getString("name"))
                        .setValue(jsonObject.getString("value"))
                        .setDescription(jsonObject.getString("description"))
                        .setDateCreated(LocalDate.parse(jsonObject.getString("date-created"), formatter))
                        .setId(jsonObject.getInt("id"));
                passwordDTOList.add(passwordDTO);
            }
            return passwordDTOList;

        } catch (IOException e) {
            System.out.println("Password repo at READ ALL method: ERROR opening file");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(PasswordDTO passwordDTO) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
            int arrSize = jsonArray.length();
            for (int i = 1; i < arrSize; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.getInt("id") == passwordDTO.getId()) {
                    jsonObject.put("value", passwordDTO.getValue())
                            .put("description", passwordDTO.getDescription())
                            .put("name", passwordDTO.getName())
                            .put("date-created", passwordDTO.getDateCreated().toString());
                    break;
                }
            }
            FileOutputStream fileOutputStream = dbConfig.getOutputStream();
            fileOutputStream.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Password repo at UPDATE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(PasswordDTO passwordDTO) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("id") == passwordDTO.getId()) {
                    jsonArray.remove(i);
                    break;
                }
            }
            FileOutputStream fileOutputStream = dbConfig.getOutputStream();
            fileOutputStream.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Password repo at DELETE method: ERROR opening file");
            e.printStackTrace();
        }
    }

    public PasswordDTO searchPasswordById(int id) {
        try {
            FileInputStream fileInputStream = dbConfig.getInputStream();
            JSONArray jsonArray = new JSONArray(new String(fileInputStream.readAllBytes()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("id") == id) {
                    return new PasswordDTO()
                            .setDateCreated(LocalDate.parse(jsonObject.getString("date-created"), formatter))
                            .setId(id)
                            .setName(jsonObject.getString("name"))
                            .setDescription(jsonObject.getString("description"))
                            .setValue(jsonObject.getString("value"));
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Password repo at searchPasswordById(int id) method: ERROR: Cannot open or write to DB file");
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<PasswordDTO> searchPasswordByName(String name) {
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
