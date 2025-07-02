package models;

import java.util.ArrayList;
import java.util.List;

public class TopicList{
  List<Topic> topicList = new ArrayList<>();

  // Criar
  public boolean createTopic(Topic topic){
    if(idIsRegistered(topic.getId())){
      System.err.println("Id já registrado");
      return false;
    }else { topicList.add(topic); return true;}
  }

  public boolean createTopic(int id,String text, String title, String subject){
    if(idIsRegistered(id)){
      System.err.println("Id já registrado");
      return false;
    }else { topicList.add(new Topic(text, id, title, subject)); return true;}
  }
  // Desabilitar
  public boolean disableTopic(Topic topicInput){
    for(Topic topic : topicList){
      if(topic == topicInput){
        topic.setText("Mensagem excluida por um moderador");
        return true;
      }
    }
    return false;
  }

  public boolean disableTopic(int id){
    for(Topic topic : topicList){
      if(id == topic.getId()){
        topic.setText("Mensagem excluida por um moderador");
        return true;
      }
    }
    return false;
  }
  // Delete
  public boolean deleteTopic(int id){
    for(Topic topic : topicList){
      if(topic.getId() == id){
        topicList.remove(topic);
        return true;
      }
    }
    return false;
  }
  // Verificações
  public boolean idIsRegistered(int id){
    for(Topic topic : topicList){
      if(id == topic.getId()){
        return true;
      }
    }
    return false;
  }
}
