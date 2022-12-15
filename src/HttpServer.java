package src;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    public static void main(String[] args) {

        Integer port = 3000;
        Path docRoot = Paths.get("./static");
        String fileName = "./myserver.jar";
        
        for (int i = 0; i < args.length; i++) {
            if (args.length < 2) {
                break;
            }
            switch(i) {

                case 0:
                    fileName = args[0];
                    break;
                case 2:
                    if (args[1].equals("--port"))
                        port = Integer.parseInt(args[2]);
                    else if (args[1].equals("--docRoot")) 
                        docRoot = Paths.get(args[2]);
                    break;
                case 4:
                    docRoot = Paths.get(args[4]);
                    break;
            }
        }
        
        try {

            
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening on port " + port + "\ndocRoot: " + docRoot + "\nfileName: " + fileName);

            try {
                File file = new File(docRoot.toString());
                file.createNewFile();
            } catch (IOException e) {
                System.exit(1);
            }
        

            Socket sc = server.accept();
            HttpClientConnection.readWrite(sc, docRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
