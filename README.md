# Sistemas_Distribuidos_Projeto
Fórum com sistema de servidor e cliente (Com JSON, CRUD. Sem Admin, mensagens, Interface)
Como inicializar:
  Inicia o servidor (solicitado qual porta utilizar)
    javac -cp ".;lib/json-20250517.jar" controllers/VerifyJson.java models/User.java Server.java
    java -cp ".;lib/json-20250517.jar" Server

  Inicia o CLiente (solicita qual ip e porta para se conectar)
    java -cp .\lib\json-20250517.jar Client.java

