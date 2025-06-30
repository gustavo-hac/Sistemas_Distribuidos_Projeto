package models;

public class Message {
  private String text;
  private int id;

  public Message(String text, int id) {
      this.text = text;
      this.id = id;
  }
  // Setters
  public void setText(String text) {
      this.text = text;
  }
  
  public void setId(int id) {
      this.id = id;
  }

  // Getters
  public int getId() {
      return id;
  }

  public String getText() {
      return text;
  }
  @Override
    public String toString() {
    return "Text: " + text + ", Id: " + id;
  }
}
