package org.example.passwordmanager.Password;

import org.example.passwordmanager.Crypto.AESCrypto;

import javax.crypto.NoSuchPaddingException;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.*;
public class PasswordService implements PasswordDAO {
    AESCrypto aesCrypto;
    PasswordRepo passwordRepo;

    public PasswordService() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        this.passwordRepo = new PasswordRepo();
        aesCrypto = AESCrypto.getInstance();
    }

    public void copyToClipboard(PasswordDTO passwordDTO) {
        String pass = passwordDTO.getValue();
        System.out.println(pass);
        String decrypted = aesCrypto.decrypt(pass);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(decrypted), null);
        System.out.println(decrypted);
    }

    public long datesPass(PasswordDTO passwordDTO) {
        LocalDate localDate = LocalDate.now();
        return Duration.between(
                localDate.atStartOfDay(),
                passwordDTO.getDateCreated().atStartOfDay()
                ).toDays();
    }

    @Override
    public void create(PasswordDTO passwordDTO) {
        if (!passwordDTO.getName().equals("master")) {
            String plainText = passwordDTO.getValue();
            String cipher = aesCrypto.encrypt(plainText);
            passwordDTO.setValue(cipher);
        }
        passwordRepo.create(passwordDTO);
    }

    @Override
    public ArrayList<PasswordDTO> readAll() {
        return this.passwordRepo.readAll();
    }

    @Override
    public void update(PasswordDTO passwordDTO) {
        passwordRepo.update(passwordDTO.setValue(aesCrypto.encrypt(passwordDTO.getValue())));
    }

    @Override
    public void delete(PasswordDTO passwordDTO) {
        this.passwordRepo.delete(passwordDTO);
    }

    @Override
    public ArrayList<PasswordDTO> searchPasswordByName(String name) {
        return passwordRepo.searchPasswordByName(name);
    }

    @Override
    public PasswordDTO searchPasswordById(int id) {
        return passwordRepo.searchPasswordById(id);
    }
}
