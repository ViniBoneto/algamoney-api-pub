# Aula 7.4: Este arquivo é usado pelo Heroku p/ especificar os cmds a serem executados pela app na sua inicialização.
#
#	Obs: Como houve o problema c/ cartão de crédito, tive que fazer o deploy no Pivotal WS, como plano b. Logo,
#	este arqvo será ignorado.
#
#	Obs 2: Como o serviço da Pivotal WS foi descontinuado, precisei fazer o deploy no Red Hat OpenShift, como plano c. 
web: java -Dserver.port=$PORT -Dspring.profiles.active=prod $JAVA_OPTS -jar target/algamoney*.jar