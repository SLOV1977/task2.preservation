package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        String pathSaveDir = "Games/savegames/";
        String nameZipFile = "zip.zip";
        String pathZipFile = pathSaveDir + nameZipFile;

        GameProgress[] gameProgress = {
                new GameProgress(35, 42, 5, 26.11),
                new GameProgress(89, 148, 45, 7652.25),
                new GameProgress(13, 5, 1, 45.41)
        };

        List<String> allSaves = new ArrayList<>();

        saveAllGameProgressObjects(gameProgress, pathSaveDir, allSaves);
        zipFIles(pathZipFile, allSaves);
        deleteSaveFiles(allSaves);
    }

    public static void saveAllGameProgressObjects(GameProgress[] gameProgress, String saveDirPath, List<String> allSaves) {
        for (int i = 0; i < gameProgress.length; i++) {
            String saveFileName = "save" + (i + 1) + ".dat";
            String saveFilePath = saveDirPath + saveFileName;
            saveGame(saveFilePath, gameProgress[i]);
            allSaves.add(saveFilePath);
            System.out.println("Создан файл сохранений \"" + saveFilePath + "\"");
        }
    }

    public static void saveGame(String saveFilePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(saveFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage(
            ));
        }
    }

    public static void zipFIles(String zipFilePath, List<String> allSaves) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (String save : allSaves) {
                File fileToZip = new File(save);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zipOut.write(buffer);
                    zipOut.closeEntry();
                    System.out.println("Файл сохранений\"" + save + "\" добавлен в архив " + "\"" + zipFilePath + "\"");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteSaveFiles(List<String> allSaves) {
        for (int i = 0; i < allSaves.size(); i++) {
            String saveFilePath = allSaves.get(i);
            System.out.println("Удалён файл \"" + saveFilePath + "\"");
            File fileDel = new File(saveFilePath);
            if (!fileDel.delete()) {
                System.out.println("Файл сохранений \"" + saveFilePath + "\" НЕ УДАЛЕН!!!");
            }
        }
    }
}