Fórum com sistema de servidor e cliente (com JSON e CRUD. Usuário administrador único).
O protocolo utilizado para troca de mensagens está na pasta Protocolo, nos arquivos “.pdf” e “.xlsx”.

Inicializar sem Margraven:

Inicie o servidor (será solicitada a porta a ser utilizada);
  
    javac -cp ".;lib/json-20250517.jar" controllers/VerifyJson.java models/User.java Server.java
    java -cp ".;lib/json-20250517.jar" Server

Inicie o cliente (será solicitado o IP e a porta para conexão).
  
    javac -cp ".;lib/json-20250517.jar" controllers/VerifyJson.java models/User.java Client.java
    java -cp ".;lib/json-20250517.jar" Client



