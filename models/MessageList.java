package models;

import java.util.ArrayList;
import java.util.List;

public class MessageList{
  List<Message> messageList = new ArrayList<>();
  
  // Criar
  public boolean createMessage(Message message){
    if(idIsRegistered(message.getId())){
      System.err.println("Id já registrado");
      return false;
    }else { messageList.add(message); return true;}
  }

  public boolean createMessage(int id,String text){
    if(idIsRegistered(id)){
      System.err.println("Id já registrado");
      return false;
    }else { messageList.add(new Message(text, id)); return true;}
  }
  // Desabilitar
  public boolean disableMessage(Message messageInput){
    for(Message message : messageList){
      if(message == messageInput){
        message.setText("Mensagem excluida por um moderador");
        return true;
      }
    }
    return false;
  }

  public boolean disableMessage(int id){
    for(Message message : messageList){
      if(id == message.getId()){
        message.setText("Mensagem excluida por um moderador");
        return true;
      }
    }
    return false;
  }
  // Delete
  public boolean deleteMessage(int id){
    for(Message message : messageList){
      if(message.getId() == id){
        messageList.remove(message);
        return true;
      }
    }
    return false;
  }
  // Verificações
  public boolean idIsRegistered(int id){
    for(Message message : messageList){
      if(id == message.getId()){
        return true;
      }
    }
    return false;
  }
}
