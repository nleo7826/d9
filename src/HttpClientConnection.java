package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Path;

public class HttpClientConnection {

    public static void readWrite(Socket sc, Path docRoot) throws IOException {

        InputStream is = sc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        // DataInputStream ds = new DataInputStream(new BufferedInputStream(in));

        String line = br.readLine();
        String[] tokens = line.toLowerCase().split(" ");
        

        OutputStream os = sc.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        
        
        if (!tokens[0].equals("get")) {
            bw.write("HTTP/1.1 405 Method Not Allowed\r\n");
            bw.write("\r\n");
            bw.write(tokens[0] + " not supported\r\n");
        }

        if (!tokens[1].equals(null)) {
            
            String tmp = tokens[1];
            if (tokens[1].equals("/")) {
                tmp = "/index.html";
            }

            try {
                File file = new File(docRoot.toString() + tmp);
                file.createNewFile();
                FileInputStream fis = new FileInputStream(file);
                byte[] arr = new byte[(int)file.length()];
                fis.read(arr);
                fis.close();

                bw.write("HTTP/1.1 200 OK\r\n");
                if (tmp.contains(".png")) {
                    bw.write("Content-Type: image/png\r\n");
                }
                bw.write("\r\n");
                for(int i = 0; i< arr.length ; i++) {
                    bw.write(arr[i]);
                 }
            } catch (FileNotFoundException e) {
                bw.write("HTTP/1.1 404 Method Not Allowed\r\n");
                bw.write("r\n");
                bw.write(tokens[1] + " not found\r\n");
                sc.close();
            }
        }
        bw.flush();
        os.close();
    }
}