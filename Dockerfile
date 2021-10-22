FROM frolvlad/alpine-gxx
WORKDIR /app/
COPY lab1.cpp ./
RUN g++ lab1.cpp -o lab1
RUN chmod +x lab1
