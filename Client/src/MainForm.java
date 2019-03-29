
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class MainForm extends javax.swing.JFrame {

    
    String username;
    String host;
    int port;
    Socket socket;
    DataOutputStream dos;
    public boolean attachmentOpen = false;
    boolean isConnected = false;
    private String mydownloadfolder = "D:\\";
    public MainForm() {
        initComponents();
      
         
    }

   
   
    public void initFrame(String username, String host, int port){
        this.username = username;
        this.host = host;
        this.port = port;
        setTitle("NWU Chat App - CLIENT [ " +  username + " ]");
        connect();
    }
    

    public void connect(){
        appendMessage(" Connecting ...", "Status", Color.GREEN, Color.GREEN);
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            // gửi username đang kết nối
            dos.writeUTF("CMD_JOIN "+ username);
            appendMessage("Connected"," Status", Color.GREEN, Color.GREEN);
            appendMessage(" Send a message now.!", "Status", Color.GREEN, Color.GREEN);
            
            // Start Client Thread
            
            new Thread(new ClientThread(socket, this)).start();
            jButton1.setEnabled(true);
            //already connected
            isConnected = true;
            
        }
        catch(IOException e) {
            isConnected = false;
            JOptionPane.showMessageDialog(this, "Could not connect to server, please try again later.!", "Connection failed",JOptionPane.ERROR_MESSAGE);
            appendMessage("[IOException]: "+ e.getMessage(), "Error", Color.RED, Color.RED);
        }
    }
    
    
     public boolean isConnected(){
        return this.isConnected;
    }
     
    
    public void appendMessage(String msg, String header, Color headerColor, Color contentColor){
        txtpane1.setEditable(true);
        getMsgHeader(header, headerColor);
        getMsgContent(msg, contentColor);
        txtpane1.setEditable(false);
    }
    
    public void appendMyMessage(String msg, String header){
        txtpane1.setEditable(true);
        getMsgHeader(header, Color.RED);
        getMsgContent(msg, Color.BLACK);
        txtpane1.setEditable(false);
    }
    
    
      public void getMsgHeader(String header, Color color){
        int len = txtpane1.getDocument().getLength();
        txtpane1.setCaretPosition(len);
        txtpane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Impact", 13), false);
        txtpane1.replaceSelection(header+":");
    }
    
    public void getMsgContent(String msg, Color color){
    
        int len = txtpane1.getDocument().getLength();
        txtpane1.setCaretPosition(len);
        txtpane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Arial", 12), false);
        txtpane1.replaceSelection(msg +"\n\n");
        
        
    
    }
     public void appendOnlineList(Vector list){
        sampleOnlineList(list); 
    }
    

    public void showOnLineList(Vector list){
        try {
            textpane2.setEditable(true);
            textpane2.setContentType("text/html");
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            sb.append("<html><table>");
            while(it.hasNext()){
                Object e = it.next();
                URL url = getImageFile();
                Icon icon = new ImageIcon(this.getClass().getResource("/images/online.png"));
                sb.append("<tr><td><b>></b></td><td>").append(e).append("</td></tr>");
                System.out.println("Online: "+ e);
            }
            sb.append("</table></body></html>");
            textpane2.removeAll();
            textpane2.setText(sb.toString());
            textpane2.setEditable(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*
      ************************************  Show list online  *********************************************
    */
    private void sampleOnlineList(Vector list){
        textpane2.setEditable(true);
        textpane2.removeAll();
        textpane2.setText("");
        Iterator i = list.iterator();
        while(i.hasNext()){
            Object e = i.next();
            /*  Hiển thị Username Online   */
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.white);
            
            Icon icon = new ImageIcon(this.getClass().getResource("/images/online.png"));
            JLabel label = new JLabel(icon);
            label.setText(" "+ e);
            panel.add(label);
            int len = textpane2.getDocument().getLength();
            textpane2.setCaretPosition(len);
            textpane2.insertComponent(panel);
            /*  Append Next Line   */
            sampleAppend();
        }
        textpane2.setEditable(false);
    }
    private void sampleAppend(){
        int len = textpane2.getDocument().getLength();
        textpane2.setCaretPosition(len);
        textpane2.replaceSelection("\n\n");
    }
    /*
      ************************************  Show Online Sample  *********************************************
    */
    
    
     /*
        Get image file path
    */
    public URL getImageFile(){
        URL url = this.getClass().getResource("/images/online.png");
        return url;
    }
    
    
    /*
        Set myTitle
    */
    public void setMyTitle(String s){
        setTitle(s);
    }
     /*
        Phương thức tải get download
    */
    public String getMyDownloadFolder(){
        return this.mydownloadfolder;
    }
    
   /*
        Set getMyHost
    */  
    public String getMyHost(){
        return this.host;
    }
    
    /*
        Set getMyPort
    */
    public int getMyPort(){
        return this.port;
    }
    
    /*
      Method to receive My Username
    */
    public String getMyUsername(){
        return this.username;
    }
    
    /*
      Update Attachment
    */
    public void updateAttachment(boolean b){
        this.attachmentOpen = b;
    }
    
  /*
        This function will open a file chooser
    */
    public void openFolder(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int open = chooser.showDialog(this, "Open Folder");
        if(open == chooser.APPROVE_OPTION){
            mydownloadfolder = chooser.getSelectedFile().toString()+"\\";
        } else {
            mydownloadfolder = "D:\\";
        }
    }
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtpane1 = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textpane2 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Sent");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(txtpane1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Online List");

        jScrollPane3.setViewportView(textpane2);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user.png"))); // NOI18N
        jLabel2.setText("  ");
        jLabel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel2AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jMenu1.setText("File Sharing");

        jMenuItem1.setText("Send File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator1);

        jMenuItem2.setText("Download");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Account");

        jMenuItem3.setText("Log out");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
      int  val = jTextField1.getText().length();
        if(val != 0 ){
            
            try {
               String content = username+" "+ jTextField1.getText();
               dos.writeUTF("CMD_CHATALL "+ content);
               appendMyMessage(" "+jTextField1.getText(), username);
               jTextField1.setText("");
           } catch (IOException e) {
               appendMessage(" Unable to send messages now, cannot connect to Server at this time, please try again later or restart this application.!", "Error", Color.RED, Color.RED);
           }
         
        }else{
       
            JOptionPane.showMessageDialog(null, "Type Something");
        
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
           try {
            String content = username+" "+ evt.getActionCommand();
            dos.writeUTF("CMD_CHATALL "+ content);
            appendMyMessage(" "+evt.getActionCommand(), username);
            jTextField1.setText("");
        } catch (IOException e) {
            appendMessage(" Unable to send messages now, cannot connect to Server at this time, please try again later or restart this application.!", "Error", Color.RED, Color.RED);
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you log out?");
        if(confirm == 0){
            try {
                socket.close();
                setVisible(false);
                /** Login Form **/
                new LoginForm().setVisible(true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jLabel2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel2AncestorAdded
        // TODO add your handling code here:
          this.username = username;
          jLabel2.setText(String.valueOf(username));
    }//GEN-LAST:event_jLabel2AncestorAdded

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
          if(!attachmentOpen){
            SendFile s = new SendFile();
            if(s.prepare(username, host, port, this)){
                s.setLocationRelativeTo(null);
                s.setVisible(true);
                attachmentOpen = true;
            } else {
                JOptionPane.showMessageDialog(this, "Unable to set File Sharing at this time, please try again later.!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
         try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int browse = chooser.showOpenDialog(this);
            if(browse == chooser.APPROVE_OPTION){
                this.mydownloadfolder = chooser.getSelectedFile().toString() +"\\";
            }
        } catch (HeadlessException e) {
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane textpane2;
    private javax.swing.JTextPane txtpane1;
    // End of variables declaration//GEN-END:variables
}
