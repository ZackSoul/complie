FROM frolvlad/alpine-gxx
WORKDIR /app/
COPY lab01.cpp ./
RUN g++ lab01.cpp -o lexer
RUN chmod +x lexer
