/**
Simple FTP Client
Program 3
CS3830 - Data Communications and Computer Networks
A simple FTP client program to send a file to a given server.
@author John W Hunter
*/

import java.io.*;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class Client extends javax.swing.JFrame
{
   private final int PORT_NUMBER = 5976;
   private final int CHUNK_SIZE = 1024;
   private Socket sock = null;
   private InputStream inSock;
   private OutputStream outSock;
   private FileInputStream fileInput;
   private File file = null;
   
   /**
    Creates new form Client
    */
   public Client()
   {
      initComponents();
   }

   /**
    This method is called from within the constructor to initialize the form.
    WARNING: Do NOT modify this code. The content of this method is always
    regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        serverAddressField = new javax.swing.JTextField();
        connectAndSend = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client - Program 3");

        fileChooser.setFileFilter( new FileNameExtensionFilter("MSWord", new String[] { "doc", "docx" }) );
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        jLabel1.setText("Server Address: ");

        serverAddressField.setText("localhost");

        connectAndSend.setText("Connect and Upload");
        connectAndSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectAndSendActionPreformed(evt);
            }
        });

        jLabel2.setText("Output Messages:");

        outputTextArea.setEditable(false);
        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);
        jScrollPane1.setViewportView(outputTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(serverAddressField)
                                .addComponent(connectAndSend, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serverAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectAndSend))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   /**
   connectAndSendActionPerformed method
   Method to handle an event from the connectAndSend button. Attempts to 
   connect to a server and send a file. If the connection fails or a file is 
   not selected, error messages are displayed. If any other errors occur, an 
   appropriate message is displayed.
   @param evt 
   */
   private void connectAndSendActionPreformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_connectAndSendActionPreformed
   {//GEN-HEADEREND:event_connectAndSendActionPreformed
      //connect to server
      if (this.file != null)
        this.connectToServer();
      //if is connected, do rest
      
      if (sock != null)
      {
         try 
         {
            //send name
            this.sendNullTerminatedString(this.file.getName());
            //send size
            this.sendNullTerminatedString("" + this.file.length());
            //send file contents
            this.sendFile(this.file.getPath());
            //read from read socket for result
            int result = this.inSock.read();
            //if result is '@' ok
            if (result == '@')
               this.outputTextArea.append("Transfer success!\n");
            //else print error
            else
               this.outputTextArea.append("Error transfering file!\n");
            
         }
         catch (IOException ex) 
         {
            this.outputTextArea.append("Error: " + ex + "\n");
         }
         //disconnect from server
         this.disconnectFromServer();
      }
      else if (file == null)
      {
         //print error message
         this.outputTextArea.append("Please select a file!\n");
         
      }
   }//GEN-LAST:event_connectAndSendActionPreformed

   /**
   fileChooserActionPerformed method
   Method to handle an event given by the fileChooser.
   @param evt 
   */
   private void fileChooserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fileChooserActionPerformed
   {//GEN-HEADEREND:event_fileChooserActionPerformed
      if (JFileChooser.APPROVE_SELECTION.equals(evt.getActionCommand()))
      {
         this.file = this.fileChooser.getSelectedFile();
         this.outputTextArea.append("File selected: " 
               + this.file.getAbsolutePath() +"\n");
      }
      else if (JFileChooser.CANCEL_SELECTION.equals(evt.getActionCommand()))
      {
         if (file != null)
         {
            this.outputTextArea.append("File deselected\n");
            this.file = null;
         }
      }

   }//GEN-LAST:event_fileChooserActionPerformed

   /**
    @param args the command line arguments
    */
   public static void main(String args[])
   {
      /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try
      {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      }
      catch (ClassNotFoundException ex)
      {
         java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (InstantiationException ex)
      {
         java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (IllegalAccessException ex)
      {
         java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (javax.swing.UnsupportedLookAndFeelException ex)
      {
         java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
        //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            new Client().setVisible(true);
         }
      });
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectAndSend;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JTextField serverAddressField;
    // End of variables declaration//GEN-END:variables

   /**
    This method takes a String s (either a file name or a file size,) as a
    parameter, turns String s into a sequence of bytes ( byte[] ) by calling
    getBytes() method, and sends the sequence of bytes to the Server. A null
    character ‘\0’ is sent to the Server right after the byte sequence.
    */
   private void sendNullTerminatedString(String s)
   {
      this.outputTextArea.append("Sending null terminated string: " + s 
            + "\n");
      try
      {
         byte[] toSend = s.getBytes();
         for ( int i = 0; i < toSend.length; i++)
         {
            this.outSock.write(toSend[i]);
         }
         this.outSock.write('\0');
      }
      catch (IOException ex)
      {
         this.outputTextArea.append("Error: " + ex + "\n");
      }
   }

   /**
    This method takes a full-path file name, decomposes the file into smaller
    chunks (each with 1024 bytes), and sends the chunks one by one to the
    Server (loop until all bytes are sent.)
    */
   private void sendFile(String fullPathFileName)
   {
      try
      {
         this.outputTextArea.append("Sending file: " + fullPathFileName 
               + "\n");
         this.fileInput = new FileInputStream(fullPathFileName);
         try
         {
            while (this.fileInput.available() > 0)
            {
               byte buffer[] = new byte[this.CHUNK_SIZE];
               int toSend = this.fileInput.read(buffer);
               this.outSock.write(buffer, 0, toSend);
            }
         }
         catch (IOException ex)
         {
            this.outputTextArea.append("Error: " + ex + "\n");
         }
      }
      catch (FileNotFoundException ex)
      {
         this.outputTextArea.append("Error: " + ex + "\n");
      }
   }
   
   /**
   connectToServer method
   Method to connect to a server. If an error occurs, an appropriate error 
   message is displayed.
   */
   void connectToServer()
   {
      try
      {
         String hostAddress = this.serverAddressField.getText();
         sock = new Socket(hostAddress, this.PORT_NUMBER);
         if (sock.isConnected())
         {
            this.outSock = sock.getOutputStream();
            this.inSock = sock.getInputStream();
            this.outputTextArea.append("Connected to " + hostAddress + "\n");
         }
         else
         {
            sock = null;
            this.outputTextArea.append("Error connecting to server: " 
                  + hostAddress + "\n");
         }
      }
      catch (IOException ex)
      {
         this.outputTextArea.append("Error: " + ex + "\nPlease check your URL"
               + " address and socket!\n");
         sock = null;
      }
   }
   
   /**
   disconnectFromServer method
   Method to disconnect from server. If an error occurs, an appropriate error 
   message is displayed.
   */
   void disconnectFromServer()
   {
      try
         {
            inSock.close();
            outSock.close();
            sock.close();
            sock = null;
            this.outputTextArea.append("Connection terminated\n");
         }
         catch (IOException ex)
         {
            this.outputTextArea.append("Error: " + ex + "\n");
         }
   }
}
