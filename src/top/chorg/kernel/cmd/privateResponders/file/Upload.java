package top.chorg.kernel.cmd.privateResponders.file;

import com.google.gson.JsonParseException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.file.UploadRequest;
import top.chorg.kernel.communication.api.file.UploadRequestReturn;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * args:
 * fullPath, level
 */
public class Upload extends CmdResponder {

    public Upload(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            String finePath = nextArg();
            finePath = finePath.replace("\n", "");
            if (!Global.masterSender.send(new Message(
                    "uploadFile",
                    Global.gson.toJson(new UploadRequest(
                            finePath,
                            Objects.requireNonNull(nextArg(int.class)),
                            Objects.requireNonNull(nextArg(int.class))
                    ))
            ))) {
                Sys.err("Upload File", "Unable to send request.");
                Global.guiAdapter.makeEvent("uploadFile", "Unable to send request");
            }
        } else {
            Sys.err("Upload File", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("uploadFile", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String arg = nextArg();
        UploadRequestReturn ret;
        try {
            ret = Global.gson.fromJson(arg, UploadRequestReturn.class);
        } catch (JsonParseException e) {
            Sys.errF("Upload File", "Error: %s", arg);
            Global.guiAdapter.makeEvent("uploadFile", arg);
            return 1;
        }
        int con = HostManager.connect(
                String.format("fileUploader-%d", ret.id),
                Global.conf.File_Server_Host,
                Global.conf.File_Server_Port
        );
        if (con != 0) {
            Sys.errF("File Upload", "Error while connecting to file uploader (value %d).", con);
            Global.guiAdapter.makeEvent("uploadFile", "Error while connecting to uploader: value " + con);
            return con;
        }
        PrintWriter pw = HostManager.getPrintWriter(String.format("fileUploader-%d", ret.id));
        if (pw == null) {
            Sys.err("File Upload", "Error while getting uploader PrintWriter.");
            Global.guiAdapter.makeEvent("uploadFile", "Error while getting uploader PrintWriter");
            return 122;
        }
        String[] fileNameTemp;
        fileNameTemp = ret.path.split(File.separator);
        pw.println("upload");
        pw.println(fileNameTemp[fileNameTemp.length - 1]);
        pw.println(ret.id);
        pw.flush();
        BufferedReader br = HostManager.getBufferedReader(String.format("fileUploader-%d", ret.id));
        if (br == null) {
            Sys.err("File Upload", "Error while getting uploader BufferedReader.");
            Global.guiAdapter.makeEvent("uploadFile", "Error while getting uploader BufferedReader");
            return 122;
        }
        String res;
        try {
            res = br.readLine();
            if (!res.equals("READY")) {
                throw new IOException(res);
            }
        } catch (IOException e) {
            Sys.errF("File Upload", "Error while communicating with file host (%s).", e.getMessage());
            Global.guiAdapter.makeEvent("uploadFile",
                    "Error while communicating with file host: value " + e.getMessage());
            return 123;
        }
        Socket socket = HostManager.getSocket(String.format("fileUploader-%d", ret.id));
        if (socket == null) {
            Sys.err("File Upload", "Error while getting uploader socket.");
            Global.guiAdapter.makeEvent("uploadFile", "Error while getting uploader socket");
            return 122;
        }
        try {
            var fin = new FileInputStream(ret.path);
            var bytes = new byte[1024];
            var os = socket.getOutputStream();
            for (int length; (length = fin.read(bytes)) != -1; ) {
                os.write(bytes, 0, length);
            }
            socket.close();
            fin.close();
        } catch (Exception e) {
            Sys.errF("File Upload", "Error while sending file content (%s).", e.getMessage());
            Global.guiAdapter.makeEvent("uploadFile",
                    "Error while sending file content: value " + e.getMessage());
            return 124;
        }
        HostManager.disconnect(String.format("fileUploader-%d", ret.id));
        Sys.infoF("File Upload", "File (%s) upload success.", ret.path);
        Global.guiAdapter.makeEvent("uploadFile", String.valueOf(ret.id));
        // TODO: GUI Actions
        return 0;
    }
}
