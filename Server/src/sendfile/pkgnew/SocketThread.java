/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendfile.pkgnew;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author Administrator
 */

 
public class SocketThread implements Runnable{
    Socket socket;
    MainForm main;
    DataInputStream dis;
    StringTokenizer st;
    //String client, filesharing_userbame;
    
    
    SocketThread(Socket socket, MainForm main){
       this.main = main;
       this.socket = socket;
       
       try {
           dis = new DataInputStream(socket.getInputStream());
       }catch (IOException e){
           main.appendMessage("[SocketThreadIOException]: " +e.getMessage());
       }
    
    }
    
      
      private void createConncetion(String receiver, String sender, String filename){
          
          try{
              main.appendMessage("[createConnection]:creating file sharing connection");
              Socket s = main.getClientList(receiver);
              if(s!=null){
                  main.appendMessage("[createConnection]: Socket OK");
                  DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                  main.appendMessage("[createConnection]: DataOutputStream OK");
                  String format = "CMD_FILE_XD " +sender +" " +receiver +""+filename;
                  dosS.writeUTF(format);
                  main.appendMessage("[createConnection]: "+ format);
              }else{
                  main.appendMessage("[createConnection]: Client not found '" + receiver + "'");
                  DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                  dos.writeUTF("CMD_SENDFILEERROR" + "Client '" + receiver + "' Not found in the list, make sure the user is online.!");
              
              }
          
          } catch (IOException e){
              main.appendMessage("[createConnection]: "+ e.getLocalizedMessage());
          }
      
      
      }

        @Override
        public void run() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
}
