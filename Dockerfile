FROM frolvlad/alpine-gxx
WORKDIR /app/
COPY lab1.cpp ./
RUN g++ lab1.cpp -o lexer
RUN chmod +x lexer
