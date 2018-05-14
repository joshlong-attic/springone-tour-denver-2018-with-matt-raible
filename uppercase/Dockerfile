FROM projectriff/java-function-invoker:0.0.6
ARG FUNCTION_JAR=/functions/upppercase-0.0.1-SNAPSHOT.jar
ARG FUNCTION_HANDLER=uppercase&main=com.example.upppercase.UpppercaseApplication
ADD target/upppercase-0.0.1-SNAPSHOT.jar $FUNCTION_JAR
ENV FUNCTION_URI file://${FUNCTION_JAR}?handler=${FUNCTION_HANDLER}
