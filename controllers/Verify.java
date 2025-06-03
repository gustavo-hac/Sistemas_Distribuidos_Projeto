package controllers;

import org.json.JSONObject;

public class Verify {
  JSONObject json = new JSONObject();

    
  public Verify(JSONObject json) {
    this.json = json;
  }

  public boolean opNULL() {
    return this.json.isNull("op");
  }

  public boolean registerNULL(){
    if(this.json.isNull("user"))
      return true;
    if(this.json.isNull("pass"))
      return true;
    if(this.json.isNull("nick"))
      return true;
    return false;
  }

  public String getOp() {
    return this.json.getString("op");
  }

} 
