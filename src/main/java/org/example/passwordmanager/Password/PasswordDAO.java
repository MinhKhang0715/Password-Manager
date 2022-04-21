package org.example.passwordmanager.Password;

import java.util.ArrayList;

public interface PasswordDAO {
    void update(PasswordDTO passwordDTO);
    void create(PasswordDTO passwordDTO);
    ArrayList<PasswordDTO> readAll();
    void delete(PasswordDTO passwordDTO);
    ArrayList<PasswordDTO> searchPasswordByName(String name);
    PasswordDTO searchPasswordById(int id);
}
