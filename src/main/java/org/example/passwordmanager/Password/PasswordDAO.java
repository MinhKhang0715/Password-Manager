package org.example.passwordmanager.Password;

import java.util.ArrayList;

public interface PasswordDAO {
    void createMasterPassword(PasswordDTO passwordDTO);
    PasswordDTO getMasterPassword();
    void create(PasswordDTO passwordDTO, String groupName);
    ArrayList<PasswordDTO> readAllFromGroup(String groupName);
    void update(PasswordDTO passwordDTO, String groupName);
    void delete(PasswordDTO passwordDTO, String groupName);
    PasswordDTO searchPasswordByIdFromGroup(int id, String groupName);
    ArrayList<PasswordDTO> searchPasswordByNameFromGroup(String name,String groupName);
}
