package top.chorg.kernel.cmd.privateResponders.file;

import com.google.gson.JsonParseException;
import top.chorg.kernel.cmd.CmdResponder;
import top.chorg.kernel.communication.HostManager;
import top.chorg.kernel.communication.Message;
import top.chorg.kernel.communication.api.file.DownloadRequestReturn;
import top.chorg.kernel.communication.auth.AuthManager;
import top.chorg.system.Global;
import top.chorg.system.Sys;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Download extends CmdResponder {

    public Download(String... args) {
        super(args);
    }

    @Override
    public int response() throws IndexOutOfBoundsException {
        if (AuthManager.isOnline()) {
            if (!Global.masterSender.send(new Message(
                    "downloadFile",
                    nextArg()       // file id
            ))) {
                Sys.err("Download File", "Unable to send request.");
                Global .guiAdapter.makeEvent("downloadFile", "Unable to send request");
            }
        } else {
            Sys.err("Download File", "User is not online, please login first.");
            Global.guiAdapter.makeEvent("downloadFile", "User is not online");
            return 1;
        }
        return 0;
    }

    @Override
    public int onReceiveNetMsg() {
        String arg = nextArg();
        DownloadRequestReturn ret;
        try {
            ret = Global.gson.fromJson(arg, DownloadRequestReturn.class);
        } catch (JsonParseException e) {
            Sys.errF("Download File", "Error: %s", arg);
            Global.guiAdapter.makeEvent("downloadFile", arg);
            return 1;
        }
        int con = HostManager.connect(
                String.format("fileDownloader-%d", ret.id),
                Global.conf.File_Server_Host,
                Global.conf.File_Server_Port
        );
        if (con != 0) {
            Sys.errF("File Download", "Error while connecting to file Downloader (value %d).", con);
            Global.guiAdapter.makeEvent("downloadFile", "Error while connecting to downloader: value " + con);
            return con;
        }
        PrintWriter pw = HostManager.getPrintWriter(String.format("fileDownloader-%d", ret.id));
        if (pw == null) {
            Sys.err("File Download", "Error while getting downloader PrintWriter.");
            Global.guiAdapter.makeEvent("downloadFile", "Error while getting uploader PrintWriter");
            return 122;
        }
        pw.println("download");
        pw.println(AuthManager.getUser().getId());
        pw.println(ret.id);
        pw.println(ret.token);
        pw.flush();
        BufferedReader br = HostManager.getBufferedReader(String.format("fileDownloader-%d", ret.id));
        if (br == null) {
            Sys.err("File Download", "Error while getting downloader BufferedReader.");
            Global.guiAdapter.makeEvent("downloadFile", "Error while getting downloader BufferedReader");
            return 122;
        }
        String res;
        String name;
        try {
            res = br.readLine();
            if (!res.equals("READY")) {
                throw new IOException(res);
            }
            name = br.readLine();
        } catch (IOException e) {
            Sys.errF("File Download", "Error while communicating with file host (%s).", e.getMessage());
            Global.guiAdapter.makeEvent("downloadFile",
                    "Error while communicating with file host: value " + e.getMessage());
            return 123;
        }
        Socket socket = HostManager.getSocket(String.format("fileDownloader-%d", ret.id));
        if (socket == null) {
            Sys.err("File Download", "Error while getting downloader socket.");
            Global.guiAdapter.makeEvent("downloadFile", "Error while getting downloader socket");
            return 122;
        }
        try {
            var fo = new FileOutputStream(String.format("downloads/%s", name));
            var bytes = new byte[1024];
            for (int length; (length = socket.getInputStream().read(bytes)) != -1; ) {
                fo.write(bytes, 0, length);
            }
            fo.close();
            socket.close();
        } catch (Exception e) {
            Sys.errF("File Download", "Error while downloading file content (%s).", e.getMessage());
            Global.guiAdapter.makeEvent("downloadFile",
                    "Error while downloading file content: value " + e.getMessage());
            return 124;
        }
        HostManager.disconnect(String.format("fileDownloader-%d", ret.id));
        Sys.infoF("File Download", "File (%s) download success.", name);
        Global.guiAdapter.makeEvent("downloadFile", "OK");
        // TODO: GUI Actions
        return 0;
    }
}
