package client.file;

import common.configuration.Conf;
import common.file.FilePathHelper;
import common.logger.Logg;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

@AllArgsConstructor
public class ClientDownloadFile implements Runnable {

    private String fileName;
    private String roomName;

    private void downloadFile() throws IOException {
        FileOutputStream fileOutputStream = null;
        try (Socket socket = new Socket(Conf.HOST, Conf.FILE_SENDER_SERVER_PORT);
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        ){
            Logg.info("Downloading file...");
            String fileFullPath = FilePathHelper.createFullPath(roomName, fileName);
            dataOutputStream.writeUTF(fileFullPath);
            boolean fileExists = dataInputStream.readBoolean();
            if (fileExists) {
                fileOutputStream = new FileOutputStream(fileName);
                byte[] buffer = new byte[2048];
                int read = 0;
                while ((read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
            } else {
                Logg.error("Download failed. The file doesn't exist.");
            }
        } catch (IOException e) {
            Logg.error("Error while downloading file. Try again later.");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            Logg.info("Download finished.");
        }
    }

    @Override
    public void run() {
        try {
            downloadFile();
        } catch (IOException e) {
            Logg.error(e.getMessage());
        }
    }

}
