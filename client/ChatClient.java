// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  String loginID;//variable d'instance
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login <"+loginID+">");//connectionEstablished()??
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommands(message);
    	}
    	else {
    		sendToServer(message);
    	}   
    }
    catch(IOException e)
    {
      clientUI.display ("The server was not able to handle the message. Client terminated.");
      quit();
    }
  }
  
  private void handleCommands(String cmd) throws IOException{
	  String[] message = cmd.split(" ", 2);
	  String command=message[0];
	  
	  switch (command){ 
	  case ("#quit"): 
		  	quit();
	  break;
	  		
	  case ("#logoff") : 
		  try {
			  if (this.isConnected()) {
				  this.closeConnection();
			  }
			  else {
				  clientUI.display("Client is already disconnected");
			  }
		  }catch (IOException e){
			 
		  }
	  break;
		  
	  case ("#sethost") :
		  if (this.isConnected()==false) {
			  String parametre=message[1].replace("<", "").replace(">", "");
			  this.setHost(parametre);
		  }
		  else {
			  clientUI.display("Disconnect before setting Host");
		  }
	  break;
	  
	  case ("#setport"):
		  if (this.isConnected()==false) {
			  String parametre=message[1].replace("<", "").replace(">", "");
			  this.setPort(Integer.parseInt(parametre));
		  }
		  else {
			  clientUI.display("Disconnect before setting Port");
		  }
	  break;
	  
	  case ("#login"):
		  if (this.isConnected()==false) {
			  if (this.isConnected()==false) {
				  try {
	                  this.openConnection();
	              } catch (IOException e) {
	            	  clientUI.display("Disconnect to login");
	              }
	              }
		  }
	  break;
	  
	  case ("#gethost"):
		  if (this.isConnected()) {
			  clientUI.display("Current host: " + this.getHost());
		  }
		  else {
			  clientUI.display("Connect to gethost");
		  }
	  break;
	  
	  case ("#getport"):
		  if (this.isConnected()) {
			  clientUI.display("Current port: " + this.getPort());
		  }
		  else {
			  clientUI.display("Connect to getport");
		  }
	  break;
	  
	  default:
  		throw new IOException("Please enter a valid command"); 
	  }
}
  
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  /**
	 * Implementation of the Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  @Override 
  protected void connectionClosed() {
	  clientUI.display("This connection has been closed");
	}

	/**
	 * Implementation of the Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override 
 protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down");
	  System.exit(0);
	}
  
  @Override 
  protected void connectionEstablished() {
	  
	}

}
//End of ChatClient class

