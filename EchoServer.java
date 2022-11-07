import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author DrTimothyC.Lethbridge
 * @author DrRobertLagan&gravere
 * @author Franccedilois &eacute;langer
 * @author PaulHolden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }


 
 //Instance methods ************************************************

  public EchoServer(int port,ChatIF serverUI) 
  {
  super(port);
  this.serverUI = serverUI;
  }
  //---------------------------------------------------------------------------------------------------------------------------------
  
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    
    
    String msgStr=(String)msg;
    if(msgStr.startsWith("#login")) {//treat locally
    	//HaSHMAP:savedInfo 
    	//setInfo and getInfo use HashMaps internally
    	
    	//isoler ID
    	String[] split = ((String)msg).split(" ", 2);
    	String loginID=split[1];
    	String key=loginID;
    	client.setInfo(key,loginID);
    }
    else {
    	try{
    		client.sendToClient("Please login first");
    		client.close();
    	}
    	catch(IOException e){
    	}
    }
    		 
    System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
    this.sendToAllClients((String)client.getInfo("loginID")+ ">"+ msg);
  }

 
 
 
 

 
 
 
 
 
 //---------------------------------------------------------------------------------------------------------------------------------
 /**
  * This method handles all data coming from the UI            
  *
  * @param message The message from the UI.    
  */
 public void handleMessageFromServerUI(String message)
 {
   try
   {
   	if (message.startsWith("#")) {
   		handleCommands(message);
   	}
   	else {
sendToAllClients("SERVER MSG>" + message);    		
serverUI.display("SERVER MSG>" + message);
		
   	}   
   }
   catch(IOException e)
   {
     serverUI.display ("The server was shut down.");
     System.exit(-1);//Generally indicates unsuccessful termination.
   }
 }
 
 private void handleCommands(String cmd) throws IOException{
	  String[] message = cmd.split(" ", 2);
	  String command=message[0];
	  
	  switch (command){ 
	//There is no method that would quit the server, since we cannot modify the OCSF we will add it here.
	  //We understand quit as terminating all activities: The java.lang.System.exit() method exits current program by terminating running Java virtual machine.
	  case ("#quit"): 
		  System.exit(0);//Generally indicates successful termination.
	  break;
	  		
	  case ("#stop") : 
		  this.stopListening();
	  break;
	  
	  case ("#close"): 
		  this.close();
		  
	  case ("#setport") :
		  if (this.isListening()==false) {
			  String parametre=message[1].replace("<", "").replace(">", "");
			  this.setPort(Integer.parseInt(parametre));;
		  }
		  else {
			  serverUI.display("Disconnect before setting p");
		  }
	  break;
	  
	  case ("#start"):
		  if (this.isListening()==false) {
			  this.listen();
		  }
		  else {
			  serverUI.display("The servers is already waiting for connections");
		  }
	  break;
	  
	  case ("#getport"):
		  if (this.isListening()) {
			  serverUI.display("Current port: " + this.getPort());
		  }
		  else {
			  serverUI.display("Connect to getport");
		  }
	              
	  break;
	  	  
	  default:
 		throw new IOException("Please enter a valid command"); 
	  }
 }
 /**
  * This method overrides the one in the superclass.  Called
  * when the server starts listening for connections.
  */
 protected void serverStarted()
 {
   System.out.println
     ("Server listening for connections on port " + getPort());
 }
 
 /**
  * This method overrides the one in the superclass.  Called
  * when the server stops listening for connections.
  */
 protected void serverStopped()
 {
   System.out.println
     ("Server has stopped listening for connections.");
 }
//Class methods ***************************************************
 
 
 /**
  * implementation of the Hook method called each time a new client connection is
  * accepted. The default implementation does nothing.
  * @param client the connection connected to the client.
  */
 @Override
 protected void clientConnected(ConnectionToClient client) {
 System.out.println("Client : "+ client + " has connected.");
 }

 /**
  * Implementation of the Hook method called each time a client disconnects.
  * The default implementation does nothing. The method
  * may be overridden by subclasses but should remains synchronized.
  *
  * @param client the connection with the client.
  */
 
 @Override 
 synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client "+ client + " has connected");
 }

 


 
//End of EchoServer class

}

 
 