# -- Dockerfile --
# 这个文件负责构建包含你的程序的 Docker 容器

# 使用 Java 12
FROM openjdk:15
# 向容器内复制文件
COPY Main.java Lexer.java Word.java Parse.java /app/
# 编译程序
WORKDIR /app/
RUN javac -d ./output Main.java
# 将当前目录设为 /app/output
WORKDIR /app/output
