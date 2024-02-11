package client.file;

import common.configuration.Conf;
import common.file.FilePathHelper;
import common.logger.Logg;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ClientSendFile implements Runnable {

    private String filePath;
    private String roomName;

    private void sendFile() {
        Logg.info("Sending file...");
        File file = new File(filePath);
        try (InputStream fileInputStream = new FileInputStream(file);
             Socket socket = new Socket(Conf.HOST, Conf.FILE_DOWNLOAD_SERVER_PORT);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            outputStream.writeUTF(FilePathHelper.createFullPath(roomName, FilePathHelper.getFileName(filePath)));
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream.write(fileBytes, 0, fileBytes.length);
        } catch (FileNotFoundException e) {
            Logg.error("File not found.");
        } catch (IOException e) {
            Logg.error("Error while sending file.");
        } finally {
            Logg.info("Sending finished.");
        }
    }

    @Override
    public void run() {
        sendFile();
    }

}
