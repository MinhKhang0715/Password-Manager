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

    public PasswordService(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        this.passwordRepo = new PasswordRepo();
        aesCrypto = AESCrypto.getInstance(bytes);
    }

    public AESCrypto getAesCrypto() {
        return aesCrypto;
    }

    public void copyToClipboard(PasswordDTO passwordDTO) {
        String pass = passwordDTO.getValue();
        String decrypted = aesCrypto.decrypt(pass);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(decrypted), null);
    }

    public long datesPass(PasswordDTO passwordDTO) {
        LocalDate localDate = LocalDate.now();
        return Duration.between(
                passwordDTO.getDateCreated().atStartOfDay(),
                localDate.atStartOfDay()
                ).toDays();
    }

    @Override
    public void createMasterPassword(PasswordDTO passwordDTO) {
        passwordRepo.createMasterPassword(passwordDTO);
    }

    @Override
    public PasswordDTO getMasterPassword() {
        return passwordRepo.getMasterPassword();
    }

    @Override
    public void create(PasswordDTO passwordDTO, String groupName) {
        passwordRepo.create(passwordDTO.setValue(aesCrypto.encrypt(passwordDTO.getValue())), groupName);
    }

    @Override
    public ArrayList<PasswordDTO> readAllFromGroup(String groupName) {
        return this.passwordRepo.readAllFromGroup(groupName);
    }

    @Override
    public void update(PasswordDTO passwordDTO, String groupName) {
        passwordRepo.update(passwordDTO.setValue(aesCrypto.encrypt(passwordDTO.getValue())), groupName);
    }

    @Override
    public void delete(PasswordDTO passwordDTO, String groupName) {
        this.passwordRepo.delete(passwordDTO, groupName);
    }

    @Override
    public ArrayList<PasswordDTO> searchPasswordByNameFromGroup(String name, String groupName) {
        return passwordRepo.searchPasswordByNameFromGroup(name, groupName);
    }

    @Override
    public PasswordDTO searchPasswordByIdFromGroup(int id, String groupName) {
        return passwordRepo.searchPasswordByIdFromGroup(id, groupName);
    }
}
