FROM gcc:10.2
WORKDIR /app/
COPY lab1.cpp ./
RUN g++ lab1.cpp -o lab1
RUN chmod +x lab1
