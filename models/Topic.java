package models;

public class Topic extends Message{
  private String title;
  private String subject;

  public Topic(String text, int id, String title, String subject ) {
      super(text, id);
      this.title = title;
      this.subject = subject;
  }
  // Setters
  public void setTitle(String title) {
      this.title = title;
  }
  
  public void setSubject(String subject) {
      this.subject = subject;
  }

  // Getters
  public String getTitle() {
      return title;
  }

  public String getSubject() {
      return subject;
  }
  @Override
  public String toString() {
  return super.toString() + " Title: " + title + ", Subject: " + subject;
  }
}
